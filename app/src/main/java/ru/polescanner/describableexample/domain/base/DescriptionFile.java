package ru.polescanner.describableexample.domain.base;

import android.content.Context;

public interface DescriptionFile {
    boolean isStored(Context c);
    boolean isCorrupted(Context c);
    void loadFromServer();
    void loadToServer();


    public static class FakeDescriptionFile implements DescriptionFile {
        protected boolean isStored;

        public FakeDescriptionFile(boolean isStored) {
            this.isStored = isStored;
        }

        @Override
        public boolean isStored(Context c) {
            return isStored;
        }

        @Override
        public boolean isCorrupted(Context c) {
            return false;
        }

        @Override
        public void loadFromServer() {
            isStored = true;
        }

        @Override
        public void loadToServer() {
            isStored = false;
        }
    }

}


