package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartEntity;

import java.util.Optional;

public interface CartRepository extends CrudRepository<CartEntity, Integer> {
    //R'n'D & change
    Optional<CartEntity> findByCustomerId(Integer customerId);
}
