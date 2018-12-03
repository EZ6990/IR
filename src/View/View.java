package View;

import ViewModel.ViewModel;
import javafx.stage.Stage;

import java.util.Observable;

public class View implements IView {


    private ViewModel viewModel;

    @Override
    public void setStage(Stage stage) {

    }

    @Override
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
