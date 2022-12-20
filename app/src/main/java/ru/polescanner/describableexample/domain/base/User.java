package ru.polescanner.describableexample.domain.base;

public class User extends DescribableEntity{
    String firstName;
    String lastName;
    String acronym;

    public User(String firstName, String lastName, String acronym) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.acronym = acronym;
    }

    public User register(String name, String familyname, String acronym) {
        return new User(name, familyname, acronym);
    }


    public String acronym() {
        return this.acronym;
    }
}
