package mu.rekolt.model;

// Enum: fixed list of grades, each carrying its own multiplier.
public enum Grade {

    // Constants call the constructor below with their specific multiplier.
    A(1.15),
    B(1.00),
    C(0.85),
    REJECT(0.00);

    // Field that each constant carries.
    private final double multiplier;

    // Constructor (auto-called by the constants above).
    Grade(double multiplier) {
        this.multiplier = multiplier;
    }

    // Getter for the multiplier.
    public double getMultiplier() {
        return multiplier;
    }

    // Static method: converts a score to a Grade.
    // Checks from the top down, so the highest eligible grade is returned.
    public static Grade fromScore(int score) {
        if (score >= 85) return A;
        if (score >= 70) return B;
        if (score >= 50) return C;
        return REJECT;
    }
}