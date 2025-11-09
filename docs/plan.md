# Milestone One Plan — CS-320 (ContactService.java)

**Branch:** enhancement/m1-plan | **Before tag:** v-before-review  
**Single file in scope:** `Portfolio_Submission/ContactService.java`

## 1) Current State (file-level review)
- `ContactService` performs **orchestration**, **validation**, and **storage** (in-memory `Map<String, Contact>`) inside the same methods.
- Methods return `boolean`, which hides *why* an operation failed (e.g., invalid phone vs. duplicate ID).
- Validation rules are inline and repeated:
  - `firstName`/`lastName`: length ≤ **10**
  - `phone`: exactly **10** digits
  - `address`: length ≤ **30**
  - `contactId`: must be unique on create; must exist on update

## 2) Gaps Observed
- Mixed concerns reduce testability and clarity.
- Boolean returns lack error semantics (no code/reason).
- Validation logic is duplicated across methods.

## 3) Enhancement Plan (single file, no new classes)
- **A. Local error model (in-file)**
  - Add at the top of the file:
    ```java
    private static enum ErrorCode { INVALID_INPUT, NOT_FOUND, CONFLICT }
    private static final class Result<T> { final T value; final ErrorCode error;
      Result(T v) { this.value = v; this.error = null; }
      Result(ErrorCode e) { this.value = null; this.error = e; } }
    ```
  - Internal helpers return `Result<?>`; public methods can still return `boolean` for compatibility while internally capturing *why*.

- **B. Centralize validation as private helpers**
  - Add:
    ```java
    private static boolean validName(String s)    { return s != null && s.length() <= 10; }
    private static boolean validPhone(String s)   { return s != null && s.matches("\\d{10}"); }
    private static boolean validAddress(String s) { return s != null && s.length() <= 30; }
    ```
  - Use these in `addContact` and `updateContact` to remove duplicated checks.

- **C. Guarded update flow**
  - In `updateContact(...)`, early-return `Result<?>` with `INVALID_INPUT` if any provided field violates rules, and `NOT_FOUND` if the ID is absent. Keep the map storage unchanged.

- **D. Javadoc invariants (top of class)**
  - Add a short block documenting the limits above and the uniqueness semantics for `contactId`.

## 4) Outcomes Mapping (For CS-499)
- **CO3 (Design/Engineering):** clearer separation via private helpers and an internal result type while staying in one file.
- **CO2 (Algorithms/Reasoning):** explicit, deterministic invariants and error codes for each failure path.
- **CO4 (Assessment):** next milestone will add boundary/negative tests that target these helpers and confirm behavior.
- **CO5 (Policies):** centralized input validation and non-leaky error handling (no raw stack traces/messages).

## 5) Screencast Talking Points (M1)
1) Show the `v-before-review` tag and open `ContactService.java` to point out mixed concerns and boolean error returns.   
2) State the CO3/CO2/CO4/CO5 mapping.  

