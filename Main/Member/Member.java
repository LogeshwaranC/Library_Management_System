package Main.Member;

import Main.DBConnect.DBConnect;
import Main.Login.Login_page;

import java.sql.*;
import java.util.*;

public class Member {
    Scanner sc = new Scanner(System.in);
    private int memberID;

    public void member() {

        if (checkAuth()) {
            System.out.println("Logged in as member!");

            while (true){
                System.out.println("""
                   1. Borrow a Book
                   2. Return a Book
                   3. View Books
                   4. View My Transactions
                   5. Logout
                  
                   
                   """);
                get_answer();
            }
        } else {
            System.out.println("Invalid login. Try again.");
            member();
        }
    }


    private boolean checkAuth() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Username: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = scanner.nextLine();

        try {
            Connection conn = DBConnect.connect();
            String query = "SELECT * FROM library_schema.librarymembers WHERE Username = ? AND Password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, inputUsername);
            statement.setString(2, inputPassword);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                this.memberID = result.getInt("MemberID");

                String username = result.getString("Name");
                System.out.println("Login successful. Username: " + username);
                return true;
            } else {
                System.out.println("Invalid credentials.");
            }
        } catch (SQLException e) {
            System.out.println("Error Checking Auth : " + e.getMessage());
        }
        return false;
    }

    public void get_answer() {
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();

        switch (input) {
            case 1:
                borrow_book();
                break;
            case 2:
                return_book();
                break;
            case 3:
                listbooks();
                break;
            case 4:
                view_transactions();
                break;
            case 5:
                Login_page l = new Login_page();
                l.loginpage();
                break;
            default:
                System.out.println("Invalid Option please try again");
                break;
        }
    }

    public void borrow_book() {

        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();
        int qty = 0;
        while (true) {
            System.out.print("Enter Quantity: ");
            try {
                qty = sc.nextInt();
                if (qty <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
            }
        }

        Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());

        try {
            Connection con = DBConnect.connect();
            String query = ("SELECT BookID, CopiesAvailable FROM Books WHERE Title = ?");
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, title);
            ResultSet result = statement.executeQuery();

            if (result.next()){
                int availableCopies = result.getInt("CopiesAvailable");
                int bookID = result.getInt("BookID");

                if (availableCopies >= qty){
                    String transactionupdateQuery = "INSERT INTO Transactions (MemberID, BookID, Quantity, TransactionDate, Type) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement transactionupdate = con.prepareStatement(transactionupdateQuery);
                    transactionupdate.setInt(1, memberID);
                    transactionupdate.setInt(2, bookID);
                    transactionupdate.setInt(3, qty);
                    transactionupdate.setTimestamp(4, currentTimestamp);
                    transactionupdate.setString(5, "Borrow");

                    transactionupdate.executeUpdate();


                    String updatequery = "UPDATE Books SET CopiesAvailable = CopiesAvailable - ?, TimesBorrowed = TimesBorrowed + ? WHERE BookID = ?";
                    PreparedStatement updateInventory = con.prepareStatement(updatequery);
                    updateInventory.setInt(1, qty);
                    updateInventory.setInt(2,qty);
                    updateInventory.setInt(3,bookID);
                    updateInventory.executeUpdate();

                    System.out.println("Book Borrowed Successfully");
                }
                else{
                    System.out.println("No copies available");
                }
            } else {
                System.out.println("Book Not found.");
            }
        } catch (Exception e) {
            System.out.println("Error in Borrowing: " + e.getMessage());
        }
    }
    public void return_book() {
        sc.nextLine();
        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();

        int qty = 0;
        while (true) {
            System.out.print("Enter Quantity to Return: ");
            try {
                qty = sc.nextInt();
                if (qty <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
            }
        }

        Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());

        try {
            Connection con = DBConnect.connect();


            String query = "SELECT BookID, CopiesAvailable FROM Books WHERE Title = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, title);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int bookID = result.getInt("BookID");


                String checkTransactionQuery = "SELECT SUM(Quantity) AS BorrowedQty FROM Transactions WHERE MemberID = ? AND BookID = ? AND Type = 'Borrow'";
                PreparedStatement checkTransaction = con.prepareStatement(checkTransactionQuery);
                checkTransaction.setInt(1, memberID);
                checkTransaction.setInt(2, bookID);
                ResultSet borrowResult = checkTransaction.executeQuery();

                int totalBorrowed = 0;
                if (borrowResult.next()) {
                    totalBorrowed = borrowResult.getInt("BorrowedQty");
                }

                String checkReturnQuery = "SELECT SUM(Quantity) AS ReturnedQty FROM Transactions WHERE MemberID = ? AND BookID = ? AND Type = 'Return'";
                PreparedStatement checkReturn = con.prepareStatement(checkReturnQuery);
                checkReturn.setInt(1, memberID);
                checkReturn.setInt(2, bookID);
                ResultSet returnResult = checkReturn.executeQuery();

                int totalReturned = 0;
                if (returnResult.next()) {
                    totalReturned = returnResult.getInt("ReturnedQty");
                }

                int currentlyHolding = totalBorrowed - totalReturned;

                if (currentlyHolding >= qty) {

                    String transactionUpdateQuery = "INSERT INTO Transactions (MemberID, BookID, Quantity, TransactionDate, Type) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement transactionUpdate = con.prepareStatement(transactionUpdateQuery);
                    transactionUpdate.setInt(1, memberID);
                    transactionUpdate.setInt(2, bookID);
                    transactionUpdate.setInt(3, qty);
                    transactionUpdate.setTimestamp(4, currentTimestamp);
                    transactionUpdate.setString(5, "Return");
                    transactionUpdate.executeUpdate();

                    String updateQuery = "UPDATE Books SET CopiesAvailable = CopiesAvailable + ? WHERE BookID = ?";
                    PreparedStatement updateInventory = con.prepareStatement(updateQuery);
                    updateInventory.setInt(1, qty);
                    updateInventory.setInt(2, bookID);
                    updateInventory.executeUpdate();

                    System.out.println("Book returned successfully.");
                } else {
                    System.out.println("You can't return more than you currently hold (" + currentlyHolding + ").");
                }

            } else {
                System.out.println("Book not found.");
            }

        } catch (Exception e) {
            System.out.println("Error in Returning: " + e.getMessage());
        }
    }

    public void listbooks(){
        try {
            Connection conn = DBConnect.connect();
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Books WHERE IsActive = TRUE";
            ResultSet result = statement.executeQuery(query);

            System.out.println("--- Active Books List ---");
            boolean found = false;

            while (result.next()) {
                found = true;
                System.out.println("Book ID: " + result.getInt("BookID"));
                System.out.println("Title: " + result.getString("Title"));
                System.out.println("Author: " + result.getString("Author"));
                System.out.println("Genre: " + result.getString("Genre"));
                System.out.println("Total Copies: " + result.getInt("TotalCopies"));
                System.out.println("Copies Available: " + result.getInt("CopiesAvailable"));
                System.out.println("Times Borrowed: " + result.getInt("TimesBorrowed"));
                System.out.println("-----------------------------");
            }
            if (!found ){
                System.out.println("No active books found in the library.");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
    }

    public void view_transactions() {
        try {
            Connection con = DBConnect.connect();

            String query = """
            SELECT t.TransactionID, b.Title, t.Quantity, t.TransactionDate, t.Type
            FROM Transactions t
            JOIN Books b ON t.BookID = b.BookID
            WHERE t.MemberID = ?
            ORDER BY t.TransactionDate DESC
        """;

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, memberID);

            ResultSet result = statement.executeQuery();

            System.out.println("\n--- Your Transaction History ---");
            System.out.printf("%-15s %-30s %-10s %-25s %-10s%n", "TransactionID", "Book Title", "Qty", "Date", "Type");
            System.out.println("--------------------------------------------------------------------------");

            boolean hasRecords = false;
            while (result.next()) {
                hasRecords = true;
                int transactionID = result.getInt("TransactionID");
                String title = result.getString("Title");
                int qty = result.getInt("Quantity");
                Timestamp date = result.getTimestamp("TransactionDate");
                String type = result.getString("Type");

                System.out.printf("%-15d %-30s %-10d %-25s %-10s%n",
                        transactionID, title, qty, date.toString(), type);
            }

            if (!hasRecords) {
                System.out.println("You have no transactions yet.");
            }

        } catch (Exception e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
    }
}
