package mu.rekolt.util;

import java.util.Scanner;

// This class handles all user input and validation so the Main class stays clean.
public class InputValidator {

    // Loop forever (while true) until the user provides a valid score.
    public static int readQualityScore(Scanner scanner) {
        while (true) {
            System.out.print("Quality score (0-100): ");
            String line = scanner.nextLine(); // Read the input as a String first (handles everything)
            try {
                // Try to convert the string to an integer
                int score = Integer.parseInt(line);
                // If it is a whole number and between 0 and 100, return it
                if (score >= 0 && score <= 100) {
                    return score;
                } else {
                    // Otherwise, tell the user and loop again
                    System.out.println("Quality score must be between 0 and 100. Please try again.");
                }
            } catch (NumberFormatException e) {
                // If the user types letters, this catches the error and prevents a crash
                System.out.println("Quality score must be a whole number. Please try again.");
            }
        }
    }

    // Loop until user enters a valid name.
    public static String readMemberName(Scanner scanner){
        while (true) {
            System.out.print("Member name: ");
            String line = scanner.nextLine();
            // Check if the string is not empty (after removing whitespace)
                if (!line.trim().isEmpty()) {
                    return line;
                } else  {
                    System.out.println("Member name cannot be empty. Please try again.");
                }
        }
    }

    // Loop until user enters a valid mass.
    public static double readMass(Scanner scanner) {
        while (true) {
            System.out.print("Mass in kg: ");
            String line = scanner.nextLine();
            try {
                // Try to convert the string to a double (decimal number)
                double mass = Double.parseDouble(line);
                // Must be above 0 and not more than 5000
                if (mass > 0 && mass <= 5000) {
                    return mass;
                } else {
                    System.out.println("Mass must be above 0 and not more than 5000 kg. Please try again.");
                }
            } catch (NumberFormatException e){
                System.out.println("Mass must be a number. Please try again.");
            }
        }
    }

    // Loop until user enters a valid week.
    public static int readWeek(Scanner scanner) {
        while (true) {
            System.out.print("Week (1-20): ");
            String line = scanner.nextLine();
            try {
                int week = Integer.parseInt(line);
                if (week >= 1 && week <= 20) {
                    return week;
                } else {
                    System.out.println("Week must be between 1 and 20. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Week must be a whole number. Please try again.");
            }
        }
    }

    // Loop until user enters a valid produce code.
    public static String readProduceCode(Scanner scanner) {
        while (true) {
            System.out.print("Produce code (MZE/BNS/POT/TEA): ");
            String line = scanner.nextLine();
            // Convert to uppercase so the user can type "mze" or "MZE"
            String code = line.trim().toUpperCase();

            // Using a switch to check the 4 valid codes
            switch (code) {
                case "MZE":
                case "BNS":
                case "POT":
                case "TEA":
                    return code;
                default:
                    System.out.println("Produce code " + code + "is not valid. Please try again.");
            }
        }
    }

    // Loop until user enters a valid Member ID.
    public static String readMemberIdentifier(Scanner scanner) {
        while (true) {
            System.out.print("Member identifier: ");
            String line = scanner.nextLine();
            // Regex: Must start with M-, followed by exactly 4 digits (e.g., M-0042)
            if (line.matches("^M-\\d{4}$")) {
                return line;
            } else {
                System.out.println("Member identifier must be in the format M-0042. Please try again.");
            }
        }
    }
}


