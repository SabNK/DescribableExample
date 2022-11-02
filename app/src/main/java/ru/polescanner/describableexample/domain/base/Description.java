package ru.polescanner.describableexample.domain.base;

import android.content.Intent;
import android.graphics.Bitmap;

public interface Description {

    Bitmap thumbnail();
    String metadata();
    boolean isStored();
    Intent explore() throws WeHaveNoFile;
    Intent addDetails() throws WeHaveNoFile;
    void download();

    default void checkIsStored() throws WeHaveNoFile {
        if (!isStored()) throw new WeHaveNoFile("We have no file!");
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
        public boolean isStored() {
            return file.isStored();
        }

        @Override
        public Intent explore() throws WeHaveNoFile {
            checkIsStored();
            return null;
        }

        @Override
        public Intent addDetails() throws WeHaveNoFile {
            checkIsStored();
            return null;
        }

        @Override
        public void download() {
            file.loadFromServer();
        }


    }
}
