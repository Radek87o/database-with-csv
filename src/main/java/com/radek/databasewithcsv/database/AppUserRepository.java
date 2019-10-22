package com.radek.databasewithcsv.database;

import com.radek.databasewithcsv.database.sql.model.AppUser;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    @Query(value = "SELECT a FROM AppUser a WHERE lower(a.lastName) LIKE %:lastName%")
    Collection<AppUser> findAllByLastName(@Param("lastName") String lastName);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT a FROM AppUser a WHERE a.phoneNumber IS NOT NULL ORDER BY a.birthDate")
    Optional<AppUser> findOneAppUserByBirthDateAndPhoneNumber();

}
