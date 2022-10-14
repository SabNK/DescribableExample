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

    public static Builder image(@NonNull String filepath, @NonNull Context context) {
        return image(filepath, null, context);
    }

    public static Builder image(@NonNull String filepath,
                                               @Nullable String hash,
                                               @NonNull Context context) {
        return new Builder(filepath, hash, context);
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash, Context context) {
            super(filepath, hash, context);
        }

        @Override
        protected Bitmap createThumbnail() {
            if (this.file.isStored()) {
                Bitmap image = file.getImage();
                if (image == null) Log.d(TAG, "getThumbnail: image null");
                return ThumbnailUtils.extractThumbnail(image,
                                                       DESCRIPTION_THUMB_PORTRAIT_WIDTH,
                                                       DESCRIPTION_THUMB_PORTRAIT_HEIGHT);
            }
            else
                return null;
        }

        @Override
        public DescriptionImpl build(){
            setMetadata();
            return new ImagePortrait(this);
        }
    }
}
