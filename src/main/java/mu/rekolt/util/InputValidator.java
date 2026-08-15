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
}
