package ru.polescanner.describableexample.domain.description;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileNotFoundException;

public interface DescriptionIO {

    String hash(@NonNull final String filepath, @Nullable final String hash) throws HashNotFromThatFileException;

    boolean isFileStored(@NonNull final String filepath);

    boolean isNotCorrupted(@NonNull final String filepath, @NonNull final String hash) throws FileNotFoundException;

    @Nullable
    Bitmap getImage(@NonNull final String filepath);

    @Nullable
    Bitmap createVideoThumbnail(@NonNull final String filepath, long timeUs);

    Uri writeExternalStorage(String filepath);
}