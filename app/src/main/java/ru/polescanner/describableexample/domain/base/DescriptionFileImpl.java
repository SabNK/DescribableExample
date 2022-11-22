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

public class DescriptionFileImpl implements DescriptionFile {
    final String filepath;
    final String hash;
    protected boolean isStored;

    //CASE FROM DATABASE
    public DescriptionFileImpl(@NonNull String filepath,
                               @NonNull String hash) {
        this.filepath = filepath;
        this.hash = hash;
    }

    //case from camera or microphone etc.
    public DescriptionFileImpl(@NonNull String filepath) throws FileNotFoundException {
        this.filepath = filepath;
        this.hash = hash();
    }

    public boolean isNotCorrupted(@NonNull final String hash, Context context) throws FileNotFoundException {
        if (isStored(context)) {
            return DescriptionUtility.getHash(filepath).equals(hash);
        }
        else {
            throw new FileNotFoundException("File is missed!");
        }
    }

    public boolean isCorrupted(Context context) {
        boolean result;
        try {
            result = isNotCorrupted(hash, context);
        }
        catch (FileNotFoundException e) {
            result = true;
        }
        return result;
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

    public boolean isStored(Context context) {
        if (!isStored) {
            String fullFilePath = context.getFilesDir() + "/" + filepath;
            File file = new File(fullFilePath);
            if (file.isFile()) {
                this.isStored = true;
            }
        }
        return isStored;
    }
    public Uri writeExternalStorage(Context context) {
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

    public Bitmap getImage(Context context) {
        if (isStored(context)) {
            String fullFilePath = context.getFilesDir() + "/" + filepath;
            return BitmapFactory.decodeFile(fullFilePath);
        }
        return null;
    }

    public void loadFromServer() {

    }

    public void loadToServer() {

    }

    @Nullable
    public Bitmap createVideoThumbnail(long timeUs, Context context) {
        if (isStored(context)) {
            Bitmap thumbnail;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                thumbnail = makeVideoThumbnail(filepath, timeUs, context);
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
    private Bitmap makeVideoThumbnail(String filepath, long timeUs, Context context) {
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
