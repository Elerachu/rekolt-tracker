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
}
