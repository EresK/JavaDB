package xml.task2;

import lombok.NonNull;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParserXML {
    private XMLEventReader reader;

    public List<Person> parseXMLFile(String filename) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        List<Person> people = new ArrayList<>();
        Person currentPerson = null;

        try (InputStream stream = new FileInputStream(filename)) {
            reader = factory.createXMLEventReader(stream);

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    switch (startElement.getName().getLocalPart()) {
                        case "person" -> {
                            currentPerson = new Person();

                            String attrId = getAttributeValue(startElement, "id");
                            if (attrId != null) currentPerson.setId(attrId);

                            String attrFullName = getAttributeValue(startElement, "name");
                            if (attrFullName != null) currentPerson.setFullName(attrFullName);
                        }

                        case "people" -> {
                        }

                        default -> {
                            if (currentPerson != null)
                                handle(currentPerson, startElement);
                            else
                                throw new IllegalStateException("Current person can not be null");
                        }
                    }
                }
                else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();

                    if ("person".equals(endElement.getName().getLocalPart())) {
                        if (currentPerson != null) {
                            people.add(currentPerson);

                            people.addAll(currentPerson.getChildren());
                            people.addAll(currentPerson.getParents());
                            people.addAll(currentPerson.getSiblings());
                            if (currentPerson.getSpouse() != null)
                                people.add(currentPerson.getSpouse());
                        }
                        else
                            throw new IllegalStateException("Current person can not be null");
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return people;
    }

    private void handle(@NonNull Person person, StartElement startElement) throws XMLStreamException {
        switch (startElement.getName().getLocalPart()) {
            case "id" -> person.setId(getValue(startElement));

            case "first", "firstname" -> person.setFirstName(getValue(startElement));

            case "surname", "family", "family-name" -> person.setLastName(getValue(startElement));

            case "children-number" ->
                    person.setChildrenNumber(tryParse(getValue(startElement)));

            case "siblings-number" ->
                    person.setSiblingsNumber(tryParse(getValue(startElement)));

            case "gender" -> person.setGender(getGender(startElement));

            case "siblings" -> {
                String[] siblingsValue = getSiblingsValue(startElement);
                if (siblingsValue != null) {
                    for (String id : siblingsValue) {
                        person.addSibling(id);
                    }
                }
            }

            case "brother" -> {
                String name = getValue(startElement);
                if (name != null)
                    person.addSibling(name, Gender.MALE);
            }

            case "sister" -> {
                String name = getValue(startElement);
                if (name != null)
                    person.addSibling(name, Gender.FEMALE);
            }

            case "son" -> {
                String id = getValue(startElement);
                if (id != null)
                    person.addChild(id, Gender.MALE);
            }

            case "daughter" -> {
                String id = getValue(startElement);
                if (id != null)
                    person.addChild(id, Gender.FEMALE);
            }

            case "child" -> {
                String name = getValue(startElement);
                if (name != null)
                    person.addChild(name);
            }

            case "parent" -> {
                String id = getValue(startElement);
                if (id != null)
                    person.addParent(id);
            }

            case "mother" -> {
                String fullName = getValue(startElement);
                if (fullName != null)
                    person.addParent(fullName, Gender.FEMALE);
            }

            case "father" -> {
                String fullName = getValue(startElement);
                if (fullName != null)
                    person.addParent(fullName, Gender.MALE);
            }

            case "spouce" -> {
                String fullName = getValue(startElement);
                if (fullName != null)
                    person.setSpouse(fullName);
            }

            case "wife" -> {
                String id = getValue(startElement);
                if (id != null)
                    person.setWife(id);
            }

            case "husband" -> {
                String id = getValue(startElement);
                if (id != null)
                    person.setHusband(id);
            }

            case "children", "fullname" -> {
                // contains other elements, but empty by itself
            }
        }
    }

    private String[] getSiblingsValue(StartElement startElement) {
        Iterator<Attribute> iterator = startElement.getAttributes();
        if (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            return attribute.getValue().strip().split(" ");
        }
        return null;
    }

    /**
     * Have side effects, change the state of startElement
     * @return - value of the element
     */
    private String getValue(StartElement startElement) throws XMLStreamException {
        String value;
        Iterator<Attribute> iterator = startElement.getAttributes();

        if (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            value = attribute.getValue().strip();
        }
        else {
            XMLEvent charsEvent = reader.nextEvent();
            if (charsEvent.isEndElement())
                return null;

            reader.nextEvent();
            value = charsEvent.asCharacters().getData().strip();
        }

        return isCorrectValue(value) ? value : null;
    }

    private String getAttributeValue(StartElement startElement, String attributeName) {
        Attribute a = startElement.getAttributeByName(new QName(attributeName));

        String value = null;
        if (a != null)
            value = a.getValue();

        return isCorrectValue(value) ? value.strip() : null;
    }

    private boolean isCorrectValue(String value) {
        if (value == null)
            return false;

        String v = value.strip();
        return !"unknown".equalsIgnoreCase(v) && !"none".equalsIgnoreCase(v);
    }

    private Gender getGender(final StartElement startElement) throws XMLStreamException {
        Iterator<Attribute> iterator = startElement.getAttributes();
        if (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            return parseGender(attribute.getValue().strip());
        }
        else {
            XMLEvent charsEvent = reader.nextEvent();
            reader.nextEvent();
            String gender = charsEvent.asCharacters().getData().strip();
            return parseGender(gender);
        }
    }

    private Gender parseGender(String gender) throws IllegalArgumentException {
        return switch (gender) {
            case "male", "M" -> Gender.MALE;
            case "female", "F" -> Gender.FEMALE;
            default -> throw new IllegalArgumentException("The value is not a gender: " + gender);
        };
    }

    private int tryParse(String num) {
        if (num != null) {
            try {
                return Integer.parseInt(num);
            }
            catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            }
        }
        return 0;
    }
}
