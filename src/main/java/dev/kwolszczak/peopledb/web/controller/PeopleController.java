package dev.kwolszczak.peopledb.web.controller;

import dev.kwolszczak.peopledb.biz.model.Person;
import dev.kwolszczak.peopledb.data.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    //when you have @Autowired constructor is not needed, dependancy injection is done automatically
  /*  public PeopleController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }*/



    @PostMapping(params = "delete=true")
    public String deletePerson(@RequestParam List<Long> selections){
        personRepository.deleteAllById(selections);
        return "redirect:people";
    }

    @ModelAttribute("person")
    public Person getPerson() {
        return new Person();
    }

    @PostMapping
    public String savePerson(@Valid Person person, Errors errors
    ) {
        System.out.println(person);
        if (!errors.hasErrors()) {
            personRepository.save(person);
            return "redirect:people"; //redirect - odswiezenie danyh, czysty formularz, people bez html to zasoby dynamiczne w template
        }
        return "people";
    }

    @ModelAttribute("people")
    //to wstrzykuje do modelu  (stored in model)     // model.addAttribute("people", personRepository.findAll());
    public Iterable<Person> getPeople() {
        //metody @MOdelAttribute sa uruchamiane przed Get POST ....
        return personRepository.findAll();
    }

    @GetMapping()
    //public String showPeoplePage(Model model) { //model jest potrzebny do ewentualnego recznego dodania mapowan, np model.addAttribute("people", personRepository.findAll());
    public String showPeoplePage() {
        return "people"; //mapowanie na (view) strone html po strzale http GET na localhost/people
    }
}
