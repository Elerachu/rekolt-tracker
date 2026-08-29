package mu.rekolt.service;

import mu.rekolt.model.Delivery;
import java.util.List;

// This class handles the 5-step payment calculation logic.
public class PaymentService {

    // Overload 1: Takes a String array (for reading from a CSV/File if needed later)
    public static double processDelivery(String [] row) {
        // Extract the data from the array
        String memberId =  row[0];
        String memberName =  row[1];
        String produceCode = row[2];
        double mass = Double.parseDouble(row[3]);
        int qualityScore = Integer.parseInt(row[4]);
        int week = Integer.parseInt(row[5]);

        // Step 1-3: Get the base rules
        String grade = GradingService.gradeFor(qualityScore);
        double basePrice = PriceService.basePriceFor(produceCode);
        double gradeMultiplier = GradingService.multiplierFor(grade);
        double categoryMultiplier = PriceService.categoryMultiplierFor(produceCode);

        // Step 1: Calculate Base Value
        double baseValue = mass * basePrice;
        // Step 2: Apply Grade Multiplier
        double gradedValue = baseValue * gradeMultiplier;
        // Step 3: Apply Category Multiplier
        double categoryValue = gradedValue * categoryMultiplier;

        // Step 4 & 5: Calculate commission, levy, and net payable
        double commission;
        double transportLevy;
        double netPayable;

        // LOGIC: If the grade is REJECT, value is 0 and no deductions are taken.
        if (grade.equals("REJECT")) {
            commission = 0.0;
            transportLevy = 0.0;
            netPayable = 0.0;
        } else {
            // Otherwise, take 5% commission and 2 MUR per kg levy
            commission = categoryValue * 0.05;
            transportLevy = mass * 2;
            netPayable = categoryValue - commission - transportLevy;
        }
        // Print to screen for the user
        System.out.printf("%s | %s | %s | Grade %s | %,.2f MUR%n", memberId, memberName, produceCode, grade, netPayable);

        return netPayable;
    }

    // Overload 2: Takes separate values and creates a row array for the first method.
    public static double processDelivery(String memberId, String memberName, String produceCode, double mass, int qualityScore, int week) {
        String[] row = {memberId, memberName, produceCode, String.valueOf(mass), String.valueOf(qualityScore), String.valueOf(week)};
        return processDelivery(row);
    }

    // Overload 3: Takes a Delivery object and calls overload 2.
    public static double processDelivery(Delivery delivery) {
        return processDelivery(delivery.getMemberId(), delivery.getMemberName(), delivery.getProduceCode(), delivery.getMass(), delivery.getQualityScore(), delivery.getWeek());
    }

    // LOGIC: Search through the list to find a delivery by member ID. Returns null if not found.
    public static Delivery searchByMemberId(List<Delivery> deliveries, String memberId) {
        for (Delivery delivery : deliveries) {
            if (delivery.getMemberId().equals(memberId)) {
                return delivery;
            }
        }
        return null;
    }
}