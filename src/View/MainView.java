package View;

import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
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
    public ListView lvDictionary;

    private Alert alert;



    public void initialize(){

        alert = new Alert(Alert.AlertType.INFORMATION);
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
                Platform.runLater(() -> displayInformationAlert(this.viewModel.updateTimeToFinish(),this.viewModel.getDocsDictionaryLength(),this.viewModel.getTermDictionaryLength()));
            }
            else if (((String)arg).equals("LOAD_INVERTED_INDEX_DONE")){
                this.btnViewDictionary.setVisible(true);
            }
            else if (((String)arg).equals("CLEAR_DONE")){
                this.btnViewDictionary.setVisible(false);
            }
        }

    }

    private void displayInformationAlert(long timeToFinish, int docsDictionaryLength, int termDictionaryLength) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Time To Finish : " + timeToFinish + "\n" +
                             "Number of Indexed Terms : " + termDictionaryLength + "\n" +
                             "Number of Indexed Documents : " + docsDictionaryLength
        );
        alert.showAndWait();
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
        LoadDictionaryToTable(this.viewModel.getDictionary());
    }

    private void LoadDictionaryToTable(HashMap<String,String> data){

        ObservableList names = FXCollections.observableArrayList();
        Object [] keys = data.keySet().toArray();
        for (Object key : keys)
            names.add(((String)key + "\t" + data.get(key)));

        SortedList<String> sorted = new SortedList<String>(names,String.CASE_INSENSITIVE_ORDER);
        this.lvDictionary.setItems(sorted);

    }
}
