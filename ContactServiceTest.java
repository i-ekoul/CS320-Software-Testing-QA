import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactServiceTest {
    private ContactService contactService;

    @BeforeEach
    public void setUp() {
        contactService = new ContactService();
    }

    @Test
    public void testAddContact() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        assertTrue(contactService.addContact(contact));
        assertFalse(contactService.addContact(contact)); // Duplicate ID
    }

    @Test
    public void testAddContactWithUniqueId() {
        Contact contact1 = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("0987654321", "Jane", "Smith", "0987654321", "456 Elm St");
        assertTrue(contactService.addContact(contact1));
        assertTrue(contactService.addContact(contact2));
    }

    @Test
    public void testDeleteContact() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        assertTrue(contactService.deleteContact("1234567890"));
        assertFalse(contactService.deleteContact("1234567890")); // Already deleted
    }

    @Test
    public void testDeleteNonExistentContact() {
        assertFalse(contactService.deleteContact("nonexistent"));
    }

    @Test
    public void testUpdateContact() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        assertTrue(contactService.updateContact("1234567890", "Jane", "Smith", "0987654321", "456 Elm St"));
        
        Contact updatedContact = contactService.getContact("1234567890");
        assertEquals("Jane", updatedContact.getFirstName());
        assertEquals("Smith", updatedContact.getLastName());
        assertEquals("0987654321", updatedContact.getPhone());
        assertEquals("456 Elm St", updatedContact.getAddress());
    }

    @Test
    public void testUpdateContactPartial() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        assertTrue(contactService.updateContact("1234567890", "Jane", null, null, null));
        
        Contact updatedContact = contactService.getContact("1234567890");
        assertEquals("Jane", updatedContact.getFirstName());
        assertEquals("Doe", updatedContact.getLastName()); // Should remain unchanged
        assertEquals("1234567890", updatedContact.getPhone()); // Should remain unchanged
        assertEquals("123 Main St", updatedContact.getAddress()); // Should remain unchanged
    }

    @Test
    public void testUpdateNonExistentContact() {
        assertFalse(contactService.updateContact("nonexistent", "Jane", "Smith", "0987654321", "456 Elm St"));
    }

    @Test
    public void testGetContact() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        Contact retrievedContact = contactService.getContact("1234567890");
        assertNotNull(retrievedContact);
        assertEquals("1234567890", retrievedContact.getContactId());
        assertEquals("John", retrievedContact.getFirstName());
        assertEquals("Doe", retrievedContact.getLastName());
        assertEquals("1234567890", retrievedContact.getPhone());
        assertEquals("123 Main St", retrievedContact.getAddress());
    }

    @Test
    public void testGetNonExistentContact() {
        assertNull(contactService.getContact("nonexistent"));
    }
} 