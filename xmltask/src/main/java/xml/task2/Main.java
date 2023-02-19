package xml.task2;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String people = "src/main/resources/task2/people.xml";
        String handled = "src/main/resources/task2/handled.xml";
        String schema = "src/main/resources/task2/schema.xsd";

        parse(people, handled);
        validate(schema, handled);
    }

    private static void parse(String inputFile, String outputFile) throws JAXBException {
        ParserXML parserXML = new ParserXML();
        PersonRelationMerger relationMerger = new PersonRelationMerger();

        List<Person> personList = parserXML.parseXMLFile(inputFile);
        System.out.println("Parsed persons: " + personList.size());

        personList = relationMerger.merge(personList);
        System.out.println(personList.size());

        People people = new People(personList);

        personToXml(people, outputFile);
    }

    private static void validate(String schemaPath, String xmlToValidate) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        File schemaFile = new File(schemaPath);
        docBuilderFactory.setSchema(schemaFactory.newSchema(schemaFile));
        docBuilderFactory.setNamespaceAware(true);
        docBuilderFactory.newDocumentBuilder().parse(xmlToValidate);
    }

    private static void personToXml(People people, String outputXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(People.class);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(people, new File(outputXml));
    }
}
