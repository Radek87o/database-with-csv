package com.radek.databasewithcsv.service;

import com.radek.databasewithcsv.database.Database;
import com.radek.databasewithcsv.database.DatabaseOperationException;
import com.radek.databasewithcsv.model.AppUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private Logger log = LoggerFactory.getLogger(AppUserService.class);

    private final Database database;

    @Autowired
    public AppUserService(Database database) {
        this.database = database;
    }

    public Collection<AppUser> addAppUsers(Collection<AppUser> appUsers) throws ServiceOperationException {
        if (appUsers == null) {
            log.error("Attempt to add null app users.");
            throw new IllegalArgumentException("App users cannot be null");
        }
        try {
            List<AppUser> validatedAppUsers = new ArrayList<>();
            for (AppUser appUser : appUsers) {
                if (!database.existsByPhoneNumber(appUser.getPhoneNumber())) {
                    validatedAppUsers.add(appUser);
                } else {
                    log.error("User {} cannot be saved in database since his phone number is not unique", appUser.getFirstName() + " " + appUser.getLastName());
                }
            }
            return database.saveAll(validatedAppUsers);
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during adding app user.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Page<AppUser> getFirstPageOfAppUsers() throws ServiceOperationException {
        try {
            return database.getAppUsers();
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during getting first page of app user.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Page<AppUser> getAppUsers(Integer pageNumber) throws ServiceOperationException {
        if (pageNumber == null) {
            log.error("Attempt to get page of app users without providing page number");
            throw new IllegalArgumentException("Attempt to get page of app users without providing page number");
        }
        try {
            return database.getAppUsers(pageNumber);
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during getting page of app user.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<AppUser> getAppUserById(Long id) throws ServiceOperationException {
        if (id == null) {
            log.error("Attempt to get app user by id providing null id.");
            throw new IllegalArgumentException("Id cannot be null");
        }
        try {
            return database.getById(id);
        } catch (DatabaseOperationException exc) {
            String message = "An error occurred during getting app user by id.";
            log.error(message, exc);
            throw new ServiceOperationException(message, exc);
        }
    }

    public Collection<AppUser> getAppUsersByLastName(String lastName) throws ServiceOperationException {
        if (lastName == null) {
            log.error("Attempt to get app user by last name providing null last name.");
            throw new IllegalArgumentException("Last name cannot be null.");
        }
        try {
            return database.getByLastName(lastName);
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during getting app user by number.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<AppUser> getOldestAppUser() throws ServiceOperationException {
        try {
            return database.getOldestAppUserWithPhoneNumber();
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during getting oldest app user.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public void deleteById(Long id) throws ServiceOperationException {
        if (id == null) {
            log.error("Attempt to delete app user providing null id");
            throw new IllegalArgumentException("Id cannot be null");
        }
        try {
            database.delete(id);
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during deleting app user by id.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public void deleteAll() throws ServiceOperationException {
        try {
            database.deleteAll();
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during deleting all app users.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public long count() throws ServiceOperationException {
        try {
            return database.count();
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during getting number of app users.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public boolean existsByPhoneNumber(String phoneNumber) throws ServiceOperationException {
        if (phoneNumber == null || phoneNumber.length() == 0) {
            return false;
        }
        try {
            return database.existsByPhoneNumber(phoneNumber);
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during checking if phone number exists in db.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public boolean existsById(Long id) throws ServiceOperationException {
        if (id == null) {
            log.error("Attempt to provide null id to check whether app user exists");
            throw new IllegalArgumentException("Id cannot be null");
        }
        try {
            return database.existsById(id);
        } catch (DatabaseOperationException e) {
            String message = "An error occurred during checking if app user exists in db.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }
}
