package ru.polescanner.describableexample.domain.description;

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
import java.util.Objects;

import ru.polescanner.describableexample.domain.base.Description;
import ru.polescanner.describableexample.domain.base.DescriptionFile;
import ru.polescanner.describableexample.domain.base.User;
import ru.polescanner.describableexample.domain.geo.Geo;


public abstract class BaseDescription implements Description {
    //ToDo change to final
    private final String thumbnail64;
    protected final Metadata metadata;
    protected final DescriptionFile file;
    protected Description reference;

    protected BaseDescription(@NonNull String thumbnail64,
                              @NonNull Metadata metadata,
                              @NonNull DescriptionFile file,
                              @Nullable Description reference) {

        this.thumbnail64 = thumbnail64;
        this.metadata = metadata;
        this.file = file;
        if (reference == null)
            this.reference = this;
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

    @Override
    public String thumbnail64() {
        return thumbnail64;
    }
    @Override
    public String metadata() {
        return metadata.toString();
    }
    @Override
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

    //ToDo Think of crash during edit. When to make a new Description.
    @Override
    public Intent editAndRefer(Context context) throws WeHaveNoFile {
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

        //ToDo Add constructor with no args - take author from singleton
        public Metadata() {
            this.author = null;
            this.timestamp = Instant.now().toEpochMilli();
            this.location = null;
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
        protected String thumbnail64;
        protected Metadata metadata;
        protected User author;
        protected long timestamp;
        protected Geo location;
        protected DescriptionFile file;
        protected Description reference;

        protected GenericBuilder(@NonNull final String filepath,
                                 @NonNull final String hash) {
            this.file = new BaseDescriptionFile(filepath, hash);
        }

        protected GenericBuilder(@NonNull final String filepath) throws FileNotFoundException {
            this.file = new BaseDescriptionFile(filepath);
        }

        public B thumbnail(@NonNull String thumbnail64) {
            this.thumbnail64 = thumbnail64;
            return self();
        }

        public B thumbnail(Context context) {
            this.thumbnail64 = this.createThumbnail64(context);
            return self();
        }

        public B author(User author){
            this.author = author;
            return self();
        }

        public B timestamp(long timestamp){
            this.timestamp = timestamp;
            return self();
        }

        public B location(Geo location){
            this.location = location;
            return self();
        }

        public B referenceDescription(Description reference){
            this.reference = reference;
            return self();
        }

        @SuppressWarnings("unchecked")
        private final B self() {
            return (B) this;
        }

        protected void setMetadata() {
            if (this.author != null) {
                if (this.timestamp != 0L)
                    this.metadata = new Metadata(this.author, this.date);
                else
                    this.metadata = new Metadata(this.author);
            } else
                this.metadata = new Metadata();
        }

        //ToDo Unclear method. Check - boolean?
        protected void checkThumbnail(Context context){
            if (thumbnail64 == null)
                thumbnail64 = createThumbnail64(context);
        }

        protected abstract String createThumbnail64(Context context);

        protected abstract BaseDescription build();

        protected String thmbnail64(@NonNull Bitmap image){
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
            byte[] b = byteArrayBitmapStream.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }

    }
}
