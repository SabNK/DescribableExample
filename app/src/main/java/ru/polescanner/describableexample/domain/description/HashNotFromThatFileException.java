package ru.polescanner.describableexample.domain.description;

public class HashNotFromThatFileException extends RuntimeException {
    public HashNotFromThatFileException(String s) {
        super(s);
    }
}

