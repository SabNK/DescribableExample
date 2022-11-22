package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public abstract class DescriptionImpl implements Description{
    //ToDo change to final
    private final Bitmap thumbnail;
    private final String thumbnail64;
    final Metadata metadata;
    final DescriptionFileImpl file;

    protected DescriptionImpl(@NonNull Bitmap thumbnail,
                              @NonNull Metadata metadata,
                              @NonNull DescriptionFileImpl file) {
        this.thumbnail = thumbnail;
        this.thumbnail64 = "";//thumbnail64(thumbnail);
        this.metadata = metadata;
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptionImpl d = (DescriptionImpl) o;
        return Objects.equals(file, d.getFile());
    }
    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    public DescriptionFile getFile() {
        return this.file;
    }

    //ToDo Move to Retrofit package
    private static String thumbnail64(@NonNull Bitmap image){
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap thumbnail() {
        return thumbnail;
    }

    public String metadata() {
        return metadata.toString();
    }

    public boolean isStored(Context context) {
        return file.isStored(context);
    }

    @Override
    public Intent explore(Context context) throws WeHaveNoFile, WeFacedExternalStorageProblems {
        checkIsStored(context);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(intentType());
        Uri data = file.writeExternalStorage(context);
        if (data == null) return null; //ToDo Refactor Bugaenko
        intent.setData(data);
        return intent;
    };

    @Override
    public Intent addDetails(Context context) throws WeHaveNoFile {
        checkIsStored(context);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType(intentType());
        Uri data = file.writeExternalStorage(context);
        if (data == null) return null; //ToDo Refactor Bugaenko
        intent.setData(data);
        return intent;
    }

    protected abstract String intentType();

    public void download() {
        this.file.loadFromServer();
    }

    //Contract
    public static GenericBuilder description(@NonNull String filepath,
                                             @Nullable String hash){
        return null;
    };

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

        //ToDo Add constructor with no args - take author from singlton
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
        protected Metadata metadata;
        protected String author;
        protected LocalDate date;
        protected DescriptionFileImpl file;

        protected GenericBuilder(@NonNull final String filepath,
                                 @NonNull final String hash) {
            this.file = new DescriptionFileImpl(filepath, hash);
        }

        protected GenericBuilder(@NonNull final String filepath) throws FileNotFoundException {
            this.file = new DescriptionFileImpl(filepath);
        }

        public B thumbnail(@Nullable Bitmap thumbnail, Context context) {
            this.thumbnail = thumbnail==null ? this.createThumbnail(context) : thumbnail;
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

        protected void checkThumbnail(Context context){
            if (thumbnail == null)
                thumbnail = createThumbnail(context);
        }

        protected abstract Bitmap createThumbnail(Context context);

        protected abstract DescriptionImpl build();

    }
}
