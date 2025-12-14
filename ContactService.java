import java.util.HashMap;
import java.util.Map;

public class ContactService {

    /**
     * Service layer for managing Contact records in memory.
     *
     * Class Invariants:
     * - ID uniqueness: Each contactId must be unique within the service
     * - Name length: firstName and lastName must have length <= 10
     * - Phone format: phone must be exactly 10 digits (numeric only)
     * - Address length: address must have length <= 30
     *
     * Note: Public methods return boolean for compatibility. Internal helpers return
     * a small Result with an ErrorCode to capture why an operation failed.
     */
    private Map<String, Contact> contacts;

    // A) Local error model (in-file)
    private static enum ErrorCode { INVALID_INPUT, NOT_FOUND, CONFLICT }

    private static final class Result<T> {
        final T value;
        final ErrorCode error;

        Result(T v) { this.value = v; this.error = null; }
        Result(ErrorCode e) { this.value = null; this.error = e; }

        boolean ok() { return this.error == null; }
    }

    public ContactService() {
        contacts = new HashMap<>();
    }

    // B) Centralized validation helpers (in-file)
    private static boolean required(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static boolean validContactId(String s) {
        return required(s);
    }

    private static boolean validName(String s) {
        return required(s) && s.length() <= 10;
    }

    private static boolean validPhone(String s) {
        return required(s) && s.matches("\\d{10}");
    }

    private static boolean validAddress(String s) {
        return required(s) && s.length() <= 30;
    }

    // --- Public API (boolean) delegates to internal Result-based helpers ---

    public boolean addContact(Contact contact) {
        Result<Void> r = addContactInternal(contact);
        return r.ok();
    }

    private Result<Void> addContactInternal(Contact contact) {
        if (contact == null) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }

        String id = contact.getContactId();
        if (!validContactId(id)) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }

        // Validate required fields on create
        if (!validName(contact.getFirstName())) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }
        if (!validName(contact.getLastName())) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }
        if (!validPhone(contact.getPhone())) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }
        if (!validAddress(contact.getAddress())) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }

        // ID must be unique
        if (contacts.containsKey(id)) {
            return new Result<>(ErrorCode.CONFLICT);
        }

        contacts.put(id, contact);
        return new Result<>((Void) null);
    }

    public boolean deleteContact(String contactId) {
        if (!validContactId(contactId)) {
            return false;
        }
        if (contacts.containsKey(contactId)) {
            contacts.remove(contactId);
            return true;
        }
        return false;
    }

    public boolean updateContact(String contactId, String firstName, String lastName, String phone, String address) {
        Result<Void> r = updateContactInternal(contactId, firstName, lastName, phone, address);
        return r.ok();
    }

    // C) Guarded update flow:
    // - NOT_FOUND if ID absent
    // - INVALID_INPUT if any provided field violates rules
    private Result<Void> updateContactInternal(String contactId, String firstName, String lastName, String phone, String address) {
        if (!validContactId(contactId)) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }

        Contact contact = contacts.get(contactId);
        if (contact == null) {
            return new Result<>(ErrorCode.NOT_FOUND);
        }

        // Validate provided fields (null means "no change")
        if (firstName != null && !validName(firstName)) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }
        if (lastName != null && !validName(lastName)) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }
        if (phone != null && !validPhone(phone)) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }
        if (address != null && !validAddress(address)) {
            return new Result<>(ErrorCode.INVALID_INPUT);
        }

        // Apply updates
        if (firstName != null) {
            contact.setFirstName(firstName);
        }
        if (lastName != null) {
            contact.setLastName(lastName);
        }
        if (phone != null) {
            contact.setPhone(phone);
        }
        if (address != null) {
            contact.setAddress(address);
        }

        return new Result<>((Void) null);
    }

    public Contact getContact(String contactId) {
        if (!validContactId(contactId)) {
            return null;
        }
        return contacts.get(contactId);
    }
}
