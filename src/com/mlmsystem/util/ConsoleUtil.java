package com.mlmsystem.util;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUtil {
    private static final Scanner scanner = new Scanner(System.in);
    
    // Rupee symbol constant
    public static final String RUPEE_SYMBOL = "₹";
    
    // Initialize with default system out
    public static final PrintStream out;
    
    static {
        // Ensure the console uses UTF-8 encoding for the rupee symbol
        System.setProperty("file.encoding", "UTF-8");
        out = System.out;
    }

    // Prevent instantiation
    private ConsoleUtil() {}
    
    public static void clearScreen() {
        try {
            // Simple cross-platform screen clearing
            for (int i = 0; i < 50; i++) {
                out.println();
            }
        } catch (Exception e) {
            // Ignore any errors during screen clearing
        }
    }

    public static void printHeader(String title) {
        out.println();
        printLine(60);
        printCentered(title, 60);
        printLine(60);
    }

    private static void printLine(int length) {
        for (int i = 0; i < length; i++) {
            out.print("=");
        }
        out.println();
    }

    private static void printCentered(String text, int width) {
        int spaces = (width - text.length()) / 2;
        for (int i = 0; i < spaces; i++) {
            out.print(" ");
        }
        out.println(text);
    }

    public static void printSuccess(String message) {
        out.println("\n[SUCCESS] " + message);
    }

    public static void printError(String message) {
        out.println("\n[ERROR] " + message);
    }

    public static void printInfo(String message) {
        out.println("\n[INFO] " + message);
    }

    public static void pressEnterToContinue() {
        out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static String readString(String prompt) {
        out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
    
    public static double readDouble(String prompt) {
        while (true) {
            try {
                out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }
    
    public static void closeScanner() {
        // Don't close System.in scanner to avoid issues
        // scanner.close();
    }
    
    // Helper method to format currency with rupee symbol
    public static String formatCurrency(double amount) {
        return String.format("%s%.2f", RUPEE_SYMBOL, amount);
    }
}