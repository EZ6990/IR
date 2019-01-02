package Model;

import IO.*;
import IO.Segments.SegmentCityReader;
import IO.Segments.SegmentDocumentReader;
import IO.Segments.SegmentTermReader;
import IR.BM25Ranker;
import IR.IRanker;
import IR.Query;
import IR.SimpleSearcher;
import MapReduce.Index.CityIndexer;
import MapReduce.Parse.*;
import MapReduce.Segment.CitySegmentFile;
import MapReduce.Segment.DocumentSegmentFile;
import MapReduce.Segment.SegmentFile;
import MapReduce.Segment.TermSegmentFile;
import TextOperations.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

public class IRMaster {

    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<AbstractDocument> document_queue;
    private ConcurrentLinkedQueue<AbstractTokenizedDocument> tokenized_queue;
    private ConcurrentLinkedQueue<HashMap<String, AbstractTermDocumentInfo>> tdi_queue;

    private Thread[] doc_readers;
    private Thread[] text_operators;
    private Thread[] parsers;

    private Runnable[] runnable_doc_readers;
    private Runnable[] runnable_text_operators;
    private Runnable[] runnable_parsers;


    private Semaphore document_reader_producer;
    private Semaphore text_operation_consumer;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;
    private Semaphore master_parser_producer;
    private Semaphore segment_file_consumer;

    private StopWords stopWords;
    private Stemmer stemmer;
    private List<QueryResult> queryResultList;

    public IRMaster(Stemmer stemmer) {
        this.files_queue = new ConcurrentLinkedQueue<File>();
        this.document_queue = new ConcurrentLinkedQueue<AbstractDocument>();
        this.tokenized_queue = new ConcurrentLinkedQueue<AbstractTokenizedDocument>();
        this.tdi_queue = new ConcurrentLinkedQueue<HashMap<String, AbstractTermDocumentInfo>>();

        this.doc_readers = new Thread[1];
        this.runnable_doc_readers = new Runnable[1];

        this.text_operators = new Thread[1];
        this.runnable_text_operators = new Runnable[1];

        this.parsers = new Thread[1];
        this.runnable_parsers = new Runnable[1];

        this.document_reader_producer = new Semaphore(5000, true);
        this.text_operation_consumer = new Semaphore(0, true);
        this.text_operation_producer = new Semaphore(10000, true);
        this.master_parser_consumer = new Semaphore(0, true);
        this.master_parser_producer = new Semaphore(30000, true);
        this.segment_file_consumer = new Semaphore(0, true);
        this.queryResultList = new ArrayList<>();

        if (!(DataProvider.getInstance().getQueriesLocation() == null)) {
            this.files_queue.add(new File(DataProvider.getInstance().getQueriesLocation()));
        }

        this.stopWords = new StopWords(DataProvider.getInstance().getStopWordsLocation());
        this.stemmer = stemmer;
    }

    public void start(String query, List<String> Filter, boolean bSemantic) throws InterruptedException, ExecutionException {

        long start = System.currentTimeMillis();
        System.out.println("Start : " + LocalTime.now());

        if (query != null) {
            this.document_reader_producer.acquire();
            this.document_queue.add(new Query("1", query,"", "", ""));
            this.text_operation_consumer.release();
            //ReaderFinished();
        }

        StartReaders();
        WaitReaders();

        List<AbstractDocument> lst = null;
        HashMap<String, String> map = null;


        if (bSemantic) {
            lst = new ArrayList<>();
            map = new HashMap<>();
            while (this.document_queue.size() > 1) {
                Query doc = (Query) this.document_queue.poll();
                map.put(doc.getID(), doc.getText().toUpperCase());
                List<String> desc = Arrays.asList(doc.getDescription().split(" "));
                List<String> narr = Arrays.asList(doc.getNarrative().split(" "));
                List<String> topics = new ArrayList<>();
                for (String word : desc) {
                    if (narr.contains(word))
                        topics.add(word);
                }


                for (String word : doc.getText().split(" ")) {
                    if (word.length() > 0) {
                        ExecutorService executor1 = Executors.newSingleThreadExecutor();
                        Callable<List<DatamuseObject>> callable1 = new Callable<List<DatamuseObject>>() {
                            @Override
                            public List<DatamuseObject> call() {
                                return DataProvider.getInstance().getQueryTopicSemantic(word, topics, "syn");
                            }
                        };
                        Future<List<DatamuseObject>> future1 = executor1.submit(callable1);

                        ExecutorService executor2 = Executors.newSingleThreadExecutor();
                        Callable<List<DatamuseObject>> callable2 = new Callable<List<DatamuseObject>>() {
                            @Override
                            public List<DatamuseObject> call() {
                                return DataProvider.getInstance().getQueryTopicSemantic(word, topics, "trg");
                            }
                        };
                        Future<List<DatamuseObject>> future2 = executor2.submit(callable2);

                        ExecutorService executor3 = Executors.newSingleThreadExecutor();
                        Callable<List<DatamuseObject>> callable3 = new Callable<List<DatamuseObject>>() {
                            @Override
                            public List<DatamuseObject> call() {
                                return DataProvider.getInstance().getQueryTopicSemantic(word, topics, "spc");
                            }
                        };
                        Future<List<DatamuseObject>> future3 = executor3.submit(callable3);

                        ExecutorService executor4 = Executors.newSingleThreadExecutor();
                        Callable<List<DatamuseObject>> callable4 = new Callable<List<DatamuseObject>>() {
                            @Override
                            public List<DatamuseObject> call() {
                                return DataProvider.getInstance().getQueryTopicSemantic(word, topics, "gen");
                            }
                        };
                        Future<List<DatamuseObject>> future4 = executor4.submit(callable4);

//                        ExecutorService executor5 = Executors.newSingleThreadExecutor();
//                        Callable<List<DatamuseObject>> callable5 = new Callable<List<DatamuseObject>>() {
//                            @Override
//                            public List<DatamuseObject> call() {
//                                return DataProvider.getInstance().getQueryTopicSemantic(word,topics,"gen");
//                            }
//                        };
//                        Future<List<DatamuseObject>> future5 = executor5.submit(callable5);

                        List<DatamuseObject> lstApi = new ArrayList<>();
                        List<DatamuseObject> lstSynApi = future1.get();
                        List<DatamuseObject> lstTrgApi = future2.get();
                        List<DatamuseObject> lstSpcApi = future3.get();
                        List<DatamuseObject> lstGenApi = future4.get();
                        //List<DatamuseObject> lstMlApi =  DataProvider.getInstance().getQuerySimilarWordSemantic(Arrays.asList(map.get(doc.getID())))

                        if (lstSynApi != null)
                            lstApi.addAll(lstSynApi);
                        if (lstTrgApi != null)
                            lstApi.addAll(lstTrgApi);
                        if (lstSpcApi != null)
                            lstApi.addAll(lstSpcApi);
                        if (lstGenApi != null)
                            lstApi.addAll(lstGenApi);
//                        if (lstMlApi != null)
//                            lstApi.addAll(lstMlApi);

                        StringBuilder semantic = new StringBuilder("");
                        lstApi.forEach(obj -> semantic.append(" ").append(obj.getWord()));
                        doc.setText(doc.getText() + semantic.toString());
                    }
                }
//                List<DatamuseObject> lstMlApi =  DataProvider.getInstance().getQuerySimilarWordSemantic(Arrays.asList(map.get(doc.getID())));
//                if (lstMlApi != null) {
//                    StringBuilder semantic = new StringBuilder("");
//                    lstMlApi.forEach(obj -> semantic.append(" ").append(obj.getWord()));
//                    doc.setText(doc.getText() + semantic.toString());
//                }
                doc.setText(doc.getText() + " " + (doc.getDescription()));
                lst.add(doc);
            }


            for (AbstractDocument doc : lst) {
                this.document_queue.add(doc);
            }
            this.document_queue.add(this.document_queue.poll());
        } else {
            lst = new ArrayList<>();

            while (this.document_queue.size() > 1) {
                Query doc = (Query) this.document_queue.poll();
                //map.put(doc.getID(), doc.getText().toUpperCase());
                List<String> desc = Arrays.asList(doc.getDescription().split(" "));
                List<String> narr = Arrays.asList(doc.getNarrative().split(" "));
                List<String> topics = new ArrayList<>();
                for (String word : desc) {
                    if (narr.contains(word))
                        topics.add(word);
                }
                lst.add(doc);
                String ans = "";
//                for (String s : topics)
//                    ans += s+" ";
                doc.setText(doc.getText() + " " + (doc.getDescription()));
            }

            for (AbstractDocument docs : lst) {
                this.document_queue.add(docs);
            }
            this.document_queue.add(this.document_queue.poll());

        }

        StartTextOperators();
        StartParsers();


        WaitTextOperators();
        WaitParsers();


        IRanker ranker = new BM25Ranker(0.2, 0.35, getAvdl(), DataProvider.getInstance().getDocumentIndexer().size());
        SimpleSearcher searcher = new SimpleSearcher();
        HashMap<AbstractTermDocumentInfo, SegmentFile> queryToRank;
        List<String> cityFilter = getCorpusCityFilterDocuments(Filter);

        while (!this.tdi_queue.isEmpty()) {
            HashMap<String, AbstractTermDocumentInfo> thisQuery = this.tdi_queue.poll();
            //thisQuery.forEach((key,value) -> System.out.println(key + " " + value));
            // DataProvider.getInstance().addRankedDocumentsForQuery(((AbstractTermDocumentInfo) thisQuery.values().toArray()[0]).getDocumentID(), ranker.returnRankedDocs(searcher.search(thisQuery, getCorpusCityFilterDocuments(Filter))));
            HashMap<String, Double> weight = null;
            if (bSemantic) {
                weight = new HashMap<String, Double>();
                for (AbstractTermDocumentInfo value : thisQuery.values()) {
                    if (map.get(value.getDocumentID()).contains(value.getTerm().getData().toUpperCase()))
                        weight.put(value.getTerm().getData().toUpperCase(), (double) 1);
                    else
                        weight.put(value.getTerm().getData().toUpperCase(), (double) 0.49);
                }
            }

            List<String> resultList = ranker.returnRankedDocs(searcher.search(thisQuery, cityFilter), weight);
            String queryNum = ((AbstractTermDocumentInfo) thisQuery.values().toArray()[0]).getDocumentID();
            QueryResult result = new QueryResult(queryNum, 0, 0, 0, "td", resultList);
            this.queryResultList.add(result);

        }


    }

    private void StartReaders() {
        for (int i = 0; i < this.doc_readers.length; i++) {
            this.doc_readers[i] = new Thread((this.runnable_doc_readers[i] = new DocumentReader(this.files_queue, this.document_queue, this.document_reader_producer, this.text_operation_consumer, XMLReader.Type.QUERY)));
            this.doc_readers[i].start();
        }
    }

    private void StartTextOperators() {
        for (int i = 0; i < this.text_operators.length; i++) {
            this.text_operators[i] = new Thread((this.runnable_text_operators[i] = new TextOperations(this.document_queue, this.tokenized_queue, new Tokenize(), this.stopWords, this.document_reader_producer, this.text_operation_consumer, this.text_operation_producer, this.master_parser_consumer)));
            this.text_operators[i].start();
        }
    }

    private void StartParsers() {
        IFilter ignore = (this.stopWords.intersection(new RulesWords()));
        for (int i = 0; i < this.parsers.length; i++) {
            this.parsers[i] = new Thread((this.runnable_parsers[i] = new MasterParser(this.tokenized_queue, this.tdi_queue, this.text_operation_producer, this.master_parser_consumer, this.master_parser_producer, this.segment_file_consumer, this.stemmer, ignore)));
            this.parsers[i].start();
        }
    }

    private void WaitReaders() throws InterruptedException {
        for (int i = 0; i < this.doc_readers.length; i++) {
            this.doc_readers[i].join();
        }
        ReaderFinished();
    }

    private void WaitTextOperators() throws InterruptedException {
        for (int i = 0; i < this.text_operators.length; i++) {
            this.text_operators[i].join();
        }
        TextOperatorsFinished();
    }

    private void WaitParsers() throws InterruptedException {
        for (int i = 0; i < this.parsers.length; i++) {
            this.parsers[i].join();
        }
        ParsersFinished();
    }

    private void ReaderFinished() {
        this.document_queue.add(new Document("DannyAndTalSendTheirRegardsYouFucker", "", "", "", "", ""));
        this.text_operation_consumer.release();
        System.out.println("Finished Read Files : " + LocalTime.now());
    }

    private void TextOperatorsFinished() {
        this.tokenized_queue.add(new TokenizedDocument("DannyAndTalSendTheirRegardsYouFucker", "", null, null, null));
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


    private double getAvdl() {

        Iterator it = DataProvider.getInstance().getDocumentIndexer().iterator();
        int total = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            total += Integer.parseInt(pair.getValue().toString().split(" ")[2]);
        }

        return ((double) total / (double) DataProvider.getInstance().getDocumentIndexer().size());
    }

    private List<String> getCorpusCityFilterDocuments(List<String> cities) {

        List<String> docs = null;
        CitySegmentFile cityPost = new CitySegmentFile(DataProvider.getInstance().getPostLocation() + "\\" + DataProvider.getInstance().getPrefixPost() + "cityPost.post", null, new SegmentCityReader());
        if (cities != null && cities.size() > 0) {
            docs = new ArrayList<String>();
            CityIndexer cityIndexer = DataProvider.getInstance().getCityIndexer();
            for (String city : cities)
                cityPost.read(new Term(city, this.stemmer), Integer.parseInt(cityIndexer.getValue(city).split(" ")[1]));
            for (Map.Entry<String, List<Info>> pair : cityPost.getData().entrySet())
                for (Info info : pair.getValue())
                    docs.add(((CityTDI) info).getDocumentID());
        }
        return docs;
    }

    public DocumentTermInfo getDocumentInfoByDocumentID(String documentId) {
        DocumentTermInfo docInfo = null;
        DocumentSegmentFile docPost = new DocumentSegmentFile(DataProvider.getInstance().getPostLocation() + "\\" + DataProvider.getInstance().getPrefixPost() + "docPost.post", null, new SegmentDocumentReader());
        String indexedData = DataProvider.getInstance().getDocumentIndexer().getValue(documentId);
        if (indexedData != null) {
            String[] splitedIndexData = indexedData.split(" ");
            docPost.read(new Term(documentId, null), Integer.parseInt(splitedIndexData[1]));
            docInfo = ((DocumentTermInfo) ((List<Info>) docPost.getData().values().toArray()[0]).get(0));
        }
        return docInfo;
    }

    public TermDocumentInfo getTermInfoByTermID(String termId, String docId) {
        TermDocumentInfo termInfo = null;
        String indexedData = DataProvider.getInstance().getTermIndexer().getValue(termId);
        if (indexedData == null)
            indexedData = DataProvider.getInstance().getTermIndexer().getValue(termId.toLowerCase());
        if (indexedData != null) {
            String[] splitedIndexData = indexedData.split(" ");
            TermSegmentFile termPost = new TermSegmentFile(DataProvider.getInstance().getPostLocation() + "\\" + splitedIndexData[0], null, new SegmentTermReader());
            termPost.read(new Term(termId, null), Integer.parseInt(splitedIndexData[1]));
            for (TermDocumentInfo info : ((Collection<TermDocumentInfo>) termPost.getData().values().toArray()[0])) {
                if (info.getDocumentID().equals(docId)) {
                    termInfo = info;
                    break;
                }
            }
        }
        return termInfo;
    }

    public void printQueriesToFile(File file) throws IOException {
        this.queryResultList.sort((o1, o2) -> o1.getQueryId().compareTo(o2.getQueryId()));
        file.delete();
        for (QueryResult qr :
                queryResultList) {
            qr.writeToPath(file);
        }
    }

    public List<QueryResult> getQueriesSearched() {
        return this.queryResultList;
    }
}
