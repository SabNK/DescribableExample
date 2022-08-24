package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;


public class Image2 extends Description {

    private Image2(Builder builder){
        super(builder.thumbnail, builder.metadata, builder.filepath, builder.hash, builder.isStored);
    }

    public static Builder image(@NonNull String filepath) {
        return image(filepath, Builder.hash(filepath));
    }

    public static Builder image(@NonNull String filepath, @NonNull String hash) {
        return new Builder(filepath, hash);
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash) {
            super(filepath, hash);
        }

        @Override
        protected Bitmap createThumbnail() {
            return null;
        }

        @Override
        public Description build(){
            setMetadata();
            isStored();
            return new Image2(this);
        }
    }
}