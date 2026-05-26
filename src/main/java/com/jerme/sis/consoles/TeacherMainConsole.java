package com.jerme.sis.consoles;

import com.jerme.sis.entities.*;
import com.jerme.sis.repository.StudentRepository;
import com.jerme.sis.repository.TeacherRepository;
import com.jerme.sis.security.*;
import com.jerme.sis.services.*;
import com.jerme.sis.util.EmailAuthenticator;
import com.jerme.sis.util.InputAuthenticator;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public record TeacherMainConsole(TeacherAccountService ACC_SERVICE,
                                 TeacherRepository TEACHER_REPO,
                                 StudentRepository STUDENT_REPO) {

    private static final Scanner IN = new Scanner(System.in);

    private static Teacher currentTeacher = null;

    private static boolean teacherLoginConsoleIsRunning = false;
    private static boolean teacherMainConsoleIsRunning  = false;

    private void loginConsole() {
        System.out.println("STUDENT MANAGEMENT SYSTEM (Teacher Console)");

        teacherLoginConsoleIsRunning = true;

        while (teacherLoginConsoleIsRunning) {
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

    private void handleLoginChoice(String choice) {
        switch (choice) {
            case "1" -> teacherSignUp();
            case "2" -> teacherSignIn();
            case "3" -> exit();
            default -> System.out.println("Wrong input!");
        }
    }

    private void teacherSignUp() {
        try {
            String email;
            String password;
            String confirmPassword;

            do {
                System.out.print("Enter email here: ");
                email = IN.nextLine();

                if (!ACC_SERVICE.hasProperEmail(email))
                    ACC_SERVICE.printEmailError(email);

                if (TEACHER_REPO.existsByEmail(email))
                    System.out.println("Email already been in used.");

            } while (!ACC_SERVICE.hasProperEmail(email)
                    || TEACHER_REPO.existsByEmail(email));

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
            String department;
            String teacherLevel;
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
                System.out.print("Enter department acronym here (e.g., CEIT): ");
                department = IN.nextLine();

                if (!ACC_SERVICE.hasProperDepartment(department))
                    ACC_SERVICE.printDepartmentError(department);

            } while (!ACC_SERVICE.hasProperDepartment(department));

            do {
                System.out.print("Enter Teacher ID here: ");
                strID = IN.nextLine();

                if (!ACC_SERVICE.hasProperIntInput(strID))
                    System.out.println("YOUR INPUT HAS NON-NUMERICAL CHARACTER.");

                else {
                    id = Integer.parseInt(strID);

                    if (TEACHER_REPO.existsById(id))
                        System.out.println("This is already been taken. Is this really yours?");
                }

            } while (!ACC_SERVICE.hasProperIntInput(strID) ||
                    TEACHER_REPO.existsById(id));

            do {
                System.out.print("Enter teacher level here: ");
                teacherLevel = IN.nextLine();

                if (!ACC_SERVICE.hasProperIntInput(teacherLevel))
                    System.out.println("YOUR INPUT HAS NON-NUMERICAL CHARACTER.");

            } while (!ACC_SERVICE.hasProperIntInput(teacherLevel) || !ACC_SERVICE.hasProperTeacherLevel(teacherLevel));

            if (TEACHER_REPO.insert(new Teacher(Integer.parseInt(strID),
                            firstName,
                            middleName,
                            lastName,
                            email,
                            PasswordHasher.hash(confirmPassword),
                            department.toUpperCase(),
                            Integer.parseInt(teacherLevel)
                    ))
                    .isPresent()) System.out.println("Account set up is done! Sign in now!");

            else System.out.println("An unexpected error occurred!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void teacherSignIn() {
        try {
            String email;
            String password;

            do {
                System.out.print("Enter email here: ");
                email = IN.nextLine();

                if (!ACC_SERVICE.hasProperEmail(email))
                    ACC_SERVICE.printEmailError(email);
                else if (!TEACHER_REPO.existsByEmail(email))
                    System.out.println("Email does not exist.");

            } while (!TEACHER_REPO.existsByEmail(email)
                    || !ACC_SERVICE.hasProperEmail(email));

            String storedPass = TEACHER_REPO.findByEmail(email)
                    .map(Teacher::getPassword).orElse("");

            do {
                System.out.print("Enter password here: ");
                password = IN.nextLine();

                if (!PasswordHasher.verify(password, storedPass))
                    System.out.println("Wrong password!");

            } while (!PasswordHasher.verify(password, storedPass));

            currentTeacher = TEACHER_REPO.findByEmail(email).orElseThrow();

            if (TEACHER_REPO.findByEmail(email).isPresent()) {
                System.out.println("Signed in successfully!");

                teacherMainConsoleIsRunning = true;
                teacherMainConsole();
            } else System.out.println("Unexpected error occurred.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void teacherMainConsole() {
        while (teacherMainConsoleIsRunning) {
            showTeacherMenu();
            System.out.print("Enter choice here: ");
            String choice = getMainConsoleChoice();
            handleMainConsoleChoice(choice);
        }
    }

    private void showTeacherMenu() {
        System.out.println("\n1. View Profile");
        System.out.println("2. Edit information");
        System.out.println("3. Edit a student password");
        System.out.println("4. Edit student GWA");
        System.out.println("5. View enrolled students");
        System.out.println("6. View enrolled students (Program)");
        System.out.println("7. Search a student");
        System.out.println("8. Sign out");
    }

    private String getMainConsoleChoice() {
        return IN.nextLine();
    }

    private void handleMainConsoleChoice(String choice) {
        switch (choice) {
            case "1" -> viewProfile();
            case "2" -> editProfile();
            case "3" -> editStudentPassword();
            case "4" -> editStudentGwa();
            case "5" -> viewAllEnrolledStudents();
            case "6" -> viewEnrolledStudentsByProgram();
            case "7" -> searchStudent();
            case "8" -> signOut();
            default -> System.out.println("Wrong input");
        }
    }

    private void viewProfile() {
        try {
            System.out.println("My Profile: ");
            System.out.printf("Name:          %s%n", currentTeacher.getFirstName() + " " + currentTeacher.getMiddleName() + " " + currentTeacher.getLastName());
            System.out.printf("Teacher ID:    %d%n", currentTeacher.getId());
            System.out.printf("Department:    %s%n", currentTeacher.getDepartment());
            System.out.printf("Teacher Level: %d%n", currentTeacher.getTeacherLevel());
            System.out.printf("Email:         %s%n", currentTeacher.getEmail());
        } catch (NullPointerException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void showEditProfileChoices() {
        System.out.println("\n1. Name");
        System.out.println("2. Password");
        System.out.println("3. Department");
        System.out.println("4. Cancel Update");
    }

    private void editPassword() {
        try {
            String newPassword;
            String confirmPassword;
            String currPassword;

            do {
                System.out.print("Enter new password here: ");
                newPassword = IN.nextLine();

                if (!ACC_SERVICE.hasProperPassword(newPassword))
                    ACC_SERVICE.printPasswordError(newPassword);

            } while (!ACC_SERVICE.hasProperPassword(newPassword));

            do {
                System.out.print("Confirm new password here: ");
                confirmPassword = IN.nextLine();

                if (confirmPassword.isBlank())
                    System.out.println("Password should not be blank.");

                else if (!newPassword.equals(confirmPassword))
                    System.out.println("Password should match!");

            } while (!newPassword.equals(confirmPassword) || confirmPassword.isBlank());

            System.out.print("Enter current password here: ");
            currPassword = IN.nextLine();

            boolean matched = PasswordHasher.verify(currPassword, currentTeacher.getPassword());

            if (!matched) {
                System.out.println("Input must match with current password!");
                return;
            }

            boolean updated = TEACHER_REPO.update(new Teacher(
                            currentTeacher.getId(),
                            currentTeacher.getFirstName(),
                            currentTeacher.getMiddleName(),
                            currentTeacher.getLastName(),
                            currentTeacher.getEmail(),
                            PasswordHasher.hash(confirmPassword),
                            currentTeacher.getDepartment(),
                            currentTeacher.getTeacherLevel()
                    )
            );

            System.out.println(updated ? "Your password has been changed!" : "An unexpected error occurred.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void editName() {
        try {
            String firstName;
            String middleName;
            String lastName;
            String currPassword;

            do {
                System.out.print("Enter new first name here: ");
                firstName = IN.nextLine();

                if (!ACC_SERVICE.hasProperName(firstName))
                    ACC_SERVICE.printNameError(firstName);

            } while (!ACC_SERVICE.hasProperName(firstName));

            do {
                System.out.print("Enter new middle name here: ");
                middleName = IN.nextLine();

                if (!ACC_SERVICE.hasProperName(middleName))
                    ACC_SERVICE.printNameError(middleName);

            } while (!ACC_SERVICE.hasProperName(middleName));

            do {
                System.out.print("Enter new last name here: ");
                lastName = IN.nextLine();

                if (!ACC_SERVICE.hasProperName(lastName))
                    ACC_SERVICE.printNameError(lastName);

            } while (!ACC_SERVICE.hasProperName(lastName));

            System.out.print("Enter current password here: ");
            currPassword = IN.nextLine();

            boolean matched = PasswordHasher.verify(currPassword, currentTeacher.getPassword());

            if (!matched) {
                System.out.println("Input must match with current password!");
                return;
            }

            boolean updated = TEACHER_REPO.update(new Teacher(
                            currentTeacher.getId(),
                            firstName,
                            middleName,
                            lastName,
                            currentTeacher.getEmail(),
                            currentTeacher.getPassword(),
                            currentTeacher.getDepartment(),
                            currentTeacher.getTeacherLevel()
                    )
            );

            System.out.println(updated ? "Your name has been updated!" : "An unexpected error occurred.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editDepartment() {
        try {
            String department;
            String currPassword;

            do {
                System.out.print("Enter new department here: ");
                department = IN.nextLine();

                if (!ACC_SERVICE.hasProperDepartment(department))
                    ACC_SERVICE.printDepartmentError(department);

            } while (!ACC_SERVICE.hasProperDepartment(department));

            System.out.print("Enter current password here: ");
            currPassword = IN.nextLine();

            boolean matched = PasswordHasher.verify(currPassword, currentTeacher.getPassword());

            if (!matched) {
                System.out.println("Input must match with current password!");
                return;
            }

            boolean updated = TEACHER_REPO.update(new Teacher(
                            currentTeacher.getId(),
                            currentTeacher.getFirstName(),
                            currentTeacher.getMiddleName(),
                            currentTeacher.getLastName(),
                            currentTeacher.getEmail(),
                            currentTeacher.getPassword(),
                            department,
                            currentTeacher.getTeacherLevel()
                    )
            );

            System.out.println(updated ? "Your department has been updated!" : "An unexpected error occurred.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editProfile() {
        showEditProfileChoices();
        System.out.print("Enter a choice here: ");
        String choice = IN.nextLine();

        switch (choice) {
            case "1" -> editName();
            case "2" -> editPassword();
            case "3" -> editDepartment();
            case "4" -> System.out.println("Edit process has been cancelled.");
            default -> System.out.println("Wrong input");
        }
    }

    private void editStudentPassword() {
        try {
            Student student = search().orElseThrow();
            String password;

            do {
                System.out.print("Enter password here: ");
                password = IN.nextLine();

                if (!ACC_SERVICE.hasProperPassword(password))
                    ACC_SERVICE.printPasswordError(password);

            } while (!ACC_SERVICE.hasProperPassword(password));

            boolean updated = STUDENT_REPO.update(new Student(
                            student.getId(),
                            student.getFirstName(),
                            student.getMiddleName(),
                            student.getLastName(),
                            student.getEmail(),
                            PasswordHasher.hash(password),
                            student.getProgram(),
                            student.getGwa()
                    )
            );

            //pag nag iba ung hashed password sa db ibig sabihin nabago ung pass
            System.out.println(updated ? "Your profile has been updated!" : "An unexpected error occurred.");

        } catch (NoSuchElementException e) {
            System.out.println("No student found.   ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editStudentGwa() {
        try {
            StudentAccountService sas = new StudentAccountService(new EmailAuthenticator(), new InputAuthenticator());
            Student student = search().orElseThrow();
            String gwa;

            System.out.println("Student has been found. Edit his/her GWA.");

            do {
                System.out.print("Enter new student GWA here: ");
                gwa = IN.nextLine();

                if (!sas.hasProperGWA(gwa))
                    sas.printGWAError(gwa);

            } while (!sas.hasProperGWA(gwa));

            boolean updated = STUDENT_REPO.update(new Student(
                    student.getId(),
                    student.getFirstName(),
                    student.getMiddleName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPassword(),
                    student.getProgram(),
                    Double.parseDouble(gwa)
                )
            );

            System.out.println(updated ? "Student's GWA has been updated!" : "An unexpected error occurred.");

        } catch (NumberFormatException e) {
            System.out.println("Your input contains a non-numerical character/s!");
        } catch (NoSuchElementException e) {
            System.out.println("Student not found.");
        }
    }

    private void viewAllEnrolledStudents() {
        String query = """
                SELECT * FROM students
                ORDER BY program
                """;

        System.out.println("================================================ALL REGISTERED STUDENTS==================================================");
        System.out.printf("|%-9s | %-45s | %-7s | %-5s | %-40s |%n", "School ID", "Name", "Program", "GWA", "School Email");

        if (STUDENT_REPO.read(query).isEmpty())
            System.out.println("|                                         NO STUDENT IS ENROLLED                                         |");

        for (Student student : STUDENT_REPO.read(query)) {
            String name = student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName();

            System.out.printf("|%-9d | %-45s | %-7s | %-5.2f | %-40s |%n",
                    student.getId(), name, student.getProgram(), student.getGwa(), student.getEmail());
        }
        System.out.println("=========================================================================================================================");
    }

    private void viewEnrolledStudentsByProgram() {
        System.out.print("Enter program to view here: ");
        String program = IN.nextLine().toUpperCase();

        boolean programIsOffered = Arrays.asList(StudentAccountService.OFFERED_PROGRAMS).contains(program);

        if (!programIsOffered) {
            System.out.println("Program is not offered by the school.");
            return;
        }

        String query = "SELECT * FROM students WHERE program = " + "'" + program + "'";

        System.out.println("================================================ALL REGISTERED STUDENTS==================================================");
        System.out.printf("|%-9s | %-45s | %-7s | %-5s | %-40s |%n", "School ID", "Name", "Program", "GWA", "School Email");

        if (STUDENT_REPO.read(query).isEmpty())
            System.out.println("|                                         NO STUDENT IS ENROLLED                                         |");

        for (Student student : STUDENT_REPO.read(query)) {
            String name = student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName();

            System.out.printf("|%-9d | %-45s | %-7s | %-5.2f | %-40s |%n",
                    student.getId(), name, student.getProgram(), student.getGwa(), student.getEmail());
        }
        System.out.println("=========================================================================================================================");

    }

    private void searchStudent() {
        try {
            Student student = search().orElseThrow();

            System.out.println("\nStudent has been found! \nHis/her information: ");
            System.out.printf("Name:       %s%n", student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName());
            System.out.printf("Student ID: %d%n", student.getId());
            System.out.printf("Program:    %s%n", student.getProgram());
            System.out.printf("Gwa:        %.2f%n", student.getGwa());
            System.out.printf("Email:      %s%n", student.getEmail());
        } catch (NoSuchElementException e) {
            System.out.println("Student does not exists!");
        }
    }

    private void signOut() {
        System.out.print("Do you want to sign out? (Y (Yes)/N (No)): ");
        String decision = IN.nextLine();

        if (decision.equalsIgnoreCase("Y")) {
            currentTeacher = null;
            teacherMainConsoleIsRunning = false;
        } else if (decision.equalsIgnoreCase("Y") && decision.equalsIgnoreCase("N")) {
            System.out.println("Decision must be between Y (Yes) or N (No)!");
        }
    }

    private Optional<Student> search() {

        try {
            System.out.print("Enter student ID here: ");
            String stringID = IN.nextLine();

            int id = Integer.parseInt(stringID);

            if (STUDENT_REPO.existsById(id))
                return STUDENT_REPO.findById(id);

        } catch (NumberFormatException e) {
            System.out.println("Error: Input contains non-numerical character/s!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void exit() {
        teacherLoginConsoleIsRunning = false;

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

        System.out.println("Successful exit.");
    }

    public static void startTeacherConsole() {

        try {
            new TeacherMainConsole(new TeacherAccountService(
                    new EmailAuthenticator(),
                    new InputAuthenticator()), new TeacherRepository(),
                    new StudentRepository())
                    .loginConsole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
