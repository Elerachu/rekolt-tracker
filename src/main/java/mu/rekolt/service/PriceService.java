package mu.rekolt.service;

// This class handles the fixed rules for produce prices, category multipliers, and grid columns.
public class PriceService {

    // Switch expression to return the exact base price per kg for each produce code.
    public static double basePriceFor(String produceCode) {
        return switch (produceCode) {
            case "MZE" -> 30;
            case "BNS" -> 90;
            case "POT" -> 45;
            case "TEA" -> 25;
            default -> throw new IllegalArgumentException("Unknown produce code " + produceCode);
        };
    }

    // Switch expression to return the category multiplier for each produce code.
    public static double categoryMultiplierFor(String produceCode) {
        return switch (produceCode) {
            case "MZE" -> 1.00; // Cereal
            case "BNS" -> 1.00; // Cereal
            case "POT" -> 0.90; // Perishable
            case "TEA" -> 1.10; // Cash Crop
            default -> throw new IllegalArgumentException("Unknown produce code " + produceCode);
        };
    }

    // Returns the column index (0 to 3) for the 2D weekly grid array.
    public static int columnFor(String produceCode) {
        return switch (produceCode) {
            case "MZE" -> 0;
            case "BNS" -> 1;
            case "POT" -> 2;
            case "TEA" -> 3;
            default -> throw new IllegalArgumentException("Unknown produce code " + produceCode);
        };
    }
}