package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public abstract class Image extends BaseDescription {
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
