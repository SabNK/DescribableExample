package ru.polescanner.describableexample.domain.base;

import java.util.ArrayList;
import java.util.List;

public interface Describable {

    public Description getAvatar();

    public default List<Description> getDescriptions(){
        //ToDo attempt to DRY
        List<Description> describable = new ArrayList<>();
        return describable;
    }

    public void addDescription(Description desc);

}
