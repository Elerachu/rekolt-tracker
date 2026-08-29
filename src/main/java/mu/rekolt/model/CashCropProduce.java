package mu.rekolt.model;

public class CashCropProduce extends Produce {

    public CashCropProduce(String code, String name, double basePricePerKg) {
        super(code, name, basePricePerKg);
    }

    @Override
    public double applyCategoryMultiplier(double gradedValue) {
        // Cash crop category multiplier: x1.10
        return gradedValue * 1.10;
    }
}