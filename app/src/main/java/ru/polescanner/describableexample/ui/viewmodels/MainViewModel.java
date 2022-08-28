package ru.polescanner.describableexample.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import ru.polescanner.describableexample.domain.base.Describable;
import ru.polescanner.describableexample.domain.base.DescribableEntity;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    // TODO: Implement the ViewModel
    public Describable describableEntity = new DescribableEntity();

}