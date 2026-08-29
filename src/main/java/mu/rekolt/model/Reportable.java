package mu.rekolt.model;

// Same idea as Payable: a contract, this time for "this thing can describe
// itself as a short line of text for a report."
public interface Reportable {
    String reportSummary();
}