# Code Review Checklist — Snapshot (CS-320 Contact Service)

> This snapshot is organized to mirror the CS-499 code review categories and to make rubric alignment explicit. Items marked **Findings (before)** reflect the v-before-review state.

---

## 1) Structure & Architecture
- [ ] Clear separation of concerns among **Service**, **Repository**, and **Validator** layers
- [ ] Narrow interfaces with single responsibility
- [ ] No circular dependencies; one-way dependency flow (Service → Repository, Service → Validator)
- [ ] Mutability minimized (identifiers and value objects are immutable)

**Findings (before):**
- Service class mixes orchestration, validation, and storage.
- Error signaling mixes exceptions with boolean/nullable returns.
- Identifiers are plain strings and mutable in call chains.

---

## 2) Correctness & Defensive Programming
- [ ] Guard clauses for null/empty, length bounds, allowed character sets
- [ ] Validation centralized and consistently applied across create/update paths
- [ ] Invariants stated near code (e.g., “phone must be 10 digits”)
- [ ] Partial updates are atomic or explicitly prohibited
- [ ] Duplicate ID or key conflicts handled predictably

**Findings (before):**
- Boundary checks not uniform between `create` and `update`.
- Invariants implied in tests but not documented near code.
- Duplicate handling inconsistent across methods.

---

## 3) Security & Data Handling
- [ ] Inputs are strictly validated and normalized (e.g., trim, canonicalize phone)
- [ ] No sensitive data appears in logs or exception messages
- [ ] Errors do not leak internal class/stack details

**Findings (before):**
- Validation sprinkled in multiple methods; some normalizations missing.
- Some error strings echo raw inputs (could be constrained).

---

## 4) Testing Depth & Quality
- [ ] Boundary tests cover min/max lengths, null/empty, invalid formats
- [ ] Negative-path tests cover every error branch
- [ ] Parameterized tests used for input matrices
- [ ] Property-based tests enforce invariants (e.g., generator for valid/invalid names)
- [ ] Mutation testing used to assess assertion strength; target score defined

**Findings (before):**
- Tests emphasize happy paths; fewer negative and boundary cases.
- No property-based or mutation testing present.

---

## 5) Naming, Docs, Readability
- [ ] Descriptive method/variable names (no magic numbers)
- [ ] Public methods have docstrings (preconditions/postconditions)
- [ ] Consistent formatting; no dead/commented-out code blocks
- [ ] Validation/error semantics described in README

**Findings (before):**
- Minor magic constants in validation; sparse docstrings for public methods.
- README does not document error semantics.

---

## 6) Performance & Observability (right-sized)
- [ ] Logging at service boundaries (info) and failures (warn/error)
- [ ] Correlation/operation IDs included in logs
- [ ] Time/cost of hot paths measured if relevant (e.g., batch create/update)

**Findings (before):**
- Logs present but inconsistent; no correlation IDs.

---

## 7) Tooling, Build, CI
- [ ] Lint/format enforced (pre-commit or CI)
- [ ] Unit + property + mutation tests run in CI
- [ ] CI publishes HTML artifacts (coverage, mutation report)
- [ ] Minimal badge(s) in README (status, coverage/mutation if available)

**Findings (before):**
- Unit tests run in CI; mutation/property-based not configured; no artifacts published.

---

## Rubric Alignment (at-a-glance)
- Structure/Design → **CO3**
- Testing/Quality → **CO4**
- Reasoning/Algorithms (validation as properties, result typing) → **CO2**
- Security/Docs/Policies (validation rigor, non-leaky errors) → **CO5**
