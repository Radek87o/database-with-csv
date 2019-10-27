package com.radek.databasewithcsv.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.radek.databasewithcsv.database.sql.model.SqlModelMapper;
import com.radek.databasewithcsv.database.sql.model.SqlModelMapperImpl;
import com.radek.databasewithcsv.generators.AppUserGenerator;
import com.radek.databasewithcsv.model.AppUser;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class AppUserDatabaseTest {

    @Mock
    AppUserRepository appUserRepository;
    SqlModelMapper sqlModelMapper = new SqlModelMapperImpl();
    AppUserDatabase database;

    @BeforeEach
    void setUp() {
        database = new AppUserDatabase(appUserRepository, sqlModelMapper);
    }

    @Test
    void shouldSaveAppUsers() throws DatabaseOperationException {
        AppUser appUser1 = AppUserGenerator.generateAppUser();
        AppUser appUser2 = AppUserGenerator.generateAppUser();

        List<AppUser> appUsersToSave = List.of(appUser1, appUser2);
        List<com.radek.databasewithcsv.database.sql.model.AppUser> sqlAppUsersToSave = appUsersToSave.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());
        when(appUserRepository.saveAll(sqlAppUsersToSave)).thenReturn(sqlAppUsersToSave);

        Collection<AppUser> result = database.saveAll(appUsersToSave);

        assertEquals(appUsersToSave, result);
        result.stream().forEach(appUser -> System.out.println(appUser));
        verify(appUserRepository).saveAll(sqlAppUsersToSave);
    }

    @Test
    void shouldSaveAllMethodThrowExceptionWhenAppUserCollectionIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.saveAll(null));
        assertEquals("List of app users cannot be null", exception.getMessage());
    }

    @Test
    void shouldSaveAllMethodReturnOnlyAppUsersWithUniquePhoneNumbers() throws DatabaseOperationException {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithPhoneNumber("555666777");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithPhoneNumber("555666777");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithPhoneNumber("545656777");

        List<AppUser> appUsersToSave = List.of(appUser1, appUser2, appUser3);
        List<com.radek.databasewithcsv.database.sql.model.AppUser> sqlAppUsersToSave = appUsersToSave.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());
        List<AppUser> savedAppUsers = List.of(appUser1, appUser3);
        List<com.radek.databasewithcsv.database.sql.model.AppUser> sqlSavedAppUsers = savedAppUsers.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());

        when(appUserRepository.saveAll(sqlAppUsersToSave)).thenReturn(sqlSavedAppUsers);

        Collection<AppUser> result = database.saveAll(appUsersToSave);

        assertEquals(savedAppUsers, result);

        verify(appUserRepository).saveAll(sqlAppUsersToSave);
    }

    @Test
    void shouldSaveAllMethodThrowExceptionWhenNonTransientDataAccessExceptionOccurDuringSavingAppUsers() {
        AppUser appUser1 = AppUserGenerator.generateAppUser();
        AppUser appUser2 = AppUserGenerator.generateAppUser();
        List<AppUser> appUsers = List.of(appUser1, appUser2);
        List<com.radek.databasewithcsv.database.sql.model.AppUser> sqlAppUsers = appUsers.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).saveAll(sqlAppUsers);

        assertThrows(DatabaseOperationException.class, () -> database.saveAll(appUsers));
        verify(appUserRepository).saveAll(sqlAppUsers);
    }

    @Test
    void shouldReturnAppUserById() throws DatabaseOperationException {
        AppUser appUser = AppUserGenerator.generateAppUserWithId(1L);
        com.radek.databasewithcsv.database.sql.model.AppUser sqlAppUser = sqlModelMapper.toSqlAppUser(appUser);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(sqlAppUser));

        Optional<AppUser> result = database.getById(1L);
        assertEquals(appUser, result.get());
        verify(appUserRepository).findById(1L);
    }

    @Test
    void shouldGetByIdMethodThrowExceptionWhenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.getById(null));
        assertEquals("Id cannot be null.", exception.getMessage());
    }

    @Test
    void shouldGetByIdMethodThrowExceptionWhenNonTransientDataAccessExceptionOccurDuringGettingById() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).findById(1L);

        assertThrows(DatabaseOperationException.class, () -> database.getById(1L));
        verify(appUserRepository).findById(1L);
    }

    @Test
    void shouldReturnAppUserByLastName() throws DatabaseOperationException {
        AppUser appUser = AppUserGenerator.generateAppUser();
        com.radek.databasewithcsv.database.sql.model.AppUser sqlAppUser = sqlModelMapper.toSqlAppUser(appUser);
        String searchName = appUser.getLastName().substring(0, 2);
        System.out.println(appUser);
        when(appUserRepository.findAllByLastNameIgnoreCaseContaining(searchName)).thenReturn(List.of(sqlAppUser));

        Collection<AppUser> result = database.getByLastName(searchName);
        assertEquals(List.of(appUser), result);
        verify(appUserRepository).findAllByLastNameIgnoreCaseContaining(searchName);
    }

    @Test
    void shouldGetByLastNameMethodThrowExceptionWhenLastNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.getByLastName(null));
        assertEquals("Last name cannot be null.", exception.getMessage());
    }

    @Test
    void shouldGetByLastNameMethodThrowExceptionWhenNonTransientDataAccessExceptionOccurDuringGettingByLastName() {
        String lastName = "Example";
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).findAllByLastNameIgnoreCaseContaining(lastName);

        assertThrows(DatabaseOperationException.class, () -> database.getByLastName(lastName));
        verify(appUserRepository).findAllByLastNameIgnoreCaseContaining(lastName);
    }

    @Test
    void shouldReturnFirstPageOfAppUsers() throws DatabaseOperationException {
        List<AppUser> appUsers = List.of(AppUserGenerator.generateAppUser());
        PageImpl pagedAppUsers = new PageImpl(appUsers);
        List<com.radek.databasewithcsv.database.sql.model.AppUser> sqlAppUsers = appUsers.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());
        PageImpl<com.radek.databasewithcsv.database.sql.model.AppUser> pagedSqlAppUsers = new PageImpl<>(sqlAppUsers);
        when(appUserRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")))).thenReturn(pagedSqlAppUsers);

        Page<AppUser> result = database.getAppUsers();
        assertEquals(pagedAppUsers, result);
        verify(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")));
    }

    @Test
    void shouldGetFirstPageOfAppUsersMethodThrowExceptionWhenNonTransientDataAccessExceptionOccurWhileGettingAppUsers() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")));

        assertThrows(DatabaseOperationException.class, () -> database.getAppUsers());
        verify(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")));
    }

    @Test
    void shouldReturnPageOfAppUsers() throws DatabaseOperationException {
        List<AppUser> appUsers = List.of(AppUserGenerator.generateAppUser());
        PageImpl pagedAppUsers = new PageImpl(appUsers);
        List<com.radek.databasewithcsv.database.sql.model.AppUser> sqlAppUsers = appUsers.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());
        PageImpl<com.radek.databasewithcsv.database.sql.model.AppUser> pagedSqlAppUsers = new PageImpl<>(sqlAppUsers);
        when(appUserRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")))).thenReturn(pagedSqlAppUsers);

        Page<AppUser> result = database.getAppUsers(0);
        assertEquals(pagedAppUsers, result);
        verify(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")));
    }

    @Test
    void shouldGetAppUsersMethodThrowExceptionWhenPageNumberIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.getAppUsers(null));
        assertEquals("Page number cannot be null", exception.getMessage());
    }

    @Test
    void shouldGetAppUsersMethodThrowExceptionWhenNonTransientDataAccessExceptionOccurWhileGettingAppUsers() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")));

        assertThrows(DatabaseOperationException.class, () -> database.getAppUsers(0));
        verify(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthDate")));
    }

    @Test
    void shouldReturnOldestAppUserWithPhoneNumber() throws DatabaseOperationException {
        AppUser appUser = AppUserGenerator.generateAppUser();
        com.radek.databasewithcsv.database.sql.model.AppUser sqlAppUser = sqlModelMapper.toSqlAppUser(appUser);
        when(appUserRepository.findAll(Sort.by(Direction.ASC, "birthDate"))).thenReturn(List.of(Optional.of(sqlAppUser).get()));

        Optional<AppUser> result = database.getOldestAppUserWithPhoneNumber();
        assertEquals(appUser, result.get());
        verify(appUserRepository).findAll(Sort.by(Direction.ASC, "birthDate"));
    }

    @Test
    void shouldReturnOldestAppUserWithPhoneNumberThrowExceptionWhenNonTransientDataAccessExceptionOccurDuringItsExecution() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).findAll(Sort.by(Direction.ASC, "birthDate"));

        assertThrows(DatabaseOperationException.class, () -> database.getOldestAppUserWithPhoneNumber());
        verify(appUserRepository).findAll(Sort.by(Direction.ASC, "birthDate"));
    }

    @Test
    void shouldDeleteAppUser() throws DatabaseOperationException {
        when(appUserRepository.existsById(1L)).thenReturn(true);
        doNothing().when(appUserRepository).deleteById(1L);

        database.delete(1L);

        verify(appUserRepository).existsById(1L);
        verify(appUserRepository).deleteById(1L);
    }

    @Test
    void shouldDeleteMethodThrowExceptionWhenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.delete(null));
        assertEquals("Id cannot be null.", exception.getMessage());
    }

    @Test
    void shouldDeleteMethodThrowExceptionWhenAttemptToDeleteNonExistingAppUser() {
        long id = 12345L;
        when(appUserRepository.existsById(id)).thenReturn(false);

        assertThrows(DatabaseOperationException.class, () -> database.delete(id));
        verify(appUserRepository).existsById(id);
        verify(appUserRepository, never()).deleteById(id);
    }

    @Test
    void shouldDeleteMethodThrowDatabaseOperationExceptionWhenNonTransientDataAccessExceptionOccurDuringDeletingAppUser() {
        when(appUserRepository.existsById(1L)).thenReturn(true);
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).deleteById(1L);

        assertThrows(DatabaseOperationException.class, () -> database.delete(1L));
        verify(appUserRepository).existsById(1L);
        verify(appUserRepository).deleteById(1L);
    }

    @Test
    void shouldDeleteMethodThrowDatabaseOperationExceptionWhenNoSuchElementExceptionOccurDuringDeletingAppUser() {
        when(appUserRepository.existsById(1L)).thenReturn(true);
        doThrow(new NoSuchElementException()).when(appUserRepository).deleteById(1L);

        assertThrows(DatabaseOperationException.class, () -> database.delete(1L));
        verify(appUserRepository).existsById(1L);
        verify(appUserRepository).deleteById(1L);
    }

    @Test
    void shouldDeleteAllAppUsers() throws DatabaseOperationException {
        doNothing().when(appUserRepository).deleteAll();

        database.deleteAll();

        verify(appUserRepository).deleteAll();
    }

    @Test
    void shouldDeleteAllMethodThrowDatabaseOperationExceptionWhenNonTransientDataAccessExceptionOccurDuringDeletingAllAppUsers() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).deleteAll();

        //then
        assertThrows(DatabaseOperationException.class, () -> database.deleteAll());
        verify(appUserRepository).deleteAll();
    }

    @Test
    void shouldReturnNumberOfAppUsers() throws DatabaseOperationException {
        when(appUserRepository.count()).thenReturn(10L);

        Long result = database.count();

        assertEquals(10L, result);
        verify(appUserRepository).count();
    }

    @Test
    void shouldCountMethodThrowDatabaseOperationExceptionWhenNonTransientDataAccessExceptionOccurDuringGettingNumberOfAppUsers() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).count();

        assertThrows(DatabaseOperationException.class, () -> database.count());
        verify(appUserRepository).count();
    }
}