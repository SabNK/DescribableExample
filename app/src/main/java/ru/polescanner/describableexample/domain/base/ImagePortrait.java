package ru.polescanner.describableexample.domain.base;

import static ru.polescanner.describableexample.domain.base.AdminConstants.DESCRIPTION_THUMB_PORTRAIT_HEIGHT;
import static ru.polescanner.describableexample.domain.base.AdminConstants.DESCRIPTION_THUMB_PORTRAIT_WIDTH;

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
              builder.filepath,
              builder.hash,
              builder.isStored,
              builder.utility);
    }

    public static Builder image(@NonNull String filepath, @NonNull DescriptionIO utility) {
        return image(filepath, utility.hash(filepath, null), utility);
    }

    public static Builder image(@NonNull String filepath,
                                @Nullable String hash,
                                @NonNull DescriptionIO utility) {
        return new Builder(filepath, hash, utility);
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash, DescriptionIO utility) {
            super(filepath, hash, utility);
        }

        @Override
        protected Bitmap createThumbnail() {
            if (this.isStored) {
                Bitmap image = utility.getImage(filepath);
                if (image == null) Log.d(TAG, "getThumbnail: image null");
                return ThumbnailUtils.extractThumbnail(image,
                                                       DESCRIPTION_THUMB_PORTRAIT_WIDTH,
                                                       DESCRIPTION_THUMB_PORTRAIT_HEIGHT);
            }
            else
                return null;
        }

        @Override
        public Description build(){
            setMetadata();
            isStored();
            return new ImagePortrait(this);
        }
    }
}
