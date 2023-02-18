package xml.task2;

import java.util.*;

public class PersonRelationMerger {
    private final Map<String, Person> peopleWithID = new HashMap<>();
    private final Map<String, Set<String>> idsByName = new HashMap<>();
    private final Map<String, Set<Person>> peopleWithoutID = new HashMap<>();

    public List<Person> merge(List<Person> people) {
        List<Person> personsHandled = handleRelations(people);

        List<Person> errors = personsHandled.stream()
                .filter(p -> !p.isConsistent(peopleWithID))
                .toList();

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

        for (Person person : peopleWithID.values()) {
            if (person.getSpouse() == null || person.getChildrenNumber() == null) {
                person.setChildrenNumber(0);
            }
        }

        errors = personsHandled.stream().filter(p ->
                !p.isConsistent(peopleWithID)).toList();

        return personsHandled;
    }

    private List<Person> handleRelations(List<Person> people) {
        for (Person person : people) {
            // checking for having id
            final String id = person.getId();
            if (id == null) {
                Set<Person> withoutID = peopleWithoutID.getOrDefault(person.getFullName(), new HashSet<>());
                withoutID.add(person);
                peopleWithoutID.put(person.getFullName(), withoutID);
                continue;
            }

            // merging collisions
            if (peopleWithID.containsKey(id)) {
                Person collision = peopleWithID.get(id);
                person.mergePerson(collision);
            }
            peopleWithID.put(id, person);
        }

        // associations several ids by the name
        for (Person person : peopleWithID.values()) {
            final String name = person.getFullName();
            if (person.getId() == null || name == null) {
                throw new RuntimeException("Person must have ID and name");
            }

            Set<String> ids = idsByName.getOrDefault(name, new HashSet<>());
            ids.add(person.getId());
            idsByName.put(name, ids);
        }

        // handling ids conflicts & merging
        for (Person person : people) {
            final String name = person.getFullName();
            if (name == null)
                continue;

            final Set<String> ids = idsByName.get(name);

            final String id = (ids.size() == 1) ? ids.iterator().next() : null;
            if (id != null) {
                person.setId(id);
                peopleWithID.get(id).mergePerson(person);
            }
        }

        // handling related persons
        for (Person person : peopleWithID.values()) {
            if (person.getSpouse() != null) {
                final Set<Person> spouseToAdd = new HashSet<>();
                findRelated(spouseToAdd, person.getSpouse());

                if (!spouseToAdd.isEmpty()) {
                    final Person spouse = spouseToAdd.iterator().next();
                    person.setSpouse(spouse);
                }
            }

            Set<Person> childrenToAdd = new HashSet<>();
            for (final Person child : person.getChildren()) {
                findRelated(childrenToAdd, child);
            }
            person.getChildren().clear();
            childrenToAdd.forEach(person::addChild);

            Set<Person> siblingsToAdd = new HashSet<>();
            for (final Person sibling : person.getSiblings()) {
                findRelated(siblingsToAdd, sibling);
            }
            person.getSiblings().clear();
            siblingsToAdd.forEach(person::addSibling);

            Set<Person> parentsToAdd = new HashSet<>();
            for (final Person parent : person.getParents()) {
                findRelated(parentsToAdd, parent);
            }
            person.getParents().clear();
            parentsToAdd.forEach(person::addParent);
        }

        return peopleWithID.values().stream()
                .sorted(Comparator.comparing(Person::getId)).toList();
    }

    private void findRelated(final Set<Person> peopleToAdd, final Person person) {
        if (person.getId() != null) {
            final String id = person.getId();
            peopleToAdd.add(peopleWithID.get(id));
        }
        else if (person.getFullName() != null) {
            final String name = person.getFullName();
            final Set<String> ids = idsByName.get(name);

            if (ids.size() == 1) {
                final String id = ids.iterator().next();
                peopleToAdd.add(peopleWithID.get(id));
            }
        }
    }

    private void solveProblem(Person person, Set<Person> others) {
        if (person.getGender() == null) {
            for (Person other : others) {
                // set gender by spouse information
                if (other.getSpouse() != null && Objects.equals(person.getId(), other.getSpouse().getId())) {
                    if (Objects.equals(other.getSpouse().getGender(), Gender.MALE)) {
                        person.setGender(Gender.MALE);
                        break;
                    }
                    else if (Objects.equals(other.getSpouse().getGender(), Gender.FEMALE)) {
                        person.setGender(Gender.FEMALE);
                        break;
                    }
                    else if (other.getGender() != null) {
                        person.setGender(Gender.reverse(other.getGender()));
                        break;
                    }
                }

                // set gender by siblings information
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

        if (person.getSpouse() == null) {
            for (Person other : others) {
                if (other.getSpouse() != null && Objects.equals(person.getId(), other.getSpouse().getId())) {
                    person.setSpouse(other);

                    if (person.getSpouse().getId() == null) {
                        for (Person innerOther : others) {
                            if (person.getSpouse().getFullName().equals(innerOther.getFullName()) &&
                                    innerOther.getSpouse() != null &&
                                    innerOther.getSpouse().getId() != null &&
                                    Objects.equals(innerOther.getSpouse().getId(), person.getId())) {
                                person.setSpouse(peopleWithID.get(innerOther.getSpouse().getId()));
                            }
                        }
                    }
                    break;
                }
            }
        }

        // find spouse by children information
        if (person.getSpouse() != null && person.getSpouse().getId() == null) {
            Set<String> spousesIds = idsByName.get(person.getSpouse().getFullName());
            for (String spouseId : spousesIds) {
                for (Person possibleChild : peopleWithID.get(spouseId).getChildren()) {
                    if (person.getChildren().contains(possibleChild)) {
                        person.resetSpouse();
                        person.setSpouse(peopleWithID.get(spouseId));
                        break;
                    }
                }

                if (person.getSpouse().getId() != null)
                    break;
            }
        }

        // find spouse by siblings information
        if (person.getSpouse() != null && person.getSpouse().getId() == null) {
            if (person.getSpouse().getSiblingsNumber() != null && person.getSpouse().getSiblingsNumber() == 1) {
                Set<String> spouseSiblingIds = idsByName.get(person.getSpouse().getFullName());
                if (spouseSiblingIds.size() == 2) {
                    for (String siblingId : spouseSiblingIds) {
                        if (Objects.equals(person.getId(), siblingId))
                            continue;
                        if (peopleWithID.get(siblingId).getId() != null) {
                            person.setSpouse(peopleWithID.get(siblingId));
                            person.getSpouse().setSpouse(peopleWithID.get(person.getId()));
                        }
                    }
                }
            }
        }

        // set spouse gender
        if (person.getSpouse() != null && person.getSpouse().getGender() == null) {
            if (person.getGender() != null)
                person.getSpouse().setGender(Gender.reverse(person.getGender()));
        }


        if (person.getChildrenNumber() == null || person.getChildrenNumber() > person.getChildren().size()) {
            if (person.getSpouse() != null) {
                for (Person spouseChild : person.getSpouse().getChildren()) {
                    person.getChildren().add(spouseChild);
                    spouseChild.addParent(person);
                }
            }

            person.setChildrenNumber(person.getChildren().size());
        }
    }
}
