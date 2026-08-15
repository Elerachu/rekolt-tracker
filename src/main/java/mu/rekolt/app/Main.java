package mu.rekolt.app;

import java.util.Scanner;

import mu.rekolt.service.PaymentService;

import mu.rekolt.service.PriceService;

import mu.rekolt.util.InputValidator;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[][] deliveries = {
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
        double mass = InputValidator.readMass(scanner);
        int week = InputValidator.readWeek(scanner);
        String produceCode = InputValidator.readProduceCode(scanner);
        String memberId = InputValidator.readMemberIdentifier(scanner);

        String[] interactiveDelivery = {memberId, memberName, produceCode, String.valueOf(mass), String.valueOf(score), String.valueOf(week)};
        PaymentService.processDelivery(interactiveDelivery);

        double[][] weeklyGrid = new double[21][4];

        double seasonTotal = 0;
        for (String[] delivery : deliveries) {
            seasonTotal += PaymentService.processDelivery(delivery);

            int deliveryWeek = Integer.parseInt(delivery[5]);
            String deliveryProduceCode = delivery[2];
            double deliveryMass = Double.parseDouble(delivery[3]);
            weeklyGrid[deliveryWeek][PriceService.columnFor(deliveryProduceCode)] += deliveryMass;
        }
        System.out.printf("Season total: %,.2f MUR%n", seasonTotal);

        System.out.println("Weekly volume grid (kg)");
        System.out.println("Week MZE BNS POT TEA Total");

        for (int deliveryWeek = 1; week <= 20; week++) {
            double rowTotal = 0;
            System.out.printf("%d ", week);
            for (int col = 0; col < 4; col++) {
                System.out.printf("%.1f ", weeklyGrid[week][col]);
                rowTotal += weeklyGrid[week][col];
            }
            System.out.printf("%.1f%n", rowTotal);
        }
    }
}