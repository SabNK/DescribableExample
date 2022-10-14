package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class DescriptionUtility implements DescriptionIO{
    private static final String TAG = "DescriptionUtility";
    private Context context;

    public DescriptionUtility(Context context) {
        this.context = context;
    }

    public DescriptionUtility() {
    }

    //ToDo refactor to Utility
    public String hash(@NonNull final String filepath, @Nullable final String hash) {
        if (!isFileStored(filepath))
            return hash;
        String calculatedHash = DescriptionUtility.getHash(filepath);;
        if (hash == null || hash.isEmpty())
            return calculatedHash;
        if (!calculatedHash.equals(hash))
            throw new HashNotFromThatFileException("Sorry!");
        return calculatedHash;
    }

    @Override
    public boolean isNotCorrupted(@NonNull final String filepath, @NonNull final String hash) throws FileNotFoundException {
        if (isFileStored(filepath)) {
            return DescriptionUtility.getHash(filepath).equals(hash);
        }
        else {
            throw new FileNotFoundException("File is missed!");
        }
    }

    @Nullable
    public String hash(@NonNull final String filepath) {
        return hash(filepath, null);
    }

    //ToDo smth corrupted check?
    @Override
    public boolean isFileStored(@NonNull String filepath) {
        String fullFilePath = context.getFilesDir() + "/" + filepath;
        File file = new File(fullFilePath);
        return file.isFile() ? true : false;
    }

    @Override
    public Bitmap getImage(@NonNull String filepath) {
        if (isFileStored(filepath)) {
            String fullFilePath = context.getFilesDir() + "/" + filepath;
            return BitmapFactory.decodeFile(fullFilePath);
        }
        return null;
    }

    //ToDo Implement this - we take size and SHA1 as Hash. THink of Inner Class.
    //https://stackoverflow.com/questions/6293713/java-how-to-create-sha-1-for-a-file
    //https://stackoverflow.com/questions/236861/how-do-you-determine-the-ideal-buffer-size-when-using-fileinputstream
    //https://stackoverflow.com/questions/5016947/how-to-discover-identical-files-without-comparing-them-to-eachother
    public static String getHash(String filename){
        return "";
    }

    public static String getHash(File file){
        return "";
    }

    @Nullable
    @Override
    public Bitmap createVideoThumbnail(@NonNull String filepath, long timeUs) {
        if (isFileStored(filepath)) {
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

    @Override
    public Uri writeExternalStorage(String filepath) {
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


    public static Bitmap getThumbnail(String filename, Context context) {
        Log.d(TAG, "getThumbnail: START OPEN " + filename);
        String yourFilePath = context.getFilesDir() + "/" + filename;
        Bitmap fullImage = BitmapFactory.decodeFile(yourFilePath);
        if (fullImage == null) Log.d(TAG, "getThumbnail: image null");
        final int THUMB_HEIGHT = 100;
        final double THUMB_RATIO_WH = 2/3.0;
        final int THUMB_WIDTH = (int) Math.round(THUMB_HEIGHT * THUMB_RATIO_WH);
        Log.d(TAG, "getThumbnail: " + THUMB_WIDTH);
        return ThumbnailUtils.extractThumbnail(fullImage, THUMB_WIDTH, THUMB_HEIGHT);
    }
    //ToDo Stupid String return - replace in Prod
    public static String saveBitmapToFile(Bitmap image, Context context){
        String filename = UUID.randomUUID().toString() + ".jpg";

        try {
            FileOutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            out.close();
            Log.d(TAG, "saveBitmapToFile: SAVED " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "saveBitmapToFile: EXCEPTION");
        }

        return filename;
    }
}
