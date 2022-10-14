package ru.polescanner.describableexample.domain.base;

import android.content.Intent;
import android.graphics.Bitmap;

public interface Description {

    Bitmap thumbnail();
    String metadata();
    boolean isStored();
    Intent view() throws WeHaveNoFile;


}
