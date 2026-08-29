package mu.rekolt.app;

// Imports:I imported these specific collections because they solve specific problems in my logic.
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import mu.rekolt.model.*; // Imports all the new OOP classes
import mu.rekolt.service.PaymentService;
import mu.rekolt.service.ReportGenerator; // Imports the new report generator
import mu.rekolt.util.InputValidator;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // App title and summary, printed first so the user knows what they're looking at.
        System.out.println("REKOLT PRODUCE TRACKER - season 2026");
        System.out.println("Tracks produce deliveries from cooperative members and computes payments automatically.");
        System.out.println("Each delivery records: member ID, member name, produce type, mass (kg), quality score, and week.");

        // LOGIC: We use an ArrayList because it preserves the order deliveries were added and is fast to loop through.
        List<Delivery> deliveries = new ArrayList<>();

        // I hardcoded the sample data for the season (using the new Produce subclasses for Objective 5)
        deliveries.add(new Delivery("M-0032", "Mia Gray", new CerealProduce("BNS", "Beans", 90.0), 780, 95, 17));
        deliveries.add(new Delivery("M-0082", "Tosin", new CashCropProduce("TEA", "Green Tea", 25.0), 1200, 89, 4));
        deliveries.add(new Delivery("M-0048", "Ubong", new CerealProduce("BNS", "Beans", 90.0), 780, 72, 6));
        deliveries.add(new Delivery("M-0025", "Bantu", new CerealProduce("MZE", "Maize", 30.0), 375, 22, 20));
        deliveries.add(new Delivery("M-0016", "Dapson", new PerishableProduce("POT", "Potatoes", 45.0), 150, 12, 11));
        deliveries.add(new Delivery("M-0054", "Wale", new CerealProduce("BNS", "Beans", 90.0), 1500, 36, 9));
        deliveries.add(new Delivery("M-0103", "Becca", new CashCropProduce("TEA", "Green Tea", 25.0), 238, 42, 1));
        deliveries.add(new Delivery("M-0008", "Chiamaka", new PerishableProduce("POT", "Potatoes", 45.0), 4589, 61, 17));
        deliveries.add(new Delivery("M-0061", "Minata", new CerealProduce("MZE", "Maize", 30.0), 643, 95, 4));
        deliveries.add(new Delivery("M-0041", "Lu Xie", new CashCropProduce("TEA", "Green Tea", 25.0), 38, 95, 20));
        deliveries.add(new Delivery("M-0023", "Kang", new CerealProduce("BNS", "Beans", 90.0), 12, 95, 15));
        deliveries.add(new Delivery("M-0108", "Dongju", new CerealProduce("MZE", "Maize", 30.0), 154, 95, 6));

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
        System.out.println("\nLoading season deliveries...");
        // Headers with neat spaces
        System.out.printf("%-12s %-10s %-10s %-8s %-12s%n", "Member ID", "Name", "Produce", "Grade", "Net Payable");
        // Underline matching the header widths
        System.out.println("------------+----------+----------+--------+------------");

        for (Delivery delivery : deliveries) {
            // The Delivery class now calculates its own net payable (Objective 5)
            double netPayable = delivery.netPayable();
            seasonTotal += netPayable; // Add to season total
            weeklyGrid[delivery.getWeek()][columnFor(delivery.getProduce().getCode())] += delivery.getMass(); // Add mass to grid

            // TO PRINT THE DATA
            System.out.printf("%-12s %-10s %-10s %-8s %-12.2f%n",
                    delivery.getMemberId(),
                    delivery.getMemberName(),
                    delivery.getProduce().getCode(),
                    Grade.fromScore(delivery.getQualityScore()),
                    netPayable);

            // Add to member totals using getOrDefault to handle first-time members
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

        boolean running = true;
        // Main menu loop
        while (running) {
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println("1. Record a delivery");
            System.out.println("2. Season figures on screen");
            System.out.println("3. Generate the season report");
            System.out.println("4. Exit");
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

                    // Simple if/else to choose the correct Produce subclass based on the code
                    Produce produce;
                    if (produceCode.equals("MZE")) {
                        produce = new CerealProduce("MZE", "Maize", 30.0);
                    } else if (produceCode.equals("BNS")) {
                        produce = new CerealProduce("BNS", "Beans", 90.0);
                    } else if (produceCode.equals("POT")) {
                        produce = new PerishableProduce("POT", "Potatoes", 45.0);
                    } else {
                        produce = new CashCropProduce("TEA", "Green Tea", 25.0);
                    }

                    // Create the new delivery object and calculate net pay
                    Delivery newDelivery = new Delivery(memberId, memberName, produce, mass, score, week);
                    double netPayable = newDelivery.netPayable();
                    seasonTotal += netPayable;
                    weeklyGrid[week][columnFor(produceCode)] += mass;

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
                    System.out.println("\n== Deliveries sorted by mass (Comparable) ==");
                    // NEW HEADER AND UNDERLINE
                    System.out.printf("%-12s %-10s %-10s%n", "Member ID", "Name", "Mass (kg)");
                    System.out.println("------------+----------+----------");

                    List<Delivery> sortedByMass = new ArrayList<>(deliveries);
                    Collections.sort(sortedByMass); // Uses compareTo in Delivery.java
                    for (Delivery d : sortedByMass) {
                        System.out.printf("%-12s %-10s %-10.1f%n", d.getMemberId(), d.getMemberName(), d.getMass());
                    }

                    // SORTING 2: Using Comparator (custom order: name, then mass)
                    System.out.println("\n== Deliveries sorted by member name, then mass (Comparator) ==");
                    // NEW HEADER AND UNDERLINE
                    System.out.printf("%-12s %-10s %-10s%n", "Member ID", "Name", "Mass (kg)");
                    System.out.println("------------+----------+----------");

                    List<Delivery> sortedByName = new ArrayList<>(deliveries);
                    sortedByName.sort(Comparator.comparing(Delivery::getMemberName).thenComparing(Delivery::getMass));
                    for (Delivery d : sortedByName) {
                        System.out.printf("%-12s %-10s %-10.1f%n", d.getMemberId(), d.getMemberName(), d.getMass());
                    }

                    // SEARCHING: Linear search for a member ID (found and not found cases)
                    System.out.println("\n== Search results ==");
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
                        // Using the Grade enum instead of GradingService (Objective 5)
                        if (Grade.fromScore(d.getQualityScore()) == Grade.REJECT) {
                            iterator.remove(); // Removes without throwing ConcurrentModificationException
                        }
                    }
                    System.out.println("\nREJECT deliveries removed.");

                    // PRINT GRID: Using printf to make neat, aligned columns
                    System.out.println("\n== Weekly Volume Grid (kg) ==");
                    // "%-6s" means left-aligned in 6 spaces for a String. "%-8.1f" means left-aligned in 8 spaces with 1 decimal.
                    System.out.printf("%-6s %-8s %-8s %-8s %-8s %-8s%n", "Week", "MZE", "BNS", "POT", "TEA", "Total");
                    System.out.println("------+--------+--------+--------+--------+--------");

                    for (int w = 1; w <= 20; w++) {
                        double rowTotal = 0;
                        System.out.printf("%-6d ", w); // Print week number
                        for (int col = 0; col < 4; col++) {
                            System.out.printf("%-8.1f ", weeklyGrid[w][col]); // Print each column
                            rowTotal += weeklyGrid[w][col];
                        }
                        System.out.printf("%-8.1f%n", rowTotal); // Print the row total
                    }
                    System.out.printf("%nSeason total: %,.2f MUR%n", seasonTotal);
                    break;

                case "3":
                    // Generate the Word document (Objective 6)
                    ReportGenerator.generateReport(deliveries);
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

    // Simple helper method to get the column index for the weekly grid
    public static int columnFor(String code) {
        if (code.equals("MZE")) return 0;
        if (code.equals("BNS")) return 1;
        if (code.equals("POT")) return 2;
        return 3; // TEA
    }
}