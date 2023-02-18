package xml.task2;

import jakarta.xml.bind.annotation.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@XmlRootElement(namespace = "xml.tasks.people")
public class People {
    private final Map<String, Person> personMap = new HashMap<>();

    public People(List<Person> people){
        for (Person p: people) {
            personMap.put(p.getId(), p);
        }
    }

    @XmlElement(name = "person")
    public List<Person> getPeople() {
        return new ArrayList<>(personMap.values());
    }

    public void addPerson(Person person) {
        personMap.putIfAbsent(person.getId(), person);
    }

    public Person getPerson(Person person) {
        return personMap.get(person.getId());
    }

    public void deletePerson(Person person) {
        personMap.remove(person.getId());
    }
}
