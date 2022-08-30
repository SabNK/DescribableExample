package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public abstract class Description {
    //ToDo change to final
    private final Bitmap thumbnail;
    private final String thumbnail64;
    final Metadata metadata;
    final String filepath;
    final String hash; //to provide cleaning of copies
    protected boolean isStored;
    protected DescriptionIO utility;

    protected Description(@NonNull Bitmap thumbnail,
                          @NonNull Metadata metadata,
                          @NonNull String filepath,
                          @NonNull String hash,
                          boolean isStored,
                          @NonNull DescriptionIO utility) {
        this.thumbnail = thumbnail;
        this.thumbnail64 = "";//thumbnail64(thumbnail);
        this.metadata = metadata;
        this.filepath = filepath;
        this.hash = hash;
        this.isStored = isStored;
        this.utility = utility;
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

    //ToDo Move to Retrofit package
    private static String thumbnail64(@NonNull Bitmap image){
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

    public boolean isStored() {
        return isStored;
    }

    public Intent view() throws WeHaveNoFile, WeFacedExternalStorageProblems{
        if (!isStored) throw new WeHaveNoFile("We need to download file. Do it immediate?");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        Uri data = utility.writeExternalStorage(filepath);
        if (data == null) return null;
        intent.setData(data);
        return intent;
    }

    public static class Metadata {
        private final String author;
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
        protected final String filepath;
        protected final String hash;
        protected boolean isStored;
        protected DescriptionIO utility;

        protected GenericBuilder(@NonNull final String filepath,
                                 @Nullable final String hash,
                                 @NonNull final DescriptionIO utility) {
            this.filepath = filepath;
            this.utility = utility;
            this.hash = utility.hash(filepath, hash);
        }

        public B thumbnail(@Nullable Bitmap thumbnail) {
            this.thumbnail = thumbnail==null ? this.createThumbnail() : thumbnail;
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

        protected void isStored() {
            if (utility.isFileStored(filepath))
                this.isStored = true;
        }

        protected void checkThumbnail(){
            if (thumbnail == null)
                thumbnail = createThumbnail();
        }


        protected abstract Bitmap createThumbnail();

        protected abstract Description build();

    }
}
