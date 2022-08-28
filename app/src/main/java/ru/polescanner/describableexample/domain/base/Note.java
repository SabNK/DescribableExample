package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class Note extends Description{

    private Note(Builder builder){
        super(builder.thumbnail,
              builder.metadata,
              builder.filepath,
              builder.hash,
              builder.isStored,
              builder.utility);
    }
    public static Builder note(@NonNull String filepath, @NonNull DescriptionIO utility) {
        return note(filepath, utility.hash(filepath, null), utility);
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
        public Description build(){
            setMetadata();
            isStored();
            return new Note(this);
        }
    }

}
