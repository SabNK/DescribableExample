package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//ToDo Check if Video has to be serializable
public class Video extends DescriptionImpl {

    private Video(Builder builder){
        super(builder.thumbnail,
              builder.metadata,
              builder.file);
    }

    public static Builder video(@NonNull String filepath, @NonNull Context context) {
        return video(filepath, null, context);
    }

    public static Builder video(@NonNull String filepath,
                                @Nullable String hash,
                                @NonNull Context context) {
        return new Builder(filepath, hash, context);
    }

    @Override
    protected String intentType() {
        return "video/*";
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash, Context context) {
            super(filepath, hash, context);
        }

        @Override
        protected Bitmap createThumbnail() {
            return file.createVideoThumbnail(1000);
        }

        @Override
        public DescriptionImpl build(){
            setMetadata();
            return new Video(this);
        }
    }
}
