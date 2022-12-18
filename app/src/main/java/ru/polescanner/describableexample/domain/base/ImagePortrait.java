package ru.polescanner.describableexample.domain.base;

import static ru.polescanner.describableexample.domain.base.AdminConstants.DESCRIPTION_THUMB_PORTRAIT_HEIGHT;
import static ru.polescanner.describableexample.domain.base.AdminConstants.DESCRIPTION_THUMB_PORTRAIT_WIDTH;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ImagePortrait extends Image {
    private static final String TAG = "ImagePortrait";

    private ImagePortrait(Builder builder){
        super(builder.thumbnail,
              builder.metadata,
              builder.file);
    }

    public static Builder description(@NonNull String filepath) {
        return description(filepath, null);
    }

    public static Builder description(@NonNull String filepath,
                                               @Nullable String hash) {
        return new Builder(filepath, hash);
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash) {
            super(filepath, hash);
        }

        @Override
        protected Bitmap createThumbnail(Context context) {
            if (this.file.isStored(context)) {
                Bitmap image = file.getImage(context);
                if (image == null) Log.d(TAG, "getThumbnail: image null");
                return ThumbnailUtils.extractThumbnail(image,
                                                       DESCRIPTION_THUMB_PORTRAIT_WIDTH,
                                                       DESCRIPTION_THUMB_PORTRAIT_HEIGHT);
            }
            else
                return null;
        }

        @Override
        public BaseDescription build(){
            setMetadata();
            return new ImagePortrait(this);
        }
    }
}
