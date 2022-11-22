package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Note extends DescriptionImpl {

    private Note(Builder builder){
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
        return "*/*";
        /*
        String[] mimetypes = {"text/plain", "text/html", "text/richtext",
                              "application/rtf", "application/x-rtf", "text/rtf"};
        fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
         */
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
        public DescriptionImpl build(){
            setMetadata();
            return new Note(this);
        }
    }
}
