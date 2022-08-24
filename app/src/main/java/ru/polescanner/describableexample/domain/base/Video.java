package ru.polescanner.describableexample.domain.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

//ToDo Check if Video has to be serializable
public class Video extends Description{

    private Video(Builder builder){
        super(builder.thumbnail, builder.metadata, builder.filepath, builder.hash, builder.isStored);
    }

    public static Builder video(@NonNull String filepath) {
        return video(filepath, Builder.hash(filepath));
    }

    public static Builder video(@NonNull String filepath, @NonNull String hash) {
        return new Builder(filepath, hash);
    }

    public static Bitmap getThumbnail(Activity activity, String path) {
        Bitmap thumbnail_bitmap;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            thumbnail_bitmap = createThumbnail(activity, path);
        } else {
            thumbnail_bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        }
        return thumbnail_bitmap;
    }
    // https://stackoverflow.com/questions/65005765/generate-thumbnail-from-sdcard-in-android-q
    private static Bitmap createThumbnail(Activity activity, String path) {
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

    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash) {
            super(filepath, hash);
        }

        @Override
        protected Bitmap createThumbnail() {
            return null;
        }

        @Override
        public Description build(){
            setMetadata();
            return new Video(this);
        }
    }
}
