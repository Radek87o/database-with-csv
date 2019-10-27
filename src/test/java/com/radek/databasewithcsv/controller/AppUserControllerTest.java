package com.radek.databasewithcsv.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radek.databasewithcsv.generators.AppUserGenerator;
import com.radek.databasewithcsv.model.AppUser;
import com.radek.databasewithcsv.service.AppUserService;
import com.radek.databasewithcsv.service.ServiceOperationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class AppUserControllerTest {

    @MockBean
    private AppUserService appUserService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnAllAppUsers() throws Exception {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(1L, "1990-01-01");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(2L, "1993-01-01");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(3L, "1991-01-01");
        AppUser appUser4 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(5L, "1992-01-01");
        AppUser appUser5 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(6L, "1989-01-01");

        Page<AppUser> firstPage = new PageImpl<>(List.of(appUser5, appUser1, appUser3, appUser4, appUser2));

        when(appUserService.getFirstPageOfAppUsers()).thenReturn(firstPage);

        String url = "/appUsers";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(firstPage)));

        verify(appUserService).getFirstPageOfAppUsers();
    }

    @Test
    public void shouldReturnEmptyListOfAppUsersWhenThereAreNoInvoicesInTheDatabase() throws Exception {
        List<AppUser> appUsers = new ArrayList<>();
        PageImpl<AppUser> emptyPage = new PageImpl<>(appUsers);
        when(appUserService.getFirstPageOfAppUsers()).thenReturn(emptyPage);

        String url = "/appUsers";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(appUserService).getFirstPageOfAppUsers();
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAllAppUsersWhenSomethingWentWrongOnServer() throws Exception {
        when(appUserService.getFirstPageOfAppUsers()).thenThrow(new ServiceOperationException());

        String url = "/appUsers";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).getFirstPageOfAppUsers();
    }

    @Test
    void shouldReturnPageOfAppUsers() throws Exception {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(1L, "1990-01-01");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(2L, "1993-01-01");
        AppUser appUser3 = AppUserGenerator.generateAppUserWithSpecificIdAndBirthDate(6L, "1989-01-01");

        Page<AppUser> firstPage = new PageImpl<>(List.of(appUser3, appUser1, appUser2));

        when(appUserService.getAppUsers(1)).thenReturn(firstPage);

        String url = String.format("/appUsers/byPageNumber?pageNumber=%d", 1);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(firstPage)));

        verify(appUserService).getAppUsers(1);
    }

    @Test
    void shouldReturnOkStatusDuringGettingAppUsersByPageNumberWhenPageWithSpecificNumberDoesNotExist() throws Exception {
        Integer number = 5;
        when(appUserService.getAppUsers(number)).thenReturn(Page.empty());

        String url = String.format("/appUsers/byPageNumber?pageNumber=%d", number);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(appUserService).getAppUsers(number);
    }

    @Test
    void shouldReturnBadRequestDuringGettingAppUsersByPageNumberWhenPageIsNull() throws Exception {
        String url = "/appUsers/byPageNumber";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAppUsersByPageNumberWhenUnexpectedErrorOccur() throws Exception {
        when(appUserService.getAppUsers(1)).thenThrow(new ServiceOperationException());

        String url = String.format("/appUsers/byPageNumber?pageNumber=%d", 1);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).getAppUsers(1);
    }

    @Test
    void shouldReturnAppUserById() throws Exception {
        AppUser appUser = AppUserGenerator.generateAppUserWithId(10L);
        when(appUserService.getAppUserById(appUser.getId())).thenReturn(Optional.of(appUser));

        String url = String.format("/appUsers/%d", appUser.getId());

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(appUser)));

        verify(appUserService).getAppUserById(appUser.getId());
    }

    @Test
    void shouldReturnNotFoundStatusWhenAppUserWithProvidedIdDoesNotExist() throws Exception {
        when(appUserService.getAppUserById(1L)).thenReturn(Optional.empty());

        String url = String.format("/appUsers/%d", 1L);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(appUserService).getAppUserById(1L);
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAppUserByIdWhenUnexpectedErrorOccur() throws Exception {
        when(appUserService.getAppUserById(10L)).thenThrow(new ServiceOperationException());

        String url = String.format("/appUsers/%d", 10);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).getAppUserById(10L);
    }

    @Test
    void shouldReturnAppUsersByLastName() throws Exception {
        AppUser appUser1 = AppUserGenerator.generateAppUserWithLastNameAndBirthDate("Kowalski", "1990-01-01");
        AppUser appUser2 = AppUserGenerator.generateAppUserWithLastNameAndBirthDate("Kowalczyk", "1989-01-01");

        String searchResult = "kow";
        List<AppUser> searchedAppUsers = List.of(appUser2, appUser1);

        when(appUserService.getAppUsersByLastName(searchResult)).thenReturn(searchedAppUsers);

        String url = String.format("/appUsers/byLastName?lastName=%s", searchResult);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(searchedAppUsers)));

        verify(appUserService).getAppUsersByLastName(searchResult);
    }

    @Test
    void shouldReturnBadRequestDuringGettingAppUserByLastNameWhenLastNameIsNull() throws Exception {
        String url = "/appUsers/byLastName";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingAppUserByLastNameWhenUnexpectedErrorOccur() throws Exception {
        String searchName = "kowalski";
        when(appUserService.getAppUsersByLastName(searchName)).thenThrow(new ServiceOperationException());

        String url = String.format("/appUsers/byLastName?lastName=%s", searchName);

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).getAppUsersByLastName(searchName);
    }

    @Test
    void shouldReturnOldestAppUser() throws Exception {
        AppUser oldestAppUser = AppUserGenerator.generateAppUserWithSpecificBirthDate("1950-01-01");

        when(appUserService.getOldestAppUser()).thenReturn(Optional.of(oldestAppUser));

        String url = "/appUsers/oldest";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(oldestAppUser)));

        verify(appUserService).getOldestAppUser();
    }

    @Test
    void shouldReturnEmptyAppUserWhenMissingAppUsersWithPhoneNumbers() throws Exception {
        when(appUserService.getOldestAppUser()).thenReturn(Optional.empty());

        String url = "/appUsers/oldest";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(appUserService).getOldestAppUser();
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingOldestAppUserWhenUnexpectedErrorOccur() throws Exception {
        when(appUserService.getOldestAppUser()).thenThrow(new ServiceOperationException());

        String url = "/appUsers/oldest";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).getOldestAppUser();
    }

    @Test
    void shouldDeleteAppUserById() throws Exception {
        AppUser appUser = AppUserGenerator.generateAppUserWithId(10L);
        when(appUserService.existsById(appUser.getId())).thenReturn(true);
        doNothing().when(appUserService).deleteById(appUser.getId());

        String url = String.format("/appUsers/%d", appUser.getId());

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(appUserService).existsById(appUser.getId());
        verify(appUserService).deleteById(appUser.getId());
    }

    @Test
    void shouldDeleteAppUserMethodReturnNotFoundStatusWhenAppUserNotExists() throws Exception {
        long id = 10L;
        when(appUserService.existsById(id)).thenReturn(false);
        doNothing().when(appUserService).deleteById(id);

        String url = String.format("/appUsers/%d", id);

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(appUserService).existsById(id);
        verify(appUserService, never()).deleteById(id);
    }

    @Test
    void shouldReturnInternalServerErrorDuringDeletingAppUserByIdWhenUnexpectedErrorOccur() throws Exception {
        Long appUserId = 1L;
        when(appUserService.existsById(appUserId)).thenReturn(true);
        doThrow(ServiceOperationException.class).when(appUserService).deleteById(appUserId);

        String url = String.format("/appUsers/%d", appUserId);

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).existsById(appUserId);
        verify(appUserService).deleteById(appUserId);
    }

    @Test
    void shouldDeleteAllAppUser() throws Exception {
        doNothing().when(appUserService).deleteAll();

        String url = "/appUsers";

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(appUserService).deleteAll();
    }

    @Test
    void shouldReturnInternalServerErrorDuringDeletingAllUsersWhenUnexpectedErrorOccur() throws Exception {
        doThrow(ServiceOperationException.class).when(appUserService).deleteAll();

        String url = "/appUsers";

        mockMvc.perform(delete(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).deleteAll();
    }

    @Test
    void shouldCountAppUser() throws Exception {
        when(appUserService.count()).thenReturn(10L);

        String url = "/appUsers/count";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(appUserService).count();
    }

    @Test
    void shouldReturnInternalServerErrorDuringGettingNumberOfAppUsers() throws Exception {
        when(appUserService.count()).thenThrow(new ServiceOperationException());

        String url = "/appUsers/count";

        mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        verify(appUserService).count();
    }
}