package mu.rekolt.service;

import mu.rekolt.model.Produce;
import mu.rekolt.model.CerealProduce;
import mu.rekolt.model.PerishableProduce;
import mu.rekolt.model.CashCropProduce;
import java.util.List;
import java.util.ArrayList;

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

    // Catalog of real Produce objects, built once when the class loads.
    private static final List<Produce> PRODUCE_CATALOG = buildCatalog();

    private static List<Produce> buildCatalog() {
        List<Produce> catalog = new ArrayList<>();
        catalog.add(new CerealProduce("MZE", "Maize", 30));
        catalog.add(new CerealProduce("BNS", "Beans", 90));
        catalog.add(new PerishableProduce("POT", "Potatoes", 45));
        catalog.add(new CashCropProduce("TEA", "Green tea leaf", 25));
        return catalog;
    }

    // Polymorphic lookup: loops the catalog and calls the abstract method,
    // letting each object's own subclass logic run — no instanceof, no casting.
    public static double categoryMultiplierPolymorphic(String produceCode, double gradedValue) {
        for (Produce p : PRODUCE_CATALOG) {
            if (p.getCode().equals(produceCode)) {
                return p.applyCategoryMultiplier(gradedValue);
            }
        }
        throw new IllegalArgumentException("Unknown produce code " + produceCode);
    }
}