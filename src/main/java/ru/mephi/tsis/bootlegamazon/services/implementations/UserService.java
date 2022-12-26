package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserAuth;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.RoleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserRepository;
import ru.mephi.tsis.bootlegamazon.services.MyUserPrincipal;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@Service
public class UserService implements UserDetailsService {

    UserAuthRepository userAuthRepository;

    UserRepository userRepository;

    RoleRepository roleRepository;

    @Autowired
    public UserService(UserAuthRepository userAuthRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.userAuthRepository = userAuthRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth user = userAuthRepository.findByUsername(username);
        UserEntity userEntity = userRepository.findByLogin(user.getUsername()).get();

        if (user == null || !userEntity.getActive()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new  MyUserPrincipal(user);
    }

    public UserDetails loadById(Integer id){
        UserAuth user = userAuthRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("User not found with id:" + id));
        return new MyUserPrincipal(user);
    }


    public List<UserAuth> allUsers() {
        return userAuthRepository.findAll();
    }

    public boolean createUser(String name, String phone, String login, String password) {
        if (userRepository.findByPhone(phone).isPresent()) {
            return false;
        }
        if (userRepository.findByLogin(login).isPresent()) {
            return false;
        }
        int roleId = roleRepository.findByName("Покупатель").get().getId();
        UserEntity userEntity = new UserEntity(null, name, phone, login, password, roleId, true);
        userRepository.save(userEntity);
        return true;
    }

    public void updateName(Integer id, String name) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id:" + id));
        userEntity.setName(name);
        userRepository.save(userEntity);
    }

    public void updatePassword(Integer id, String password) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id:" + id));
        userEntity.setPassword(password);
        userRepository.save(userEntity);
    }

    public void update(Integer id, String name, String password) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id:" + id));
        userEntity.setName(name);
        userEntity.setPassword(password);
        userRepository.save(userEntity);
    }

    public String getUserNameById(Integer id){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id:" + id));
        return userEntity.getName();
    }

    public String getUserLoginById(Integer id){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id:" + id));
        return userEntity.getLogin();
    }

}

