package View;

import ViewModel.ViewModel;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;

public class MainView implements IView {


    private Stage stage;
    private ViewModel viewModel;

    public CheckBox cbStemmer;
    public Button btnViewDictionary;
    public Button btnLoad;
    public Button btnClear;
    public Button btnStart;
    public Button dcCorpusPath;
    public Button dcPostPath;
    public TextField tfCorpusInputPath;
    public TextField tfPostOutputPath;
    public TableView tbDictionary;



    public void initialize(){

        this.btnStart.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(tfCorpusInputPath.textProperty(),tfPostOutputPath.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return (tfCorpusInputPath.getText().isEmpty() || tfPostOutputPath.getText().isEmpty());
            }
        });

        this.btnClear.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(tfPostOutputPath.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return (tfPostOutputPath.getText().isEmpty());
            }
        });

        this.btnLoad.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(tfPostOutputPath.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return (tfPostOutputPath.getText().isEmpty());
            }
        });
    }


    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public String getCorpusLocation() {
        return this.tfCorpusInputPath.getText();
    }

    @Override
    public String getStopWordsLocationLocation() {
        return this.tfCorpusInputPath.getText();
    }

    @Override
    public String getPostLocation() {
        return this.tfPostOutputPath.getText();
    }

    @Override
    public void setLanguage() {

    }

    @Override
    public void setDictionary() {

    }

    @Override
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == this.viewModel){
            if (((String)arg).equals("INVERTED_INDEX_DONE")){
                this.btnViewDictionary.setVisible(true);
            }
            else if (((String)arg).equals("LOAD_INVERTED_INDEX_DONE")){
                this.btnViewDictionary.setVisible(true);
            }
            else if (((String)arg).equals("CLEAR_DONE")){
                this.tbDictionary.setVisible(false);
                this.btnViewDictionary.setVisible(false);
            }
        }

    }

    public String OpenFolderChooser() {
        DirectoryChooser dc = new DirectoryChooser();
        File selected = dc.showDialog(new Stage());
        if (selected != null)
            return selected.getAbsolutePath();

        return "";
    }

    public void SetCorpusPath(ActionEvent actionEvent) {
        String  location = OpenFolderChooser();
        this.tfCorpusInputPath.setText(location);
    }

    public void SetPostPath(ActionEvent actionEvent) {
        String location = OpenFolderChooser();
        this.tfPostOutputPath.setText(location);
    }

    public void startInvertedIndex(ActionEvent actionEvent) {
        this.viewModel.startInvertedIndex(this.tfCorpusInputPath.getText(),this.tfPostOutputPath.getText(),this.cbStemmer.isSelected());
    }

    public void Clear(ActionEvent actionEvent) {
        this.viewModel.Clear(this.tfPostOutputPath.getText());
    }

    public void LoadIndexers(ActionEvent actionEvent) {
        this.viewModel.LoadIndexers(this.tfPostOutputPath.getText(),this.cbStemmer.isSelected());
    }

    public void ShowDictionary(ActionEvent actionEvent) {
        tbDictionary.setVisible(true);
    }
}
