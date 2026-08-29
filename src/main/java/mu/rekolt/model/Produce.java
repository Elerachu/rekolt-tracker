package mu.rekolt.model;

// Abstract: cannot be created directly. Only its subclasses can.
public abstract class Produce {

    // Fields are "final" because they are set once and never change.
    private final String code;
    private final String name;
    private final double basePricePerKg;

    // "protected" lets only subclasses call this constructor.
    // "this" separates the fields from the parameter names.
    protected Produce(String code, String name, double basePricePerKg) {
        this.code = code;
        this.name = name;
        this.basePricePerKg = basePricePerKg;
    }

    // Getters (Encapsulation)
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getBasePricePerKg() {
        return basePricePerKg;
    }

    // Abstract method: no body here. Forces every subclass
    // to implement its own category multiplier (e.g. x1.00, x0.90, x1.10).
    public abstract double applyCategoryMultiplier(double gradedValue);
}