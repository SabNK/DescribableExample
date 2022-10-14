package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public abstract class Image extends DescriptionImpl {
    protected Image(@NonNull Bitmap thumbnail,
                    @NonNull Metadata metadata,
                    @NonNull DescriptionFile file) {
        super(thumbnail, metadata, file);
    }
}
