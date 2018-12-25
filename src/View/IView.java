package View;

import ViewModel.ViewModel;
import javafx.stage.Stage;

import java.util.Observer;

public interface IView extends Observer {

    void setStage(Stage stage);
    void setViewModel(ViewModel viewModel);




}
