package mu.rekolt.util;

import java.util.Scanner;

public class InputValidator {
    public static int readQualityScore(Scanner scanner) {
        while (true) {
            System.out.print("Quality score (0-100): ");
            String line = scanner.nextLine();
            try {
                int score = Integer.parseInt(line);
                if (score >= 0 && score <= 100) {
                    return score;
                } else {
                    System.out.println("Quality score must be between 0 and 100. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Quality score must be a whole number. Please try again.");
            }
        }
    }

    public static String readMemberName(Scanner scanner){
        while (true) {
            System.out.print("Member name: ");
            String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    return line;
                } else  {
                    System.out.println("Member name cannot be empty. Please try again.");
                }
        }
    }

    public static double readMass(Scanner scanner) {
        while (true) {
            System.out.print("Mass in kg: ");
            String line = scanner.nextLine();
            try {
                double mass = Double.parseDouble(line);
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

    public static String readProduceCode(Scanner scanner) {
        while (true) {
            System.out.print("Produce code (MZE/BNS/POT/TEA): ");
            String line = scanner.nextLine();
            String code = line.trim().toUpperCase();

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

    public static String readMemberIdentifier(Scanner scanner) {
        while (true) {
            System.out.print("Member identifier: ");
            String line = scanner.nextLine();
            if (line.matches("^M-\\d{4}$")) {
                return line;
            } else {
                System.out.println("Member identifier must be in the format M-0042. Please try again.");
            }
        }
    }
}


