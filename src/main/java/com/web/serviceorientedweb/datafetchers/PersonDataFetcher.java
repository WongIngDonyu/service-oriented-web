package com.web.serviceorientedweb.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.web.serviceorientedweb.models.Person;
import com.web.serviceorientedweb.services.PersonService;
import com.web.serviceorientedweb.services.dtos.PersonDto;
import com.web.serviceorientedweb.services.dtos.PersonViewDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
        List<Person> persons = personService.getAllPersons();
        List<PersonDto> personDtos = new ArrayList<>();
        for (Person person : persons) {
            PersonDto personDto = new PersonDto(person.getFirstName(), person.getLastName(), person.getPhone());
            personDtos.add(personDto);
        }
        return personDtos;
    }

    @DgsQuery
    public PersonViewDto personById(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        Person person = personService.getPersonById(id);
        return new PersonViewDto(
                person.getFirstName(),
                person.getLastName(),
                person.getPatronymic(),
                person.getEmail(),
                person.getPhone(),
                person.getRace().getRaceName()
        );
    }

    @DgsMutation
    public PersonDto createPerson(DataFetchingEnvironment dataFetchingEnvironment) {
        Map<String, Object> personInput = dataFetchingEnvironment.getArgument("person");
        PersonViewDto newPerson = personService.createPerson(new PersonViewDto(
                (String) personInput.get("firstName"),
                (String) personInput.get("lastName"),
                (String) personInput.get("patronymic"),
                (String) personInput.get("email"),
                (String) personInput.get("phone"),
                (String) personInput.get("raceName")
        ));
        return new PersonDto(newPerson.getFirstName(), newPerson.getLastName(), newPerson.getPhone());
    }

    @DgsMutation
    public String deletePerson(DataFetchingEnvironment dataFetchingEnvironment) {
        UUID id = UUID.fromString(dataFetchingEnvironment.getArgument("id"));
        personService.deletePerson(id);
        return "Person with ID " + id + " was deleted";
    }
}
