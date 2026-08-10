package mu.rekolt.app;

public class Main {
    public static void main(String[] args) {
        // Step 1: inputs
        double mass = 236.0;
        int qualityScore = 91;
        double basePrice = 90;

        // Step 2: base value
        double baseValue = mass * basePrice;

        // Step 3: grade multiplier
        double gradeMultiplier = 1.15;
        double gradedValue = baseValue * gradeMultiplier;

        // Step 4: category multiplier
        double categoryMultiplier = 1.00;
        double categoryValue = gradedValue * categoryMultiplier;

        // Step 5: commission
        double commission = categoryValue * 0.05;

        // Step 6: transport levy
        double transportLevy = mass * 2;

        // Net payable
        double netPayable = categoryValue - commission - transportLevy;

        // Output
        System.out.println("Delivery D-1001 recorded. Grade A");
        System.out.printf("Base value %.0f x %.2f = %,.2f%n", mass, basePrice, baseValue);
        System.out.printf("Grade A x %.2f = %,.2f%n", gradeMultiplier, gradedValue);
        System.out.printf("Cereal x %.2f = %,.2f%n", categoryMultiplier, categoryValue);
        System.out.printf("Commission 5%% - %,.2f%n", commission);
        System.out.printf("Transport levy %.0f x 2.00 - %,.2f%n", mass, transportLevy);
        System.out.printf("NET PAYABLE = %,.2f MUR%n", netPayable);
    }
}