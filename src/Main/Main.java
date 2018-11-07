package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.Document;
import java.io.File;
import java.io.FileFilter;

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

    public static void main(String[] args) {
        File corpus = new File("d:\\documents\\users\\talmalu\\Downloads\\corpus\\corpus");
        File [] corpus_sub_dirs = corpus.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (File dir : corpus_sub_dirs){
            File [] file = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return !(pathname.isDirectory());
                }
            });

        }

    }
}