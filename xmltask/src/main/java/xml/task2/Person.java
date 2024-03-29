package xml.task2;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "xml.task.people")
public class Person {

    public Person() {
        parents = new HashSet<>();

        brothers = new HashSet<>();
        sisters = new HashSet<>();
        siblings = new HashSet<>();

        sons = new HashSet<>();
        daughters = new HashSet<>();
        children = new HashSet<>();
    }

    public Person(@NonNull String id) {
        this();
        this.id = id;
    }

    /* FIELDS */

    @XmlID
    @XmlAttribute
    private String id;

    @XmlElement(name = "first-name", required = true)
    private String firstName;

    @XmlElement(name = "last-name", required = true)
    private String lastName;

    @XmlAttribute(required = true)
    private Gender gender;

    @XmlAttribute(name = "children-number")
    private Integer childrenNumber;

    @XmlAttribute(name = "siblings-number")
    private Integer siblingsNumber;

    @XmlIDREF
    private Person father;

    @XmlIDREF
    private Person mother;

    @XmlTransient
    private final Set<Person> parents;

    @XmlIDREF
    private Person spouse;

    @XmlElementWrapper(name = "brothers")
    @XmlElement(name = "brother")
    @XmlIDREF
    private final Set<Person> brothers;

    @XmlElementWrapper(name = "sisters")
    @XmlElement(name = "sister")
    @XmlIDREF
    private final Set<Person> sisters;

    @XmlTransient
    private final Set<Person> siblings;

    @XmlElementWrapper(name = "sons")
    @XmlElement(name = "son")
    @XmlIDREF
    private final Set<Person> sons;

    @XmlElementWrapper(name = "daughters")
    @XmlElement(name = "daughter")
    @XmlIDREF
    private final Set<Person> daughters;

    @XmlTransient
    private final Set<Person> children;

    /* GETTERS */

    public String getFullName() {
        if ((firstName != null) && (lastName != null)) {
            return String.format("%s %s", firstName, lastName);
        }
        return null;
    }

    /* SETTERS */

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setFirstName(@NonNull String firstName) {
        if ((this.firstName != null) && !this.firstName.equals(firstName)) {
            throw new Error("FirstName already set");
        }
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        if ((this.lastName != null) && !this.lastName.equals(lastName)) {
            throw new Error("LastName already set");
        }
        this.lastName = lastName;
    }

    public void setFullName(@NonNull String fullName) {
        String[] words = fullName.split(" +");
        setFirstName(words[0]);
        setLastName(words[1]);
    }

    public void setGender(@NonNull Gender gender) {
        if ((this.gender != null) && !this.gender.equals(gender)) {
            throw new Error("Gender already set");
        }
        this.gender = gender;
    }

    public void setChildrenNumber(int childrenNumber) {
        if ((this.childrenNumber != null) && (this.childrenNumber != childrenNumber)) {
            throw new Error("Children number already set");
        }
        this.childrenNumber = childrenNumber;
    }

    public void setSiblingsNumber(int siblingsNumber) {
        if ((this.siblingsNumber != null) && (this.siblingsNumber != siblingsNumber)) {
            throw new Error("Siblings number already set");
        }
        this.siblingsNumber = siblingsNumber;
    }

    public void setSpouse(@NonNull Person spouse) {
        if ((this.spouse != null) && (this.spouse.getId() != null) && !spouse.equals(this.spouse)) {
            throw new Error("Spouse already set");
        }

        this.spouse = spouse;
        spouse.spouse = this;

        if (spouse.childrenNumber != null) {
            this.setChildrenNumber(spouse.childrenNumber);
        }
        else if (childrenNumber != null) {
            spouse.setChildrenNumber(childrenNumber);
        }
    }

    public void setSpouse(@NonNull String name) {
        if ((spouse != null) && !Objects.equals(name, spouse.getFirstName())) {
            throw new Error("Spouse already set");
        }
        Person spouse = new Person();
        spouse.setFullName(name);
        setSpouse(spouse);
    }

    public void setWife(@NonNull String id) {
        if ((spouse != null) && !Objects.equals(id, spouse.getId())) {
            throw new Error("Wife already set");
        }
        Person wife = new Person(id);
        wife.setGender(Gender.FEMALE);
        setSpouse(wife);
        setGender(Gender.MALE);
    }

    public void setHusband(@NonNull String id) {
        if ((spouse != null) && !Objects.equals(id, spouse.getId())) {
            throw new Error("Husband already set");
        }
        Person husband = new Person(id);
        husband.setGender(Gender.MALE);
        setSpouse(husband);
        setGender(Gender.FEMALE);
    }

    public void resetSpouse() {
        spouse = null;
    }

    /* METHODS */

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

    public boolean isConsistent(@NonNull final Map<String, Person> personById) {
        return (id != null) && (firstName != null) && (lastName != null) && (gender != null) &&
                (parents.size() <= 2) && (childrenNumber != null) && (siblingsNumber != null) &&
                (children.size() == childrenNumber) && (siblings.size() == siblingsNumber) &&
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
                Stream.concat(Stream.of(children, siblings, parents)
                                .flatMap(Collection::stream),
                        Stream.ofNullable(spouse))
                        .allMatch(p -> p.id != null && personById.get(p.id) == p);
    }

    public void lightMerge(@NonNull Person person) {
        if ((id == null) || (person.id == null) || !id.equals(person.id)) {
            System.err.println(this);
            System.err.println(person);
            throw new IllegalStateException("Incorrect id");
        }

        if (person.firstName != null)
            setFirstName(person.firstName);

        if (person.lastName != null)
            setLastName(person.lastName);

        if (person.gender != null)
            setGender(person.gender);

        if (person.childrenNumber != null)
            setChildrenNumber(person.childrenNumber);

        if (person.siblingsNumber != null)
            setSiblingsNumber(person.siblingsNumber);
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
        return String.format("Person: id: %s%n" +
                        "firstName: %s, lastName: %s%n" +
                        "gender: %s%n" +
                        "children: %d, siblings: %d",
                id, firstName, lastName, gender, childrenNumber, siblingsNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || (getClass() != obj.getClass()))
            return false;

        Person person = (Person) obj;
        if ((id != null) && (person.id != null) && (id.equals(person.id)))
            return true;

        return (getFullName() != null) &&
                (person.getFullName() != null) &&
                getFullName().equals(person.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, gender, childrenNumber, siblingsNumber);
    }
}
