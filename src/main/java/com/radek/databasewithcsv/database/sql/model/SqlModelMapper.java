package com.radek.databasewithcsv.database.sql.model;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SqlModelMapper {

    @Mapping(target = "withFirstName", source = "firstName")
    @Mapping(target = "withLastName", source = "lastName")
    @Mapping(target = "withBirthDate", source = "birthDate")
    @Mapping(target = "withPhoneNumber", source = "phoneNumber")
    AppUser toSqlAppUser(com.radek.databasewithcsv.model.AppUser appUser);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withFirstName", source = "firstName")
    @Mapping(target = "withLastName", source = "lastName")
    @Mapping(target = "withBirthDate", source = "birthDate")
    @Mapping(target = "withPhoneNumber", source = "phoneNumber")
    com.radek.databasewithcsv.model.AppUser toAppUser(AppUser sqlAppUser);
}
