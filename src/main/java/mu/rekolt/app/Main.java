package mu.rekolt.app;

import java.util.Scanner;

import mu.rekolt.service.GradingService;

import mu.rekolt.util.InputValidator;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int score = InputValidator.readQualityScore(scanner);

        String memberName = InputValidator.readMemberName(scanner);
        System.out.println("Member name: " + memberName);

        double mass = InputValidator.readMass(scanner);
        System.out.println("Mass: " + mass);

        String grade = GradingService.gradeFor(score);
        System.out.println("Grade:" + grade);

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