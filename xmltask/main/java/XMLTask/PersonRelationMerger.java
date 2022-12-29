package XMLTask;

import java.util.*;

public class PersonRelationMerger {
    private final Map<String, Person> peopleWithId = new HashMap<>();
    private final Map<String, Set<String>> idsByName = new HashMap<>();
    private final Map<String, Set<Person>> peopleWithoutID = new HashMap<>();

    public List<Person> merge(List<Person> people) {
        List<Person> personList = handleRelations(people);

        List<Person> errors = personList.stream()
                .filter(p -> !p.isConsistent(peopleWithId))
                .toList();

        System.out.println("Persons after handle relations: " + personList.size() + "\n" +
                "Errors: " + errors.size());

        for (Person problemPerson : errors) {
            Set<Person> peopleWithoutId = new HashSet<>();

            for (Set<Person> personSet : peopleWithoutID.values()) {

                for (Person personWithoutId : personSet)
                    if (personWithoutId.getFullName().equals(problemPerson.getFullName())) {
                        peopleWithoutId.add(personWithoutId);
                    }
            }

            solveProblem(problemPerson, peopleWithoutId);
        }

        for (Person person : peopleWithId.values()) {
            if (person.getSpouse() == null || person.getChildrenNumber() == null) {
                person.setChildrenNumber(0);
            }
        }

        errors = personList.stream().filter(p ->
                !p.isConsistent(peopleWithId)).toList();

        System.out.println("Errors: " + errors.size() + "\n" +
                "Persons: " + personList.size());

        return personList;
    }

    private List<Person> handleRelations(List<Person> persons) {
        for (final Person person : persons) {
            final String id = person.getId();
            if (id == null) {
                Set<Person> personsWithNoId = peopleWithoutID.getOrDefault(person.getFullName(), new HashSet<>());
                personsWithNoId.add(person);
                peopleWithoutID.put(person.getFullName(), personsWithNoId);
                continue;
            }

            if (peopleWithId.containsKey(id)) {
                Person collision = peopleWithId.get(id);
                person.mergePerson(collision);
            }
            peopleWithId.put(id, person);
        }

        for (final Person person : peopleWithId.values()) {
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
                peopleWithId.get(id).mergePerson(person);
            }
        }

        for (final Person person : peopleWithId.values()) {
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

        return peopleWithId.values().stream()
                .sorted(Comparator.comparing(Person::getId)).toList();
    }

    private void connectRelatives(final Set<Person> peopleToAdd, final Person person) {
        if (person.getId() != null) {
            final String id = person.getId();
            peopleToAdd.add(peopleWithId.get(id));
        }
        else if (person.getFullName() != null) {
            final String name = person.getFullName();
            final Set<String> ids = idsByName.get(name);

            if (ids.size() == 1) {
                final String id = ids.iterator().next();
                peopleToAdd.add(peopleWithId.get(id));
            }
        }
    }

    private void solveProblem(Person person, Set<Person> others) {
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
                                person.setSpouse(peopleWithId.get(innerOther.getSpouse().getId()));
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (person.getSpouse() != null && person.getSpouse().getId() == null) {
            Set<String> spouses = idsByName.get(person.getSpouse().getFullName());
            for (String spouse : spouses) {
                for (Person possibleChild : peopleWithId.get(spouse).getChildren()) {
                    if (person.getChildren().contains(possibleChild)) {
                        person.resetSpouse();
                        person.setSpouse(peopleWithId.get(spouse));
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
                Set<String> spouseSiblings = idsByName.get(person.getSpouse().getFullName());
                if (spouseSiblings.size() == 2) {
                    for (String sibling : spouseSiblings) {
                        if (Objects.equals(person.getId(), sibling))
                            continue;
                        if (peopleWithId.get(sibling).getId() != null) {
                            person.setSpouse(peopleWithId.get(sibling));
                            person.getSpouse().setSpouse(peopleWithId.get(person.getId()));
                        }
                    }
                }
            }
        }
    }
}
