package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface DescriptionIO {

    String hash(@NonNull final String filepath, @Nullable final String hash) throws HashNotFromThatFileException;

    boolean isFileStored(@NonNull final String filepath);

    boolean isNotCorrupted(@NonNull final String filepath, @NonNull final String hash);

    @Nullable
    Bitmap getImage(@NonNull final String filepath);

    @Nullable
    Bitmap createVideoThumbnail(@NonNull final String filepath, long timeUs);

    Uri writeExternalStorage(String filepath);


}

;
