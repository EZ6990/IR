package Model;

import IO.*;
import MapReduce.Index.CityIndexer;
import MapReduce.Index.DocumentIndexer;
import MapReduce.Index.TermIndexer;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.MasterParser;
import MapReduce.Segment.SegmentFile;
import MapReduce.Segment.SegmentFiles;
import MapReduce.Segment.SegmentFilesWriter;
import MapReduce.Segment.TermSegmentFile;
import TextOperations.Document;
import TextOperations.TextOperations;
import TextOperations.Tokenize;
import TextOperations.StopWords;
import TextOperations.RulesWords;
import TextOperations.Stemmer;
import TextOperations.IFilter;
import TextOperations.TokenizedDocument;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalTime;
import java.util.BitSet;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Master {


    private long TimeToInvertIndex;
    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<AbstractDocument> document_queue;
    private ConcurrentLinkedQueue<AbstractTokenizedDocument> tokenized_queue;
    private ConcurrentLinkedQueue<HashMap<String,AbstractTermDocumentInfo>> tdi_queue;

    private ConcurrentLinkedQueue<SegmentFile> destSegmentFilesQueue;

    private Thread [] doc_readers;
    private Thread [] text_operators;
    private Thread [] parsers;
    private Thread [] segments;
    private Thread [] segment_posting;

    private Runnable [] runnable_doc_readers;
    private Runnable [] runnable_text_operators;
    private Runnable [] runnable_parsers;
    private Runnable [] runnable_segments;
    private Runnable [] runnable_segment_posting;

    private Semaphore document_reader_producer;
    private Semaphore text_operation_consumer;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;
    private Semaphore master_parser_producer;
    private Semaphore segment_file_consumer;
    private Semaphore segment_file_producer;
    private Semaphore segment_writer_consumer;

    private StopWords stopWords;
    private Stemmer stemmer;

    private TermIndexer termIndexer;
    private CityIndexer cityIndexer;
    private DocumentIndexer documentIndexer;


    public Master(Stemmer stemmer) {
        this.files_queue = new ConcurrentLinkedQueue<File>();
        this.document_queue = new ConcurrentLinkedQueue<AbstractDocument>();
        this.tokenized_queue = new ConcurrentLinkedQueue<AbstractTokenizedDocument>();
        this.tdi_queue = new ConcurrentLinkedQueue<HashMap<String,AbstractTermDocumentInfo>>();
        this.destSegmentFilesQueue = new ConcurrentLinkedQueue<SegmentFile>();

        this.doc_readers = new Thread[2];
        this.runnable_doc_readers = new Runnable[2];

        this.text_operators = new Thread[8];
        this.runnable_text_operators = new Runnable[8];

        this.parsers = new Thread[12];
        this.runnable_parsers = new Runnable[12];

        this.segments = new Thread[2];
        this.runnable_segments = new Runnable[2];

        this.segment_posting= new Thread[3];
        this.runnable_segment_posting = new Runnable[3];

        this.document_reader_producer = new Semaphore(10000,true);
        this.text_operation_consumer=  new Semaphore(0,true);
        this.text_operation_producer = new Semaphore(10000,true);
        this.master_parser_consumer = new Semaphore(0,true);
        this.master_parser_producer = new Semaphore(40000,true);
        this.segment_file_consumer = new Semaphore(0,true);
        this.segment_file_producer = new Semaphore(150,true);
        this.segment_writer_consumer = new Semaphore(0,true);


        if (!(DataProvider.getInstance().getCorpusLocation() == null)) {
            LoadDocuments(DataProvider.getInstance().getCorpusLocation());
            this.stopWords = new StopWords(DataProvider.getInstance().getStopWordsLocation());
        }
        this.stemmer = stemmer;
    }

    private void LoadDocuments(String location){
        File corpus = new File(location);
        File[] corpus_sub_dirs = corpus.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (File dir : corpus_sub_dirs) {
            File[] files = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return !(pathname.isDirectory());
                }
            });
            for (File file : files)
                this.files_queue.add(file);
        }
    }

    public void start() throws InterruptedException {

        long start = System.currentTimeMillis();
        System.out.println("Start : " + LocalTime.now());
        StartReaders();
        StartTextOperators();
        StartParsers();
        StartSegments();
        StartSegmentFilesPosting();
        WaitReaders();
        WaitTextOperators();
        WaitParsers();
        WaitSegments();
        WaitSegmentFilesPosting();

        String postLocation = DataProvider.getInstance().getPostLocation();
        String prefix = DataProvider.getInstance().getPrefixPost();

        System.out.println("Start indexing: " + LocalTime.now());
        this.termIndexer = new TermIndexer(postLocation + "\\" + prefix + "termIndexer.index");
        this.termIndexer.CreatePostFiles(postLocation);
        this.termIndexer.write();
        System.out.println("End indexing: " + LocalTime.now());

        System.out.println("Start indexing city: " + LocalTime.now());
        this.cityIndexer = new CityIndexer(postLocation + "\\" + prefix + "cityIndexer.index");
        this.cityIndexer.CreatePostFiles(postLocation);
        this.cityIndexer.write();
        System.out.println("End indexing city: " + LocalTime.now());

        System.out.println("Start indexing document: " + LocalTime.now());
        this.documentIndexer = new DocumentIndexer(postLocation + "\\" + prefix + "documentIndexer.index");
        this.documentIndexer.CreatePostFiles(postLocation);
        this.documentIndexer.write();
        System.out.println("End indexing document: " + LocalTime.now());

        this.TimeToInvertIndex = System.currentTimeMillis() - start;

        DataProvider.getInstance().setTermIndexer(this.termIndexer);
        DataProvider.getInstance().setDocumentIndexer(this.documentIndexer);
        DataProvider.getInstance().setCityIndexer(this.cityIndexer);

    }


    private void StartReaders(){
        for (int i = 0; i < this.doc_readers.length ; i++) {
            this.doc_readers[i] = new Thread((this.runnable_doc_readers[i] = new DocumentReader(this.files_queue,this.document_queue,this.document_reader_producer,this.text_operation_consumer, XMLReader.Type.DOCUMENT)));
            this.doc_readers[i].start();
        }
    }
    private void StartTextOperators(){
        for (int i = 0; i < this.text_operators.length ; i++) {
            this.text_operators[i] = new Thread((this.runnable_text_operators[i] = new TextOperations(this.document_queue,this.tokenized_queue,new Tokenize(),this.stopWords,this.document_reader_producer,this.text_operation_consumer,this.text_operation_producer,this.master_parser_consumer)));
            this.text_operators[i].start();
        }
    }
    private void StartParsers() {
        IFilter ignore = (this.stopWords.intersection(new RulesWords()));
        for (int i = 0; i < this.parsers.length ; i++) {
            this.parsers[i] = new Thread((this.runnable_parsers[i] = new MasterParser(this.tokenized_queue,this.tdi_queue,this.text_operation_producer,this.master_parser_consumer,this.master_parser_producer,this.segment_file_consumer,this.stemmer,ignore)));
            this.parsers[i].start();
        }
    }
    private void StartSegments() {
        for (int i = 0; i < this.segments.length ; i++) {
            this.segments[i] = new Thread((this.runnable_segments[i] = new SegmentFiles(this.tdi_queue,this.destSegmentFilesQueue,this.master_parser_producer,this.segment_file_consumer,this.segment_file_producer,this.segment_writer_consumer)));
            ((SegmentFiles)this.runnable_segments[i]).setThreadID((int)this.segments[i].getId());
            this.segments[i].start();
        }
    }
    private void StartSegmentFilesPosting() {
        for (int i = 0; i < this.segment_posting.length ; i++) {
            this.segment_posting[i] = new Thread((this.runnable_segment_posting[i] = new SegmentFilesWriter(this.destSegmentFilesQueue,this.segment_file_producer,this.segment_writer_consumer)));
            this.segment_posting[i].start();
        }
    }

    private void WaitReaders() throws InterruptedException {
        for (int i = 0; i < this.doc_readers.length ; i++) {
            this.doc_readers[i].join();
        }
        ReaderFinished();
    }
    private void WaitTextOperators() throws InterruptedException {
        for (int i = 0; i < this.text_operators.length ; i++) {
            this.text_operators[i].join();
        }
        TextOperatorsFinished();
    }
    private void WaitParsers() throws InterruptedException {
        for (int i = 0; i < this.parsers.length ; i++) {
            this.parsers[i].join();
        }
        ParsersFinished();
    }
    private void WaitSegments() throws InterruptedException {
        for (int i = 0; i < this.segments.length ; i++) {
            this.segments[i].join();
        }
        SegmentsFinished();
    }
    private void WaitSegmentFilesPosting() throws InterruptedException {
        for (int i = 0; i < this.segment_posting.length ; i++) {
            this.segment_posting[i].join();
        }
        SegmentFilesPostingFinished();
    }

    private void ReaderFinished(){
        this.document_queue.add(new Document("DannyAndTalSendTheirRegardsYouFucker","","","","",""));
        this.text_operation_consumer.release();
        System.out.println("Finished Read Files : " + LocalTime.now());
    }
    private void TextOperatorsFinished() {
        this.tokenized_queue.add(new TokenizedDocument("DannyAndTalSendTheirRegardsYouFucker","",null,null,null));
        this.master_parser_consumer.release();
        System.out.println("Finished Text Operations : " + LocalTime.now());
    }
    private void ParsersFinished() {
        for (int i = 0; i < this.runnable_segments.length ; i++) {
            ((SegmentFiles)this.runnable_segments[i]).Stop();
        }
        HashMap<String,AbstractTermDocumentInfo> temp = new HashMap<String,AbstractTermDocumentInfo>();
        temp.put("DannyAndTalSendTheirRegardsYouFucker",null);
        this.tdi_queue.add(temp);
        this.segment_file_consumer.release();
        System.out.println("Finished Parsing : " + LocalTime.now());
    }
    private void SegmentsFinished() {
        for (int i = 0; i < this.runnable_segment_posting.length ; i++) {
            ((SegmentFilesWriter)this.runnable_segment_posting[i]).Stop();
        }
        this.destSegmentFilesQueue.add(new TermSegmentFile("DannyAndTalSendTheirRegardsYouFucker",null,null));
        this.segment_writer_consumer.release();
        System.out.println("Finished Segments File : " + LocalTime.now());
    }
    private void SegmentFilesPostingFinished() { System.out.println("Finished Segment Posting : " + LocalTime.now()); }

    public long getTimeToInvertIndex() {
        return TimeToInvertIndex;
    }

    public int getDocumentDictionarySize() {
        return this.documentIndexer.size();
    }
}
