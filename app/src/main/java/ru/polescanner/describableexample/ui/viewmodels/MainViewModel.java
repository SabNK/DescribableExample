package ru.polescanner.describableexample.ui.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import ru.polescanner.describableexample.domain.base.Describable;
import ru.polescanner.describableexample.domain.base.DescribableEntity;
import ru.polescanner.describableexample.domain.base.Description;
import ru.polescanner.describableexample.domain.base.DescriptionUtility;
import ru.polescanner.describableexample.domain.base.Image;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    // TODO: Implement the ViewModel
    public Describable describableEntity = new DescribableEntity();

}