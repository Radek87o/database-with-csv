package com.radek.databasewithcsv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.radek.databasewithcsv.csvhelper.converters.LocalDateConverter;
import com.radek.databasewithcsv.csvhelper.converters.NamesConverter;

import java.util.Objects;

public class AppUser {

    private Long id;

    @CsvCustomBindByName(column = "first_name", converter = NamesConverter.class)
    private String firstName;

    @CsvCustomBindByName(column = "last_name", converter = NamesConverter.class)
    private String lastName;

    @CsvCustomBindByName(column = "birth_date", converter = LocalDateConverter.class)
    private String birthDate;

    @CsvBindByName(column = "phone_no")
    private String phoneNumber;

    public AppUser() {
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

    public String getBirthDate() {
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
        return Objects.equals(getFirstName(), appUser.getFirstName())
            && Objects.equals(getLastName(), appUser.getLastName())
            && Objects.equals(getBirthDate(), appUser.getBirthDate())
            && Objects.equals(getPhoneNumber(), appUser.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getBirthDate(), getPhoneNumber());
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
        private String birthDate;
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

        public Builder withBirthDate(String birthDate) {
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
