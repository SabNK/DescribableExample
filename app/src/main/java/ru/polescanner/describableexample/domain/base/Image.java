package ru.polescanner.describableexample.domain.base;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public abstract class Image extends Description{
    protected Image(@NonNull Bitmap thumbnail,
                    @NonNull Metadata metadata,
                    @NonNull String filepath,
                    @NonNull String hash,
                    boolean isStored,
                    DescriptionIO utility) {
        super(thumbnail, metadata, filepath, hash, isStored, utility);
    }




}
