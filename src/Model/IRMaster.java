package Model;

import IO.DataProvider;
import IO.DocumentReader;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.MasterParser;
import TextOperations.*;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class IRMaster {

    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<Document> document_queue;
    private ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue;
    private ConcurrentLinkedQueue<HashMap<String,AbstractTermDocumentInfo>> tdi_queue;

    private Thread [] doc_readers;
    private Thread [] text_operators;
    private Thread [] parsers;

    private Runnable [] runnable_doc_readers;
    private Runnable [] runnable_text_operators;
    private Runnable [] runnable_parsers;


    private Semaphore document_reader_producer;
    private Semaphore text_operation_consumer;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;
    private Semaphore master_parser_producer;
    private Semaphore segment_file_consumer;

    private StopWords stopWords;
    private Stemmer stemmer;


    public IRMaster(Stemmer stemmer) {
        this.files_queue = new ConcurrentLinkedQueue<File>();
        this.document_queue = new ConcurrentLinkedQueue<Document>();
        this.tokenized_queue = new ConcurrentLinkedQueue<TokenizedDocument>();
        this.tdi_queue = new ConcurrentLinkedQueue<HashMap<String,AbstractTermDocumentInfo>>();

        this.doc_readers = new Thread[1];
        this.runnable_doc_readers = new Runnable[1];

        this.text_operators = new Thread[1];
        this.runnable_text_operators = new Runnable[1];

        this.parsers = new Thread[1];
        this.runnable_parsers = new Runnable[1];

        this.document_reader_producer = new Semaphore(5000,true);
        this.text_operation_consumer=  new Semaphore(0,true);
        this.text_operation_producer = new Semaphore(10000,true);
        this.master_parser_consumer = new Semaphore(0,true);
        this.master_parser_producer = new Semaphore(30000,true);
        this.segment_file_consumer = new Semaphore(0,true);


        if (!(DataProvider.getInstance().getCorpusLocation() == null)) {
            LoadQueries(DataProvider.getInstance().getQueriesLocation());
            this.stopWords = new StopWords(DataProvider.getInstance().getStopWordsLocation());
        }
        this.stemmer = stemmer;
    }
    private void LoadQueries(String location){
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


        WaitReaders();
        WaitTextOperators();
        WaitParsers();

    }
    private void StartReaders(){
        for (int i = 0; i < this.doc_readers.length ; i++) {
            this.doc_readers[i] = new Thread((this.runnable_doc_readers[i] = new DocumentReader(this.files_queue,this.document_queue,this.document_reader_producer,this.text_operation_consumer)));
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
    private void ReaderFinished(){
        for (int i = 0; i < this.runnable_text_operators.length ; i++) {
            ((TextOperations)this.runnable_text_operators[i]).Stop();
        }
        this.document_queue.add(new Document("DannyAndTalSendTheirRegardsYouFucker","","","","",""));
        this.text_operation_consumer.release();
        System.out.println("Finished Read Files : " + LocalTime.now());
    }
    private void TextOperatorsFinished() {
        for (int i = 0; i < this.parsers.length ; i++) {
            ((MasterParser)this.runnable_parsers[i]).Stop();
        }
        this.tokenized_queue.add(new TokenizedDocument("DannyAndTalSendTheirRegardsYouFucker","",null,null,null));
        this.master_parser_consumer.release();
        System.out.println("Finished Text Operations : " + LocalTime.now());
    }
    private void ParsersFinished() {
//        for (int i = 0; i < this.runnable_segments.length ; i++) {
//            ((SegmentFiles)this.runnable_segments[i]).Stop();
//        }
//        HashMap<String,AbstractTermDocumentInfo> temp = new HashMap<String,AbstractTermDocumentInfo>();
//        tmp.put("DannyAndTalSendTheirRegardsYouFucker",null);
//        this.tdi_queue.add(temp);
//        this.segment_file_consumer.release();
        System.out.println("Finished Parsing : " + LocalTime.now());
    }
}