package com.jerme.sis;

import com.jerme.sis.consoles.StudentMainConsole;
import com.jerme.sis.consoles.TeacherMainConsole;

import java.util.Scanner;

public class App {

    private static boolean sisConsoleIsRunning = true;


    private static void showMenu() {
        System.out.println("1. Teacher console");
        System.out.println("2. Student console");
        System.out.println("3. Exit");
    }

    private static void handleChoice(String choice) {
        switch (choice) {
            case "1" -> startTeacherConsole();
            case "2" -> startStudentConsole();
            case "3" -> {
                sisConsoleIsRunning = false;

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

            default -> System.out.println("Wrong input!");
        }
    }

    private static void startTeacherConsole() {
        TeacherMainConsole.startTeacherConsole();
    }

    private static void startStudentConsole() {
        StudentMainConsole.getStudentConsoleStarting();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sisConsoleIsRunning) {
            System.out.println("\nSTUDENT MANAGEMENT SYSTEM");
            showMenu();

            System.out.print("Enter choice here: ");
            String choice = sc.nextLine();

            handleChoice(choice);
        }
    }
}
