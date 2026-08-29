package mu.rekolt.model;

// Extends Produce: inherits code, name, and base price.
public class CerealProduce extends Produce {

    // Constructor passes values straight to the parent (super) constructor.
    public CerealProduce(String code, String name, double basePricePerKg) {
        super(code, name, basePricePerKg);
    }

    // @Override ensures this method matches the abstract method exactly.
    @Override
    public double applyCategoryMultiplier(double gradedValue) {
        // Cereal multiplier from the payment rules: x1.00
        return gradedValue * 1.00;
    }
}