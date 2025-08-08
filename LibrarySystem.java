import java.util.*;
import java.util.concurrent.*;

// Functional Interface
interface BookAction {
    void execute(Book book);
}

// Book class
class Book {
    int id;
    String title;
    boolean isAvailable;

    Book(int id, String title) {
        this.id = id;
        this.title = title;
        this.isAvailable = true;
    }

    public String toString() {
        return id + ": " + title + " (Available: " + isAvailable + ")";
    }
}

// Exception class
class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

// Thread using Thread class
class IssueThread extends Thread {
    private Book book;

    public IssueThread(Book book) {
        this.book = book;
    }

    public void run() {
        try {
            if (!book.isAvailable) {
                throw new BookNotAvailableException("Error: Book already issued.");
            }
            book.isAvailable = false;
            System.out.println("✅ Book issued successfully.");
        } catch (BookNotAvailableException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Thread using Runnable interface
class ReturnThread implements Runnable {
    private Book book;

    public ReturnThread(Book book) {
        this.book = book;
    }

    public void run() {
        try {
            if (book.isAvailable) {
                throw new BookNotAvailableException("⚠️ Error: Book is already available.");
            }
            book.isAvailable = true;
            System.out.println("✅ Book returned successfully.");
        } catch (BookNotAvailableException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class LibrarySystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Book> books = new HashMap<>();

        books.put(1, new Book(1, "Java Programming"));
        books.put(2, new Book(2, "Data Structures"));
        books.put(3, new Book(3, "Operating Systems"));

        // Lambda Expression for displaying a book
        BookAction displayBook = (book) -> System.out.println(book);

        while (true) {
            System.out.println("\n=== Library Menu ===");
            System.out.println("1. View All Books");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            if (choice == 4) {
                System.out.println("Thank you for using the Library System!");
                break;
            }

            switch (choice) {
                case 1:
                    System.out.println("\nAvailable Books:");
                    for (Book book : books.values()) {
                        displayBook.execute(book);
                    }
                    break;

                case 2:
                    System.out.print("Enter Book ID to issue: ");
                    int issueId = scanner.nextInt();
                    Book bookToIssue = books.get(issueId);
                    if (bookToIssue != null) {
                        new IssueThread(bookToIssue).start();
                    } else {
                        System.out.println("❌ Book ID not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Book ID to return: ");
                    int returnId = scanner.nextInt();
                    Book bookToReturn = books.get(returnId);
                    if (bookToReturn != null) {
                        Thread t = new Thread(new ReturnThread(bookToReturn));
                        t.start();
                    } else {
                        System.out.println("❌ Book ID not found.");
                    }
                    break;

                default:
                    System.out.println("⚠️ Invalid choice.");
            }

            // Optional delay for thread output
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        scanner.close();
    }
}

