package src.algo.example.telephone;

public class Contact {
    private transient final String name;
    private transient String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return String.format(
            "Subscriber: %s, Phone Number: %s",
            this.name,
            this.phoneNumber
        );
    }
}
