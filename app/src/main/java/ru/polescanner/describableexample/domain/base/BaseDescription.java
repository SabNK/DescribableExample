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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Objects;

import ru.polescanner.describableexample.domain.geo.Geo;


public abstract class BaseDescription implements Description{
    //ToDo change to final
    private final String thumbnail64;
    protected final Metadata metadata;
    protected final DescriptionFileImpl file;
    protected Description reference;

    protected BaseDescription(@NonNull String thumbnail64,
                              @NonNull Metadata metadata,
                              @NonNull DescriptionFileImpl file) {
        this.thumbnail64 = thumbnail64;
        this.metadata = metadata;
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDescription d = (BaseDescription) o;
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

    public String thumbnail() {
        return thumbnail64;
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
        private final User author;
        private final long timestamp;
        private final Geo location;

        //ToDo Checks from SecureByDesign
        public Metadata(@NonNull final User author,
                        @NonNull final long timestamp,
                        @NonNull final Geo location) {
            this.author = author;
            this.timestamp = timestamp;
            this.location = location;
        }

        public Metadata() {
            this.author = null;
            this.timestamp = Instant.now().toEpochMilli();
        }

        public Metadata(@NonNull final String author) {
            this(author, LocalDate.now());
        }

        //ToDo Add constructor with no args - take author from singleton
        public Metadata() {
            this("");
        }

        @NonNull
        @Override
        //ToDo Wrong Local TimeDate
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
            return this.author.acronym() + " " + localDate.format(formatter);
        }

    }


    abstract static class GenericBuilder<B extends GenericBuilder<B>> {
        protected String thumbnail;
        protected Metadata metadata;
        protected User author;
        protected Instant timestamp;
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

        protected abstract BaseDescription build();

    }
}
