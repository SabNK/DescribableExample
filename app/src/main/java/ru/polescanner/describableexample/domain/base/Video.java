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
        super(builder.thumbnail,
              builder.metadata,
              builder.filepath,
              builder.hash,
              builder.isStored,
              builder.utility);
    }

    public static Builder video(@NonNull String filepath, @NonNull DescriptionIO utility) {
        return video(filepath, utility.hash(filepath, null), utility);
    }

    public static Builder video(@NonNull String filepath,
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
            return utility.createVideoThumbnail(filepath, 1000);
        }

        @Override
        public Description build(){
            setMetadata();
            isStored();
            checkThumbnail();
            return new Video(this);
        }
    }
}
