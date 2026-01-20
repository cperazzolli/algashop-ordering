package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class CustomerTestDataBuilder {

    private CustomerTestDataBuilder() {
    }

    public static Customer.BrandNewCustomerBuild brandNewCustomerBuild() {
        return Customer.brandNew()
                .fullName(new FullName("John"," Doe"))
                .birthDate(new BirthDate(LocalDate.of(1985, 5, 22)))
                .email(new Email("jhon.doe@email.com"))
                .phone(new Phone("11-4545-5454"))
                .document(new Document("123456"))
                .prommotionNotificationsAllowed(false)
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build());
    }

    public static Customer.ExistingCustomerBuild existingCustomerBuild() {
       return Customer.existing()
               .id(new CustomerId())
               .fullName(new FullName("John"," Doe"))
               .birthDate(new BirthDate(LocalDate.of(1985, 5, 22)))
               .email(new Email("jhon.doe@email.com"))
               .phone(new Phone("11-4545-5454"))
               .document(new Document("123456"))
               .prommotionNotificationsAllowed(true)
               .loyaltyPoints(new LoyaltyPoints(10))
               .registradAt(OffsetDateTime.now())
               .archived(false)
               .address(Address.builder()
                       .street("Bourbon Street")
                       .number("1134")
                       .neighborhood("North Ville")
                       .city("York")
                       .state("South California")
                       .zipCode(new ZipCode("12345"))
                       .complement("Apt. 114")
                       .build());
    }


    public static Customer.ExistingCustomerBuild existingCustomerAnonymizedBuild() {
        return Customer.existing()
                .id(new CustomerId())
                .fullName(new FullName("Anonymous"," Anonymous"))
                .birthDate(new BirthDate(LocalDate.of(1985, 5, 22)))
                .email(new Email("john.doe@email.com"))
                .phone(new Phone("11-4545-5454"))
                .document(new Document("123456"))
                .prommotionNotificationsAllowed(false)
                .archived(true)
                .registradAt(OffsetDateTime.of(LocalDateTime.of(2026,1,14,5,25), ZoneOffset.of("-03:00")))
                .archivedAt(OffsetDateTime.of(LocalDateTime.of(2026,1,14,5,25), ZoneOffset.of("-03:00")))
                .loyaltyPoints(new LoyaltyPoints())
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build());
    }
}
