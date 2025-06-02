package Main.Librarian;

import Main.DBConnect.DBConnect;
import Main.Login.Login_page;

import java.sql.*;
import java.util.Scanner;

public class Members_Management {
    int recursion_count = 0;

    Scanner sc = new Scanner(System.in);

    public void showMembersMenu() {
        int recursion_count = 0;
        while (true) {

            System.out.println(""" 
                    --- Manage Members Menu ---
                    
                    1. Add New Library Member
                    2. Remove Library Member
                    3. View All Library Member
                    4. Back to Menu card
                    5. Logout  """);

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addmember();
                    break;
                case 2:
                    removemember();
                    break;
                case 3:
                    viewmembers();
                    break;
                case 4:
                    add_recursion_count(1);
                    Librarian l = new Librarian();
                    l.showMenu();
                    break;
                case 5:
                    Login_page li = new Login_page();
                    li.loginpage();
                    break;
                default:
                    System.out.println("Invalid Option please try again");
                    break;
            }
        }
    }

    public void addmember(){
        System.out.print("Enter username : ");
        String username = sc.next();

        System.out.print("Enter Password : ");
        String password = sc.next();

        System.out.print("Enter Member Name : ");
        String name = sc.next();


        try {

            Connection con = DBConnect.connect();
            String query = "INSERT INTO librarymembers (Username, Password, Name) VALUES( ?,?,?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2, password);
            statement.setString(3,name);

            int result = statement.executeUpdate();

            if (result > 0){
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book.");
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    public void removemember(){
        System.out.println("""
                Remove using
                
                1. Username
                2. Member Name 
                3. Exit
                """);
        int choice = sc.nextInt();

        switch (choice){
            case 1:
                System.out.print("Enter Username :");
                String username = sc.next();
                try{
                    Connection con = DBConnect.connect();
                    String query = "DELETE FROM librarymembers WHERE Username = ? ";
                    PreparedStatement statement = con.prepareStatement(query);
                    statement.setString(1,username);
                    int result = statement.executeUpdate();

                    if (result > 0){
                        System.out.println("member Deleted Successfully");
                    }else {
                        System.out.println("No Members found with that Username" + username);
                }
        } catch (Exception e) {
                    System.out.println("Error in Removing " + e.getMessage());
                }
                break;

            case 2 :
                System.out.print("Enter Membername :");
                String name = sc.next();
                try{
                    Connection con = DBConnect.connect();
                    String query = "DELETE FROM librarymembers WHERE Name = ? ";
                    PreparedStatement statement = con.prepareStatement(query);
                    statement.setString(1,name);
                    int result = statement.executeUpdate();

                    if (result > 0){
                        System.out.println("member Deleted Successfully");
                    }else {
                        System.out.println("No Members found with that Username");
                    }
                } catch (Exception e) {
                    System.out.println("Error in Removing " + e.getMessage());
                }
                break;
            case 3:
                add_recursion_count(1);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                break;
        }
    }

    public void viewmembers(){

        System.out.println("""
                Choose an option 
                
                1. List Only Members Name
                2. List Members full details
                3. Exit
                 
                """);
        int choice = sc.nextInt();

        switch (choice){
            case 1:
                try {
                    Connection con = DBConnect.connect();
                    String query = " SELECT Name FROM librarymembers";

                    PreparedStatement statement = con.prepareStatement(query);

                    ResultSet result = statement.executeQuery();

                    while (result.next()){
                        System.out.println(result.getString("Name"));
                    }


                } catch (Exception e) {
                    System.out.println("Error Viewing members List: " + e.getMessage());
                }
                break;

            case 2:
                try{
                    Connection con = DBConnect.connect();
                    String query = "SELECT * FROM librarymembers";
                    PreparedStatement statement = con.prepareStatement(query);

                    ResultSet result = statement.executeQuery();

                    while (result.next()){
                        System.out.println("Username : " + result.getString("Name"));
                        System.out.println("Password : " + result.getString("Password"));
                        System.out.println("Member name : " + result.getString("Name"));
                        System.out.println("------");
                    }
                }
                catch (Exception e ){
                    System.out.println("Error in fetching List" + e.getMessage());
                }
            case 3:
                add_recursion_count(1);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                break;
        }



    }

    public void add_recursion_count(int count) {
        if (recursion_count < 10) {
            recursion_count++;
            System.out.println("Warning: You have " + (10 - recursion_count) + " tries left for back to Menu Card.");
            showMembersMenu();

        } else {
            System.out.println("You're Reaching the limit Please Login again");

        }
        Login_page l = new Login_page();
        l.loginpage();

    }
}
