package ru.polescanner.describableexample.domain.base;

//ToDo Check if Video has to be serializable
public class Video /*extends Description*/ {
    //TODO security issue
    private final String videoHash;

    public Video(String videoHash) {
        super();
        this.videoHash = videoHash;
    }

    //@Override
    public String toString64() {
        return "";
    }
}
