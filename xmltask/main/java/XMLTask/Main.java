package XMLTask;

import java.util.*;

public class Main {
    private static final Map<String, Person> personById = new HashMap<>();
    private static final Map<String, Set<String>> idsByName = new HashMap<>();
    private static final Map<String, Set<Person>> personsWithoutID = new HashMap<>();

    public static void main(String[] args) {
        String fileName = "src/main/resources/people.xml";
        ParserXML parserXML = new ParserXML();

        List<Person> persons = parserXML.parseXMLFile(fileName);

        System.out.println("Persons parse: " + persons.size());

        persons = combinePeople(persons);

        System.out.println("Persons combine: " + persons.size());

        List<Person> errors = persons.stream().filter(p ->
                !p.checkConsistency(personById)).toList();

        System.out.println("Errors: " + errors.size());

        for (Person problemPerson : errors) {
            Set<Person> personsNoId = new HashSet<>();
            for (final Set<Person> personSet : personsWithoutID.values()) {
                for (Person personNoId : personSet)
                    if (personNoId.getFullName().equals(problemPerson.getFullName())) {
                        personsNoId.add(personNoId);
                    }
            }
            solveProblem(problemPerson, personsNoId);
        }

        for (final Person person : personById.values()) {
            if (person.getSpouse() == null || person.getChildrenNumber() == null) {
                person.setChildrenNumber(0);
            }
        }

        errors = persons.stream().filter(p ->
                !p.checkConsistency(personById)).toList();

        System.out.println("Errors: " + errors.size());

        System.out.println("Persons: " + persons.size());

        //printPersons(persons);
    }

    private static void printPersons(List<Person> personList) {
        for (Person p : personList) {
            System.out.println(p.toStringMain());
            System.out.println();
        }
    }

    private static List<Person> combinePeople(List<Person> persons) {
        for (final Person person : persons) {
            final String id = person.getId();
            if (id == null) {
                Set<Person> personsWithNoId = personsWithoutID.getOrDefault(person.getFullName(), new HashSet<>());
                personsWithNoId.add(person);
                personsWithoutID.put(person.getFullName(), personsWithNoId);
                continue;
            }

            if (personById.containsKey(id)) {
                Person collision = personById.get(id);
                person.mergePerson(collision);
            }
            personById.put(id, person);
        }

        for (final Person person : personById.values()) {
            final String name = person.getFullName();
            if (person.getId() == null || name == null) {
                throw new RuntimeException("Person must have ID and name");
            }

            Set<String> ids = idsByName.getOrDefault(name, new HashSet<>());
            ids.add(person.getId());
            idsByName.put(name, ids);
        }

        List<String> conflicts = new ArrayList<>();

        for (final Person person : persons) {
            final String name = person.getFullName();
            if (name == null) continue;

            final Set<String> ids = idsByName.get(name);
            final String id = ids.size() == 1 ?
                    ids.iterator().next() : null;

            if (ids.size() > 1) {
                conflicts.addAll(ids);
            }

            if (id != null) {
                person.setId(id);
                personById.get(id).mergePerson(person);
            }
        }

        for (final Person person : personById.values()) {
            if (person.getSpouse() != null) {
                final Set<Person> spouseToAdd = new HashSet<>();
                connectRelatives(spouseToAdd, person.getSpouse());

                if (!spouseToAdd.isEmpty()) {
                    final Person spouse = spouseToAdd.iterator().next();
                    person.setSpouse(spouse);
                }
            }

            Set<Person> childrenToAdd = new HashSet<>();
            for (final Person child : person.getChildren()) {
                connectRelatives(childrenToAdd, child);
            }
            person.getChildren().clear();
            childrenToAdd.forEach(person::addChild);

            Set<Person> siblingsToAdd = new HashSet<>();
            for (final Person sibling : person.getSiblings()) {
                connectRelatives(siblingsToAdd, sibling);
            }
            person.getSiblings().clear();
            siblingsToAdd.forEach(person::addSibling);

            Set<Person> parentsToAdd = new HashSet<>();
            for (final Person parent : person.getParents()) {
                connectRelatives(parentsToAdd, parent);
            }
            person.getParents().clear();
            parentsToAdd.forEach(person::addParent);
        }

        return personById.values().stream()
                .sorted(Comparator.comparing(Person::getId)).toList();
    }

    private static void connectRelatives(final Set<Person> peopleToAdd, final Person person) {
        if (person.getId() != null) {
            final String id = person.getId();
            peopleToAdd.add(personById.get(id));
        }
        else if (person.getFullName() != null) {
            final String name = person.getFullName();
            final Set<String> ids = idsByName.get(name);

            if (ids.size() == 1) {
                final String id = ids.iterator().next();
                peopleToAdd.add(personById.get(id));
            }
        }
    }

    private static void solveProblem(Person person, Set<Person> others) {
        if (person.getGender() == null) {
            for (Person other : others) {
                if (other.getSpouse() != null && Objects.equals(person.getId(), other.getSpouse().getId())) {
                    if (Objects.equals(other.getSpouse().getGender(), Gender.MALE))
                        person.setGender(Gender.MALE);
                    else if (Objects.equals(other.getSpouse().getGender(), Gender.FEMALE))
                        person.setGender(Gender.FEMALE);
                }
            }
        }

        if (person.getGender() == null) {
            for (Person other : others) {
                for (Person otherSibling : other.getSiblings()) {
                    for (Person personSibling : person.getSiblings()) {
                        if (personSibling.getId().equals(otherSibling.getId())) {
                            if (other.getGender() != null) {
                                person.setGender(other.getGender());
                            }
                        }
                    }
                }
            }
        }

        if (person.getGender() == null) {
            int femaleCount = 0;
            int maleCount = 0;

            for (Person other : others) {
                if (other.getGender() != null && other.getGender().equals(Gender.MALE))
                    maleCount++;
                if (other.getGender() != null && other.getGender().equals(Gender.FEMALE))
                    femaleCount++;
            }

            if (femaleCount == 0 && maleCount > 0)
                person.setGender(Gender.MALE);
            if (maleCount == 0 && femaleCount > 0)
                person.setGender(Gender.FEMALE);
        }

        if (person.getSpouse() == null) {
            for (Person other : others) {
                if (other.getSpouse() != null && Objects.equals(person.getId(), other.getSpouse().getId())) {
                    person.setSpouse(other);
                    if (person.getSpouse().getId() == null) {
                        for (Person innerOther : others) {
                            if (person.getSpouse() != null &&
                                    person.getSpouse().getFullName().equals(innerOther.getFullName()) &&
                                    innerOther.getSpouse() != null &&
                                    innerOther.getSpouse().getId() != null &&
                                    !Objects.equals(innerOther.getSpouse().getId(), person.getId())) {
                                person.setSpouse(personById.get(innerOther.getSpouse().getId()));
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (person.getSpouse() != null && person.getSpouse().getId() == null) {
            Set<String> spouces = idsByName.get(person.getSpouse().getFullName());
            for (String spouce : spouces) {
                for (Person possibleChild : personById.get(spouce).getChildren()) {
                    if (person.getChildren().contains(possibleChild)) {
                        person.resetSpouse();
                        person.setSpouse(personById.get(spouce));
                        break;
                    }
                }
                if (person.getSpouse().getId() != null) {
                    break;
                }
            }
        }

        if (person.getSpouse() != null && person.getSpouse().getGender() == null) {
            if ((Objects.equals(person.getGender(), Gender.MALE))) {
                person.getSpouse().setGender(Gender.FEMALE);
            }
            else if ((Objects.equals(person.getGender(), Gender.FEMALE))) {
                person.getSpouse().setGender(Gender.MALE);
            }
        }

        if (person.getChildrenNumber() == null || person.getChildrenNumber() > person.getChildren().size()) {
            if (person.getSpouse() != null) {
                for (Person spouseChild : person.getSpouse().getChildren()) {
                    person.getChildren().add(spouseChild);
                    spouseChild.addParent(person);
                }
            }
        }

        if (person.getSpouse() != null && person.getSpouse().getId() == null) {
            if (person.getSpouse().getSiblingsNumber() != null && person.getSpouse().getSiblingsNumber() == 1) {
                Set<String> spouceSiblings = idsByName.get(person.getSpouse().getFullName());
                if (spouceSiblings.size() == 2) {
                    for (String sibling : spouceSiblings) {
                        if (Objects.equals(person.getId(), sibling))
                            continue;
                        if (personById.get(sibling).getId() != null) {
                            person.setSpouse(personById.get(sibling));
                            person.getSpouse().setSpouse(personById.get(person.getId()));
                        }
                    }
                }
            }
        }
    }
}
