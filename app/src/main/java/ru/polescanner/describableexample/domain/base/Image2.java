package ru.polescanner.describableexample.domain.base;

import androidx.annotation.NonNull;


public class Image2 extends Description {

    private Image2(Builder builder){
        super(builder.thumbnail, builder.metadata, builder.filepath, builder.hash, builder.isStored);
    }

    public static Builder image(@NonNull String filename) {
        return new Builder(filename);
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filename) {
            super(filename);
        }

        @Override
        public Description build(){
            setMetadata();
            return new Image2(this);
        }
    }
}