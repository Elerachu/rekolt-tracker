package mu.rekolt.model;

public class Delivery {
    private final String memberId;
    private final String memberName;
    private final String produceCode;
    private final double mass;
    private final int qualityScore;
    private final int week;

    public Delivery(String memberId, String memberName, String produceCode, double mass, int qualityScore, int week) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.produceCode = produceCode;
        this.mass = mass;
        this.qualityScore = qualityScore;
        this.week = week;
    }

    public String getMemberId() {
        return memberId;
    }

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
}