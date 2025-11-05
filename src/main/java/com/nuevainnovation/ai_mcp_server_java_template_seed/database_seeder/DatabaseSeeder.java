package com.nuevainnovation.ai_mcp_server_java_template_seed.database_seeder;


import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Address;
import com.nuevainnovation.ai_mcp_server_java_template_seed.model.Contact;
import com.nuevainnovation.ai_mcp_server_java_template_seed.repository.AddressRepository;
import com.nuevainnovation.ai_mcp_server_java_template_seed.repository.ContactRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final Random random = ThreadLocalRandom.current();

    // --- Realistic Data Sets ---
    private static final String[] FIRST_NAMES = {"James", "Mary", "Robert", "Patricia", "John", "Jennifer", "Michael", "Linda", "David", "Elizabeth", "William", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"};
    private static final String[] DOMAINS = {"example.com", "corp.com", "mailservice.net", "businessco.org", "inboxpro.io"};
    private static final String[] STREET_TYPES = {"St", "Ave", "Blvd", "Rd", "Ln", "Pkwy"};
    private static final String[] CITIES = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville", "Fort Worth", "Columbus"};
    private static final String[] STATE_PROVINCES = {"CA", "NY", "TX", "FL", "IL", "PA", "AZ", "OH", "NC", "MI", "GA", "NJ", "VA", "WA", "MA", "IN"};
    private static final String[] COUNTRIES = {"USA", "Canada", "Mexico"};


    public DatabaseSeeder(ContactRepository contactRepository, AddressRepository addressRepository) {
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed if the database is nearly empty
        if (contactRepository.count() < 100) {
            System.out.println("Seeding 1000 realistic Contacts and Addresses...");

            List<Contact> contacts = createContacts(1000);
            List<Contact> savedContacts = contactRepository.saveAll(contacts);
            createAddresses(savedContacts);

            System.out.println("Seeding complete. Total contacts: " + contactRepository.count());
            System.out.println("Total addresses: " + addressRepository.count());
        }
    }

    private List<Contact> createContacts(int count) {
        List<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String domain = DOMAINS[random.nextInt(DOMAINS.length)];

            // Create a unique email by adding a number or initial
            String emailPrefix = (random.nextBoolean() ? (firstName.toLowerCase().charAt(0) + lastName.toLowerCase()) : (firstName.toLowerCase() + "." + lastName.toLowerCase()));
            String email = emailPrefix + i + "@" + domain;

            // Generate a random, realistic-looking phone number (e.g., 555-123-4567)
            String phoneNumber = String.format("%03d-%03d-%04d",
                    500 + random.nextInt(99),
                    100 + random.nextInt(899),
                    1000 + random.nextInt(8999)
            );

            Contact contact = new Contact();
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setEmail(email);
            contact.setPhoneNumber(phoneNumber);
            contacts.add(contact);
        }
        return contacts;
    }

    private void createAddresses(List<Contact> savedContacts) {
        List<Address> addresses = new ArrayList<>();

        for (Contact contact : savedContacts) {

            // Select random location data for the address
            String city = CITIES[random.nextInt(CITIES.length)];
            String stateProvince = STATE_PROVINCES[random.nextInt(STATE_PROVINCES.length)];
            String country = COUNTRIES[random.nextInt(COUNTRIES.length)];

            // --- Primary Address ---
            Address primaryAddress = new Address();
            primaryAddress.setContactId(contact.getContactId());

            // Generate realistic street address (e.g., 1234 Main St)
            String streetNumber = String.valueOf(100 + random.nextInt(9000));
            String streetName = contact.getLastName() + " " + STREET_TYPES[random.nextInt(STREET_TYPES.length)];
            primaryAddress.setStreetAddress(streetNumber + " " + streetName);

            primaryAddress.setCity(city);
            primaryAddress.setStateProvince(stateProvince);

            // Generate realistic 5-digit zip code
            primaryAddress.setZipCode(String.format("%05d", 10000 + random.nextInt(89999)));
            primaryAddress.setCountry(country);
            addresses.add(primaryAddress);

            // --- Secondary Address (Occasional) ---
            // Roughly 30% of contacts get a secondary address
            if (random.nextDouble() < 0.3) {
                Address secondaryAddress = new Address();
                secondaryAddress.setContactId(contact.getContactId());

                // Use a different address structure
                secondaryAddress.setStreetAddress("PO Box " + String.valueOf(100 + random.nextInt(5000)));

                // Use a different city/state/country combination
                String altCity = CITIES[random.nextInt(CITIES.length)];
                String altState = STATE_PROVINCES[random.nextInt(STATE_PROVINCES.length)];
                secondaryAddress.setCity("Alt-" + altCity);
                secondaryAddress.setStateProvince(altState);
                secondaryAddress.setZipCode(String.format("%05d", 90000 + random.nextInt(9999)));
                secondaryAddress.setCountry(country);
                addresses.add(secondaryAddress);
            }
        }
        addressRepository.saveAll(addresses);
    }
}