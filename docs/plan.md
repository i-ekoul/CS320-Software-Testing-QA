# Enhancement Plan — CS-320 Contact Service (Milestone One)

**Branch:** `enhancement/m1-plan`
**Before tag:** `v-before-review`
**Objective:** Improve design quality, test rigor, and reliability without expanding feature scope.

This plan addresses four areas: architecture, error semantics, validation rules, and quality engineering (tests + CI). The end state is a cohesive service with centralized validation, a predictable error model, strong tests, and visible CI evidence.

---

## Target Architecture (Service–Repository–Validator)

We refactor to three components:

* **ContactService (orchestration only)**
  Receives requests, invokes validation, routes to storage, and returns a typed result.
* **ContactRepository (persistence abstraction)**
  CRUD + conflict checks (e.g., “exists by ID”). Retain in-memory or current approach; no new DB this milestone.
* **ContactValidator (centralized rules)**
  Enforces all input constraints once; returns field-level findings; removes scattered checks.

**Immutability** applies to identifiers and value objects. For example, `ContactId` and `PhoneNumber` are constructed once with invariant checks, preventing downstream state drift.

**Architecture sketch**

```text
+------------------+         +---------------------+
|  ContactService  |-------> |  ContactRepository  |  (storage abstraction)
|  (orchestration) |         +---------------------+
|                  |-------> +---------------------+
|                  |         |  ContactValidator   |  (centralized rules)
+------------------+         +---------------------+
         |
         v
      Result<T>
```

---

## Error Semantics via `Result<T>`

Replace ad-hoc exceptions for expected failures with a typed result.

```java
// Language-agnostic illustration using Java-style records and sealed interfaces.
sealed interface Result<T> permits Result.Ok, Result.Err {
  record Ok<T>(T value) implements Result<T> {}
  record Err<T>(ErrorCode code, String message, java.util.Map<String, String> fields)
      implements Result<T> {}
}

enum ErrorCode { INVALID_INPUT, NOT_FOUND, CONFLICT, INTERNAL }
```

**Guidelines**

* Use `Ok<T>` for success and `Err<T>` for expected failures (invalid input, not found, conflict).
* Do not leak stack traces or internal types in messages.
* Include a correlation ID in logs (not in return payloads) to trace operations.

---

## Centralized Validation Rules

### Name

* Trimmed and non-empty.
* Length in `[1, 30]`.
* Alphabetic plus spaces and hyphens (document exact charset).

### Phone

* Canonicalize to digits only.
* Exactly **10** digits (document locale assumption).
* Provide a helper to strip non-digits and validate.

### ID

* Generated once and immutable.
* Updates require an existing ID.

### Update semantics

* Reject invalid partial updates.
* Apply “reject-invalid-fast” guard clauses at the service boundary.

**Example validator contract**

```java
public interface ContactValidator {
  ValidationResult validateCreate(Contact candidate);    // exhaustive checks
  ValidationResult validateUpdate(Contact candidate);    // includes ID presence & immutability
}

public record ValidationResult(boolean valid, java.util.Map<String,String> fieldErrors) {}
```

---

## Testing Strategy

### Boundary & Negative (parameterized where sensible)

* Names at lengths `0, 1, 30, 31`; illegal characters; whitespace-only.
* Phones with non-digits, wrong length, and formatted variants (ensure canonicalization or rejection).
* Create vs. Update: duplicates, unknown IDs, missing IDs on update.

**Illustrative parameterized test**

```java
@ParameterizedTest
@ValueSource(strings = {"", " ", "A", "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE"}) // 31 chars
void nameBoundaries_rejected(String name) {
  var v = new ContactValidatorImpl();
  var result = v.validateCreate(new Contact(null, name, "123-456-7890"));
  assertFalse(result.valid());
  assertTrue(result.fieldErrors().containsKey("name"));
}
```

### Property-Based

* Generators for valid/invalid names and phones; validator accepts all valid and rejects all invalid classes.
* Idempotence: canonicalize a valid phone, re-validate → remains valid with identical canonical value.
* Stability: same invalid input → same `ErrorCode` and field map.
  *(Framework examples: jqwik / QuickTheories / ScalaCheck equivalents.)*

### Mutation Testing (assertion strength)

* Focus on `ContactValidator` and `ContactService` negative paths.
* Target mutation score: **≥ 75%** (record actual score in the next milestone).
* If runtime is heavy, scope to changed modules in PRs; run full on `main` nightly.

### Coverage & Evidence

* Aim **> 90%** line coverage for validator; ensure all error branches are hit.
* Publish HTML coverage and mutation reports as CI artifacts.

---

## Observability & CI

### Logging

* Per-operation **CorrelationId** (UUID).
* **Info:** on create/update/delete with non-sensitive identifiers.
* **Warn/Error:** on validation failures and conflicts with concise codes and correlation ID.
* Parse-friendly log lines (e.g., `op=update id=... correlationId=... code=INVALID_INPUT`).

### GitHub Actions (outline)

```yaml
name: ci
on: [push, pull_request]
jobs:
  build-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
      - name: Lint & Format
        run: ./gradlew spotlessCheck || ./mvnw -q -DskipTests=false verify
      - name: Unit Tests (incl. parameterized/property)
        run: ./gradlew test || ./mvnw -q test
      - name: Mutation Tests (module-scoped)
        run: ./gradlew pitest || echo "Configure PIT as needed"
      - name: Publish Reports
        uses: actions/upload-artifact@v4
        with:
          name: reports
          path: |
            **/build/reports/**
            **/target/pit-reports/**
            **/target/site/jacoco/**
```

**Artifacts**

* `coverage/` (HTML)
* `mutation-report/` (HTML)

**Optional Badges**

* Build, coverage, mutation (if supported).

---

## Verification Plan (what will prove improvement)

* **Design:** show new class boundaries and short excerpts of repository/validator interfaces.
* **Tests:** boundary/negative and property-based suites passing.
* **Mutation:** HTML report showing ≥ **75%** score (goal).
* **Logs:** sample validation failure with correlation ID and error code.
* **Before/After:** concise diff of `ContactService` illustrating orchestration-only vs. prior mixed responsibilities.

---

## Risks & Mitigations

* **Over-refactor regressions** → Small commits; continuous tests; preserve public API behavior.
* **Mutation runtime** → Scope to changed modules for PRs; full runs nightly.
* **Validator rules drifting back into service** → PR checklist: “All rules live in `ContactValidator`.”

---

## CS-499 Outcomes Mapping

| Outcome                        | Evidence in this Plan                                                         |
| ------------------------------ | ----------------------------------------------------------------------------- |
| **CO3 (Design/Engineering)**   | Service–Repository–Validator architecture; immutability; typed results        |
| **CO4 (Testing/Assessment)**   | Boundary/negative, parameterized, property-based, and mutation testing        |
| **CO2 (Algorithms/Reasoning)** | Explicit invariants and canonicalization rules; predictable result typing     |
| **CO5 (Security/Policies)**    | Strict validation, non-leaky errors, documented semantics, logging discipline |

---

## Screencast Outline (Milestone One)

1. Show `v-before-review` and summarize current structure.
2. Open this plan and explain target architecture and error model.
3. Walk the test matrix and mutation goal.
4. Show CI artifact plan and how evidence will be captured in the next milestone.
5. Point to this branch (`enhancement/m1-plan`) as the evidence location.
