package com.nuevainnovation.ai_mcp_server_java_template_seed.repository;



import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Custom query to find all addresses for a given contact ID
    List<Address> findByContactId(Long contactId);
}