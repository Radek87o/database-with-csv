package com.radek.databasewithcsv.database;

import com.radek.databasewithcsv.database.sql.model.AppUser;
import com.radek.databasewithcsv.database.sql.model.SqlModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserDatabase implements Database {

    private final AppUserRepository appUserRepository;
    private Logger log = LoggerFactory.getLogger(AppUserDatabase.class);
    private SqlModelMapper sqlModelMapper;

    public AppUserDatabase(AppUserRepository appUserRepository, SqlModelMapper sqlModelMapper) {
        this.appUserRepository = appUserRepository;
        this.sqlModelMapper = sqlModelMapper;
    }

    @Override
    public Collection<com.radek.databasewithcsv.model.AppUser> saveAll(Collection<com.radek.databasewithcsv.model.AppUser> appUsers) throws DatabaseOperationException {
        if (appUsers == null) {
            log.error("Attempt to save empty collection of users");
            throw new IllegalArgumentException("List of app users cannot be null");
        }
        try {
            List<AppUser> sqlAppUsers = appUsers.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());
            List<AppUser> savedAppUsers = appUserRepository.saveAll(sqlAppUsers);
            return savedAppUsers.stream().map(appUser -> sqlModelMapper.toAppUser(appUser)).collect(Collectors.toList());
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during saving collection of users.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public com.radek.databasewithcsv.model.AppUser save(com.radek.databasewithcsv.model.AppUser appUser) throws DatabaseOperationException {
        if (appUser == null) {
            log.error("Attempt to save null app user");
            throw new IllegalArgumentException("App user cannot be null.");
        }
        if (appUser.getPhoneNumber() != null && appUserRepository.existsByPhoneNumber(appUser.getPhoneNumber())) {
            log.error("Attempt to save user with already existing phone number");
            throw new IllegalArgumentException("Phone number has to be unique");
        }
        try {
            AppUser sqlAppUser = sqlModelMapper.toSqlAppUser(appUser);
            return sqlModelMapper.toAppUser(appUserRepository.save(sqlAppUser));
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during saving app user.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Optional<com.radek.databasewithcsv.model.AppUser> getById(Long id) throws DatabaseOperationException {
        if (id == null) {
            log.error("Attempt to provide null id");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        try {
            Optional<AppUser> foundAppUser = appUserRepository.findById(id);
            if (foundAppUser.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(sqlModelMapper.toAppUser(foundAppUser.get()));
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting app user by id.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Collection<com.radek.databasewithcsv.model.AppUser> getByLastName(String lastName) throws DatabaseOperationException {
        if (lastName == null) {
            log.error("Attempt to provide empty last name");
            throw new IllegalArgumentException("Last name cannot be null.");
        }
        try {
            Collection<AppUser> appUsersWithLastName = appUserRepository.findAllByLastName(lastName);
            return appUsersWithLastName.stream().map(appUser -> sqlModelMapper.toAppUser(appUser)).collect(Collectors.toList());
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting app users by last name.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Page<com.radek.databasewithcsv.model.AppUser> getAppUsers(Integer pageNumber) throws DatabaseOperationException {
        if (pageNumber == null) {
            log.error("Attempt to receive page of users without indicating page number");
            throw new IllegalArgumentException("Page number cannot be null");
        }
        try {
            Page<AppUser> basePage = appUserRepository.findAll(PageRequest.of(0, 5));
            if (pageNumber > basePage.getTotalPages()) {
                log.error("Attempt to get page of users for non-existing page number");
                throw new IllegalArgumentException("Page number cannot be greater than total number of pages");
            }
            Page<AppUser> appUsers = appUserRepository.findAll(PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "birthDate")));
            return appUsers.map(appUser -> sqlModelMapper.toAppUser(appUser));
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting page of app users";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Optional<com.radek.databasewithcsv.model.AppUser> getOldestAppUserWithPhoneNumber() throws DatabaseOperationException {
        try {
            Optional<AppUser> oldestAppUser = appUserRepository.findOneAppUserByBirthDateAndPhoneNumber();
            if (oldestAppUser.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(sqlModelMapper.toAppUser(oldestAppUser.get()));
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting the oldest app user";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseOperationException {
        if (id == null) {
            log.error("Attempt to delete app user providing null id.");
            throw new IllegalArgumentException("Id cannot be null.");
        }
        if (!appUserRepository.existsById(id)) {
            log.error("Attempt to delete non-existing app user.");
            throw new DatabaseOperationException(String.format("There was no app user in database with id: %s", id));
        }
        try {
            appUserRepository.deleteById(id);
        } catch (NonTransientDataAccessException | NoSuchElementException e) {
            String message = "An error occurred during deleting app user.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public void deleteAll() throws DatabaseOperationException {
        try {
            appUserRepository.deleteAll();
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during deleting all app users.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public long count() throws DatabaseOperationException {
        try {
            return appUserRepository.count();
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting number of app users.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }
}
