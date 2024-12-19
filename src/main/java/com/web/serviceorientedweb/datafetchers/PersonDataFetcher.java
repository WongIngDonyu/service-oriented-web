package com.web.serviceorientedweb.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.web.serviceorientedweb.services.PersonService;
import org.web.transportapi.dto.PersonDto;
import org.web.transportapi.dto.PersonViewDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@DgsComponent
public class PersonDataFetcher {

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @DgsQuery
    public List<PersonDto> allPersons() {
        return personService.getAllPersons();
    }

    @DgsQuery
    public PersonViewDto personById(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        PersonViewDto personViewDto = personService.getPersonById(id);
        return personViewDto;
    }

    @DgsMutation
    public PersonDto createPerson(DataFetchingEnvironment dataFetchingEnvironment) {
        Map<String, Object> personInput = dataFetchingEnvironment.getArgument("person");
        PersonViewDto newPerson = new PersonViewDto(
                (String) personInput.get("firstName"),
                (String) personInput.get("lastName"),
                (String) personInput.get("patronymic"),
                (String) personInput.get("email"),
                (String) personInput.get("phone"),
                (String) personInput.get("raceName")
        );
        PersonViewDto createdPerson = personService.createPerson(newPerson);
        return new PersonDto(createdPerson.getFirstName(), createdPerson.getLastName(), createdPerson.getPhone()
        );
    }

    @DgsMutation
    public String deletePerson(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        personService.deletePerson(id);
        return "Person with ID " + id + " was deleted";
    }
}



