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