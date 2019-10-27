package com.radek.databasewithcsv.database;

import com.radek.databasewithcsv.database.sql.model.AppUser;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    Collection<AppUser> findAllByLastNameIgnoreCaseContaining(String lastName);

    boolean existsByPhoneNumber(String phoneNumber);
}
