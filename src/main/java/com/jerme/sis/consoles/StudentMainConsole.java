package com.jerme.sis.consoles;

import com.jerme.sis.entities.*;
import com.jerme.sis.repository.StudentRepository;
import com.jerme.sis.security.*;
import com.jerme.sis.services.*;
import com.jerme.sis.util.EmailAuthenticator;
import com.jerme.sis.util.InputAuthenticator;

import java.util.Scanner;


//username: jere@plv.edu.ph, password: jermecyrus12!
public class StudentMainConsole {

    private static final Scanner IN = new Scanner(System.in);

    private static Student currentStudent = null;

    private final StudentAccountService ACC_SERVICE;

    private final StudentRepository STUDENT_REPO;

    private static boolean loginConsoleIsRunning = false;

    public StudentMainConsole(StudentAccountService ACC_SERVICE, StudentRepository STUDENT_REPO) {
        this.ACC_SERVICE = ACC_SERVICE;
        this.STUDENT_REPO = STUDENT_REPO;
    }

    private void loginConsole() throws Exception {
        System.out.println("STUDENT MANAGEMENT SYSTEM (Student Console)");

        loginConsoleIsRunning = true;

        while (loginConsoleIsRunning) {
            showLoginMenu();
            System.out.print("Enter choice here: ");
            String choice = getLoginChoice();
            handleLoginChoice(choice);
        }
    }

    private void showLoginMenu() {
        System.out.println("\n1. Sign up");
        System.out.println("2. Sign in");
        System.out.println("3. Exit");
    }

    private String getLoginChoice() {
        return IN.nextLine();
    }

    private void handleLoginChoice(String choice) throws Exception {
        switch (choice) {
            case "1" -> studentSignUp();
            case "2" -> studentSignIn();
            case "3" -> exit();
            default -> System.out.println("Wrong input!");
        }
    }

    private void studentSignUp() throws Exception {
        String email;
        String password;
        String confirmPassword;

        do {
            System.out.print("Enter email here: ");
            email = IN.nextLine();

            if (!ACC_SERVICE.hasProperEmail(email))
                ACC_SERVICE.printEmailError(email);

            if (STUDENT_REPO.existsByEmail(email))
                System.out.println("Email already been in used.");

        } while (!ACC_SERVICE.hasProperEmail(email)
                || STUDENT_REPO.existsByEmail(email));

        do {
            System.out.print("Enter password here: ");
            password = IN.nextLine();

            if (!ACC_SERVICE.hasProperPassword(password))
                ACC_SERVICE.printPasswordError(password);

        } while (!ACC_SERVICE.hasProperPassword(password));

        do {
            System.out.print("Enter confirmation of password here: ");
            confirmPassword = IN.nextLine();

            if (!confirmPassword.equals(password)) System.out.println("Password does not match.");

        } while (confirmPassword.isBlank() || !confirmPassword.equals(password));

        System.out.println("Account set up has been completed! Edit your personal information now!.");

        String firstName;
        String middleName;
        String lastName;
        String strID;
        String program;
        String gwa;
        int id = 0;

        do {
            System.out.print("Enter first name here: ");
            firstName = IN.nextLine();

            if (!ACC_SERVICE.hasProperName(firstName))
                ACC_SERVICE.printNameError(firstName);

        } while (!ACC_SERVICE.hasProperName(firstName));

        do {
            System.out.print("Enter middle name here: ");
            middleName = IN.nextLine();

            if (!ACC_SERVICE.hasProperName(middleName))
                ACC_SERVICE.printNameError(middleName);

        } while (!ACC_SERVICE.hasProperName(middleName));

        do {
            System.out.print("Enter last name here: ");
            lastName = IN.nextLine();

            if (!ACC_SERVICE.hasProperName(lastName))
                ACC_SERVICE.printNameError(lastName);

        } while (!ACC_SERVICE.hasProperName(lastName));

        do {
            System.out.print("Enter program's acronym here (e.g., BSIT): ");
            program = IN.nextLine();

            if (!ACC_SERVICE.hasOfferedProgram(program))
                ACC_SERVICE.printOfferedProgramError(program);

        } while (!ACC_SERVICE.hasOfferedProgram(program));

        do {
            System.out.print("Enter School ID here: ");
            strID = IN.nextLine();

            if (!ACC_SERVICE.hasProperIntInput(strID))
                System.out.println("YOUR INPUT HAS NON-NUMERICAL CHARACTER.");

            else {
                id = Integer.parseInt(strID);

                if (STUDENT_REPO.existsById(id))
                    System.out.println("This is already been taken. Is this really yours?");
            }

        } while (!ACC_SERVICE.hasProperIntInput(strID) ||
                STUDENT_REPO.existsById(id));

        do {
            System.out.print("Enter General Weighted Average (GWA) here: ");
            gwa = IN.nextLine();

            if (!ACC_SERVICE.hasProperGWA(gwa))
                ACC_SERVICE.printGWAError(gwa);

        } while (!ACC_SERVICE.hasProperGWA(gwa));

        if (STUDENT_REPO.insert(new Student(Integer.parseInt(strID),
                        firstName,
                        middleName,
                        lastName,
                        email,
                        PasswordHasher.hash(confirmPassword),
                        program.toUpperCase(),
                        Double.parseDouble(gwa)
                ))
                .isPresent()) System.out.println("Account set up is done! Sign in now!");

        else System.out.println("An unexpected error occurred!");
    }

    private void studentSignIn() throws Exception {
        String email;
        String password;

        do {
            System.out.print("Enter email here: ");
            email = IN.nextLine();

            if (!ACC_SERVICE.hasProperEmail(email))
                ACC_SERVICE.printEmailError(email);
            else if (!STUDENT_REPO.existsByEmail(email))
                System.out.println("Email does not exist.");

        } while (!STUDENT_REPO.existsByEmail(email)
                || !ACC_SERVICE.hasProperEmail(email));

        String storedPass = STUDENT_REPO.findByEmail(email)
                .map(Student::getPassword).orElse("");

        do {
            System.out.print("Enter password here: ");
            password = IN.nextLine();

            if (!PasswordHasher.verify(password, storedPass))
                System.out.println("Wrong password!");

        } while (!PasswordHasher.verify(password, storedPass));

        currentStudent = STUDENT_REPO.findByEmail(email).orElseThrow();

        if (STUDENT_REPO.findByEmail(email).isPresent()) {
            System.out.println("Signed in successfully!");

            studentMainConsole();
        }

        else System.out.println("Unexpected error occurred.");

    }

    private void exit() {
        loginConsoleIsRunning = false;

        try {
            System.out.print("\rExiting.");
            Thread.sleep(500);
            System.out.print("\rExiting..");
            Thread.sleep(500);
            System.out.print("\rExiting...");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("\rSuccessful exit.");
    }

    private boolean mainConsoleIsRunning = false;

    private void studentMainConsole() {
        mainConsoleIsRunning = true;

        while (mainConsoleIsRunning) {
            showConsoleChoice();
            System.out.print("Enter choice here: ");
            String choice = getConsoleChoice();
            handleMainConsoleChoice(choice);
        }
    }

    private void showConsoleChoice() {
        System.out.println("\n1. View Profile");
        System.out.println("2. Update your information");
        System.out.println("3. View Top 10 students");
        System.out.println("4. Search for a student by ID");
        System.out.println("5. Delete account");
        System.out.println("6. Sign out");
    }

    private String getConsoleChoice() {
        return IN.nextLine();
    }

    private void handleMainConsoleChoice(String choice) {
        switch (choice.trim()) {
            case "1" -> viewProfile();
            case "2" -> updateProfile();
            case "3" -> viewTop10Students();
            case "4" -> searchStudentById();
            case "5" -> delete();
            case "6" -> signOut();
            default  -> System.out.println("Wrong input!");
        }
    }

    private void viewProfile() {
        try {
            System.out.println("My Profile: ");
            System.out.printf("Name:       %s%n", currentStudent.getFirstName() + " " + currentStudent.getMiddleName() + " " + currentStudent.getLastName());
            System.out.printf("Student ID: %d%n", currentStudent.getId());
            System.out.printf("Program:    %s%n", currentStudent.getProgram());
            System.out.printf("GWA:        %.2f%n", currentStudent.getGwa());
            System.out.printf("Email:      %s%n", currentStudent.getEmail());
        } catch (NullPointerException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void updateProfile() {
        showUpdateMenu();
        System.out.print("Enter update choice here: ");
        String choice = IN.nextLine();
        handleUpdateChoice(choice);
    }

    private void showUpdateMenu() {
        System.out.println("\n1. Update name");
        System.out.println("2. Update program"); //for possible course shifting :)
        System.out.println("3. Update password");
        System.out.println("4. Cancel update");
    }

    private void handleUpdateChoice(String choice) {
        try {
            switch (choice) {
                case "1" -> updateName();
                case "2" -> updateProgram();
                case "3" -> updatePassword();
                case "4" -> System.out.print("");
                default -> System.out.println("Wrong input!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        String password;
        String confirmation;

        try {
            do {
                System.out.print("Enter password here: ");
                password = IN.nextLine();

            } while (!PasswordHasher.verify(password, currentStudent.getPassword()));

            do {
                System.out.print("Are you sure you want delete your account (Y/N)? ");
                confirmation = IN.nextLine();

                if (!confirmation.equalsIgnoreCase("Y") && !confirmation.equalsIgnoreCase("N"))
                    System.out.println("Your input has other input aside from Y or N.");

            } while (!confirmation.equalsIgnoreCase("Y") && !confirmation.equalsIgnoreCase("N"));

            if (confirmation.equalsIgnoreCase("Y")) {
                System.out.println("Account deleted successfully.");
                STUDENT_REPO.delete(currentStudent.getId());
                currentStudent = null;
                mainConsoleIsRunning = false;

            } else if (confirmation.equalsIgnoreCase("N"))
                System.out.println("Deletion cancelled.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateName() {
        String firstName;
        String middleName;
        String lastName;
        String password;

        do {
            System.out.print("Enter first name here: ");
            firstName = IN.nextLine();

            if (!ACC_SERVICE.hasProperName(firstName))
                ACC_SERVICE.printNameError(firstName);

        } while (!ACC_SERVICE.hasProperName(firstName));

        do {
            System.out.print("Enter middle name here: ");
            middleName = IN.nextLine();

            if (!ACC_SERVICE.hasProperName(middleName))
                ACC_SERVICE.printNameError(middleName);

        } while (!ACC_SERVICE.hasProperName(middleName));

        do {
            System.out.print("Enter last name here: ");
            lastName = IN.nextLine();

            if (!ACC_SERVICE.hasProperName(lastName))
                ACC_SERVICE.printNameError(lastName);

        } while (!ACC_SERVICE.hasProperName(lastName));

        System.out.print("Enter password to execute update here: ");
        password = IN.nextLine();

        try {
            boolean match = PasswordHasher.verify(password, currentStudent.getPassword());

            if (match) {
                boolean hasBeenUpdated = STUDENT_REPO.update(new Student(
                        currentStudent.getId(),
                        firstName,
                        middleName,
                        lastName,
                        currentStudent.getEmail(),
                        currentStudent.getPassword(),
                        currentStudent.getProgram(),
                        currentStudent.getGwa()
                ));

                System.out.println(hasBeenUpdated ? "Name has been updated!" : "Error while updating the name. Try again.");

            } else System.out.println("Wrong password! Try again. ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProgram() {
        String program;
        String password;

        do {
            System.out.print("Enter new program here: ");
            program = IN.nextLine();

            if (!ACC_SERVICE.hasOfferedProgram(program))
                ACC_SERVICE.hasOfferedProgram(program);

        } while (!ACC_SERVICE.hasOfferedProgram(program));

        try {
            System.out.print("Enter password here to execute update here: ");
            password = IN.nextLine();

            if (!PasswordHasher.verify(password, currentStudent.getPassword()))
                System.out.println("Password does not match. Try again. ");

            else {
                boolean hasBeenUpdated = STUDENT_REPO.update(new Student(
                        currentStudent.getId(),
                        currentStudent.getFirstName(),
                        currentStudent.getMiddleName(),
                        currentStudent.getLastName(),
                        currentStudent.getEmail(),
                        currentStudent.getPassword(),
                        program,
                        currentStudent.getGwa()
                ));

                System.out.println(hasBeenUpdated ? "Program has been updated! " : "Unexpected error occurred while updating program.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePassword() throws Exception {
        String currentPassword;
        String newPassword;
        String confirmNewPassword;

        do {
            System.out.print("Enter current password here: ");
            currentPassword = IN.nextLine();

            if (!PasswordHasher.verify(currentPassword, currentStudent.getPassword()))
                System.out.println("Incorrect password!");

        } while (!PasswordHasher.verify(currentPassword, currentStudent.getPassword()));

        do {
            System.out.print("Enter new password here: ");
            newPassword = IN.nextLine();

            if (!ACC_SERVICE.hasProperPassword(newPassword))
                ACC_SERVICE.printPasswordError(newPassword);

        } while (!ACC_SERVICE.hasProperPassword(newPassword));

        do {
            System.out.print("Enter confirmation of new password here: ");
            confirmNewPassword = IN.nextLine();

            if (confirmNewPassword.isBlank())
                System.out.println("Input cannot be blank.");

            else if (!newPassword.equals(confirmNewPassword))
                System.out.println("Password does not match.");

        } while (!newPassword.equals(confirmNewPassword) || confirmNewPassword.isBlank());

        boolean hasBeenUpdated = STUDENT_REPO.update(new Student(
                currentStudent.getId(),
                currentStudent.getFirstName(),
                currentStudent.getMiddleName(),
                currentStudent.getLastName(),
                currentStudent.getEmail(),
                PasswordHasher.hash(confirmNewPassword),
                currentStudent.getProgram(),
                currentStudent.getGwa()
        ));

        System.out.println(hasBeenUpdated ? "Password has been changed." : "Error with password update. Try again.");
    }

    private void viewTop10Students() {
        String query = """
                SELECT * FROM students
                ORDER BY gwa
                LIMIT 10
                """;

        System.out.println("====================================================TOP 10 STUDENTS======================================================");
        System.out.printf("|%-9s | %-45s | %-7s | %-5s | %-40s |%n", "School ID", "Name", "Program", "GWA", "School Email");

        if (STUDENT_REPO.read(query).isEmpty())
            System.out.println("|                                           TOP STUDENT IS EMPTY                                          |");

        for (Student student : STUDENT_REPO.read(query)) {
            String name = student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName();

            System.out.printf("|%-9d | %-45s | %-7s | %-5.2f | %-40s |%n",
                    student.getId(), name, student.getProgram(), student.getGwa(), student.getEmail());
        }
        System.out.println("=========================================================================================================================");
    }

    private void searchStudentById() {
        String id;

        do {
            System.out.print("Enter ID to find here: ");
            id = IN.nextLine();

            if (!ACC_SERVICE.hasProperIntInput(id))
                System.out.println("YOUR INPUT HAS NON-NUMERICAL CHARACTER.");

        } while (!ACC_SERVICE.hasProperIntInput(id));

        if (STUDENT_REPO.findById(Integer.parseInt(id)).isPresent()) {
            Student student = STUDENT_REPO.findById(Integer.parseInt(id)).get();

            String name = student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName();
            System.out.println("=======================================================FOUND STUDENT=====================================================");
            System.out.printf("|%-9s | %-45s | %-7s | %-5s | %-40s |%n", "School ID", "Name", "Program", "GWA", "School Email");
            System.out.printf("|%-9d | %-45s | %-7s | %-5.2f | %-40s |%n",
                    student.getId(), name, student.getProgram(), student.getGwa(), student.getEmail());
            System.out.println("=========================================================================================================================");

        } else {
            System.out.println("==========================================================================================================");
            System.out.println("|                                           NO STUDENT FOUND                                             |");
            System.out.println("==========================================================================================================");
        }
    }

    private void signOut() {
        System.out.print("Do you want to sign out? (Y (Yes)/N (No)): ");
        String decision = IN.nextLine();

        if (decision.equalsIgnoreCase("Y")) {
            currentStudent = null;
            mainConsoleIsRunning = false;
            System.out.println("Signed out successfully!");
        } else if (decision.equalsIgnoreCase("Y") && decision.equalsIgnoreCase("N")) {
            System.out.println("Decision must be between Y (Yes) or N (No)!");
        }
    }

    public static void getStudentConsoleStarting() {

        try {
            new StudentMainConsole(new StudentAccountService(
                    new EmailAuthenticator(),
                    new InputAuthenticator()),
                    new StudentRepository()).
                    loginConsole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}