package ru.polescanner.describableexample.domain.base;

import java.util.ArrayList;
import java.util.List;

public interface Describable {

    DescriptionImpl getAvatar();

    default List<DescriptionImpl> getDescriptions(){
        //ToDo attempt to DRY
        List<DescriptionImpl> describable = new ArrayList<>();
        return describable;
    }

    void addDescription(DescriptionImpl desc);

    void setAvatar(DescriptionImpl desc);

}
