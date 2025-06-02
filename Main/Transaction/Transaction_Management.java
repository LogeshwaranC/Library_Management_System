package Main.Transaction;

import Main.DBConnect.DBConnect;
import Main.Librarian.Librarian;
import Main.Login.Login_page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Scanner;

public class Transaction_Management {
    Scanner sc = new Scanner(System.in);
    Librarian l = new Librarian();


    public void showTransactionHistoryMenu() {
        while (true) {

            System.out.println(""" 
                    --- Transaction History Menu ---
                    
                    1. View all Transactions
                    2. Search in Transactions
                    3. Back to Menu card
                    4. Exit
                    """);

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    view_transaction();
                    break;
                case 2:
                    search_transaction();
                    break;
                case 3:
                    l.add_recursion_count(1);
                    break;
                case 4:
                    Login_page lp = new Login_page();
                    lp.loginpage();
                    break;
                default:
                    System.out.println("Invalid Option please try again");
                    break;
            }
        }
    }

    public void view_transaction(){
        try {

            Connection con = DBConnect.connect();

            String query = """
            SELECT t.TransactionID, m.Name, b.Title, t.Quantity, t.TransactionDate, t.Type
            FROM Transactions t
            JOIN LibraryMembers m ON t.MemberID = m.MemberID
            JOIN Books b ON t.BookID = b.BookID
            ORDER BY t.TransactionDate DESC
        """;

            PreparedStatement statement = con.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            System.out.println("\n--- All Borrow/Return Transactions ---");
            System.out.printf("%-15s %-20s %-30s %-10s %-25s %-10s%n",
                    "TransactionID", "Member", "Book Title", "Qty", "Date", "Type");
            System.out.println("""
                    ----------------------------------------------------------------------------------------------
                    """);


            boolean hasRecords = false;
            while (result.next()) {
                hasRecords = true;
                int transactionID = result.getInt("TransactionID");
                String memberName = result.getString("Name");
                String title = result.getString("Title");
                int qty = result.getInt("Quantity");
                Timestamp date = result.getTimestamp("TransactionDate");
                String type = result.getString("Type");

                System.out.printf("%-15d %-20s %-30s %-10d %-25s %-10s%n",
                        transactionID, memberName, title, qty, date.toString(), type);
            }

            if (!hasRecords) {
                System.out.println("No transactions found.");
            }

        } catch (Exception e) {
            System.out.println("Error retrieving all transactions: " + e.getMessage());
        }
    }
    public void search_transaction(){
        System.out.print("Enter Transaction ID to search: ");
        int txnID = sc.nextInt();

        try {
            Connection con = DBConnect.connect();

            String query = """
            SELECT t.TransactionID, m.Name, m.Username, b.Title, t.Quantity, t.TransactionDate, t.Type
            FROM Transactions t
            JOIN LibraryMembers m ON t.MemberID = m.MemberID
            JOIN Books b ON t.BookID = b.BookID
            WHERE t.TransactionID = ?
        """;

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, txnID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                System.out.println("\n--- Transaction Details ---");
                System.out.println("Transaction ID: " + result.getInt("TransactionID"));
                System.out.println("Member Name   : " + result.getString("Name"));
                System.out.println("Username      : " + result.getString("Username"));
                System.out.println("Book Title    : " + result.getString("Title"));
                System.out.println("Quantity      : " + result.getInt("Quantity"));
                System.out.println("Date          : " + result.getTimestamp("TransactionDate"));
                System.out.println("Type          : " + result.getString("Type"));
            } else {
                System.out.println("Transaction ID not found.");
            }

        } catch (Exception e) {
            System.out.println("Error retrieving transaction: " + e.getMessage());
        }
    }

}
