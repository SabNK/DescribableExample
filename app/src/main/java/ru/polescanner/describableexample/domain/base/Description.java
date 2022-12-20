package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public interface Description {

    String thumbnail();
    String metadata();
    boolean isStored(Context c);
    Intent explore(Context c) throws WeHaveNoFile;
    Intent addDetails(Context c) throws WeHaveNoFile;
    void download();

    default void checkIsStored(Context c) throws WeHaveNoFile {
        if (!isStored(c)) throw new WeHaveNoFile("We have no file!");
    }

    public static class FakeDescription implements Description {
        protected DescriptionFile file;

        protected FakeDescription() {
            this.file = new DescriptionFile.FakeDescriptionFile(false);
        }

        @Override
        public Bitmap thumbnail() {
            return null;
        }

        @Override
        public String metadata() {
            return "Description Metadata";
        }

        @Override
        public boolean isStored(Context c) {
            return file.isStored(c);
        }

        @Override
        public Intent explore(Context c) throws WeHaveNoFile {
            checkIsStored(c);
            return null;
        }

        @Override
        public Intent addDetails(Context c) throws WeHaveNoFile {
            checkIsStored(c);
            return null;
        }

        @Override
        public void download() {
            file.loadFromServer();
        }


    }
}
