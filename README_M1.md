# CS-499 Milestone 1 — Code Review (CS-320)

**Artifact:** `Portfolio_Submission/ContactService.java`  
**Scope:** Single file only (no new classes/files)  
**Baseline tag:** `v-before-review`  
**Plan branch:** `enhancement/m1-plan`

---

## Quick links
- Before tag: https://github.com/i-ekoul/CS320-Software-Testing-QA/releases/tag/v-before-review
- Enhancement branch: https://github.com/i-ekoul/CS320-Software-Testing-QA/tree/enhancement/m1-plan
- Plan (docs/plan.md): https://github.com/i-ekoul/CS320-Software-Testing-QA/blob/enhancement/m1-plan/docs/plan.md
- Checklist (docs/checklist.md): https://github.com/i-ekoul/CS320-Software-Testing-QA/blob/enhancement/m1-plan/docs/checklist.md

---

## What this video will show
1. Open the **before tag** and show `ContactService.java` as the single artifact.
2. Point out current issues briefly:
   - Validation, storage, and orchestration are mixed in methods.
   - Boolean returns hide *why* an operation failed.
   - Inline, duplicated rules (name ≤ 10, phone = 10 digits, address ≤ 30).
3. Open `docs/plan.md` and summarize the enhancement:
   - Add a tiny in-file `Result` + `ErrorCode` for internal clarity.
   - Centralize validation into private helpers (`validName`, `validPhone`, `validAddress`).
   - Guarded update flow (reject invalid or missing ID early).
   - Add concise Javadoc listing rules and ID semantics.
4. Open `docs/checklist.md` and note what will be verified during implementation in the next milestone (boundary and negative tests).
5. Close by restating the single-file scope and outcomes mapping (below).

---

## Outcomes mapping (how this plan satisfies CS-499)
- **CO3 (Design/Engineering):** private validators and an internal result pattern clarify responsibilities within one file.
- **CO2 (Algorithms/Reasoning):** explicit, deterministic invariants (lengths, digits) and clear failure codes.
- **CO4 (Assessment):** boundary/negative tests planned next to verify rule enforcement and error paths.
- **CO5 (Policies):** centralized validation; non-leaky, user-safe error semantics.

---

## Submission checklist (for me)
- [ ] `v-before-review` exists and opens to `ContactService.java`.
- [ ] Branch `enhancement/m1-plan` exists with `docs/plan.md` and `docs/checklist.md`.
- [ ] This `README_M1.md` is committed on the `enhancement/m1-plan` branch.
- [ ] MP4 screencast recorded following the “What this video will show” steps.
