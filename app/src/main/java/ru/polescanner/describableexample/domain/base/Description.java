package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


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
        this.thumbnail64 = "";//thumbnail64(thumbnail);
        this.metadata = metadata;
        this.filename = filename;
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description d = (Description) o;
        return Objects.equals(hash, d.hash);
    }
    @Override
    public int hashCode() {
        return Objects.hash(hash);
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
            this(author, LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("dd.MM.yy")));
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

    abstract static class GenericBuilder<B extends GenericBuilder<B>> {
        protected Bitmap thumbnail;
        protected Description.Metadata metadata;
        protected String author;
        protected LocalDate date;
        protected final String filename;
        protected final String hash;

        protected GenericBuilder(String filename) {
            this.filename = filename;
            //ToDo check this
            this.hash = DescriptionUtility.getHash(filename);
        }

        public B thumbnail(Bitmap thumbnail) {
            this.thumbnail = thumbnail;
            return self();
        }

        public B author(String author){
            this.author = author;
            return self();
        }

        public B date(String dateDDPointMMPointYY){
            this.date = LocalDate.parse(dateDDPointMMPointYY, DateTimeFormatter.ofPattern("dd.MM.yy"));
            return self();
        }

        @SuppressWarnings("unchecked")
        private final B self() {
            return (B) this;
        }

        protected void setMetadata() {
            if (this.author != null) {
                if (this.date != null)
                    this.metadata = new Metadata(this.author, this.date);
                else
                    this.metadata = new Metadata(this.author);
            } else
                this.metadata = new Metadata();
        }

        protected abstract Description build();
    }
}
