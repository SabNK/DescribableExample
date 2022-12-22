package ru.polescanner.describableexample.domain.base;

public class BaseUser extends DescribableEntity implements User{
    String firstName;
    String lastName;
    String acronym;

    public BaseUser(String firstName, String lastName, String acronym) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.acronym = acronym;
    }

    public BaseUser register(String name, String familyname, String acronym) {
        return new BaseUser(name, familyname, acronym);
    }

    @Override
    public String acronym() {
        return this.acronym;
    }
}
