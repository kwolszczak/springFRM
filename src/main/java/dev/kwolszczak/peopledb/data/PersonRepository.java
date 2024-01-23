package dev.kwolszczak.peopledb.data;

import dev.kwolszczak.peopledb.biz.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person,Long> {

    @Query(nativeQuery = true,value = "select photo_filename from person where id = :id")
    public String findFileNameById(@Param("id")Long id);
}
