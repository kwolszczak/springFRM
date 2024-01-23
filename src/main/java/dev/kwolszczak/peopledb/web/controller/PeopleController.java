package dev.kwolszczak.peopledb.web.controller;

import dev.kwolszczak.peopledb.biz.model.Person;
import dev.kwolszczak.peopledb.data.FileStorageRepository;
import dev.kwolszczak.peopledb.data.PersonRepository;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FileStorageRepository fileStorageRepository;

    //when you have @Autowired constructor is not needed, dependancy injection is done automatically
  /*  public PeopleController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }*/



   @PostMapping(params = "delete=true")
    public String deletePerson(@RequestParam(required = false) List<Long> selections){
        personRepository.deleteAllById(selections);
        return "redirect:people";
    }

   @PostMapping(params = "edit=true")
    public String editPerson(@RequestParam(required = false) List<Long> selections, Model model){

       Optional<Person> person = personRepository.findById(selections.get(0));
       model.addAttribute("person", person);
       return "people";
    }

    @PostMapping(params = "del")
    public String deletePerson(@RequestParam Long del){
        personRepository.deleteById(del);
        return "redirect:people";
    }

    @ModelAttribute("person")
    public Person getPerson() {
        return new Person();
    }

    @PostMapping
    public String savePerson(@Valid Person person, Errors errors, @RequestParam("photoFilename") MultipartFile photoFile) throws IOException {
        log.info(person);
        log.info("FileName: "+photoFile.getOriginalFilename());
        log.info("File size: "+photoFile.getSize());
        log.info("errors:"+errors);
        if (!errors.hasErrors()) {
            fileStorageRepository.save(photoFile.getOriginalFilename(), photoFile.getInputStream());
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
