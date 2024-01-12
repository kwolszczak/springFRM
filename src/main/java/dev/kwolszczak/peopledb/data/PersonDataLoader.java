package dev.kwolszczak.peopledb.data;

import dev.kwolszczak.peopledb.biz.model.Person;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class PersonDataLoader implements ApplicationRunner {

    private PersonRepository personRepository;

    public PersonDataLoader(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Person> people = null;
        if (personRepository.count() == 0) {
            people = List.of(
                    new Person(null, "jake", "snake", LocalDate.of(1950, 12, 12), new BigDecimal("12400"),"dummy@sample.com"),
                    new Person(null, "sarah", "smith", LocalDate.of(1968, 1, 6), new BigDecimal("122400"),"dummy@sample.com"),
                    new Person(null, "johny", "jackson", LocalDate.of(1970, 7, 11), new BigDecimal("57400"),"dummy@sample.com"),
                    new Person(null, "bobby", "kim", LocalDate.of(1980, 5, 1), new BigDecimal("89700"),"dummy@sample.com")
            );
        }
        personRepository.saveAll(people);
    }
}
