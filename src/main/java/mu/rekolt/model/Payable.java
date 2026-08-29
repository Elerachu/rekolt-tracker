package mu.rekolt.model;

// Interface = a contract. Classes that "implement" it promise to provide these methods.
// Unlike "extends" (one parent class), a class can implement MULTIPLE interfaces.
public interface Payable {

    // No body here. Any class implementing this MUST have its own version of this method.
    // Returns the net amount of money owed.
    double netPayable();
}