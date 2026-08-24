package mu.rekolt.app;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import mu.rekolt.service.PaymentService;
import mu.rekolt.service.PriceService;
import mu.rekolt.util.InputValidator;
import mu.rekolt.model.Delivery;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery("M-0032", "Mia Gray", "BNS", 780, 95, 17));
        deliveries.add(new Delivery("M-0082", "Tosin", "TEA", 1200, 89, 4));
        deliveries.add(new Delivery("M-0048", "Ubong", "BNS", 780, 72, 6));
        deliveries.add(new Delivery("M-0025", "Bantu", "MZE", 375, 22, 20));
        deliveries.add(new Delivery("M-0016", "Dapson", "POT", 150, 12, 11));
        deliveries.add(new Delivery("M-0054", "Wale", "BNS", 1500, 36, 9));
        deliveries.add(new Delivery("M-0103", "Becca", "TEA", 238, 42, 1));
        deliveries.add(new Delivery("M-0008", "Chiamaka", "POT", 4589, 61, 17));
        deliveries.add(new Delivery("M-0061", "Minata", "MZE", 643, 95, 4));
        deliveries.add(new Delivery("M-0041", "Lu Xie", "TEA", 38, 95, 20));
        deliveries.add(new Delivery("M-0023", "Kang", "BNS", 12, 95, 15));
        deliveries.add(new Delivery("M-0108", "Dongju", "MZE", 154, 95, 6));

        double[][] weeklyGrid = new double[21][4];
        double seasonTotal = 0;
        HashMap<String, Double> memberTotals = new HashMap<>();
        HashMap<String, List<Delivery>> deliveriesByMember = new HashMap<>();


        // Process the season's hardcoded deliveries once at startup
        for (Delivery delivery : deliveries) {
            double netPayable = PaymentService.processDelivery(delivery);
            seasonTotal += netPayable;
            weeklyGrid[delivery.getWeek()][PriceService.columnFor(delivery.getProduceCode())] += delivery.getMass();

            double currentTotal = memberTotals.getOrDefault(delivery.getMemberId(), 0.0);
            memberTotals.put(delivery.getMemberId(), currentTotal + netPayable);

            if (!deliveriesByMember.containsKey(delivery.getMemberId())) {
                deliveriesByMember.put(delivery.getMemberId(), new ArrayList<Delivery>());
            }
            deliveriesByMember.get(delivery.getMemberId()).add(delivery);
        }

        System.out.println("REKOLT PRODUCE TRACKER - season 2026");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Record a delivery      3. Generate the season report");
            System.out.println("2. Season figures on screen   4. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    int score = InputValidator.readQualityScore(scanner);
                    String memberName = InputValidator.readMemberName(scanner);
                    double mass = InputValidator.readMass(scanner);
                    int week = InputValidator.readWeek(scanner);
                    String produceCode = InputValidator.readProduceCode(scanner);
                    String memberId = InputValidator.readMemberIdentifier(scanner);

                    Delivery newDelivery = new Delivery(memberId, memberName, produceCode, mass, score, week);
                    double netPayable = PaymentService.processDelivery(newDelivery);
                    seasonTotal += netPayable;
                    weeklyGrid[week][PriceService.columnFor(produceCode)] += mass;

                    double currentTotal = memberTotals.getOrDefault(memberId, 0.0);
                    memberTotals.put(memberId, currentTotal + netPayable);

                    if (!deliveriesByMember.containsKey(memberId)) {
                        deliveriesByMember.put(memberId, new ArrayList<Delivery>());
                    }
                    deliveriesByMember.get(memberId).add(newDelivery);
                    break;

                case "2":
                    System.out.println("Weekly volume grid (kg)");
                    System.out.println("Week MZE BNS POT TEA Total");
                    for (int w = 1; w <= 20; w++) {
                        double rowTotal = 0;
                        System.out.printf("%d ", w);
                        for (int col = 0; col < 4; col++) {
                            System.out.printf("%.1f ", weeklyGrid[w][col]);
                            rowTotal += weeklyGrid[w][col];
                        }
                        System.out.printf("%.1f%n", rowTotal);
                    }
                    System.out.printf("Season total: %,.2f MUR%n", seasonTotal);
                    break;

                case "3":
                    System.out.println("Report generation will be implemented in Objective 6.");
                    break;

                case "4":
                    System.out.println("Goodbye.");
                    running = false;
                    break;

                default:
                    System.out.println("Please choose 1, 2, 3, or 4.");
            }
        }
    }
}