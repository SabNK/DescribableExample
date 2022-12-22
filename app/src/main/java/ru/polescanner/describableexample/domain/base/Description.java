package ru.polescanner.describableexample.domain.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import ru.polescanner.describableexample.domain.description.WeHaveNoFile;

public interface Description {

    String thumbnail64();
    String metadata();
    boolean isStored(Context c);
    Intent explore(Context c) throws WeHaveNoFile;
    Intent editAndRefer(Context c) throws WeHaveNoFile;
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
        public String thumbnail64() {
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
        public Intent editAndRefer(Context c) throws WeHaveNoFile {
            checkIsStored(c);
            return null;
        }

        @Override
        public void download() {
            file.loadFromServer();
        }


    }
}
