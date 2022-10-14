package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DescriptionFile {
    final String filepath;
    final String hash;
    protected boolean isStored;
    private Context context;

    //CASE FROM DATABASE
    public DescriptionFile (@NonNull String filepath,
                            @NonNull String hash,
                            @NonNull Context context) {
        this.filepath = filepath;
        this.isStored = isStored();
        this.hash = hash;
        this.context = context;
    }
    //case from camera
    public DescriptionFile(@NonNull String filepath,
                           @NonNull Context context) throws FileNotFoundException {
        this.filepath = filepath;
        this.isStored = isStored();
        this.hash = hash();
        this.context = context;
    }

    public boolean isNotCorrupted(@NonNull final String hash) throws FileNotFoundException {
        if (isStored()) {
            return DescriptionUtility.getHash(filepath).equals(hash);
        }
        else {
            throw new FileNotFoundException("File is missed!");
        }
    }

    private String hash() throws FileNotFoundException {
        if (!isStored) {
            throw new FileNotFoundException("File is missed!");
        }
        String calculatedHash = DescriptionUtility.getHash(filepath); // ToDo Refactor
        if (hash == null || hash.isEmpty())
            return calculatedHash;
        if (!calculatedHash.equals(hash))
            throw new HashNotFromThatFileException("Sorry!");
        return calculatedHash;
    }

    protected boolean isStored() {
        if (!isStored) {
            String fullFilePath = context.getFilesDir() + "/" + filepath;
            File file = new File(fullFilePath);
            if (file.isFile()) {
                this.isStored = true;
            }
        }
        return isStored;
    }
    public Uri writeExternalStorage() {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            String internalFilePath = context.getFilesDir() + "/" + filepath;
            String externalFilePath = context.getExternalFilesDir("Description") + "/" + filepath;

            try {
                File internalFile = new File(internalFilePath);
                File externalFile = new File(externalFilePath);
                FileInputStream fis = new FileInputStream(internalFile);
                FileOutputStream fos = new FileOutputStream(externalFile);
                byte[] data = new byte[(int) internalFile.length()];
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(data,0,data.length);
                fos.write(data);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Uri.parse(externalFilePath);
        }
        return null;
    }

    private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public Bitmap getImage() {
        if (isStored()) {
            String fullFilePath = context.getFilesDir() + "/" + filepath;
            return BitmapFactory.decodeFile(fullFilePath);
        }
        return null;
    }

    @Nullable
    public Bitmap createVideoThumbnail(long timeUs) {
        if (isStored()) {
            Bitmap thumbnail;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                thumbnail = makeVideoThumbnail(filepath, timeUs);
            } else {
                thumbnail = ThumbnailUtils.createVideoThumbnail(filepath,
                                                                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            }
            return thumbnail;
        }
        else
            return null;
    }
    // https://stackoverflow.com/questions/65005765/generate-thumbnail-from-sdcard-in-android-q
    private Bitmap makeVideoThumbnail(String filepath, long timeUs) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Bitmap bitmap = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, Uri.parse(context.getFilesDir() + "/" + filepath));
            bitmap = mediaMetadataRetriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
