package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Audio extends DescriptionImpl {

    private Audio(Builder builder){
        super(builder.thumbnail,
              builder.metadata,
              builder.file);
    }

    @Override
    protected String intentType(){
        return "/*"; //ToDo complete with MediaRecorder
    }


    public static Builder audio(@NonNull String filepath, @NonNull Context context) {
        return audio(filepath, null, context);
    }

    public static Builder audio(@NonNull String filepath,
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
            return null;
        }

        @Override
        public DescriptionImpl build(){
            setMetadata();
            return new Audio(this);
        }
    }

}
