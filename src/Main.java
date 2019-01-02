
import IO.Segments.SemanticDatamuse;
import MapReduce.Index.TermIndexer;
import Model.SimpleInvertedIndexModel;
import TextOperations.Stemmer;
import View.IView;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getResource("MainView.fxml").openStream());
        Parent root = loader.getRoot();

        IView view = (IView)loader.getController();
        SimpleInvertedIndexModel model = new SimpleInvertedIndexModel();
        ViewModel viewModel = new ViewModel(model);

        view.setStage(primaryStage);
        model.addObserver(viewModel);
        viewModel.addObserver(view);

        view.setViewModel(viewModel);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 864, 604));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
//        TermIndexer stemterm = new TermIndexer("d:\\documents\\users\\talmalu\\Documents\\Tal\\post\\stem_termIndexer.index");
//        TermIndexer nostemterm = new TermIndexer("d:\\documents\\users\\talmalu\\Documents\\Tal\\post\\no_stem_termIndexer.index");
//
//        stemterm.read();
//        int totalstemterm = 0;
//        for (String val :stemterm.getTermNumberOfOccurrenceMap().values()){
//            totalstemterm += Integer.parseInt(val);
//        }
//
//        nostemterm.read();
//        int totalnostemterm = 0;
//        for (String val :nostemterm.getTermNumberOfOccurrenceMap().values()){
//            totalnostemterm += Integer.parseInt(val);
//        }


    }
}
