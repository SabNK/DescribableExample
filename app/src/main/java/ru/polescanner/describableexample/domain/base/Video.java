package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//ToDo Check if Video has to be serializable
public class Video extends BaseDescription {

    private Video(Builder builder){
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

    @Override
    protected String intentType() {
        return "video/*";
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash) {
            super(filepath, hash);
        }

        @Override
        protected Bitmap createThumbnail(Context context) {
            return file.createVideoThumbnail(1000, context);
        }

        @Override
        public BaseDescription build(){
            setMetadata();
            return new Video(this);
        }
    }
}
