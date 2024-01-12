package dev.kwolszczak.peopledb.web.controller;

import dev.kwolszczak.peopledb.biz.model.Person;
import dev.kwolszczak.peopledb.data.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private PersonRepository personRepository;

    public PeopleController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @ModelAttribute("people") //to wstrzykuje do modelu       // model.addAttribute("people", personRepository.findAll());
    public Iterable<Person> getPeople() {
        return personRepository.findAll();
    }

    @GetMapping()
    public String showPeoplePage(Model model) {
        return "people.html"; //mapowanie na strone html po strzale http GET na localhost/people
    }
}
