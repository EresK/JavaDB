package XMLTask;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Stream;

@Getter
public class Person {
    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Person spouse;

    private Integer childrenNumber;
    private Integer siblingsNumber;

    private final Set<Person> parents = new HashSet<>();
    private final Set<Person> siblings = new HashSet<>();
    private final Set<Person> children = new HashSet<>();

    public Person() {
    }

    public Person(@NonNull String id) {
        this.id = id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setFirstName(@NonNull String firstName) {
        if (this.firstName != null && !this.firstName.equals(firstName)) {
            throw new Error("FirstName already set");
        }
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        if (this.lastName != null && !this.lastName.equals(lastName)) {
            throw new Error("Lastname already set");
        }
        this.lastName = lastName;
    }

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return null;
    }

    public void setFullName(@NonNull String fullName) {
        String[] words = fullName.split(" +");
        setFirstName(words[0]);
        setLastName(words[1]);
    }

    public void setGender(@NonNull Gender gender) {
        if (this.gender != null && !this.gender.equals(gender)) {
            throw new Error("Gender already set");
        }
        this.gender = gender;
    }

    public void setChildrenNumber(int childrenNumber) {
        if (this.childrenNumber != null && this.childrenNumber != childrenNumber) {
            throw new Error("Children number already set");
        }
        this.childrenNumber = childrenNumber;
    }

    public void setSiblingsNumber(int siblingsNumber) {
        if (this.siblingsNumber != null && this.siblingsNumber != siblingsNumber) {
            throw new Error("Siblings number already set");
        }
        this.siblingsNumber = siblingsNumber;
    }

    public void addSibling(@NonNull Person sibling) {
        if (sibling.siblingsNumber == null) {
            sibling.siblingsNumber = siblingsNumber;
        }
        if (siblingsNumber == null) {
            siblingsNumber = sibling.siblingsNumber;
        }

        siblings.add(sibling);
        sibling.siblings.add(this);
    }

    public void addSibling(@NonNull String id) {
        addSibling(new Person(id));
    }

    public void addSibling(@NonNull String name, @NonNull Gender gender) {
        Person sibling = new Person();
        sibling.setFullName(name);
        sibling.setGender(gender);
        addSibling(sibling);
    }

    public void addChild(@NonNull Person child) {
        children.add(child);
        child.parents.add(this);
    }

    public void addChild(@NonNull String name) {
        Person child = new Person();
        child.setFullName(name);
        addChild(child);
    }

    public void addChild(@NonNull String id, @NonNull Gender gender) {
        Person child = new Person(id);
        child.setGender(gender);
        addChild(child);
    }

    public void addParent(@NonNull Person parent) {
        parent.children.add(this);
        parents.add(parent);
    }

    public void addParent(@NonNull String id) {
        Person parent = new Person(id);
        addParent(parent);
    }

    public void addParent(@NonNull String fullName, @NonNull Gender gender) {
        Person parent = new Person();
        parent.setFullName(fullName);
        parent.setGender(gender);
        addParent(parent);
    }

    public void setSpouse(@NonNull Person spouse) {
        if (this.spouse != null && this.spouse.getId() != null && !spouse.equals(this.spouse)) {
            throw new Error("Spouse already set");
        }

        this.spouse = spouse;
        spouse.spouse = this;

        if (spouse.childrenNumber != null) {
            this.setChildrenNumber(spouse.childrenNumber);
        }
        else if (this.childrenNumber != null) {
            spouse.setChildrenNumber(this.childrenNumber);
        }
    }

    public void setSpouse(@NonNull String name) {
        if (this.spouse != null && !Objects.equals(name, spouse.getFirstName())) {
            throw new Error("Spouse already set");
        }
        Person spouse = new Person();
        spouse.setFullName(name);
        setSpouse(spouse);
    }

    public void resetSpouse() {
        this.spouse = null;
    }

    public void setWife(@NonNull String id) {
        if (this.spouse != null && !Objects.equals(id, this.spouse.getId())) {
            throw new Error("Wife already set");
        }
        Person wife = new Person(id);
        wife.setGender(Gender.FEMALE);
        setSpouse(wife);
        setGender(Gender.MALE);
    }

    public void setHusband(@NonNull String id) {
        if (this.spouse != null && !Objects.equals(id, this.spouse.getId())) {
            throw new Error("Husband already set");
        }
        Person husband = new Person(id);
        husband.setGender(Gender.MALE);
        setSpouse(husband);
        setGender(Gender.FEMALE);
    }

    public boolean isConsistent(@NonNull final Map<String, Person> personById) {
        return id != null && firstName != null && lastName != null && gender != null &&
                parents.size() <= 2 && childrenNumber != null && siblingsNumber != null &&
                children.size() == childrenNumber && siblings.size() == siblingsNumber &&

                (spouse != null || childrenNumber == 0) &&

                children.stream()
                        .map(Person::getParents)
                        .allMatch(s -> Arrays.asList(s.toArray()).contains(this)) &&

                parents.stream()
                        .map(Person::getChildren)
                        .allMatch(s -> Arrays.asList(s.toArray()).contains(this)) &&

                siblings.stream()
                        .map(Person::getSiblings)
                        .allMatch(s -> Arrays.asList(s.toArray()).contains(this)) &&

                Stream.concat(
                        Stream.of(children, siblings, parents)
                                .flatMap(Collection::stream),
                        Stream.ofNullable(spouse)
                ).allMatch(p -> p.id != null && personById.get(p.id) == p);
    }

    public void lightMerge(@NonNull Person person) {
        if (this.id == null || person.id == null || !this.id.equals(person.id)) {
            System.err.println(this);
            System.err.println(person);
            throw new IllegalStateException("Incorrect id");
        }

        if (person.firstName != null)
            this.setFirstName(person.firstName);

        if (person.lastName != null)
            this.setLastName(person.lastName);

        if (person.gender != null)
            this.setGender(person.gender);

        if (person.childrenNumber != null)
            this.setChildrenNumber(person.childrenNumber);

        if (person.siblingsNumber != null)
            this.setSiblingsNumber(person.siblingsNumber);
    }

    public void mergePerson(@NonNull Person person) {
        if (person == this) return;

        lightMerge(person);

        if (person.spouse != null) {
            this.spouse = person.spouse;
            this.spouse.spouse = this;
        }

        for (Person parent : person.parents) {
            parent.children.remove(person);
            parent.children.add(this);
        }

        for (Person child : person.children) {
            child.parents.remove(person);
            child.parents.add(this);
            this.children.add(child);
        }

        for (Person sibling : person.siblings) {
            sibling.siblings.remove(person);
            sibling.siblings.add(this);
            this.siblings.add(sibling);
        }
    }

    @Override
    public String toString() {
        return "Person: id: " + id + "\n" +
                "firstName: " + firstName + ", lastName: " + lastName + "\n" +
                "gender: " + gender + "\n" +
                "children-number: " + childrenNumber + ", siblings-number: " + siblingsNumber;
    }

    public String toStringFull() {
        StringBuilder builder = new StringBuilder();
        builder.append("Person: id: ").append(id).append("\n")
                .append("firstName: ").append(firstName).append(", lastName: ").append(lastName).append("\n")
                .append("gender: ").append(gender).append("\n")
                .append("children-number: ").append(childrenNumber)
                .append(", siblings-number: ").append(siblingsNumber).append("\n");

        builder.append("parents: ");
        for (Person p: parents) {
            builder.append("id: ").append(p.id).append(" ").append(p.gender).append("; ");
        }
        builder.append("\n");

        builder.append("siblings: ");
        for (Person p: siblings) {
            builder.append("id: ").append(p.id).append(" ").append(p.gender).append("; ");
        }
        builder.append("\n");

        if (spouse != null)
            builder.append("spouse: id: ").append(spouse.id).append(" ").append(spouse.gender).append("\n");

        builder.append("children: ");
        for (Person p: children) {
            builder.append("id: ").append(p.id).append(" ").append(p.gender).append("; ");
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (this.id != null && person.id != null && this.id.equals(person.id))
            return true;

        return this.getFullName() != null && person.getFullName() != null && this.getFullName().equals(person.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, gender, childrenNumber, siblingsNumber);
    }
}
