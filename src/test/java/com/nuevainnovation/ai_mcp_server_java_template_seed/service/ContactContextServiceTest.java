package com.nuevainnovation.ai_mcp_server_java_template_seed.service;

import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Address;
import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Contact;
import com.nuevainnovation.ai_mcp_server_java_template_seed.repository.AddressRepository;
import com.nuevainnovation.ai_mcp_server_java_template_seed.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContactContextServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private ContactContextService contactContextService;

    private Contact mockContact;
    private Address mockAddress1;

    @BeforeEach
    void setUp() {
        mockContact = new Contact();
        mockContact.setContactId(1L);
        mockContact.setFirstName("John");
        mockContact.setLastName("Doe");
        mockContact.setEmail("john.doe@test.com");
        mockContact.setPhoneNumber("555-1234");

        mockAddress1 = new Address();
        mockAddress1.setAddressId(10L);
        mockAddress1.setContactId(1L);
        mockAddress1.setStreetAddress("123 Main St");
        mockAddress1.setCity("Testville");
        mockAddress1.setZipCode("12345");
    }

    @Test
    void getContactInfo_found() {
        // Arrange
        String email = "john.doe@test.com";
        when(contactRepository.findByEmail(email)).thenReturn(Optional.of(mockContact));

        // Act
        Optional<Contact> result = contactContextService.getContactInfo(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(contactRepository, times(1)).findByEmail(email);
    }

    @Test
    void getContactInfo_notFound() {
        // Arrange
        String email = "nonexistent@test.com";
        when(contactRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<Contact> result = contactContextService.getContactInfo(email);

        // Assert
        assertFalse(result.isPresent());
        verify(contactRepository, times(1)).findByEmail(email);
    }

    @Test
    void getContactAddresses_found() {
        // Arrange
        Long contactId = 1L;
        Address mockAddress2 = new Address();
        mockAddress2.setAddressId(11L);
        mockAddress2.setContactId(1L);
        mockAddress2.setStreetAddress("456 Oak Ave");
        List<Address> expectedAddresses = Arrays.asList(mockAddress1, mockAddress2);
        when(addressRepository.findByContactId(contactId)).thenReturn(expectedAddresses);

        // Act
        List<Address> result = contactContextService.getContactAddresses(contactId);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("123 Main St", result.get(0).getStreetAddress());
        verify(addressRepository, times(1)).findByContactId(contactId);
    }

    @Test
    void getContactAddresses_notFound() {
        // Arrange
        Long contactId = 99L;
        when(addressRepository.findByContactId(contactId)).thenReturn(List.of());

        // Act
        List<Address> result = contactContextService.getContactAddresses(contactId);

        // Assert
        assertTrue(result.isEmpty());
        verify(addressRepository, times(1)).findByContactId(contactId);
    }
}
