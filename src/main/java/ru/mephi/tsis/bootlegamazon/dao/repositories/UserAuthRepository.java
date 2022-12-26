package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {
    UserAuth findByUsername(String username);

}


