package ru.polescanner.describableexample.domain.base;

import java.util.List;

public interface Describable {

    public Description getAvatar();

    public default List<Description> getDescriptions(){
        //ToDo attempt to DRY
        List<Description> describable = null;
        return describable;
    };

    public void addDescription(Description desc);

}
