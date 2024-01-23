package dev.kwolszczak.peopledb.web.controller;

import dev.kwolszczak.peopledb.biz.model.Person;
import dev.kwolszczak.peopledb.biz.service.PersonService;
import dev.kwolszczak.peopledb.data.FileStorageRepository;
import dev.kwolszczak.peopledb.data.PersonRepository;
import dev.kwolszczak.peopledb.exception.StorageException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FileStorageRepository fileStorageRepository;
    @Autowired
    private PersonService personService;

    //when you have @Autowired constructor is not needed, dependancy injection is done automatically
  /*  public PeopleController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }*/


    @ModelAttribute("person")
    public Person getPerson() {
        return new Person();
    }

    @ModelAttribute("people")
    //to wstrzykuje do modelu  (stored in model)     // model.addAttribute("people", personRepository.findAll());
    public Iterable<Person> getPeople() {
        //metody @MOdelAttribute sa uruchamiane przed Get POST ....
        return personRepository.findAll();
    }

    @PostMapping(params = "edit=true")
    public String editPerson(@RequestParam(required = false) List<Long> selections, Model model) {

        Optional<Person> person = personRepository.findById(selections.get(0));
        model.addAttribute("person", person);
        return "people";
    }

    @PostMapping(params = "delete=true")
    public String deletePerson(@RequestParam(required = false) List<Long> selections) {
        personService.deleteAllById(selections);
        return "redirect:people";
    }

    @PostMapping(params = "del")
    public String deletePerson(@RequestParam("del") Long id) {
        personService.deleteById(id);
        return "redirect:people";
    }

    @PostMapping
    public String savePerson(Model model, @Valid Person person, Errors errors, @RequestParam("photoFilename") MultipartFile photoFile) throws IOException {
        log.info(person);
        log.info("FileName: " + photoFile.getOriginalFilename());
        log.info("File size: " + photoFile.getSize());
        log.info("errors:" + errors);
        if (!errors.hasErrors()) {
            try {
                personService.save(person, photoFile.getInputStream());
                return "redirect:people"; //redirect - odswiezenie danyh, czysty formularz, people bez html to zasoby dynamiczne w template
            } catch (StorageException e) {
                model.addAttribute("errorMsg", "Couldn't save data :( :(");
                return "people";
            }
        }
        return "people";
    }

    @GetMapping()
    //public String showPeoplePage(Model model) { //model jest potrzebny do ewentualnego recznego dodania mapowan, np model.addAttribute("people", personRepository.findAll());
    public String showPeoplePage() {
        return "people"; //mapowanie na (view) strone html po strzale http GET na localhost/people
    }

    @GetMapping("/images/{resource}")
    public ResponseEntity<Resource> getImages(@PathVariable("resource") String resource) {
        String dispo = "attachment; filename=%s";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, format(dispo, resource))
                .body(fileStorageRepository.findByName(resource));
    }
}
