package Main;

//public class Main extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//
//
//        Parent root = FXMLLoader.load(getClass().getResource("Main/sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


public class Main {

    public static void main(String[] args) throws InterruptedException {

//        try {
//            CountryInfo c = new CountryInfo("Jerusalem");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        DataProvider d = new DataProvider("");
//        System.out.println(DataProvider.getCountryInfo("Rome").getCountryName());

        Master splinter = new Master();
        splinter.start();


        //SegmentTermWriter segmentWriter = new SegmentTermWriter();
//        HashMap<String, PriorityQueue<TermDocumentInfo>> map = new HashMap<>();
//
//        ///////////////////////////
//        Term term = new Term("kabab");
//        TermDocumentInfo tdi = new TermDocumentInfo(term, "D01");
//        PriorityQueue<TermDocumentInfo> que = new PriorityQueue<>(Comparator.comparing(o->o.getDocumentID()));
//        que.add(tdi);
//
//        Term term1 = new Term("kababibabi");
//        TermDocumentInfo tdi2 = new TermDocumentInfo(term, "D01");
//        PriorityQueue<TermDocumentInfo> que2 = new PriorityQueue<>(Comparator.comparing(o->o.getDocumentID()));
//        que2.add(tdi);
//        //////////////////////////////
//
//
//        map.put("kabab", que);
//        map.put("kababibabi", que2);

        //////////////////////////
        //segmentWriter.write(map);


    }


}