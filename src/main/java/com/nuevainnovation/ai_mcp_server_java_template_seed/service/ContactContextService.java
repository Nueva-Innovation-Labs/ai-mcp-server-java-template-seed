package com.nuevainnovation.ai_mcp_server_java_template_seed.service;


import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Address;
import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Contact;
import com.nuevainnovation.ai_mcp_server_java_template_seed.repository.AddressRepository;
import com.nuevainnovation.ai_mcp_server_java_template_seed.repository.ContactRepository;
import org.springframework.ai.tool.Tool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ContactContextService {

    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;

    public ContactContextService(ContactRepository contactRepository, AddressRepository addressRepository) {
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
    }

    /** 1. Contact information Tool */
    // Use @Tool annotation. The name and description are crucial for the AI model.
    @Tool(name = "getContactInfo",
            description = "Retrieves a contact's full details (name, email, phone) by email address.")
    public Optional<Contact> getContactInfo(String email) {
        return contactRepository.findByEmail(email);
    }

    /** 2. Address information Tool */
    @Tool(name = "getContactAddresses",
            description = "Retrieves a list of addresses (street, city, zip code) for a contact using their ID.")
    public List<Address> getContactAddresses(Long contactId) {
        return addressRepository.findByContactId(contactId);
    }
}