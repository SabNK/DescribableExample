package ru.polescanner.describableexample.domain.base;

public interface DescriptionFile {
    boolean isStored();
    boolean isCorrupted();
    void loadFromServer();
    void loadToServer();


    public static class FakeDescriptionFile implements DescriptionFile {
        protected boolean isStored;

        public FakeDescriptionFile(boolean isStored) {
            this.isStored = isStored;
        }

        @Override
        public boolean isStored() {
            return isStored;
        }

        @Override
        public boolean isCorrupted() {
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


