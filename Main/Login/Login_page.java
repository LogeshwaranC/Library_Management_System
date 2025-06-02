package Main.Login;

import Main.Librarian.Librarian;
import Main.Member.Member;

import java.util.Scanner;

public class Login_page {
    public static void loginpage(){
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("""
            Welcome to Library Management System"
            
            1. Login as Member
            2. Login as Librarian
            3. Exit 
            
            """);

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    Member m = new Member();
                    m.member();
                case 2:
                    Librarian l = new Librarian();
                    l.librarian();
                case 3:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }

        }
    }
}
