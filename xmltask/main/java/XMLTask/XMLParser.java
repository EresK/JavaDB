package XMLTask;

import lombok.NonNull;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Locale;

public class XMLParser {
    public HashSet<Person> parse(String path) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(path));

        HashSet<Person> people = new HashSet<>();
        Person person = new Person();

        int event;
        String localName;

        while (reader.hasNext()) {
            event = reader.getEventType();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT -> {
                    localName = reader.getLocalName().toLowerCase(Locale.ROOT);

                    if ("person".equals(localName)) {
                        person = new Person();
                        handleAttributes(person, localName, reader);
                    }
                    else {
                        handleAttributes(person, localName, reader);
                    }
                }

                case XMLStreamConstants.END_ELEMENT -> {
                    localName = reader.getLocalName().toLowerCase(Locale.ROOT);

                    if ("person".equals(localName)) {
                        people.add(person);
                        person = new Person();
                    }
                }
            }
            reader.next();
        }

        return people;
    }

    private void handleAttributes(@NonNull Person person, @NonNull String element, XMLStreamReader reader) {
        Person relatedPerson = new Person();

        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String value = reader.getAttributeValue(i);
            String localName = reader.getAttributeLocalName(i).toLowerCase(Locale.ROOT);

            switch (element) {
                case "person" -> {
                    switch (localName) {
                        case "id", "val", "value" -> person.setId(value);
                        case "name" -> person.getNames().add(value);
                    }
                }

                case "parent", "father", "mother" -> {
                    switch (localName) {
                        case "id", "val", "value" -> {
                            relatedPerson.setId(value);
                            person.getParents().add(relatedPerson);
                        }
                        case "name" -> {
                            relatedPerson.getNames().add(value);
                            person.getParents().add(relatedPerson);
                        }
                    }
                }

                case "siblings", "sister", "brother" -> {
                    switch (localName) {
                        case "id", "val", "value" -> {
                            relatedPerson.setId(value);
                            person.getSiblings().add(relatedPerson);
                        }
                        case "name" -> {
                            relatedPerson.getNames().add(value);
                            person.getSiblings().add(relatedPerson);
                        }
                        case "count" -> {
                            try {
                                person.setSiblingsNumber(Integer.parseInt(value));
                            }
                            catch (NumberFormatException e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    }
                }

                case "husband", "wife", "spouce" -> {
                    switch (localName) {
                        case "id", "val", "value" -> {
                            relatedPerson.setId(value);
                            person.getSpouse().add(relatedPerson);
                        }
                        case "name" -> {
                            relatedPerson.getNames().add(value);
                            person.getSpouse().add(relatedPerson);
                        }
                    }
                }

                case "son", "daughter", "children", "child" -> {
                    switch (localName) {
                        case "id", "val", "value" -> {
                            relatedPerson.setId(value);
                            person.getChildren().add(relatedPerson);
                        }
                        case "name" -> {
                            relatedPerson.getNames().add(value);
                            person.getChildren().add(relatedPerson);
                        }
                        case "count" -> {
                            try {
                                person.setChildrenNumber(Integer.parseInt(value));
                            }
                            catch (NumberFormatException e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    }
                }

                case "id" -> {
                    switch (localName) {
                        case "val", "value" -> person.setId(value);
                    }
                }

                case "firstname", "first" -> {
                    switch (localName) {
                        case "val", "value" -> person.setFirstName(value);
                    }
                }

                case "surname", "family-name", "family" -> {
                    switch (localName) {
                        case "val", "value" -> person.setLastName(value);
                    }
                }

                case "fullname" -> {
                    switch (localName) {
                        case "val", "value" -> person.setFullName(value);
                    }
                }

                case "gender" -> {
                    switch (localName) {
                        case "val", "value" -> person.setGender(value);
                    }
                }

                case "siblings-number" -> {
                    switch (localName) {
                        case "val", "value" ->  {
                            try {
                                person.setSiblingsNumber(Integer.parseInt(value));
                            }
                            catch (NumberFormatException e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    }
                }

                case "children-number" -> {
                    switch (localName) {
                        case "val", "value" ->  {
                            try {
                                person.setChildrenNumber(Integer.parseInt(value));
                            }
                            catch (NumberFormatException e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    public void getAllElementsAndAttributes(String path) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(path));

        HashSet<String> elements = new HashSet<>();
        HashSet<String> attributes = new HashSet<>();

        int event;

        while (reader.hasNext()) {
            event = reader.getEventType();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT -> {
                    elements.add(reader.getLocalName());

                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                        attributes.add(reader.getAttributeLocalName(i));
                    }
                }
                case  XMLStreamConstants.END_ELEMENT -> elements.add(reader.getLocalName());
            }
            reader.next();
        }

        System.out.println("Elements: " + elements.size());
        for (String elem: elements)
            System.out.println(elem);

        System.out.println("\nAttributes: " + attributes.size());
        for (String attr: attributes)
            System.out.println(attr);
        System.out.println();
    }

    public void getPersonRecords(String path) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(path));

        int personRecords = 0;

        int event;
        String localName;

        while (reader.hasNext()) {
            event = reader.getEventType();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT -> {
                    localName = reader.getLocalName().toLowerCase(Locale.ROOT);

                    if (localName.equals("person"))
                        personRecords += 1;
                }
                // case  XMLStreamConstants.END_ELEMENT -> elements.add(reader.getLocalName());
            }
            reader.next();
        }

        System.out.println("Person records: " + personRecords);
    }
}
