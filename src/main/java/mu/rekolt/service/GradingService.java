package mu.rekolt.service;

// This class handles the rules for turning a score into a grade, and a grade into a multiplier.
public class GradingService {

    // Uses if/else-if to check the exact boundaries (100-85 is A, 84-70 is B, 69-50 is C, below 50 is REJECT).
    public static String gradeFor (int score) {
        if (score >= 85) {
            return "A";
        } else if (score >= 70) {
            return "B";
        } else if (score >= 50) {
            return "C";
        } else {
            return "REJECT";
        }
    }

    // Uses a switch expression to return the exact multiplier for each grade.
    public static double multiplierFor (String grade) {
        return switch (grade) {
            case "A" -> 1.15;
            case "B" -> 1.00;
            case "C" -> 0.85;
            case "REJECT" -> 0.00; // Reject pays nothing
            // If an unknown grade is passed in, stop the program with an error.
            default -> throw new IllegalArgumentException("Unknown grade " + grade);
        };
    }


}
