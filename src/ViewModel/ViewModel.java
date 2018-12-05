package ViewModel;

import Model.IInvertedIndexModel;
import View.IView;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private final IInvertedIndexModel model;

    public ViewModel(IInvertedIndexModel model) {
        this.model = model;
    }
    @Override
    public void update(Observable o, Object arg) {

    }


}
