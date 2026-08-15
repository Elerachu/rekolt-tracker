package mu.rekolt.service;

public class PriceService {
    public static double basePriceFor(String produceCode) {
        return switch (produceCode) {
            case "MZE" -> 30;
            case "BNS" -> 90;
            case "POT" -> 45;
            case "TEA" -> 25;
            default -> throw new IllegalArgumentException("Unknown produce code " + produceCode);
        };
    }

    public static double categoryMultiplierFor(String produceCode) {
        return switch (produceCode) {
            case "MZE" -> 1.00;
            case "BNS" -> 1.00;
            case "POT" -> 0.90;
            case "TEA" -> 1.10;
            default -> throw new IllegalArgumentException("Unknown produce code " + produceCode);
        };
    }
}