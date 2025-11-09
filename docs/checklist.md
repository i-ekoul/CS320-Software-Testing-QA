# Code Review Checklist (CS-320, ContactService.java)

> Scope: **file** — `Portfolio_Submission/ContactService.java`  
> Baseline: `v-before-review` tag

---

## A) Structure
- [ ] Validation in **private helpers** (not inlined in add/update)
- [ ] Map storage operations clearly separated from validation/orchestration
- [ ] No hidden state; inputs come via method params

**Before (notes):** Validation, storage, and flow mixed inside methods.

---

## B) Rules
- [ ] `firstName`, `lastName`: non-null, length ≤ **10**
- [ ] `phone`: non-null, exactly **10** digits (`\d{10}`)
- [ ] `address`: non-null, length ≤ **30**

**Before (notes):** Rules present but inlined/duplicated.

---

## C) ID Semantics
- [ ] **Add**: `contactId` required & **unique**
- [ ] **Update**: `contactId` must **exist**; reject if missing

**Before (notes):** Checks exist but reasons not explicit.

---

## D) Error Signaling
- [ ] Internal helpers use tiny `Result` + `ErrorCode` (in-file)
- [ ] Public methods may still return `boolean`, but reason is captured internally
- [ ] No raw stack traces in normal error paths

**Before (notes):** Boolean-only returns obscure *why* (invalid/not found/conflict).

---

## E) Minimal Tests to Add
- [ ] Boundaries: name 10/11, address 30/31, phone 10/9/11 digits
- [ ] Negatives: null/empty fields, non-digit phone, duplicate ID on add, unknown ID on update
- [ ] Positives: valid add, valid update

---

## F) Documentation
- [ ] Javadoc at class top: rule sheet + ID uniqueness/update requirements
- [ ] One-line comments: where validation is called; what each helper checks

---

## Rubric Mapping (For CS-499)
- **CO3** design (helpers & separation) · **CO2** reasoning (explicit invariants) · **CO4** assessment (planned boundary/negative tests) · **CO5** policies (safe, centralized validation)
