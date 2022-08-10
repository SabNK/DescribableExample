package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class DescriptionUtility {
    private static final String TAG = "DescriptionUtility";



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
        String filename = UUID.randomUUID().toString();

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
