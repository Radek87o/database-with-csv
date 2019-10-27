package com.radek.databasewithcsv.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.radek.databasewithcsv.database.Database;
import com.radek.databasewithcsv.database.DatabaseOperationException;
import com.radek.databasewithcsv.generators.AppUserGenerator;
import com.radek.databasewithcsv.model.AppUser;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private Database database;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void shouldAddAppUsers() throws DatabaseOperationException, ServiceOperationException {
        List<AppUser> appUsersToSave = List.of(AppUserGenerator.generateAppUserWithId(1L), AppUserGenerator.generateAppUserWithId(2L));
        when(database.saveAll(appUsersToSave)).thenReturn(appUsersToSave);

        Collection<AppUser> result = appUserService.addAppUsers(appUsersToSave);

        assertEquals(appUsersToSave, result);
        verify(database).saveAll(appUsersToSave);
    }

    @Test
    void addAppUsersMethodShouldSaveOnlyAppUsersWithUniquePhoneNumber() throws DatabaseOperationException, ServiceOperationException {
        String phoneNumber = "555666777";

        AppUser appUser1 = AppUserGenerator.generateAppUser();
        AppUser appUser2 = AppUserGenerator.generateAppUserWithPhoneNumber(phoneNumber);
        AppUser appUser3 = AppUserGenerator.generateAppUserWithPhoneNumber(phoneNumber);

        List<AppUser> appUsersToSave = List.of(appUser1, appUser2, appUser3);
        List<AppUser> savedAppUsers = List.of(appUser1, appUser2);

        when(database.saveAll(appUsersToSave)).thenReturn(savedAppUsers);

        Collection<AppUser> result = appUserService.addAppUsers(appUsersToSave);

        assertEquals(savedAppUsers, result);
        verify(database).saveAll(appUsersToSave);
    }

    @Test
    void addAppUsersMethodShouldThrowExceptionWhenNullParameterIsPassed() throws DatabaseOperationException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> appUserService.addAppUsers(null));
        assertEquals("App users cannot be null", exception.getMessage());
        verify(database, never()).saveAll(anyCollection());
    }

    @Test
    void addAppUsersMethodShouldThrowExceptionWhenErrorOccurDuringAddingAppUsersToDatabase() throws DatabaseOperationException {
        List<AppUser> appUsers = List.of(AppUserGenerator.generateAppUser(), AppUserGenerator.generateAppUser());
        doThrow(DatabaseOperationException.class).when(database).saveAll(appUsers);

        assertThrows(ServiceOperationException.class, () -> appUserService.addAppUsers(appUsers));
        verify(database).saveAll(appUsers);
    }

    @Test
    void shouldGetFirstPageOfAppUsers() throws DatabaseOperationException, ServiceOperationException {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(1L, "1990-01-01");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(2L, "1993-01-01");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(3L, "1991-01-01");
        AppUser appUser4 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(4L, "1994-01-01");
        AppUser appUser5 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(5L, "1992-01-01");
        AppUser appUser6 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(6L, "1989-01-01");

        List<AppUser> appUsersToSave = List.of(appUser1, appUser2, appUser3, appUser4, appUser5, appUser6);
        List<AppUser> appUsersToReceive = List.of(appUser6, appUser1, appUser3, appUser5, appUser2);

        Page<AppUser> receivedAppUsers = new PageImpl<>(appUsersToReceive);

        when(database.saveAll(appUsersToSave)).thenReturn(appUsersToSave);
        when(database.getAppUsers()).thenReturn(receivedAppUsers);

        Collection<AppUser> saveResult = appUserService.addAppUsers(appUsersToSave);
        Page<AppUser> result = appUserService.getFirstPageOfAppUsers();

        assertEquals(appUsersToSave, saveResult);
        assertEquals(receivedAppUsers, result);

        verify(database).saveAll(appUsersToSave);
        verify(database).getAppUsers();
    }

    @Test
    void getFirstPageOfAppUsersMethodShouldThrowExceptionWhenErrorOccurDuringGettingPageOfAppUsers() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).getAppUsers();

        assertThrows(ServiceOperationException.class, () -> appUserService.getFirstPageOfAppUsers());
        verify(database).getAppUsers();
    }

    @Test
    void shouldGetAppUsers() throws DatabaseOperationException, ServiceOperationException {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(1L, "1990-01-01");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(2L, "1993-01-01");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(3L, "1991-01-01");
        AppUser appUser4 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(4L, "1994-01-01");
        AppUser appUser5 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(5L, "1992-01-01");
        AppUser appUser6 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(6L, "1989-01-01");

        List<AppUser> appUsersToSave = List.of(appUser1, appUser2, appUser3, appUser4, appUser5, appUser6);
        List<AppUser> appUsersFirstPage = List.of(appUser6, appUser1, appUser3, appUser5, appUser2);
        List<AppUser> appUsersSecondPage = List.of(appUser4);

        Page<AppUser> firstPage = new PageImpl<>(appUsersFirstPage);
        Page<AppUser> secondPage = new PageImpl<>(appUsersSecondPage);

        when(database.saveAll(appUsersToSave)).thenReturn(appUsersToSave);
        when(database.getAppUsers(1)).thenReturn(firstPage);
        when(database.getAppUsers(2)).thenReturn(secondPage);

        Collection<AppUser> saveResult = appUserService.addAppUsers(appUsersToSave);
        Page<AppUser> result1 = appUserService.getAppUsers(1);
        Page<AppUser> result2 = appUserService.getAppUsers(2);

        assertEquals(appUsersToSave, saveResult);
        assertEquals(firstPage, result1);
        assertEquals(secondPage, result2);

        verify(database).saveAll(appUsersToSave);
        verify(database).getAppUsers(1);
        verify(database).getAppUsers(2);
    }

    @Test
    void getAppUserMethodShouldThrowExceptionWhenPageNumberIsNull() throws DatabaseOperationException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> appUserService.getAppUsers(null));
        assertEquals("Attempt to get page of app users without providing page number", exception.getMessage());
        verify(database, never()).getAppUsers(null);
    }

    @Test
    void getAppUsersMethodShouldThrowExceptionWheErrorOccurDuringGettingAppUsers() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).getAppUsers(1);
        assertThrows(ServiceOperationException.class, () -> appUserService.getAppUsers(1));
        verify(database).getAppUsers(1);
    }

    @Test
    void shouldGetAppUserById() throws DatabaseOperationException, ServiceOperationException {
        AppUser appUser = AppUserGenerator.generateAppUserWithId(1L);
        when(database.getById(1L)).thenReturn(Optional.of(appUser));

        Optional<AppUser> result = appUserService.getAppUserById(1L);

        assertEquals(Optional.of(appUser), result);
        verify(database).getById(1L);
    }

    @Test
    void getAppUserByIdMethodShouldThrowExceptionWhenNullIdIsPassed() throws DatabaseOperationException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> appUserService.getAppUserById(null));
        assertEquals("Id cannot be null", exception.getMessage());
        verify(database, never()).getById(null);
    }

    @Test
    void getAppUserByIdShouldThrowExceptionWhenUnexpectedErrorOccur() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).getById(1L);
        assertThrows(ServiceOperationException.class, () -> appUserService.getAppUserById(1L));
        verify(database).getById(1L);
    }

    @Test
    void shouldGetAppUsersByLastName() throws DatabaseOperationException, ServiceOperationException {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithLastName("Kowalski");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithLastName("Kowalczyk");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithLastName("Nowak");

        List<AppUser> appUsers = List.of(appUser1, appUser2, appUser3);
        List<AppUser> filteredAppUsers = List.of(appUser1, appUser2);
        String lastName = "kow";

        when(database.saveAll(appUsers)).thenReturn(appUsers);
        when(database.getByLastName(lastName)).thenReturn(filteredAppUsers);

        Collection<AppUser> saveResult = appUserService.addAppUsers(appUsers);
        Collection<AppUser> result = appUserService.getAppUsersByLastName(lastName);

        assertEquals(appUsers, saveResult);
        assertEquals(filteredAppUsers, result);
        verify(database).saveAll(appUsers);
        verify(database).getByLastName(lastName);
    }

    @Test
    void getAppUsersByLastNameMethodShouldThrowExceptionWhenLastNameIsNull() throws DatabaseOperationException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> appUserService.getAppUsersByLastName(null));
        assertEquals("Last name cannot be null.", exception.getMessage());
        verify(database, never()).getByLastName(null);
    }

    @Test
    void getAppUserByLastNameShouldThrowExceptionWhenUnexpectedErrorOccur() throws DatabaseOperationException {
        String lastName = "Kowalski";
        doThrow(DatabaseOperationException.class).when(database).getByLastName(lastName);
        assertThrows(ServiceOperationException.class, () -> appUserService.getAppUsersByLastName(lastName));
        verify(database).getByLastName(lastName);
    }

    @Test
    void shouldGetOldestAppUser() throws DatabaseOperationException, ServiceOperationException {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithSpecificBirthDate("1985-12-09");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithSpecificBirthDate("1981-11-09");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithSpecificBirthDate("1988-12-23");

        PageImpl<AppUser> requestedAppUsers = new PageImpl<>(List.of(appUser1, appUser2, appUser3));
        Optional<AppUser> oldestAppUser = Optional.of(appUser2);

        when(database.getAppUsers()).thenReturn(requestedAppUsers);
        when(database.getOldestAppUserWithPhoneNumber()).thenReturn(oldestAppUser);

        Page<AppUser> resultOfRequestingPageOfUsers = appUserService.getFirstPageOfAppUsers();
        Optional<AppUser> resultOfOldestAppUser = appUserService.getOldestAppUser();

        assertEquals(requestedAppUsers, resultOfRequestingPageOfUsers);
        assertEquals(oldestAppUser, resultOfOldestAppUser);

        verify(database).getAppUsers();
        verify(database).getOldestAppUserWithPhoneNumber();
    }

    @Test
    void getOldestAppUserMethodShouldReturnEmptyOptionalWhenAnyUserHasNotPhoneNumber() throws DatabaseOperationException, ServiceOperationException {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithSpecificBirthDateAndWithoutPhoneNumber("1985-12-09");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithSpecificBirthDateAndWithoutPhoneNumber("1981-11-09");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithSpecificBirthDateAndWithoutPhoneNumber("1988-12-23");

        PageImpl<AppUser> requestedAppUsers = new PageImpl<>(List.of(appUser1, appUser2, appUser3));

        when(database.getAppUsers()).thenReturn(requestedAppUsers);
        when(database.getOldestAppUserWithPhoneNumber()).thenReturn(Optional.empty());

        Page<AppUser> resultOfRequestingPageOfUsers = appUserService.getFirstPageOfAppUsers();
        Optional<AppUser> resultOfOldestAppUser = appUserService.getOldestAppUser();

        assertEquals(requestedAppUsers, resultOfRequestingPageOfUsers);
        assertEquals(Optional.empty(), resultOfOldestAppUser);

        verify(database).getAppUsers();
        verify(database).getOldestAppUserWithPhoneNumber();
    }

    @Test
    void getOldestAppUserMethodShouldThrowExceptionWhenUnexpectedErrorOccur() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).getOldestAppUserWithPhoneNumber();
        assertThrows(ServiceOperationException.class, () -> appUserService.getOldestAppUser());
        verify(database).getOldestAppUserWithPhoneNumber();
    }

    @Test
    void shouldDeleteAppUserById() throws DatabaseOperationException, ServiceOperationException {
        doNothing().when(database).delete(1L);
        appUserService.deleteById(1L);
        verify(database).delete(1L);
    }

    @Test
    void deleteByIdMethodShouldThrowExceptionWhenIdIsNull() throws DatabaseOperationException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> appUserService.deleteById(null));
        assertEquals("Id cannot be null", exception.getMessage());
        verify(database, never()).delete(null);
    }

    @Test
    void deleteByIdMethodShouldThrowExceptionWhenUnexpectedErrorOccur() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).delete(1L);
        assertThrows(ServiceOperationException.class, () -> appUserService.deleteById(1L));
        verify(database).delete(1L);
    }

    @Test
    void shouldDeleteAllAppUsers() throws ServiceOperationException, DatabaseOperationException {
        doNothing().when(database).deleteAll();
        appUserService.deleteAll();
        verify(database).deleteAll();
    }

    @Test
    void deleteAllMethodShouldThrowExceptionWhenUnexpectedErrorOccurDuringDeletingAllAppUsers() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).deleteAll();
        assertThrows(ServiceOperationException.class, () -> appUserService.deleteAll());
        verify(database).deleteAll();
    }

    @Test
    void shouldCountAppUsers() throws DatabaseOperationException, ServiceOperationException {
        when(database.count()).thenReturn(15L);
        long result = appUserService.count();

        assertEquals(15L, result);
        verify(database).count();
    }

    @Test
    void countMethodShouldThrowExceptionWhenUnexpectedErrorOccurDuringGettingNumberOfAppUsers() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).count();
        assertThrows(ServiceOperationException.class, () -> appUserService.count());
        verify(database).count();
    }

    @Test
    void shouldExistByIdMethodReturnTrueWhenAppUserExists() throws DatabaseOperationException, ServiceOperationException {
        when(database.existsById(1L)).thenReturn(true);
        boolean result = appUserService.existsById(1L);

        assertTrue(result);
        verify(database).existsById(1L);
    }

    @Test
    void shouldExistByIdMethodReturnFalseWhenAppUserExists() throws DatabaseOperationException, ServiceOperationException {
        when(database.existsById(1L)).thenReturn(false);
        boolean result = appUserService.existsById(1L);

        assertFalse(result);
        verify(database).existsById(1L);
    }

    @Test
    void existByIdMethodShouldThrowExceptionWhenNullIsPassed() throws DatabaseOperationException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> appUserService.existsById(null));
        assertEquals("Id cannot be null", exception.getMessage());
        verify(database, never()).existsById(null);
    }

    @Test
    void existByIdMethodShouldThrowExceptionWhenUnexpectedErrorOccurDuringCheckingWhetherAppUSerExists() throws DatabaseOperationException {
        doThrow(DatabaseOperationException.class).when(database).existsById(1L);
        assertThrows(ServiceOperationException.class, () -> appUserService.existsById(1L));
        verify(database).existsById(1L);
    }
}
