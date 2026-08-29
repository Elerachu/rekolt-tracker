package mu.rekolt.service;

import mu.rekolt.model.Delivery;
import java.util.List;

public class PaymentService {
    // Simple linear search. Loops through the list and returns the first match.
    public static Delivery searchByMemberId(List<Delivery> deliveries, String memberId) {
        for (Delivery d : deliveries) {
            if (d.getMemberId().equals(memberId)) {
                return d;
            }
        }
        return null; // Returns null if not found
    }
}