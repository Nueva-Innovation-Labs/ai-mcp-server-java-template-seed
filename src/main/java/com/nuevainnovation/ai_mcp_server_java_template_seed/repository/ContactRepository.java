package com.nuevainnovation.ai_mcp_server_java_template_seed.repository;


import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Custom query to find a contact by email
    Optional<Contact> findByEmail(String email);
}