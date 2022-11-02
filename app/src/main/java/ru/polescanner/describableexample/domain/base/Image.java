package ru.polescanner.describableexample.domain.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

public abstract class Image extends DescriptionImpl {
    protected Image(@NonNull Bitmap thumbnail,
                    @NonNull Metadata metadata,
                    @NonNull DescriptionFileImpl file) {
        super(thumbnail, metadata, file);
    }

    @Override
    protected String intentType(){
        return "image/*";
    }
}
