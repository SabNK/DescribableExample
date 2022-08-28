package ru.polescanner.describableexample.domain.base;

public class WeFacedExternalStorageProblems extends RuntimeException {
    public WeFacedExternalStorageProblems(String message) {
        super(message);
    }
}
