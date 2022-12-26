package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {
    @Query("select u from UserEntity u where u.id = ?1 and u.active = true")
    @Override
    Optional<UserEntity> findById(Integer id);

    @Query("select u from UserEntity u where u.active = true")
    Page<UserEntity> findAll(Pageable pageable);

    @Query("select u from UserEntity u where u.login = ?1")
    Optional<UserEntity> findByLogin(String userLogin);

    @Query("select u from UserEntity u where u.phone = ?1")
    Optional<UserEntity> findByPhone(String userPhone);

}
