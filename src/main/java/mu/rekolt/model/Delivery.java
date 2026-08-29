package mu.rekolt.model;

// Delivery implements Comparable so we can sort deliveries naturally by mass
public class Delivery implements Comparable<Delivery> {
    // Private fields: encapsulation. These cannot be changed after creation (final).
    private final String memberId;
    private final String memberName;
    private final String produceCode;
    private final double mass;
    private final int qualityScore;
    private final int week;

    // Constructor: sets up a new delivery object when it is created.
    public Delivery(String memberId, String memberName, String produceCode, double mass, int qualityScore, int week) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.produceCode = produceCode;
        this.mass = mass;
        this.qualityScore = qualityScore;
        this.week = week;
    }

    // Getters: used by other classes to read the private fields.
    public String getMemberId() {
        return memberId;}
    public String getMemberName() {
        return memberName;
    }
    public String getProduceCode() {
        return produceCode;
    }
    public double getMass() {
        return mass;
    }
    public int getQualityScore() {
        return qualityScore;
    }
    public int getWeek() {
        return week;
    }

    // Overriding the compareTo method required by the Comparable interface.
    // This tells Java how to sort a list of Delivery objects (by mass, from smallest to largest).
    @Override
    public int compareTo(Delivery other) {
        return Double.compare(this.mass, other.mass);
    }
}