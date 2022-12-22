package ru.polescanner.describableexample.domain.description;

public class WeFacedExternalStorageProblems extends RuntimeException {
    public WeFacedExternalStorageProblems(String message) {
        super(message);
    }
}
