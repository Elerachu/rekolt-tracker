package mu.rekolt.app;

// Imports: We import these specific collections because they solve specific problems in our logic.
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import mu.rekolt.service.GradingService;
import mu.rekolt.service.PaymentService;
import mu.rekolt.service.PriceService;
import mu.rekolt.util.InputValidator;
import mu.rekolt.model.Delivery;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // LOGIC: We use an ArrayList because it preserves the order deliveries were added and is fast to loop through.
        List<Delivery> deliveries = new ArrayList<>();

        // Hardcoded sample data for the season (since reading from files is out of scope for this assessment)
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

        // LOGIC: 2D array [week (1-20)][columns (0-3)]. Direct array index access is O(1), much faster than a Map here.
        double[][] weeklyGrid = new double[21][4]; // Index 0 is unused; size 21 so week 20 is valid.
        double seasonTotal = 0;

        // LOGIC: Map<String, Double> for O(1) lookup and accumulation of totals per member.
        HashMap<String, Double> memberTotals = new HashMap<>();

        // LOGIC: Map<String, List> to store multiple deliveries per member.
        HashMap<String, List<Delivery>> deliveriesByMember = new HashMap<>();

        // LOGIC: HashSet automatically prevents duplicate member IDs.
        HashSet<String> distinctMemberIds = new HashSet<>();

        // Process the season's hardcoded deliveries once at startup
        for (Delivery delivery : deliveries) {
            double netPayable = PaymentService.processDelivery(delivery);
            seasonTotal += netPayable; // Add to season total
            weeklyGrid[delivery.getWeek()][PriceService.columnFor(delivery.getProduceCode())] += delivery.getMass(); // Add mass to grid

            // Add to member totals (using getOrDefault to handle first-time members)
            double currentTotal = memberTotals.getOrDefault(delivery.getMemberId(), 0.0);
            memberTotals.put(delivery.getMemberId(), currentTotal + netPayable);

            // Add delivery to the member's list of deliveries
            if (!deliveriesByMember.containsKey(delivery.getMemberId())) {
                deliveriesByMember.put(delivery.getMemberId(), new ArrayList<Delivery>());
            }
            deliveriesByMember.get(delivery.getMemberId()).add(delivery);

            // Add to distinct members set
            distinctMemberIds.add(delivery.getMemberId());
        }

        System.out.println("REKOLT PRODUCE TRACKER - season 2026");

        boolean running = true;
        // Main menu loop
        while (running) {
            System.out.println();
            System.out.println("1. Record a delivery      3. Generate the season report");
            System.out.println("2. Season figures on screen   4. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            // Switch statement to handle menu selection
            switch (choice) {
                case "1":
                    // The order: ID -> Name -> Code -> Mass -> Score -> Week
                    String memberId = InputValidator.readMemberIdentifier(scanner);
                    String memberName = InputValidator.readMemberName(scanner);
                    String produceCode = InputValidator.readProduceCode(scanner);
                    double mass = InputValidator.readMass(scanner);
                    int score = InputValidator.readQualityScore(scanner);
                    int week = InputValidator.readWeek(scanner);

                    // Create the new delivery object and calculate net pay
                    Delivery newDelivery = new Delivery(memberId, memberName, produceCode, mass, score, week);
                    double netPayable = PaymentService.processDelivery(newDelivery);
                    seasonTotal += netPayable;
                    weeklyGrid[week][PriceService.columnFor(produceCode)] += mass;

                    // Update member totals and lists for the new delivery
                    double currentTotal = memberTotals.getOrDefault(memberId, 0.0);
                    memberTotals.put(memberId, currentTotal + netPayable);

                    if (!deliveriesByMember.containsKey(memberId)) {
                        deliveriesByMember.put(memberId, new ArrayList<Delivery>());
                    }
                    deliveriesByMember.get(memberId).add(newDelivery);
                    distinctMemberIds.add(memberId);
                    break;

                case "2":
                    // SORTING 1: Using Comparable (natural order by mass)
                    List<Delivery> sortedByMass = new ArrayList<>(deliveries);
                    Collections.sort(sortedByMass); // Uses compareTo in Delivery.java
                    System.out.println("Deliveries sorted by mass (Comparable):");
                    for (Delivery d : sortedByMass) {
                        System.out.println(d.getMemberId() + " " + d.getMass() + "kg");
                    }

                    // SORTING 2: Using Comparator (custom order: name, then mass)
                    List<Delivery> sortedByName = new ArrayList<>(deliveries);
                    sortedByName.sort(Comparator.comparing(Delivery::getMemberName).thenComparing(Delivery::getMass));
                    System.out.println("Deliveries sorted by member name, then mass (Comparator):");
                    for (Delivery d : sortedByName) {
                        System.out.println(d.getMemberName() + " " + d.getMass() + "kg");
                    }

                    // SEARCHING: Linear search for a member ID (found and not found cases)
                    Delivery found = PaymentService.searchByMemberId(deliveries, "M-0032");
                    if (found != null) {
                        System.out.println("Found: " + found.getMemberName());
                    } else {
                        System.out.println("Member not found.");
                    }

                    Delivery notFound = PaymentService.searchByMemberId(deliveries, "M-9999");
                    if (notFound != null) {
                        System.out.println("Found: " + notFound.getMemberName());
                    } else {
                        System.out.println("Member not found.");
                    }

                    // ITERATOR: Safe removal of REJECT deliveries from the list while looping
                    Iterator<Delivery> iterator = deliveries.iterator();
                    while (iterator.hasNext()) {
                        Delivery d = iterator.next();
                        String grade = GradingService.gradeFor(d.getQualityScore());
                        if (grade.equals("REJECT")) {
                            iterator.remove(); // Removes without throwing ConcurrentModificationException
                        }
                    }
                    System.out.println("REJECT deliveries removed.");

                    // PRINT GRID: Looping through the 2D array to print the volume table
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
                    // Placeholder for Objective 6
                    System.out.println("Report generation will be implemented in Objective 6.");
                    break;

                case "4":
                    // Exit the loop
                    System.out.println("Goodbye.");
                    running = false;
                    break;

                default:
                    // Catches any number other than 1-4
                    System.out.println("Please choose 1, 2, 3, or 4.");
            }
        }
    }
}