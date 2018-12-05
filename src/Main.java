
import Model.SimpleInvertedIndexModel;
import View.IView;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getResource("MainView.fxml").openStream());
        Parent root = loader.getRoot();

        IView view = (IView)loader.getController();
        SimpleInvertedIndexModel model = new SimpleInvertedIndexModel();
        ViewModel viewModel = new ViewModel(model);

        model.addObserver(viewModel);
        viewModel.addObserver(view);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
