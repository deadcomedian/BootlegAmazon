package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.StatusEntity;

import java.time.LocalDate;

public interface StatusRepository extends CrudRepository<StatusEntity, Integer> {

    @Query("select s from StatusEntity s where s.name = ?1")
    Iterable<StatusEntity> findAllByStatusName(String statusName);

    @Query("select s from StatusEntity s where s.name = ?1")
    StatusEntity findByStatusName(String statusName);
}
