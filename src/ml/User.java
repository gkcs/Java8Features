package ml;

public class User {
    final String phoneNumber;
    int numberOfCalls;
    int numberOfReceptions;

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof User && phoneNumber.equals(((User) o).phoneNumber);
    }

    @Override
    public int hashCode() {
        return phoneNumber.hashCode();
    }
}
