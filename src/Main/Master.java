package Main;

import IO.DocumentReader;
import MapReduce.*;
import TextOperations.Document;
import TextOperations.TextOperations;
import TextOperations.Tokenize;
import TextOperations.StopWords;
import TextOperations.TokenizedDocument;

import javax.swing.text.Segment;
import java.io.File;
import java.io.FileFilter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Master {


    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<Document> document_queue;
    private ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue;
    private ConcurrentLinkedQueue<HashMap<String,TermDocumentInfo>> tdi_queue;

    private ConcurrentLinkedQueue<HashMap<String, List<TermDocumentInfo>>> destTermQueue;
    private ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> destDocQueue;

    private Thread [] doc_readers;
    private Thread [] text_operators;
    private Thread [] parsers;
    private Thread [] segments;
    private Thread [] term_posting;
    private Thread [] doc_posting;

    private Runnable [] runnable_doc_readers;
    private Runnable [] runnable_text_operators;
    private Runnable [] runnable_parsers;
    private Runnable [] runnable_segments;
    private Runnable [] runnable_term_posting;
    private Runnable [] runnable_doc_posting;

    private Semaphore document_reader_producer;
    private Semaphore text_operation_consumer;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;
    private Semaphore master_parser_producer;
    private Semaphore segment_file_consumer;
    private Semaphore segment_file_term_producer;
    private Semaphore segment_file_doc_producer;
    private Semaphore term_writer_consumer;
    private Semaphore doc_writer_consumer;




    public Master() {
        this.files_queue = new ConcurrentLinkedQueue<File>();
        this.document_queue = new ConcurrentLinkedQueue<Document>();
        this.tokenized_queue = new ConcurrentLinkedQueue<TokenizedDocument>();
        this.tdi_queue = new ConcurrentLinkedQueue<HashMap<String,TermDocumentInfo>>();
        this.destTermQueue = new ConcurrentLinkedQueue<HashMap<String, List<TermDocumentInfo>>>();
        this.destDocQueue = new ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>>();

        this.doc_readers = new Thread[1];
        this.runnable_doc_readers = new Runnable[1];

        this.text_operators = new Thread[3];
        this.runnable_text_operators = new Runnable[3];

        this.parsers = new Thread[5];
        this.runnable_parsers = new Runnable[5];

        this.segments = new Thread[2];
        this.runnable_segments = new Runnable[2];

        this.term_posting= new Thread[1];
        this.runnable_term_posting = new Runnable[1];

        this.doc_posting = new Thread[1];
        this.runnable_doc_posting = new Runnable[1];

        this.document_reader_producer = new Semaphore(5000,true);
        this.text_operation_consumer=  new Semaphore(0,true);
        this.text_operation_producer = new Semaphore(5000,true);
        this.master_parser_consumer = new Semaphore(0,true);
        this.master_parser_producer = new Semaphore(50000,true);
        this.segment_file_consumer = new Semaphore(0,true);
        this.segment_file_term_producer = new Semaphore(20,true);
        this.segment_file_doc_producer = new Semaphore(20,true);
        this.term_writer_consumer = new Semaphore(0,true);
        this.doc_writer_consumer = new Semaphore(0,true);



        LoadDocuments("D:\\documents\\users\\talmalu\\Downloads\\corpus\\corpus");
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

        System.out.println("Start : " + LocalTime.now());
        StartReaders();
        StartTextOperators();
        StartParsers();
        StartSegments();
        StartTermPosting();
        StartDocPosting();
        WaitReaders();
        WaitTextOperators();
        WaitParsers();
        WaitSegments();
        WaitTermPosting();
        WaitDocPosting();
        System.out.println("End : " + LocalTime.now());
//        int size = 0;
//        for (int i = 0; i < this.runnable_segments.length; i++)
//            size+=((SegmentFiles)this.runnable_segments[i]).getPostingSize();
//
//        System.out.println("Size Of Dictionery : " + size);
    }


    private void StartReaders(){
        for (int i = 0; i < this.doc_readers.length ; i++) {
            this.doc_readers[i] = new Thread((this.runnable_doc_readers[i] = new DocumentReader(this.files_queue,this.document_queue,this.document_reader_producer,this.text_operation_consumer)));
            this.doc_readers[i].start();
        }
    }
    private void StartTextOperators(){
        for (int i = 0; i < this.text_operators.length ; i++) {
            this.text_operators[i] = new Thread((this.runnable_text_operators[i] = new TextOperations(this.document_queue,this.tokenized_queue,new Tokenize(),new StopWords(),this.document_reader_producer,this.text_operation_consumer,this.text_operation_producer,this.master_parser_consumer)));
            this.text_operators[i].start();
        }
    }
    private void StartParsers() {
        for (int i = 0; i < this.parsers.length ; i++) {
            this.parsers[i] = new Thread((this.runnable_parsers[i] = new MasterParser(this.tokenized_queue,this.tdi_queue,this.text_operation_producer,this.master_parser_consumer,this.master_parser_producer,this.segment_file_consumer)));
            this.parsers[i].start();
        }
    }
    private void StartSegments() {
        for (int i = 0; i < this.segments.length ; i++) {
            this.segments[i] = new Thread((this.runnable_segments[i] = new SegmentFiles(this.tdi_queue,this.destTermQueue,this.destDocQueue,this.master_parser_producer,this.segment_file_consumer,this.segment_file_term_producer,this.segment_file_doc_producer,this.term_writer_consumer,this.doc_writer_consumer)));
            this.segments[i].start();
        }
    }
    private void StartTermPosting() {
        for (int i = 0; i < this.term_posting.length ; i++) {
            this.term_posting[i] = new Thread((this.runnable_term_posting[i] = new SegmentTermWriter(this.destTermQueue,this.segment_file_term_producer,this.term_writer_consumer)));
            this.term_posting[i].start();
        }
    }
    private void StartDocPosting() {
        for (int i = 0; i < this.doc_posting.length ; i++) {
            this.doc_posting[i] = new Thread((this.runnable_doc_posting[i] = new SegmentDocumentWriter(this.destDocQueue,this.segment_file_doc_producer,this.doc_writer_consumer)));
            this.doc_posting[i].start();
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
    private void WaitTermPosting() throws InterruptedException {
        for (int i = 0; i < this.term_posting.length ; i++) {
            this.term_posting[i].join();
        }
        TermPostingFinished();
    }
    private void WaitDocPosting() throws InterruptedException {
        for (int i = 0; i < this.doc_posting.length ; i++) {
            this.doc_posting[i].join();
        }
        DocPostingFinished();
    }

    private void ReaderFinished(){
        for (int i = 0; i < this.runnable_text_operators.length ; i++) {
            ((TextOperations)this.runnable_text_operators[i]).Stop();
        }
        System.out.println("Finished Read Files : " + LocalTime.now());
    }
    private void TextOperatorsFinished() {
        for (int i = 0; i < this.parsers.length ; i++) {
            ((MasterParser)this.runnable_parsers[i]).Stop();
        }
        System.out.println("Finished Text Operations : " + LocalTime.now());
    }
    private void ParsersFinished() {
        for (int i = 0; i < this.runnable_segments.length ; i++) {
            ((SegmentFiles)this.runnable_segments[i]).Stop();
        }
        System.out.println("Finished Parsing : " + LocalTime.now());
    }
    private void SegmentsFinished() {
        for (int i = 0; i < this.runnable_term_posting.length ; i++) {
            ((SegmentTermWriter)this.runnable_term_posting[i]).Stop();
        }
        for (int i = 0; i < this.runnable_doc_posting.length ; i++) {
            ((SegmentDocumentWriter)this.runnable_doc_posting[i]).Stop();
        }
        System.out.println("Finished Segments File : " + LocalTime.now());
    }
    private void TermPostingFinished() { System.out.println("Finished Term Posting : " + LocalTime.now()); }
    private void DocPostingFinished() { System.out.println("Finished Doc Posting  : " + LocalTime.now()); }
}
