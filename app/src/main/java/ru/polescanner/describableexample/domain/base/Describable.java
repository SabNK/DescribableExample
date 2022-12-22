package ru.polescanner.describableexample.domain.base;

import java.util.ArrayList;
import java.util.List;

import ru.polescanner.describableexample.domain.description.BaseDescription;

public interface Describable {

    BaseDescription getAvatar();

    default List<BaseDescription> getDescriptions(){
        //ToDo attempt to DRY
        List<BaseDescription> describable = new ArrayList<>();
        return describable;
    }

    void addDescription(BaseDescription desc);

    void setAvatar(BaseDescription desc);

}
