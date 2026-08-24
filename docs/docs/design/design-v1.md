## Scope Statement

The REKOLT Produce Tracker is a Java console application built for REKOLT Planters' Cooperative to record produce deliveries during a season, compute each member's payment automatically and consistently, and generate a single Word document season report summarising every member's earnings.

The system replaces a manual, paper-based process (hand-written slips and manual calculation) that was slow and error-prone, aiming to eliminate calculation errors, duplicate payments, and the eleven-day season-end reconciliation delay described in the brief.

**In scope:**
- Recording individual deliveries (member identifier, name, produce code, mass, quality score, week) with input validation
- Computing payment per delivery using the fixed five-step rule set (base value, grade multiplier, category multiplier, commission, transport levy)
- Aggregating totals per member and per week across the season
- Generating a single Word document report, one section per member, with a season-total reconciliation
- A console menu allowing repeated delivery recording, on-screen figures, report generation, and exit

**Out of scope:**
- Persisting data between program runs (no file reading or writing of delivery data, explicitly excluded by the assignment brief)
- Multi-user access or concurrent use
- Editing or deleting a delivery once recorded
- Any GUI. The interface is console-only

## Assumptions

1. Each delivery belongs to exactly one member, identified uniquely by their member identifier (format `M-####`).
2. A member may make more than one delivery in a season; their payments accumulate across all their deliveries.
3. The season's delivery data exists only in memory for the duration of one program run, per the assignment's explicit assumption for Objective 3.
4. Quality scores, produce codes, and masses are provided in good faith by the clerk at the point of entry. The system's role is to validate format and range, not to verify the physical accuracy of a weighing or grading decision.
5. The treasurer runs the report generation once at the end of the season, after all deliveries for that season have been recorded.

## Functional Requirements

**FR1.** The system shall validate a member identifier matches the pattern `M-####` (letter M, hyphen, four digits), re-prompting on invalid input.
**FR2.** The system shall validate a member name is non-empty and not whitespace-only.
**FR3.** The system shall validate a produce code is one of MZE, BNS, POT, or TEA (case-insensitive), rejecting any other value.
**FR4.** The system shall validate mass is a decimal number greater than 0 and not more than 5000 kg.
**FR5.** The system shall validate quality score is a whole number between 0 and 100 inclusive.
**FR6.** The system shall validate week of delivery is a whole number between 1 and 20 inclusive.
**FR7.** The system shall compute a delivery's grade from its quality score using fixed boundaries (85 to 100 = A, 70 to 84 = B, 50 to 69 = C, below 50 = REJECT).
**FR8.** The system shall compute net payable for each delivery using the five-step rule (base value, grade multiplier, category multiplier, commission, transport levy).
**FR9.** The system shall assign a REJECT delivery a net payable of zero, with no commission or transport levy deducted.
**FR10.** The system shall hold all delivery data in memory for the duration of one program run, without reading from or writing to a file.
**FR11.** The system shall accumulate each member's total payment across all their deliveries in the season.
**FR12.** The system shall track total volume delivered per week per produce type.
**FR13.** The system shall present a console menu with four options (record delivery, season figures, generate report, exit) that loops until exit is chosen.
**FR14.** The system shall generate a single Word document season report with one section per member, including their delivery details, commission, levy, and net payable, plus a season-total reconciliation.

## Non-Functional Requirements

**NFR1.** The system shall never terminate with an uncaught exception because of invalid user input.
**NFR2.** Money values shall display rounded to two decimal places, with no intermediate calculation value rounded.
**NFR3.** Source code shall be organised into the `app`, `model`, `service`, and `util` packages.
**NFR4.** The project shall build and run from a clean clone using a single documented Maven command.
**NFR5.** Each objective shall be developed on its own feature branch with atomic, descriptive commits, merged into `main` via `--no-ff`.

## Noun-Verb Analysis

**Nouns extracted from the requirements** (candidate classes): Delivery, Member, Produce, CerealProduce, PerishableProduce, CashCropProduce, Grade, Payment, Season, MenuOption, GradingService, PriceService, PaymentService, InputValidator, Main, Report.

**Verbs extracted** (candidate methods/behaviour): validate, compute, grade, record, accumulate, generate, search, sort, remove.

### Classes kept
- **Delivery** (model) — one recorded delivery's raw data
- **Member** (model) — a cooperative member, referenced by delivery records
- **Produce**, **CerealProduce**, **PerishableProduce**, **CashCropProduce** (model) — the abstract produce hierarchy and its valuation behaviour (FR8)
- **Grade** (model, enum) — the four grade bands and their multipliers (FR7)
- **GradingService**, **PriceService**, **PaymentService** (service) — grading, pricing, and payment orchestration logic (FR7, FR8, FR9)
- **InputValidator** (util) — all input validation (FR1 to FR6)
- **Main** (app) — entry point and console menu (FR13)

### Classes rejected, with reasons

1. **Payment** — rejected as a standalone class. A payment is fully determined by a `Delivery` plus the fixed rule constants; it has no identity or state of its own beyond a single computed number. Modelling it separately would duplicate data already on `Delivery` without adding behaviour, so it's kept as a `double` return value from `PaymentService` instead.

2. **Season** — rejected as a standalone class. The season's state is already fully represented by the collections held in `Main` (the delivery list and the aggregate maps). A `Season` wrapper class was considered, but at this scale it would only add a layer of indirection around existing collections without a clear behavioural benefit.

3. **MenuOption** — rejected as a class or enum. The four menu choices are fixed, mutually exclusive, and require no shared behaviour beyond dispatching to the right action, which a plain `switch` on user input already does cleanly. Introducing a type for this would be unnecessary complexity for four fixed options.