package mu.rekolt.service;
import mu.rekolt.model.Delivery;

public class PaymentService {
    public static double processDelivery(String [] row) {
        String memberId =  row[0];
        String memberName =  row[1];
        String produceCode = row[2];
        double mass = Double.parseDouble(row[3]);
        int qualityScore = Integer.parseInt(row[4]);
        int week = Integer.parseInt(row[5]);

        String grade = GradingService.gradeFor(qualityScore);
        double basePrice = PriceService.basePriceFor(produceCode);
        double gradeMultiplier = GradingService.multiplierFor(grade);
        double categoryMultiplier = PriceService.categoryMultiplierFor(produceCode);

        double baseValue = mass * basePrice;
        double gradedValue = baseValue * gradeMultiplier;
        double categoryValue = gradedValue * categoryMultiplier;

        double commission;
        double transportLevy;
        double netPayable;

        if (grade.equals("REJECT")) {
            commission = 0.0;
            transportLevy = 0.0;
            netPayable = 0.0;
        } else {
            commission = categoryValue * 0.05;
            transportLevy = mass * 2;
            netPayable = categoryValue - commission - transportLevy;
        }
        System.out.printf("%s | %s | %s | Grade %s | %,.2f MUR%n", memberId, memberName, produceCode, grade, netPayable);

        return netPayable;

    }

    public static double processDelivery(String memberId, String memberName, String produceCode, double mass, int qualityScore, int week) {
        String[] row = {memberId, memberName, produceCode, String.valueOf(mass), String.valueOf(qualityScore), String.valueOf(week)};
        return processDelivery(row);
    }

    public static double processDelivery(Delivery delivery) {
        return processDelivery(delivery.getMemberId(), delivery.getMemberName(), delivery.getProduceCode(), delivery.getMass(), delivery.getQualityScore(), delivery.getWeek());
    }

}
