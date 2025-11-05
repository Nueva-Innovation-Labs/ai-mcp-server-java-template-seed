package com.nuevainnovation.ai_mcp_server_java_template_seed.model;



import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    // The @OneToMany relationship is often defined in the main entity
    // but is omitted here for simplicity and focus on context retrieval.
}