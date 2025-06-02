package Main.Librarian;

import Main.Login.Login_page;
import Main.Transaction.Transaction_Management;

import java.util.Scanner;

public class Librarian {
    int recursion_count = 0;
    private Scanner sc = new Scanner(System.in);

    public void librarian() {
        System.out.println("Librarian login");
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (user.equals("admin") && pass.equals("admin")) {
            System.out.println("Logged in as librarian.");
            showMenu();
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    public void showMenu() {

        while (true) {
            System.out.println(""" 
                    
                    1. Manage Books
                    2. Manage Members
                    3. View Transactions
                    4. Logout
                    
                    Choose an option """);
            int choise = sc.nextInt();

            switch (choise) {
                case 1:
                    Book_management bm = new Book_management();
                    bm.showBookMenu();

                    break;
                case 2:
                    Members_Management mm = new Members_Management();
                    mm.showMembersMenu();

                    break;
                case 3:
                    Transaction_Management tm = new Transaction_Management();
                    tm.showTransactionHistoryMenu();
                    break;
                case 4:
                    Login_page l = new Login_page();
                    l.loginpage();
                    break;
                default:
                    System.out.println("Invalid Option please try again");
                    break;
            }

        }
    }

    public void add_recursion_count(int count) {
        if (recursion_count < 10) {
            recursion_count++;
            System.out.println("Warning: You have " + (10 - recursion_count) + " tries left for back to Menu Card.");
            showMenu();

        } else {
            System.out.println("You're Reaching the limit Please Login again");

        }Login_page l = new Login_page();
        l.loginpage();

    }
}
