package ru.polescanner.describableexample.domain.base;

import java.util.ArrayList;
import java.util.List;

public interface Describable {

    Description getAvatar();

    default List<Description> getDescriptions(){
        //ToDo attempt to DRY
        List<Description> describable = new ArrayList<>();
        return describable;
    }

    void addDescription(Description desc);

    void setAvatar(Description desc);

}
