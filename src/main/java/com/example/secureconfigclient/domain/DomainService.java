package com.example.secureconfigclient.domain;

import com.example.secureconfigclient.domain.health.ServiceHealth;
import com.example.secureconfigclient.repository.PersonRepository;
import com.example.secureconfigclient.repository.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DomainService {

    private static final String TEMPLATE = "Hello, %s!";

    @Autowired
    PersonRepository personRepository;

    public DomainResponse serveWithName(final Long personId) {
        final Optional<Person> person = personRepository.findById(personId);
        return DomainResponse.of(String.format(TEMPLATE, person.get().getFirstName()), "OK");
    }

    /**
     * Test healthcheck.
     * @return Up id OK.
     */
    public String healthCheck() {
        ServiceHealth health = new ServiceHealth("UP");
        return health.getStatus();
    }


}
