# Milestone One Plan — CS-320 Contact Service

**Branch:** `enhancement/m1-plan` | **Before tag:** `v-before-review`

## 1) Current State (summary)
- Single `ContactService` class handles orchestration **and** validation **and** storage (in-memory map).
- Error signaling uses booleans/conditionals (unclear reasons for failure).
- Validation rules are scattered in methods:
  - first/last name ≤ 10 chars, phone = 10 digits, address ≤ 30, ID unique on create.

## 2) Gaps Observed
- Mixed responsibilities → harder to test and reason about.
- Booleans hide failure causes (no error codes).
- Rules duplicated/inconsistent across create/update.

## 3) Enhancement Plan (focused)
- **Separation:** introduce `ContactValidator` (all rules in one place) and `ContactRepository` (map stays, but behind an interface). `ContactService` orchestrates only.
- **Error model:** replace booleans with a tiny `Result` type: `Ok<T>` or `Err(code, message, fieldErrors)`.
- **Rule sheet:** keep current limits verbatim (first/last ≤ 10, phone = 10 digits, address ≤ 30, ID uniqueness).
- **Tests (next milestone):** add boundary/negative tests for those limits; keep scope small.

## 4) Outcomes Mapping
- **CO3 (Design):** separation of concerns; simpler service.
- **CO2 (Reasoning):** explicit invariants; deterministic error codes.
- **CO4 (Assessment):** boundary/negative tests planned; clearer pass/fail.
- **CO5 (Policies):** centralized validation; non-leaky error messages.
