package mu.rekolt.model;

public class PerishableProduce extends Produce {

    public PerishableProduce(String code, String name, double basePricePerKg) {
        super(code, name, basePricePerKg);
    }

    @Override
    public double applyCategoryMultiplier(double gradedValue) {
        // Perishable category multiplier: x0.90
        return gradedValue * 0.90;
    }
}