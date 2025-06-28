import java.util.HashMap;
import java.util.Map;

public class ContactService {
    private Map<String, Contact> contacts;

    public ContactService() {
        contacts = new HashMap<>();
    }

    public boolean addContact(Contact contact) {
        if (contacts.containsKey(contact.getContactId())) {
            return false; // Contact ID must be unique
        }
        contacts.put(contact.getContactId(), contact);
        return true;
    }

    public boolean deleteContact(String contactId) {
        if (contacts.containsKey(contactId)) {
            contacts.remove(contactId);
            return true;
        }
        return false;
    }

    public boolean updateContact(String contactId, String firstName, String lastName, String phone, String address) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            return false;
        }
        if (firstName != null && firstName.length() <= 10) {
            contact.setFirstName(firstName);
        }
        if (lastName != null && lastName.length() <= 10) {
            contact.setLastName(lastName);
        }
        if (phone != null && phone.matches("\\d{10}")) {
            contact.setPhone(phone);
        }
        if (address != null && address.length() <= 30) {
            contact.setAddress(address);
        }
        return true;
    }

    public Contact getContact(String contactId) {
        return contacts.get(contactId);
    }
} 