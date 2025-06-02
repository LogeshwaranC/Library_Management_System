package Main.Librarian;

import Main.DBConnect.DBConnect;
import Main.Login.Login_page;

import javax.tools.Tool;
import java.sql.*;
import java.util.*;

public class Book_management {
    Scanner sc = new Scanner(System.in);
    public void showBookMenu() {
        int recursion_count = 0;
        while (true) {

            System.out.println(""" 
            --- Manage Books Menu ---
            1. Add New Book
            2. Remove Book
            3. View All Books
            4. Update Available Copies
            5. View All Transactions
            6. Back to Menu card
            7. Logout  """);

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();


            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    viewBooks();
                    break;
                case 4:
                    updateCopies();
                    break;
                case 5:
                    viewTransactions();
                    break;
                case 6:
                    Librarian l = new Librarian();
                    l.add_recursion_count(1);
                    break;
                case 7:
                    System.out.println("Logging out...");
                    Login_page lp = new Login_page();
                    lp.loginpage();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }

    private void addBook() {
        try {
            sc.nextLine();
            System.out.print("Enter Book Title: ");
            String title = sc.nextLine();

            System.out.print("Enter Author: ");
            String author = sc.nextLine();

            System.out.print("Enter Genre: ");
            String genre = sc.nextLine();

            System.out.print("Enter Total Copies: ");
            int totalCopies = Integer.parseInt(sc.nextLine());

            boolean isAvailable = totalCopies > 0;

            Connection conn = DBConnect.connect();
            String sql = "INSERT INTO Books (Title, Author, Genre, TotalCopies, CopiesAvailable) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, title);
            pst.setString(2, author);
            pst.setString(3, genre);
            pst.setInt(4, totalCopies);
            pst.setInt(5, totalCopies);

            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book.");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    private void removeBook() {
        try {
            sc.nextLine();
            System.out.print("Enter Book Title to remove: ");
            String title = sc.nextLine();

            sc.nextLine();

            Connection conn = DBConnect.connect();

            String query = "SELECT * FROM Books WHERE Title = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, title);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String softdeleteQuery = "UPDATE Books SET IsActive = FALSE WHERE Title = ?";
                PreparedStatement statement1 = conn.prepareStatement(softdeleteQuery);
                statement1.setString(1, title);
                int deleted = statement1.executeUpdate();
                if (deleted > 0) {
                    System.out.println("Book marked as inactive (soft deleted)..");
                } else {
                    System.out.println("Failed to remove the book." + title);
                }
            } else {
                System.out.println("Book with Title " + title + " does not exist in database.");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error removing book: " + e.getMessage());
        }
    }
    private void updateCopies() {
        try {
            System.out.print("Enter Book Title to add more copies: ");
            sc.nextLine();
            String title = sc.nextLine();


            Connection conn = DBConnect.connect();

            String checkSql = "SELECT * FROM Books WHERE Title = ? AND IsActive = TRUE";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, title);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                int bookID = rs.getInt("BookID");
                int totalCopies = rs.getInt("TotalCopies");
                int availableCopies = rs.getInt("CopiesAvailable");

                System.out.println("Book Found: " + title);
                System.out.println("Total Copies: " + totalCopies);
                System.out.println("Available Copies: " + availableCopies);

                int copiesToAdd;


                while (true) {
                    try {
                        System.out.print("Enter number of new copies to add: ");
                        copiesToAdd = Integer.parseInt(sc.nextLine());

                        if (copiesToAdd < 0) {
                            System.out.println("Number of copies cannot be negative.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                }

                int newTotal = totalCopies + copiesToAdd;
                int newAvailable = availableCopies + copiesToAdd;

                String updateSql = "UPDATE Books SET TotalCopies = ?, CopiesAvailable = ? WHERE BookID = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateSql);
                updatePst.setInt(1, newTotal);
                updatePst.setInt(2, newAvailable);
                updatePst.setInt(3, bookID);

                int rows = updatePst.executeUpdate();
                if (rows > 0) {
                    System.out.println("Successfully added " + copiesToAdd + " copies.");
                    System.out.println("New Total Copies: " + newTotal);
                    System.out.println("New Available Copies: " + newAvailable);
                } else {
                    System.out.println("Update failed.");
                }

            } else {
                System.out.println("Book not found or is inactive.");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error updating copies: " + e.getMessage());
        }
    }


    private void viewBooks() {
        try {
            Connection conn = DBConnect.connect();
            String query = "SELECT Title, Author, Genre, TotalCopies, CopiesAvailable FROM Books WHERE IsActive = TRUE";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            System.out.println("\n--- Available Books ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String genre = rs.getString("Genre");
                int total = rs.getInt("TotalCopies");
                int available = rs.getInt("CopiesAvailable");

                System.out.println("Title           : " + title);
                System.out.println("Author          : " + author);
                System.out.println("Genre           : " + genre);
                System.out.println("Total Copies    : " + total);
                System.out.println("Available Copies: " + available);
                System.out.println("------------------------------");
            }

            if (!found) {
                System.out.println("No active books found in the library.");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error fetching available books: " + e.getMessage());
        }
    }
    private void viewTransactions() {
        try {
            Connection conn = DBConnect.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Transactions ORDER BY TransactionDate DESC");

            System.out.println("\n--- All Transactions ---");
            while (rs.next()) {
                System.out.println("Transaction ID: " + rs.getInt("TransactionID"));
                System.out.println("Member ID: " + rs.getInt("MemberID"));
                System.out.println("Book ID: " + rs.getInt("BookID"));
                System.out.println("Quantity: " + rs.getInt("Quantity"));
                System.out.println("Type: " + rs.getString("Type"));
                System.out.println("Date: " + rs.getTimestamp("TransactionDate"));
                System.out.println("-----------------------------");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
    }

}
