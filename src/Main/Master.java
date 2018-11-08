package Main;

import IO.DocumentReader;
import TextOperations.Document;
import TextOperations.TextOperations;
import TextOperations.Tokenize;
import TextOperations.StopWords;
import TextOperations.TokenizedDocument;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Master {


    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<Document> document_queue;
    private ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue;

    private Thread [] doc_readers;
    private Thread [] text_operators;

    public Master() {
        this.files_queue = new ConcurrentLinkedQueue<File>();
        this.document_queue = new ConcurrentLinkedQueue<Document>();
        this.tokenized_queue = new ConcurrentLinkedQueue<TokenizedDocument>();
        this.doc_readers = new Thread[2];
        this.text_operators = new Thread[3];

        LoadDocuments("d:\\documents\\users\\talmalu\\Downloads\\corpus\\corpus");
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

        StartReaders();
        StartTextOperators();
        WaitReaders();
        WaitTextOperators();


    }

    private void StartReaders(){
        for (int i = 0; i < this.doc_readers.length ; i++) {
            this.doc_readers[i] = new Thread(new DocumentReader(this.files_queue,this.document_queue));
            this.doc_readers[i].start();
        }
    }
    private void StartTextOperators(){
        for (int i = 0; i < this.text_operators.length ; i++) {
            this.text_operators[i] = new Thread(new TextOperations(this.document_queue,this.tokenized_queue,new Tokenize(),new StopWords()));
            this.text_operators[i].start();
        }
    }

    private void WaitReaders() throws InterruptedException {
        for (int i = 0; i < this.doc_readers.length ; i++) {
            this.doc_readers[i].join();
        }
    }

    private void WaitTextOperators() throws InterruptedException {
        for (int i = 0; i < this.text_operators.length ; i++) {
            this.text_operators[i].join();
        }
    }

    private void ReaderFinished(){
        for (int i = 0; i < this.text_operators.length ; i++) {
        }
    }
}
