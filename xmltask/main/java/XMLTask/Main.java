package XMLTask;

import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = "/home/eres/IdeaProjects/XMLTask/src/main/resources/people.xml";

        XMLParser parser = new XMLParser();

        parser.getAllElementsAndAttributes(path);
//        parser.getPersonRecords(path);

        HashSet<Person> people = parser.parse(path);
        System.out.println("People: " + people.size());
    }
}
