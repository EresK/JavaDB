package xml.task2;

import jakarta.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum Gender {
    MALE, FEMALE;

    public static Gender reverse(Gender gender) {
        return gender == Gender.MALE ? Gender.FEMALE : Gender.MALE;
    }
}
