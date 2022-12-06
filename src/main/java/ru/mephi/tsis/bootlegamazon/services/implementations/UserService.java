package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserAuth;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.services.MyUserPrincipal;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserAuthRepository userAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth user = userAuthRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new  MyUserPrincipal(user);
    }
}

