# Collections Rationale

## ArrayList<Delivery> — season deliveries
**Access pattern:** sequential iteration (processing every delivery each season run) and append-only growth (new deliveries added via the menu).
**Ordering requirement:** insertion order matters — deliveries should stay in the order they were recorded.
**Lookup cost:** O(n) for a linear search by member ID, acceptable given a season realistically holds a few dozen entries, not thousands.
**Rejected alternative:** LinkedList. It offers faster insertion/removal at arbitrary positions, but this project only ever appends at the end and iterates from the front — ArrayList's contiguous storage gives faster iteration and indexed access for that exact pattern, with no real benefit from LinkedList's strengths.

## HashMap<String, Double> — total payment per member
**Access pattern:** repeated update-by-key (adding each new delivery's net payable to a running total).
**Ordering requirement:** none — the treasurer needs each member's total, not a particular order.
**Lookup cost:** O(1) average for get/put, critical since this happens once per delivery.
**Rejected alternative:** TreeMap. It would keep entries sorted by member ID automatically, but at O(log n) per operation instead of O(1) — a cost with no benefit here, since nothing in this project actually needs the totals in ID order.

## HashMap<String, List<Delivery>> — deliveries per member
**Access pattern:** same key-based update pattern as memberTotals, but appending to a list rather than accumulating a number.
**Ordering requirement:** none for the map itself; the inner list preserves each member's own delivery order.
**Lookup cost:** O(1) average to reach a member's list.
**Rejected alternative:** a single flat ArrayList<Delivery> filtered by member ID on demand. This would mean an O(n) scan every time the report needs one member's deliveries, repeated once per member — far more expensive than paying the grouping cost once during recording.

## HashSet<String> — distinct member identifiers
**Access pattern:** insert-only, called once per delivery.
**Ordering requirement:** none — only membership (which IDs exist) matters, not their order.
**Lookup cost:** O(1) average for add/contains, and duplicates are silently rejected with no extra logic needed.
**Rejected alternative:** an ArrayList with a manual "if not already contains, add" check. `ArrayList.contains()` is O(n), so checking uniqueness this way would cost O(n) per delivery instead of O(1) — a meaningful difference as the season grows.

## double[21][4] — weekly volume grid
**Access pattern:** direct indexed access by week number and produce column, both known, fixed, small ranges (1–20, 0–3).
**Ordering requirement:** not applicable — this is a lookup table by position, not a collection of items to order.
**Lookup cost:** O(1), the fastest possible, since array indexing is direct memory access.
**Rejected alternative:** HashMap<Integer, double[]>. This would add hashing overhead for no benefit, since the valid range of weeks is small, fixed, and known in advance — an array's direct indexing is strictly better here.

## Comparable<Delivery> vs Comparator<Delivery> — sorting
**Comparable** (implemented on `Delivery`, sorting by mass) defines the class's own natural/default order, used any time a plain `Collections.sort()` is called with no explicit rule.
**Comparator** (built separately in `Main`, sorting by member name then mass) provides an alternate, swappable order without modifying `Delivery` itself — useful when a different view of the same data is needed without redefining what "natural order" means for the class.
**Rejected alternative:** hardcoding a second sort field directly into `compareTo`. This was rejected because `Comparable` should express one unambiguous natural order per class; multiple independent sort views belong in separate `Comparator` objects instead, keeping `Delivery` itself simple and not tied to every possible sort a caller might want.