package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartEntity;

import java.util.Optional;

public interface CartRepository extends CrudRepository<CartEntity, Integer> {
    //R'n'D & change
    @Query("select c from CartEntity c where c.userId = ?1 and c.active = true")
    Optional<CartEntity> findByUserId(String userId);
}
