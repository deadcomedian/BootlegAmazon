package ru.mephi.tsis.bootlegamazon.dao.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Integer> {

    @Query("select c from CategoryEntity c where c.id = ?1 and c.active = true")
    Optional<CategoryEntity> findByCategoryId(Integer id);
    @Query("select c from CategoryEntity c where c.name = ?1 and c.active = true")
    Optional<CategoryEntity> findByName(String categoryName);

//    @Override
//    @Query("select c from CategoryEntity c where c.active = true")
//    Iterable<CategoryEntity> findAll();

    @Query("select c from CategoryEntity c where c.active = true")
    Page<CategoryEntity> findAll(Pageable pageable);
}
