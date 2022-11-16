package ru.mephi.tsis.bootlegamazon.dao.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

    @Query("select c from CategoryEntity c where c.id = ?1 and c.active = true")
    Optional<CategoryEntity> findByCategoryId(Integer id);
    @Query("select c from CategoryEntity c where c.name = ?1 and c.active = true")
    Optional<CategoryEntity> findByName(String categoryName);
}
