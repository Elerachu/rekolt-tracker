package mu.rekolt.app;

import java.util.Scanner;

import mu.rekolt.service.GradingService;

import mu.rekolt.util.InputValidator;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] [] deliveries = {
                {"M-0032", "Mia Gray", "BNS", "780", "95", "17"},
                {"M-0082", "Tosin", "TEA", "1200", "89", "4"},
                {"M-0048", "Ubong", "BNS", "780", "72", "6"},
                {"M-0025", "Bantu", "MZE", "375", "22", "20"},
                {"M-0016", "Dapson", "POT", "150", "12", "11"},
                {"M-0054", "Wale", "BNS", "1500", "36", "9"},
                {"M-0103", "Becca", "TEA", "238", "42", "1"},
                {"M-0008", "Chiamaka", "POT", "4589", "61", "17"},
                {"M-0061", "Minata", "MZE", "643", "95", "4"},
                {"M-0041", "Lu Xie", "TEA", "38", "95", "20"},
                {"M-0023", "Kang", "BNS", "12", "95", "15"},
                {"M-0108", "Dongju", "MZE", "154", "95", "6"}
        };

        int score = InputValidator.readQualityScore(scanner);

        String memberName = InputValidator.readMemberName(scanner);
        System.out.println("Member name: " + memberName);

        double mass = InputValidator.readMass(scanner);
        System.out.println("Mass: " + mass);

        String grade = GradingService.gradeFor(score);
        System.out.println("Grade:" + grade);

        int week = InputValidator.readWeek(scanner);
        System.out.println("Week: " + week);

        String produceCode = InputValidator.readProduceCode(scanner);
        System.out.println("Produce code: " + produceCode);

        String memberId = InputValidator.readMemberIdentifier(scanner);
        System.out.println("Member ID: " + memberId);

        // Step 1: inputs
        double basePrice = 90;

        // Step 2: base value
        double baseValue = mass * basePrice;

        // Step 3: grade multiplier
        double gradeMultiplier = GradingService.multiplierFor(grade);
        double gradedValue = baseValue * gradeMultiplier;

        // Step 4: category multiplier
        double categoryMultiplier = 1.00;
        double categoryValue = gradedValue * categoryMultiplier;

        // Step 5: commission
        double commission;

        // Step 6: transport levy
        double transportLevy;

        // Net payable
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

        // Output
        System.out.println("Delivery D-1001 recorded. Grade; " + grade);
        System.out.printf("Base value %.0f x %.2f = %,.2f%n", mass, basePrice, baseValue);
        System.out.printf("Grade %s x %.2f = %,.2f%n", grade, gradeMultiplier, gradedValue);
        System.out.printf("Cereal x %.2f = %,.2f%n", categoryMultiplier, categoryValue);
        System.out.printf("Commission 5%% - %,.2f%n", commission);
        System.out.printf("Transport levy %.0f x 2.00 - %,.2f%n", mass, transportLevy);
        System.out.printf("NET PAYABLE = %,.2f MUR%n", netPayable);
    }

}