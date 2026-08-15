package mu.rekolt.service;

public class GradingService {
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

    public static double multiplierFor (String grade) {
        return switch (grade) {
            case "A" -> 1.15;
            case "B" -> 1.00;
            case "C" -> 0.85;
            case "REJECT" -> 0.00;
            default -> throw new IllegalArgumentException("Unknown grade " + grade);
        };
    }


}
