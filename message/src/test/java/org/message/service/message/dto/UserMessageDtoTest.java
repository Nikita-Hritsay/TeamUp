package org.message.service.message.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMessageDtoTest {

    @Test
    void testRecordCreation() {
        UserMessageDto dto = new UserMessageDto(1L, "John", "Doe", "john@example.com", "1234567890");

        assertEquals(1L, dto.id());
        assertEquals("John", dto.firstname());
        assertEquals("Doe", dto.lastname());
        assertEquals("john@example.com", dto.email());
        assertEquals("1234567890", dto.mobileNumber());
    }

    @Test
    void testRecordEquality() {
        UserMessageDto dto1 = new UserMessageDto(1L, "John", "Doe", "john@example.com", "1234567890");
        UserMessageDto dto2 = new UserMessageDto(1L, "John", "Doe", "john@example.com", "1234567890");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testRecordInequality() {
        UserMessageDto dto1 = new UserMessageDto(1L, "John", "Doe", "john@example.com", "1234567890");
        UserMessageDto dto2 = new UserMessageDto(2L, "Jane", "Smith", "jane@example.com", "0987654321");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testRecordWithNullValues() {
        UserMessageDto dto = new UserMessageDto(null, null, null, null, null);

        assertNull(dto.id());
        assertNull(dto.firstname());
        assertNull(dto.lastname());
        assertNull(dto.email());
        assertNull(dto.mobileNumber());
    }

    @Test
    void testRecordToString() {
        UserMessageDto dto = new UserMessageDto(1L, "John", "Doe", "john@example.com", "1234567890");
        String str = dto.toString();

        assertTrue(str.contains("John"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("john@example.com"));
    }
}
