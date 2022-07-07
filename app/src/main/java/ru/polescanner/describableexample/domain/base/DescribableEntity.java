package ru.polescanner.describableexample.domain.base;

import java.util.ArrayList;
import java.util.List;

public class DescribableEntity implements Describable{
    List<Description> descriptions;

    public DescribableEntity() {
        this.descriptions = new ArrayList<>();
    }

    @Override
    public Description getAvatar() {
        return null;
    }

    @Override
    public List<Description> getDescriptions() {
        return descriptions;
    }

    @Override
    public void addDescription(Description desc) {
        descriptions.add(desc);
    }
}
