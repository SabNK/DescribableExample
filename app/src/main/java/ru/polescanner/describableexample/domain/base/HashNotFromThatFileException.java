package ru.polescanner.describableexample.domain.base;

public class HashNotFromThatFileException extends RuntimeException {
    public HashNotFromThatFileException(String s) {
        super(s);
    }
}

