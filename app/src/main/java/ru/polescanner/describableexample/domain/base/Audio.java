package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class Audio extends Description{

    private Audio(Builder builder){
        super(builder.thumbnail,
              builder.metadata,
              builder.filepath,
              builder.hash,
              builder.isStored,
              builder.utility);
    }
    public static Builder audio(@NonNull String filepath, @NonNull DescriptionIO utility) {
        return audio(filepath, utility.hash(filepath, null), utility);
    }

    public static Builder audio(@NonNull String filepath,
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
            checkThumbnail();
            return new Audio(this);
        }
    }

}
