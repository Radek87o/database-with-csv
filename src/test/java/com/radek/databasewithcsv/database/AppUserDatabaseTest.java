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
    void shouldSaveInvoices() throws DatabaseOperationException {
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
    void shouldSaveAppUser() throws DatabaseOperationException {
        AppUser appUser = AppUserGenerator.generateAppUser();
        com.radek.databasewithcsv.database.sql.model.AppUser sqlAppUser = sqlModelMapper.toSqlAppUser(appUser);
        when(appUserRepository.save(sqlAppUser)).thenReturn(sqlAppUser);

        AppUser result = database.save(appUser);

        assertEquals(appUser, result);
        verify(appUserRepository).save(sqlAppUser);
    }

    @Test
    void shouldSaveMethodThrowExceptionWhenAppUserIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.save(null));
        assertEquals("App user cannot be null.", exception.getMessage());
    }

    @Test
    void shouldSaveMethodThrowExceptionWhenPhoneNumberIsNotUnique() {
        AppUser appUser = AppUserGenerator.generateAppUser();
        when(appUserRepository.existsByPhoneNumber(appUser.getPhoneNumber())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.save(appUser));
        assertEquals("Phone number has to be unique", exception.getMessage());
        verify(appUserRepository).existsByPhoneNumber(appUser.getPhoneNumber());
        verify(appUserRepository, never()).save(sqlModelMapper.toSqlAppUser(appUser));
    }

    @Test
    void shouldSaveMethodThrowExceptionWhenNonTransientDataAccessExceptionOccurDuringSavingAppUser() {
        AppUser appUser = AppUserGenerator.generateAppUser();
        com.radek.databasewithcsv.database.sql.model.AppUser sqlAppUser = sqlModelMapper.toSqlAppUser(appUser);
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).save(sqlAppUser);

        assertThrows(DatabaseOperationException.class, () -> database.save(appUser));
        verify(appUserRepository).save(sqlAppUser);
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
        when(appUserRepository.findAllByLastName(searchName)).thenReturn(List.of(sqlAppUser));

        Collection<AppUser> result = database.getByLastName(searchName);
        assertEquals(List.of(appUser), result);
        verify(appUserRepository).findAllByLastName(searchName);
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
        }).when(appUserRepository).findAllByLastName(lastName);

        assertThrows(DatabaseOperationException.class, () -> database.getByLastName(lastName));
        verify(appUserRepository).findAllByLastName(lastName);
    }

    /*@Test
    void shouldReturnPageOfAppUsers() throws DatabaseOperationException {
        List<AppUser> appUsers = List.of(AppUserGenerator.generateAppUser());
        PageImpl pagedAppUsers = new PageImpl(appUsers);
        List<com.radek.databasewithcsv.database.sql.model.AppUser> sqlAppUsers = appUsers.stream().map(appUser -> sqlModelMapper.toSqlAppUser(appUser)).collect(Collectors.toList());
        PageImpl<com.radek.databasewithcsv.database.sql.model.AppUser> pagedSqlAppUsers = new PageImpl<>(sqlAppUsers);
        when(appUserRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthName")))).thenReturn(pagedSqlAppUsers);

        Page<AppUser> result = database.getAppUsers(0);
        assertEquals(pagedAppUsers, result);
        verify(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthName")));
    }*/

    @Test
    void shouldGetAppUsersMethodThrowExceptionWhenPageNumberIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> database.getAppUsers(null));
        assertEquals("Page number cannot be null", exception.getMessage());
    }

    /*@Test
    void shouldGetAppUsersMethodThrowExceptionWhenNonTransientDataAccessExceptionOccurWhileGettingAppUsers() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthName")));

        assertThrows(DatabaseOperationException.class, () -> database.getAppUsers(0));
        verify(appUserRepository).findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "birthName")));
    }*/

    @Test
    void shouldReturnOldestAppUserWithPhoneNumber() throws DatabaseOperationException {
        AppUser appUser = AppUserGenerator.generateAppUser();
        com.radek.databasewithcsv.database.sql.model.AppUser sqlAppUser = sqlModelMapper.toSqlAppUser(appUser);
        when(appUserRepository.findOneAppUserByBirthDateAndPhoneNumber()).thenReturn(Optional.of(sqlAppUser));

        Optional<AppUser> result = database.getOldestAppUserWithPhoneNumber();
        assertEquals(appUser, result.get());
        verify(appUserRepository).findOneAppUserByBirthDateAndPhoneNumber();
    }

    @Test
    void shouldReturnOldestAppUserWithPhoneNumberThrowExceptionWhenNonTransientDataAccessExceptionOccurDuringItsExecution() {
        doThrow(new NonTransientDataAccessException("") {
        }).when(appUserRepository).findOneAppUserByBirthDateAndPhoneNumber();

        assertThrows(DatabaseOperationException.class, () -> database.getOldestAppUserWithPhoneNumber());
        verify(appUserRepository).findOneAppUserByBirthDateAndPhoneNumber();
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