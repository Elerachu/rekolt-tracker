# REKOLT Produce Tracker

A Java console application for REKOLT Planters' Cooperative to record
produce deliveries and compute payments to members.

## Type and Rounding Decisions

- `mass` and `basePrice` are `double` because they are continuous,
  fractional quantities (e.g. 236.5 kg is valid).
- `qualityScore` and the week number are `int` because the spec defines
  them as whole numbers only (0-100, 1-20).
- All monetary intermediate values (baseValue, gradedValue, categoryValue,
  commission, transportLevy, netPayable) are kept as `double` through
  every calculation step, with full precision preserved.
- Rounding to 2 decimals happens only at display time, using `printf`'s
  `%.2f` format specifier. The underlying `double` values are never
  rounded or reassigned, so later calculations always use full precision.

## Implementation Logic and Problem-Solving Approach

### 1. Overall Architecture
I did not want to put all the code in `Main.java` because it would be too hard to read. 
I split the code into packages so that each part of the program has a specific job:
`app` (Main): Only handles the menu loop and printing to the console.
`util` (InputValidator): Handles user input and validation. 
This prevents `Main` from getting messy with loops.
`service` (Price, Grading, Payment): Handles the business rules (the math).
`model` (Delivery): The data object that holds the delivery details.

### 2. My Flow of Logic (How it runs)
When the program starts, it seeds hardcoded deliveries into an `ArrayList`. 
I chose an `ArrayList` because I just need to add items and loop through them in order.
I then iterate through this list once at startup to:
1. Calculate the pay for each existing delivery.
2. Add the mass to the 2D `weeklyGrid` array.
3. Update the `HashMap` for total pay per member.
4. Group deliveries by member ID using another `HashMap`.
5. Add unique member IDs to a `HashSet`.

### 3. Data Structures and Why I Chose Them
* **HashMap for Member Totals:** I needed to update a member's total every time they made a delivery. A Map gives me O(1) average lookup time (constant time), meaning it doesn't get slower as the list grows, which is much faster than scanning an ArrayList every time.
* **HashSet for Unique Members:** The program needs to know all distinct members. A `HashSet` automatically prevents duplicates without me having to write an `if` statement to check if a member ID already exists.
* **2D Array for Weekly Grid:** The weeks (1-20) and produce types (4) are fixed, small, and known numbers. An array allows direct `O(1)` indexing, which is faster than using a Map here.
* **Comparable vs Comparator:** I implemented `Comparable` on the `Delivery` class to define its natural order (by mass). Later in the menu, I used a separate `Comparator` to sort by name, then mass. This keeps `Delivery` simple and allows multiple ways to sort without changing the class.

### 4. The 5-Step Payment Calculation Logic
In `PaymentService`, the logic mirrors the requirement doc exactly:
1. Base Value = `mass * basePrice`.
2. Grade Multiplier (A=1.15, B=1.00, C=0.85).
3. Category Multiplier (Cereal=1.00, Perishable=0.90, Cash Crop=1.10).
4. REJECT Case Logic: I wrote an `if/else` statement. If the grade is "REJECT", the commission and transport levy are set to `0.0` and the total net payable is `0.0`, as per the rules. Otherwise, it calculates the 5% commission and the 2 MUR/kg levy.
5. I only round to 2 decimals when printing using `printf("%.2f")`, leaving the underlying `double` values fully unrounded for subsequent calculations.

### 5. Validation & Error Handling
To ensure the program never crashes with a stack trace (as required by the rubric), I used `while(true)` loops in `InputValidator`.
If a user types "abc" for mass, the `try-catch` catches the `NumberFormatException` and politely asks them to try again. If a user types "M-0042", I used a Regular Expression (`^M-\d{4}$`) to ensure it strictly matches the required format.

### 6. Menu Interaction
I used a `switch` statement inside a `while` loop to handle the menu. The `running` boolean variable controls the loop. If the user types "4", `running` becomes false, breaking the loop and exiting the program safely.

### 7. Correcting my Initial Mistake (Input Order)
During development, I initially wrote the menu so it asked for the quality score first. However, I re-read the sample run in the assignment specification and realized it required Member ID -> Name -> Produce Code -> Mass -> Score -> Week. I fixed my `Main.java` to match the specification exactly so the user experience matches the PDF.
