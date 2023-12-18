package src.algo.example.telephone;

import java.io.InputStream;
import java.util.Scanner;

public class TelephoneDirectoryApplication {
    private final Scanner scanner;
    private final ContactList contactList;

    private transient String lastCommand;

    public TelephoneDirectoryApplication(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
        this.contactList = new ContactList();
    }

    public static void main(String[] args) {
        TelephoneDirectoryApplication application =
            new TelephoneDirectoryApplication(System.in);
        while (application.continues()) {
            application.execute();
        }
        application.destroy();
    }

    public void destroy() {
        this.scanner.close();
    }

    public String readLine() {
        return this.scanner.nextLine().trim();
    }

    public void printMenu() {
        System.out.println("+------------------------------+");
        System.out.println("| Telephone Directory          |");
        System.out.println("+------------------------------+");
        System.out.println("| Add    (A)                   |");
        System.out.println("| Search (S)                   |");
        System.out.println("| Update (U)                   |");
        System.out.println("| Delete (D)                   |");
        System.out.println("| Print  (P)                   |");
        System.out.println("| Quit   (Q)                   |");
        System.out.println("+------------------------------+");
        System.out.print(">>> ");
    }

    public boolean continues() {
        printMenu();
        this.lastCommand = readLine();
        return !this.lastCommand.equalsIgnoreCase("q");
    }

    public void execute() {
        switch (this.lastCommand) {
            case "a":
            case "A":
                add();
                break;
            case "s":
            case "S":
                search();
                break;
            case "u":
            case "U":
                update();
                break;
            case "d":
            case "D":
                delete();
                break;
            case "p":
            case "P":
                print();
                break;
        }
    }

    public void add() {
        System.out.println("+------------------------------+");
        System.out.println("| Input a name.                |");
        System.out.println("+------------------------------+");
        System.out.print(">>> ");
        String name = readLine();
        if (this.contactList.hasName(name)) {
            System.out.println("+------------------------------+");
            System.out.println("| Already added.               |");
            System.out.println("+------------------------------+");
            return;
        }
        System.out.println("+------------------------------+");
        System.out.println("| Input its phone number.      |");
        System.out.println("+------------------------------+");
        System.out.print(">>> ");
        String phoneNumber = readLine();
        this.contactList.add(name, phoneNumber);
    }

    public void search() {
        System.out.println("+------------------------------+");
        System.out.println("| Input a name.                |");
        System.out.println("+------------------------------+");
        System.out.print(">>> ");
        String name = readLine();
        if (!this.contactList.hasName(name)) {
            System.out.println("+------------------------------+");
            System.out.println("| Cannot find the name.        |");
            System.out.println("+------------------------------+");
            return;
        }
        Contact data = this.contactList.get(name);
        System.out.println("+------------------------------+");
        System.out.printf("| Name: %1$-22s |\n", data.getName());
        System.out.printf("| Number: %1$-20s |\n", data.getPhoneNumber());
        System.out.println("+------------------------------+");
    }

    public void update() {
        System.out.println("+------------------------------+");
        System.out.println("| Input a name.                |");
        System.out.println("+------------------------------+");
        System.out.print(">>> ");
        String name = readLine();
        if (!this.contactList.hasName(name)) {
            System.out.println("+------------------------------+");
            System.out.println("| Cannot find the name.        |");
            System.out.println("+------------------------------+");
            return;
        }
        System.out.println("+------------------------------+");
        System.out.println("| Input its new phone number.  |");
        System.out.println("+------------------------------+");
        System.out.print(">>> ");
        String phoneNumber = readLine();
        this.contactList.update(name, phoneNumber);
    }

    public void delete() {
        System.out.println("+------------------------------+");
        System.out.println("| Input a name.                |");
        System.out.println("+------------------------------+");
        System.out.print(">>> ");
        String name = readLine();
        if (!this.contactList.hasName(name)) {
            System.out.println("+------------------------------+");
            System.out.println("| Cannot find the name.        |");
            System.out.println("+------------------------------+");
            return;
        }
        this.contactList.remove(name);
        System.out.println("+------------------------------+");
        System.out.println("| The contact is deleted.      |");
        System.out.println("+------------------------------+");
    }

    public void print() {
        System.out.println("+------------------------------+");
        System.out.println("| Contact List                 |");
        System.out.println("+------------------------------+");
        for (Contact data : this.contactList) {
            System.out.printf("|  Name: %1$-21s |\n", data.getName());
            System.out.printf("|  Number: %1$-19s |\n", data.getPhoneNumber());
            System.out.println("+------------------------------+");
        }
    }
}
