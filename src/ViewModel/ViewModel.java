package ViewModel;

import Model.IModel;
import java.util.Observable;
import java.util.Observer;

public class ViewModel  extends Observable implements Observer {

    private final IModel model;

    public ViewModel(IModel model) {
        this.model = model;
    }
    @Override
    public void update(Observable o, Object arg) {

    }


}
