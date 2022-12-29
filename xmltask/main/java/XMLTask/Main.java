package XMLTask;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filename = "src/main/resources/people.xml";
        String outFilename = "src/main/resources/ready.txt";

        ParserXML parserXML = new ParserXML();
        PersonRelationMerger relationMerger = new PersonRelationMerger();

        List<Person> personList = parserXML.parseXMLFile(filename);

        System.out.println("Parsed persons: " + personList.size());

        personList = relationMerger.merge(personList);

//        writePersons(personList, outFilename);
//        printPersons(personList);
    }

    private static void writePersons(List<Person> people, String outFilename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilename))) {
            for (Person p: people) {
                writer.write(p.toStringFull());
                writer.newLine();
                writer.newLine();
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void printPersons(List<Person> personList) {
        for (Person p : personList) {
            System.out.println(p.toStringFull());
            System.out.println();
        }
    }
}
