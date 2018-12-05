package View;

import ViewModel.ViewModel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Observable;

public class MainView implements IView {


    public Button dcCorpusPath;
    public Button dcPostPath;
    public TextField tfCorpusInputPath;
    public TextField tfPostOutputPath;

    private ViewModel viewModel;

    @Override
    public void setStage(Stage stage) {

    }

    @Override
    public String getCorpusLocation() {
        return null;
    }

    @Override
    public String getStopWordsLocationLocation() {
        return null;
    }

    @Override
    public void getLanguage() {

    }

    @Override
    public void getDictionary() {

    }

    @Override
    public void setViewModel(ViewModel viewModel) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
