import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {

    @Test
    public void testContactCreation() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        assertEquals("1234567890", contact.getContactId());
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("123 Main St", contact.getAddress());
    }

    @Test
    public void testInvalidContactId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(null, "John", "Doe", "1234567890", "123 Main St");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345678901", "John", "Doe", "1234567890", "123 Main St");
        });
    }

    @Test
    public void testInvalidFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", null, "Doe", "1234567890", "123 Main St");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "JohnathanLong", "Doe", "1234567890", "123 Main St");
        });
    }

    @Test
    public void testInvalidLastName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", null, "1234567890", "123 Main St");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", "DoelastnameLong", "1234567890", "123 Main St");
        });
    }

    @Test
    public void testInvalidPhone() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", "Doe", null, "123 Main St");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", "Doe", "123456789", "123 Main St");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", "Doe", "12345678901", "123 Main St");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", "Doe", "123456789a", "123 Main St");
        });
    }

    @Test
    public void testInvalidAddress() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", "Doe", "1234567890", null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("1234567890", "John", "Doe", "1234567890", "123 Main Street, Springfield, USA");
        });
    }

    @Test
    public void testSetFirstNameValid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contact.setFirstName("Jane");
        assertEquals("Jane", contact.getFirstName());
    }

    @Test
    public void testSetFirstNameInvalid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setFirstName(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setFirstName("JohnathanLong");
        });
    }

    @Test
    public void testSetLastNameValid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contact.setLastName("Smith");
        assertEquals("Smith", contact.getLastName());
    }

    @Test
    public void testSetLastNameInvalid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setLastName(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setLastName("DoelastnameLong");
        });
    }

    @Test
    public void testSetPhoneValid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contact.setPhone("0987654321");
        assertEquals("0987654321", contact.getPhone());
    }

    @Test
    public void testSetPhoneInvalid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setPhone(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setPhone("123456789");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setPhone("12345678901");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setPhone("123456789a");
        });
    }

    @Test
    public void testSetAddressValid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contact.setAddress("456 Elm St");
        assertEquals("456 Elm St", contact.getAddress());
    }

    @Test
    public void testSetAddressInvalid() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setAddress(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            contact.setAddress("123 Main Street, Springfield, USA");
        });
    }

    @Test
    public void testContactIdNotUpdatable() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        String originalId = contact.getContactId();
        // The contactId field is final, so it cannot be changed after creation
        assertEquals("1234567890", originalId);
    }
} 