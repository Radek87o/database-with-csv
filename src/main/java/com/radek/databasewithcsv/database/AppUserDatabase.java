package com.radek.databasewithcsv.database;

import com.radek.databasewithcsv.database.sql.model.AppUser;
import com.radek.databasewithcsv.database.sql.model.SqlModelMapper;

import java.util.ArrayList;
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
import org.springframework.data.domain.Sort.Direction;
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
            List<AppUser> sqlValidatedAppUsers = new ArrayList<>();
            for (AppUser appUser : sqlAppUsers) {
                if (appUser.getPhoneNumber().length() == 0 || !appUserRepository.existsByPhoneNumber(appUser.getPhoneNumber())) {
                    sqlValidatedAppUsers.add(appUser);
                } else {
                    log.error("User {} cannot be saved in database since his phone number is not unique", appUser.getFirstName() + " " + appUser.getLastName());
                }
            }
            List<AppUser> savedAppUsers = appUserRepository.saveAll(sqlValidatedAppUsers);
            return savedAppUsers.stream().map(appUser -> sqlModelMapper.toAppUser(appUser)).collect(Collectors.toList());
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during saving collection of users.";
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
            Collection<AppUser> appUsersWithLastName = appUserRepository.findAllByLastNameIgnoreCaseContaining(lastName);
            return appUsersWithLastName.stream().map(appUser -> sqlModelMapper.toAppUser(appUser)).collect(Collectors.toList());
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting app users by last name.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public Page<com.radek.databasewithcsv.model.AppUser> getAppUsers() throws DatabaseOperationException {
        try {
            Page<AppUser> appUsers = appUserRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")));
            return appUsers.map(appUser -> sqlModelMapper.toAppUser(appUser));
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting first page of app users";
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
            List<AppUser> appUsers = appUserRepository.findAll(Sort.by(Direction.ASC, "birthDate"));
            if (appUsers.isEmpty()) {
                return Optional.empty();
            }
            AppUser oldestAppUser = appUsers.stream().filter(appUser -> !appUser.getPhoneNumber().equals("")).collect(Collectors.toList()).get(0);
            return Optional.of(sqlModelMapper.toAppUser(oldestAppUser));
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

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) throws DatabaseOperationException {
        if (phoneNumber == null || phoneNumber.length() == 0) {
            return false;
        }
        try {
            return appUserRepository.existsByPhoneNumber(phoneNumber);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during getting number of app users.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }

    @Override
    public boolean existsById(Long id) throws DatabaseOperationException {
        if (id == null) {
            log.error("Attempt to provide null id");
            throw new IllegalArgumentException("Id cannot be null");
        }
        try {
            return appUserRepository.existsById(id);
        } catch (NonTransientDataAccessException e) {
            String message = "An error occurred during checking whether app user exists.";
            log.error(message, e);
            throw new DatabaseOperationException(message, e);
        }
    }
}
