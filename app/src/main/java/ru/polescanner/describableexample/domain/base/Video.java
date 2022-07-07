package ru.polescanner.describableexample.domain.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

//ToDo Check if Video has to be serializable
public class Video /*extends Description*/ {
    //TODO security issue
    private final String videoHash;

    public Video(String videoHash) {
        super();
        this.videoHash = videoHash;
    }

    //@Override
    public String toString64() {
        return "";
    }

    public Bitmap getThumbnail(Activity activity, String path) {
        Bitmap thumbnail_bitmap;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            thumbnail_bitmap = createThumbnail(activity, path);

        } else {
            thumbnail_bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        }
        return thumbnail_bitmap;
    }
    // https://stackoverflow.com/questions/65005765/generate-thumbnail-from-sdcard-in-android-q
    private Bitmap createThumbnail(Activity activity, String path) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Bitmap bitmap = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(activity, Uri.parse(path));
            bitmap = mediaMetadataRetriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
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
