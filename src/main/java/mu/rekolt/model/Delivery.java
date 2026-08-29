package mu.rekolt.model;

// "implements" means this class promises to provide the methods in Payable and Reportable.
public class Delivery implements Comparable<Delivery>, Payable, Reportable {

    // Private final fields (Invariants): they cannot be changed after creation.
    private final String memberId;
    private final String memberName;
    private final Produce produce; // This is POLYMORPHISM! It can be Cereal, Perishable, or CashCrop.
    private final double mass;
    private final int qualityScore;
    private final int week;

    // Constructor sets up a new delivery object.
    public Delivery(String memberId, String memberName, Produce produce, double mass, int qualityScore, int week) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.produce = produce;
        this.mass = mass;
        this.qualityScore = qualityScore;
        this.week = week;
    }

    // Encapsulation: Getters to read the private fields.
    public String getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public Produce getProduce() { return produce; }
    public double getMass() { return mass; }
    public int getQualityScore() { return qualityScore; }
    public int getWeek() { return week; }

    // This method calculates the exact 5-step payment rules.
    @Override
    public double netPayable() {
        double baseValue = mass * produce.getBasePricePerKg(); // Step 1
        Grade grade = Grade.fromScore(qualityScore); // Find the grade
        double gradedValue = baseValue * grade.getMultiplier(); // Step 2
        double categorizedValue = produce.applyCategoryMultiplier(gradedValue); // Step 3 (Polymorphism!)

        // Step 4: If the grade is REJECT, the value is exactly 0.
        if (grade == Grade.REJECT) {
            return 0.0;
        }

        // Step 5: Deduct 5% commission and 2 MUR/kg levy.
        double commission = categorizedValue * 0.05;
        double transportLevy = mass * 2.00;
        return categorizedValue - commission - transportLevy;
    }

    // Implemented for the Word report (Objective 6).
    @Override
    public String reportSummary() {
        return memberId + " | " + memberName + " | " + produce.getCode() + " | Net: " + netPayable();
    }

    // Implemented for sorting by mass (Objective 3).
    @Override
    public int compareTo(Delivery other) {
        if (this.mass < other.mass) return -1;
        if (this.mass > other.mass) return 1;
        return 0;
    }
}