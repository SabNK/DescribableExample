package ru.polescanner.describableexample.domain.base;

import java.util.ArrayList;
import java.util.List;

import ru.polescanner.describableexample.domain.description.BaseDescription;
import ru.polescanner.describableexample.domain.description.Image;

public class DescribableEntity implements Describable{
    List<BaseDescription> descriptions;
    Image avatar;

    public DescribableEntity() {
        this.descriptions = new ArrayList<>();
    }

    @Override
    public BaseDescription getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(BaseDescription desc) {
        if (desc instanceof Image) {
            if (!descriptions.contains(desc))
                addDescription(desc);
            this.avatar = (Image) desc;
        }
    }

    @Override
    public List<BaseDescription> getDescriptions() {
        return descriptions;
    }

    @Override
    public void addDescription(BaseDescription desc) {
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
