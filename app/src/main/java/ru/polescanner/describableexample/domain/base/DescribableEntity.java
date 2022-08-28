package ru.polescanner.describableexample.domain.base;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DescribableEntity implements Describable{
    List<Description> descriptions;
    Image avatar;

    public DescribableEntity() {
        this.descriptions = new ArrayList<>();
    }

    @Override
    public Description getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(Description desc) {
        if (desc instanceof Image) {
            if (!descriptions.contains(desc))
                addDescription(desc);
            this.avatar = (Image) desc;
        }
    }

    @Override
    public List<Description> getDescriptions() {
        return descriptions;
    }

    @Override
    public void addDescription(Description desc) {
        descriptions.add(desc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DescribableEntity that = (DescribableEntity) o;

        if (descriptions != null ? !descriptions.equals(that.descriptions) : that.descriptions != null)
            return false;
        return avatar != null ? avatar.equals(that.avatar) : that.avatar == null;
    }

    @Override
    public int hashCode() {
        int result = descriptions != null ? descriptions.hashCode() : 0;
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        return result;
    }
}
