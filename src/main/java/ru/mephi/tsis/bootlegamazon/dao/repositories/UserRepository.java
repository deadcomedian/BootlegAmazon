package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    @Query("select u from UserEntity u where u.id = ?1 and u.active = true")
    @Override
    Optional<UserEntity> findById(Integer id);

    @Query("select u from UserEntity u where u.active = true")
    List<UserEntity> findAll();

    @Query("select u from UserEntity u where u.login = ?1")
    Optional<UserEntity> findByLogin(String userLogin);

    @Query("select u from UserEntity u where u.phone = ?1")
    Optional<UserEntity> findByPhone(String userPhone);

}
