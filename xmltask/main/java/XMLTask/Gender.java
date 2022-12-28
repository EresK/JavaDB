package XMLTask;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender reverse(Gender gender) {
        return gender == Gender.MALE ? Gender.FEMALE : Gender.MALE;
    }
}
