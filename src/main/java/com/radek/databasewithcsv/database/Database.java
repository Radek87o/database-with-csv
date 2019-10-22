package com.radek.databasewithcsv.database;

import com.radek.databasewithcsv.model.AppUser;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface Database {

    Collection<AppUser> saveAll(Collection<AppUser> appUsers) throws DatabaseOperationException;

    AppUser save(AppUser appUser) throws DatabaseOperationException;

    Optional<AppUser> getById(Long id) throws DatabaseOperationException;

    Collection<AppUser> getByLastName(String lastName) throws DatabaseOperationException;

    Page<AppUser> getAppUsers(Integer pageNumber) throws DatabaseOperationException;

    Optional<AppUser> getOldestAppUserWithPhoneNumber() throws DatabaseOperationException;

    void delete(Long id) throws DatabaseOperationException;

    void deleteAll() throws DatabaseOperationException;

    long count() throws DatabaseOperationException;
}
