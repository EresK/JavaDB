package XMLTask;

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
                        case "id" -> currentPerson.setId(getValue(startElement));

                        case "first", "firstname" -> currentPerson.setFirstName(getValue(startElement));

                        case "surname", "family", "family-name" -> currentPerson.setLastName(getValue(startElement));

                        case "children-number" ->
                                currentPerson.setChildrenNumber(Integer.parseInt(getValue(startElement)));

                        case "siblings-number" ->
                                currentPerson.setSiblingsNumber(Integer.parseInt(getValue(startElement)));

                        case "gender" -> currentPerson.setGender(getGender(startElement));

                        case "siblings" -> {
                            String[] siblingsValue = getSiblingsValue(startElement);
                            if (siblingsValue != null) {
                                for (String id : siblingsValue) {
                                    currentPerson.addSibling(id);
                                }
                            }
                        }

                        case "brother" -> {
                            String name = getValue(startElement);
                            currentPerson.addSibling(name, Gender.MALE);
                        }

                        case "sister" -> {
                            String name = getValue(startElement);
                            currentPerson.addSibling(name, Gender.FEMALE);
                        }

                        case "son" -> {
                            String id = getValue(startElement);
                            currentPerson.addChild(id, Gender.MALE);
                        }

                        case "daughter" -> {
                            String id = getValue(startElement);
                            currentPerson.addChild(id, Gender.FEMALE);
                        }

                        case "child" -> {
                            String name = getValue(startElement);
                            currentPerson.addChild(name);
                        }

                        case "parent" -> {
                            String id = getValue(startElement);
                            if (id != null) {
                                currentPerson.addParent(id);
                            }
                        }

                        case "mother" -> {
                            String fullname = getValue(startElement);
                            currentPerson.addParent(fullname, Gender.FEMALE);
                        }

                        case "father" -> {
                            String fullname = getValue(startElement);
                            currentPerson.addParent(fullname, Gender.MALE);
                        }

                        case "children", "fullname" -> {
                        }

                        case "spouce" -> {
                            String fullname = getValue(startElement);
                            if (fullname != null) {
                                currentPerson.setSpouse(fullname);
                            }
                        }

                        case "wife" -> {
                            String id = getValue(startElement);
                            currentPerson.setWife(id);
                        }

                        case "husband" -> {
                            String id = getValue(startElement);
                            currentPerson.setHusband(id);
                        }
                    }
                }
                else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals("person")) {
                        people.add(currentPerson);

                        people.addAll(currentPerson.getChildren());
                        people.addAll(currentPerson.getParents());
                        people.addAll(currentPerson.getSiblings());
                        if (currentPerson.getSpouse() != null)
                            people.add(currentPerson.getSpouse());
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return people;
    }

    private String[] getSiblingsValue(final StartElement startElement) {
        Iterator<Attribute> iterator = startElement.getAttributes();
        if (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            return attribute.getValue().strip().split(" ");
        }
        return null;
    }

    private String getValue(final StartElement startElement) throws XMLStreamException {
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

    private Gender getGender(final StartElement event) throws XMLStreamException {
        Iterator<Attribute> iterator = event.getAttributes();
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
}
