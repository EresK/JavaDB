package XMLTask;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
public class Person {
    private String id = "";
    private String firstName;
    private String lastName;
    private String fullName;
    private String gender;

    private HashSet<String> names;

    private int siblingsNumber;
    private int childrenNumber;

    private final HashSet<Person> parents;
    private final HashSet<Person> siblings;
    private final HashSet<Person> spouse;
    private final HashSet<Person> children;

    public Person() {
        names = new HashSet<>();

        parents = new HashSet<>();
        siblings = new HashSet<>();
        spouse = new HashSet<>();
        children = new HashSet<>();
    }

    public Person(String id) {
        this.id = id;

        names = new HashSet<>();

        parents = new HashSet<>();
        siblings = new HashSet<>();
        spouse = new HashSet<>();
        children = new HashSet<>();
    }

    public void setSiblingsNumber(int siblingsNumber) {
        if (siblingsNumber > this.siblingsNumber)
            this.siblingsNumber = siblingsNumber;
    }

    public void setChildrenNumber(int childrenNumber) {
        if (childrenNumber > this.childrenNumber)
            this.childrenNumber = childrenNumber;
    }

    public boolean hasId() {
        return (id != null) && !id.equals("");
    }
}
