package Main;

import IO.HTTPWebRequest;
import IO.XMLReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.select.Elements;

import javax.swing.text.Document;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

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


public class Main{

    public static void main(String[] args) throws InterruptedException {

//        try {
//            CountryInfo c = new CountryInfo("Jerusalem");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Master splinter = new Master();
        splinter.start();
        
    }


}