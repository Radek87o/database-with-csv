package com.radek.databasewithcsv.database.sql.model;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private final Long id;

    @Column(nullable = false)
    private final String firstName;

    @Column(nullable = false)
    private final String lastName;

    @Column(nullable = false)
    private final LocalDate birthDate;

    private final String phoneNumber;

    private AppUser() {
        this.id = null;
        this.firstName = null;
        this.lastName = null;
        this.birthDate = null;
        this.phoneNumber = null;
    }

    private AppUser(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.phoneNumber = builder.phoneNumber;
    }

    public static AppUser.Builder builder() {
        return new AppUser.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        return Objects.equals(getId(), appUser.getId())
            && Objects.equals(getFirstName(), appUser.getFirstName())
            && Objects.equals(getLastName(), appUser.getLastName())
            && Objects.equals(getBirthDate(), appUser.getBirthDate())
            && Objects.equals(getPhoneNumber(), appUser.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getBirthDate(), getPhoneNumber());
    }

    @Override
    public String toString() {
        return "AppUser{"
            + "id=" + id
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", birthDate=" + birthDate
            + ", phoneNumber='" + phoneNumber + '\''
            + '}';
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String phoneNumber;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public AppUser build() {
            return new AppUser(this);
        }
    }
}
