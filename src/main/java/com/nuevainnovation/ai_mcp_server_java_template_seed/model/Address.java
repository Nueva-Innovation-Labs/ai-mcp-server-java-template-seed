package com.nuevainnovation.ai_mcp_server_java_template_seed.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@Getter @Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(name = "contact_id")
    private Long contactId; // Foreign key column

    private String streetAddress;
    private String city;
    private String stateProvince;
    private String zipCode;
    private String country;

    // Optional: Define a many-to-one relationship to Contact
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "contact_id", insertable = false, updatable = false)
    // private Contact contact;
}