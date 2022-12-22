package ru.polescanner.describableexample.domain.description;

import static ru.polescanner.describableexample.domain.description.AdminConstants.DESCRIPTION_THUMB_LANDSCAPE_HEIGHT;
import static ru.polescanner.describableexample.domain.description.AdminConstants.DESCRIPTION_THUMB_LANDSCAPE_WIDTH;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ImageLandscape extends Image {
    private static final String TAG = "ImageLandscape";

    private ImageLandscape(Builder builder){
        super(builder.thumbnail64,
              builder.metadata,
              builder.file,
              builder.reference);
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
        protected String createThumbnail64(Context context) {
            if (this.file.isStored(context)) {
                Bitmap image = file.getImage(context);
                if (image == null) Log.d(TAG, "getThumbnail: image null");
                //ToDo review (see video)
                Bitmap thumbnail =  ThumbnailUtils.extractThumbnail(image,
                                                       DESCRIPTION_THUMB_LANDSCAPE_WIDTH,
                                                       DESCRIPTION_THUMB_LANDSCAPE_HEIGHT);
                return thumbnail64(thumbnail);
            }
            else
                return null;
        }

        @Override
        public BaseDescription build(){
            setMetadata();
            return new ImageLandscape(this);
        }
    }
}