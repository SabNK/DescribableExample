package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Audio extends BaseDescription {

    private Audio(Builder builder){
        super(builder.thumbnail,
              builder.metadata,
              builder.file);
    }

    @Override
    protected String intentType(){
        return "/*"; //ToDo complete with MediaRecorder
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
            return null;
        }

        @Override
        public BaseDescription build(){
            setMetadata();
            return new Audio(this);
        }
    }

}
