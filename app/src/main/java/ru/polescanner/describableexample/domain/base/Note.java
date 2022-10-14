package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class Note extends DescriptionImpl {

    private Note(Builder builder){
        super(builder.thumbnail,
              builder.metadata,
              builder.file);
    }
    public static Builder note(@NonNull String filepath, @NonNull Context context) {
        return note(filepath, null, context);
    }

    public static Builder note(@NonNull String filepath,
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
            return null;
        }

        @Override
        public DescriptionImpl build(){
            setMetadata();
            isStored();
            return new Note(this);
        }
    }

}
