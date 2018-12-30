package View;

import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class MainView implements IView {


    public Button b_saveResults;
    private Stage stage;
    private ViewModel viewModel;

    public CheckBox cbStemmer;
    public CheckBox cbSemantic;
    public Button btnViewLanguages;
    public Button btnViewDictionary;
    public Button btnLoad;
    public Button btnClear;
    public Button btnStart;
    public Button dcCorpusPath;
    public Button dcPostPath;
    public TextField tfCorpusInputPath;
    public TextField tfPostOutputPath;
    public TextField tfQueries;

    public TableView tbl_Dictionary;
    public TableColumn colTerm;
    public TableColumn colFrequency;

    public ListView lvCountriesFilter;
    public ListView lvQueries;
    public ListView lvQueriesResults;

    private Alert alert;

    public void initializeView(){

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
        this.tfCorpusInputPath.textProperty().bindBidirectional(this.viewModel.strCorpusLocationProperty());
        this.tfPostOutputPath.textProperty().bindBidirectional(this.viewModel.strPostingLocationProperty());
        this.tfQueries.textProperty().bindBidirectional(this.viewModel.strQueryProperty());
        this.lvCountriesFilter.itemsProperty().bindBidirectional(this.viewModel.observableListViewItemsProperty());
        this.lvQueries.itemsProperty().bindBidirectional(this.viewModel.observableQueriesListViewItemsProperty());
        this.lvQueriesResults.itemsProperty().bindBidirectional(this.viewModel.observableQueriesResultListViewItemsProperty());
        this.cbStemmer.selectedProperty().bindBidirectional(this.viewModel.bStemmingProperty());
        this.cbSemantic.selectedProperty().bindBidirectional(this.viewModel.bSemanticProperty());
        this.tbl_Dictionary.itemsProperty().bind(this.viewModel.observableTableVIewProperty());

        this.lvCountriesFilter.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.lvQueries.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
                        getQueryResultListById(newValue);
                    }
                });

        this.lvQueriesResults.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
                        getDocumentEntitiesByDocumentID(newValue);
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

        this.colTerm.setCellValueFactory(
                new PropertyValueFactory<TermIndexerData, String>("term")
        );
        this.colFrequency.setCellValueFactory(
                new PropertyValueFactory<TermIndexerData, String>("frequency")
        );
    }


    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        initializeView();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == this.viewModel){
            if (((String)arg).equals("INVERTED_INDEX_DONE")){
                this.btnViewDictionary.setVisible(true);
                this.btnViewLanguages.setVisible(true);
            }
            else if (((String)arg).equals("LOAD_INVERTED_INDEX_DONE")){
                this.btnViewLanguages.setVisible(false);
                this.btnViewDictionary.setVisible(true);
            }
            else if (((String)arg).equals("CLEAR_DONE")){
                this.btnViewLanguages.setVisible(false);
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
    public String OpenFileChooser() {
        FileChooser fc = new FileChooser();
        File selected = fc.showOpenDialog(new Stage());
        if (selected != null)
            return selected.getAbsolutePath();

        return "";
    }
    public String OpenQuerySavedPath() {
        FileChooser fc = new FileChooser();
        File selected = fc.showOpenDialog(new Stage());
        if (selected != null)
            return selected.getAbsolutePath();

        return "";
    }
    public void SetCorpusPath(ActionEvent actionEvent) {
        String  location = OpenFolderChooser();
        this.tfCorpusInputPath.textProperty().setValue(location);
    }

    public void SetPostPath(ActionEvent actionEvent) {
        String location = OpenFolderChooser();
        this.tfPostOutputPath.textProperty().setValue(location);
    }

    public void startInvertedIndex(ActionEvent actionEvent) {
        this.viewModel.startInvertedIndex();
    }

    public void Clear(ActionEvent actionEvent) {
        this.viewModel.Clear(this.tfPostOutputPath.getText());
    }

    public void LoadIndexers(ActionEvent actionEvent) {
        this.viewModel.LoadIndexers();
    }

    public void ShowDictionary(ActionEvent actionEvent) {
        this.viewModel.getTermTF();
    }

    public void ShowLanguages(ActionEvent actionEvent) {
        this.viewModel.getLanguages();
    }

    public void SetQueriesPath(ActionEvent actionEvent) {
        String  location = OpenFileChooser();
        this.tfQueries.setText(location);
    }
    public void Search(ActionEvent actionEvent) {
        this.viewModel.Search(this.lvCountriesFilter.getSelectionModel().getSelectedItems());
    }

    public void ShowCountries(ActionEvent actionEvent) {
        this.viewModel.getCountries();
    }

    public void saveResults(ActionEvent actionEvent) {
        String path=OpenQuerySavedPath();
        this.viewModel.saveQueryResults(path);
    }

    private void getQueryResultListById(String id){
        this.viewModel.getQueryResultById(id);
    }

    private void getDocumentEntitiesByDocumentID(String id) {
        this.viewModel.getDocumentEntitiesByDocumentID(id);
    }


}
