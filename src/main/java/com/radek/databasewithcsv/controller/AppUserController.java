package com.radek.databasewithcsv.controller;

import com.radek.databasewithcsv.csvhelper.CsvCustomParsingException;
import com.radek.databasewithcsv.csvhelper.CustomCsvReader;
import com.radek.databasewithcsv.model.AppUser;
import com.radek.databasewithcsv.service.AppUserService;
import com.radek.databasewithcsv.service.ServiceOperationException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/appUsers")
public class AppUserController {

    private Logger log = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping(produces = "application/json", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestParam(required = false) MultipartFile file) throws IOException, CsvCustomParsingException, ServiceOperationException {
        if (file == null) {
            log.error("Missing file parameter");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing file parameter");
        }
        if (!file.getContentType().equals("text/csv")) {
            log.error("Invalid file format");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file format");
        }
        List<AppUser> appUsers = CustomCsvReader.appUserBuilder(file.getBytes());
        if (appUsers.isEmpty()) {
            log.error("Attempt to add file without any correct app user");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to add file without any correct app user");
        }
        log.info("Saving app user list to database");
        Collection<AppUser> responseBody = appUserService.addAppUsers(appUsers);
        return ResponseHelper.createJsonCreatedResponse(responseBody);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getFirstPage() throws ServiceOperationException {
        log.info("Getting first page of sorted app users");
        Page<AppUser> responseBody = appUserService.getFirstPageOfAppUsers();
        return ResponseHelper.createJsonOkResponse(responseBody);
    }

    @GetMapping(value = "/byPageNumber", produces = "application/json")
    public ResponseEntity<?> getAppUsers(@RequestParam(required = false, name = "pageNumber") Integer pageNumber) throws ServiceOperationException {
        if (pageNumber == null) {
            log.error("Attempt to provide null page number");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to provide null page number");
        }
        log.info("Getting page number: {} of sorted app users", pageNumber);
        Page<AppUser> responseBody = appUserService.getAppUsers(pageNumber);
        if (pageNumber > responseBody.getTotalPages()) {
            log.info("Page for provided page number: {} doesn't exists", pageNumber);
        }
        return ResponseHelper.createJsonOkResponse(responseBody);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws ServiceOperationException {
        Optional<AppUser> appUser = appUserService.getAppUserById(id);
        if (appUser.isEmpty()) {
            log.error("Attempt to get app user by id that does not exist in database.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "App user for provided id doesn't exist");
        }
        return ResponseHelper.createJsonOkResponse(appUser);
    }

    @GetMapping(value = "/byLastName", produces = "application/json")
    public ResponseEntity<?> getByLastName(@RequestParam(required = false, name = "lastName") String lastName) throws ServiceOperationException {
        if (lastName == null) {
            log.error("Attempt to get app users without providing last name parameter.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt to get app users by last name without providing any name.");
        }
        Collection<AppUser> responseBody = appUserService.getAppUsersByLastName(lastName);
        return ResponseHelper.createJsonOkResponse(responseBody);
    }

    @GetMapping(value = "/oldest", produces = "application/json")
    public ResponseEntity<?> getOldestAppUser() throws ServiceOperationException {
        Optional<AppUser> responseBody = appUserService.getOldestAppUser();
        return ResponseHelper.createJsonOkResponse(responseBody);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws ServiceOperationException {
        if (!appUserService.existsById(id)) {
            log.error("Attempt to delete non-existing app user");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "App user with provided id doesn't exist");
        }
        appUserService.deleteById(id);
        log.debug("Deleted app user with id {}.", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteAll() throws ServiceOperationException {
        appUserService.deleteAll();
        log.debug("Deleted all app users from db");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<?> count() throws ServiceOperationException {
        return ResponseHelper.createJsonOkResponse(appUserService.count());
    }
}
