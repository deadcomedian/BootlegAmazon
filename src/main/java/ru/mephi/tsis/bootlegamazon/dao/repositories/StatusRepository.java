package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.StatusEntity;

public interface StatusRepository extends CrudRepository<StatusEntity, Integer> {

}
