package ru.polescanner.describableexample.domain.base;

import static ru.polescanner.describableexample.domain.base.AdminConstants.DESCRIPTION_THUMB_LANDSCAPE_HEIGHT;
import static ru.polescanner.describableexample.domain.base.AdminConstants.DESCRIPTION_THUMB_LANDSCAPE_WIDTH;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;

import androidx.annotation.NonNull;


public class ImageLandscape extends Image {
    private static final String TAG = "ImageLandscape";

    private ImageLandscape(Builder builder){
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
                                @NonNull String hash,
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
                //ToDo review (see video)
                return ThumbnailUtils.extractThumbnail(image,
                                                       DESCRIPTION_THUMB_LANDSCAPE_WIDTH,
                                                       DESCRIPTION_THUMB_LANDSCAPE_HEIGHT);
            }
            else
                return null;
        }

        @Override
        public Description build(){
            setMetadata();
            isStored();
            return new ImageLandscape(this);
        }
    }
}