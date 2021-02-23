package com.sistema.managementapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.managementapi.dto.mapper.PersonMapper;
import com.sistema.managementapi.dto.request.PersonDTO;
import com.sistema.managementapi.dto.response.MessageResponseDTO;
import com.sistema.managementapi.entity.Person;
import com.sistema.managementapi.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {
    private PersonRespository personRespository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO) {
        Person personToSave = personMapper.toModel(personDTO);
        Person savedPerson = personRespository.save(personToSave);
        return createMessageResponse(savedPerson.getId(), "Created person with id: ");
    }

    public List<PersonDTO> listAll() {
        List<Person> list = personRespository.findAll();
        return list.stream().map(personMapper::toDTO).collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = verifyIfExist(id);
        return personMapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException {
        Person person = verifyIfExist(id);
        personRespository.delete(person);
    }

    private Person verifyIfExist(Long id) throws PersonNotFoundException {
        return personRespository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    public MessageResponseDTO update(PersonDTO personDTO, Long id) throws PersonNotFoundException {
        verifyIfExist(id);
        Person personToUpdate = personMapper.toModel(personDTO);
        Person updatePerson = personRespository.save(personToUpdate);
        return createMessageResponse(updatePerson.getId(), "updated person with id: ");

    }

    private MessageResponseDTO createMessageResponse(Long id, String s) {
        return MessageResponseDTO.builder().message(s + id).build();
    }
}
