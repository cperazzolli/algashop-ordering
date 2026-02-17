package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.VALIDATION_ERROR_FULLNAME_IS_NULL;

public class Customer implements AggregateRoot<CustomerId> {
    private CustomerId id;
    private FullName fullName;
    private BirthDate birthDate;
    private Email email;
    private Phone phone;
    private Document document;
    private Boolean prommotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registradAt;
    private OffsetDateTime archivedAt;
    private LoyaltyPoints loyaltyPoints;
    private Address address;

    @Builder(builderClassName = "BrandNewCustomerBuild", builderMethodName = "brandNew")
    private static Customer customer(FullName fullName, BirthDate birthDate, Email email, Phone phone, Document document,
                                    Boolean prommotionNotificationsAllowed,Address address) {
        return new Customer(
                new CustomerId(),
                fullName,
                birthDate,
                email,
                phone,
                document,
                prommotionNotificationsAllowed,
                false,
                OffsetDateTime.now(),
                null,
                LoyaltyPoints.ZERO,
                address
        );

    }
    @Builder(builderClassName = "ExistingCustomerBuild", builderMethodName = "existing")
    private static Customer createExisting(CustomerId id, FullName fullName, BirthDate birthDate, Email email, Phone phone, Document document,
                                    Boolean prommotionNotificationsAllowed, Boolean archived, OffsetDateTime registradAt,
                                    OffsetDateTime archivedAt, LoyaltyPoints loyaltyPoints,Address address) {
        return new Customer(id,
                fullName,
                birthDate,
                email,
                phone,
                document,
                prommotionNotificationsAllowed,
                archived,
                registradAt,
                archivedAt,
                loyaltyPoints,
                address);

    }

    private Customer(CustomerId id, FullName fullName, BirthDate birthDate, Email email, Phone phone, Document document,
                    Boolean prommotionNotificationsAllowed, Boolean archived, OffsetDateTime registradAt,
                    OffsetDateTime archivedAt, LoyaltyPoints loyaltyPoints,Address address) {
        this.setId(id);
        this.setFullName(fullName);
        this.setBirthDate(birthDate);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setRegistradAt(registradAt);
        this.setPrommotionNotificationsAllowed(prommotionNotificationsAllowed);
        this.setArchived(archived);
        this.setRegistradAt(registradAt);
        this.setAchivedAt(archivedAt);
        this.setLoyaltyPoints(loyaltyPoints);
        this.setAddress(address);
    }

    public void addLoyaltyPoints(LoyaltyPoints loyaltyPointsAdded) {
      verifyIfChangeable();
       this.setLoyaltyPoints(this.loyaltyPoints.add(loyaltyPointsAdded));
    }

    public void archive() {
      if (this.isArchived()) {
         throw new CustomerArchivedException();
      }
      this.setArchived(true);
      this.setAchivedAt(OffsetDateTime.now());
      this.setFullName(new FullName("Anonymous","Anonymous"));
      this.setPhone(new Phone("00-0000-0000"));
      this.setDocument(new Document("000-00-0000"));
      this.setEmail(new Email(UUID.randomUUID() + "@anonymous.com"));
      this.setBirthDate(null);
      this.setPrommotionNotificationsAllowed(false);
      this.setAddress(this.address.toBuilder()
              .number("Anonymized")
              .complement(null)
              .build());
    }

    public void enablePromotionNotifications() {
        verifyIfChangeable();
        this.setPrommotionNotificationsAllowed(true);
    }

    public void disablePromotionNotifications() {
        verifyIfChangeable();
        this.setPrommotionNotificationsAllowed(false);
    }

    public void changeName(FullName newName) {
        verifyIfChangeable();
        this.setFullName(newName);
    }

    public void changeEmail(Email email) {
        verifyIfChangeable();
        this.setEmail(email);
    }

    public void changeAddress(Address address) {
        verifyIfChangeable();
        this.setAddress(address);
    }

    public void changeBirthDate(BirthDate birthDate) {
        verifyIfChangeable();
        this.setBirthDate(birthDate);
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullName() {
        return fullName;
    }

    public BirthDate birthDate() {
        return birthDate;
    }

    public Email email() {
        return email;
    }

    public Phone phone() {
        return phone;
    }

    public Document document() {
        return document;
    }

    public Boolean isPrommotionNotificationsAllowed() {
        return prommotionNotificationsAllowed;
    }

    public Address address() {
        return address;
    }

    public boolean isArchived() {
        return archived;
    }

    public OffsetDateTime registradAt() {
        return registradAt;
    }

    public OffsetDateTime quiveredAt() {
        return archivedAt;
    }

    public LoyaltyPoints loyaltyPoints() {
        return loyaltyPoints;
    }

    private void setId(CustomerId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setFullName(FullName fullName) {
        Objects.requireNonNull(fullName,VALIDATION_ERROR_FULLNAME_IS_NULL);
        this.fullName = fullName;
    }

    private void setBirthDate(BirthDate birthDate) {
        this.birthDate = birthDate;
    }

    private void setEmail(Email email) {
        this.email = email;
    }

    private void setPhone(Phone phone) {
        this.phone = phone;
    }

    private void setDocument(Document document) {
        this.document = document;
    }

    private void setPrommotionNotificationsAllowed(Boolean prommotionNotificationsAllowed) {
        Objects.requireNonNull(prommotionNotificationsAllowed);
        this.prommotionNotificationsAllowed = prommotionNotificationsAllowed;
    }

    private void setArchived(Boolean archived) {
        Objects.requireNonNull(archived);
        this.archived = archived;
    }

    private void setRegistradAt(OffsetDateTime registradAt) {
        Objects.requireNonNull(registradAt);
        this.registradAt = registradAt;
    }

    private void setAchivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    private void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    private void setAddress(Address address) {
        Objects.requireNonNull(address);
        this.address = address;
    }

    private void verifyIfChangeable() {
        if(this.isArchived()) {
            throw new CustomerArchivedException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
