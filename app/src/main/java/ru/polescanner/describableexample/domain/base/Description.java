package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public abstract class Description {
    //ToDo change to final
    private final Bitmap thumbnail;
    private final String thumbnail64;
    final Metadata metadata;
    final String filename;
    final String hash; //to provide cleaning of copies

    protected Description(@NonNull Bitmap thumbnail,
                          @NonNull Metadata metadata,
                          @NonNull String filename,
                          @NonNull String hash) {
        this.thumbnail = thumbnail;
        this.thumbnail64 = thumbnail64(thumbnail);
        this.metadata = metadata;
        this.filename = filename;
        this.hash = hash;
    }


    public abstract String toString64();

    private String thumbnail64(@NonNull Bitmap image){
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getMetadata() {
        return metadata.toString();
    }

    public static class Metadata {
        private final String author;
        //ToDo Check LocalDate? - Date is legacy.
        private final LocalDate date;
        //ToDo Checks from SecureByDesign
        public Metadata(@NonNull final String author, @NonNull final LocalDate date) {
            this.author = author;
            this.date = date;
        }

        public Metadata(@NonNull final String author, @NonNull final String dateInString) {
            this(author, LocalDate.parse(dateInString));
        }

        public Metadata(@NonNull final String author) {
            this(author, LocalDate.now());
        }
        //ToDo Add constructor with no args - take author in singlton
        public Metadata() {
            this("");
        }

        @NonNull
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            return this.author + " " + this.date.format(formatter);
        }
    }
}
