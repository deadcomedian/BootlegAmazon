package ru.mephi.tsis.bootlegamazon.services.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.RoleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.RoleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.RoleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.UserNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.CustomerProfile;
import ru.mephi.tsis.bootlegamazon.models.Role;
import ru.mephi.tsis.bootlegamazon.services.CustomerProfileService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomerProfileServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void createCustomerProfile(String customerName, String customerPhone, String customerLogin, String customerPassword, Role role) {
        UserEntity userEntity = new UserEntity(
                null,
                customerName,
                customerPhone,
                customerLogin,
                customerPassword,
                role.getRoleId(),
                true
        );
        userRepository.save(userEntity);
    }

    @Override
    public void deleteCustomerProfile(Integer id) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Could not find user with id: " + id));
        userEntity.setActive(false);
        userRepository.save(userEntity);
    }

    @Override
    public void updateName(Integer id, String customerName) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Could not find user with id: " + id));
        userEntity.setName(customerName);
        userRepository.save(userEntity);
    }

    @Override
    public void updatePhone(Integer id, String customerPhone) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Could not find user with id: " + id));
        userEntity.setPhone(customerPhone);
        userRepository.save(userEntity);
    }

    @Override
    public void updatePassword(Integer id, String customerPassword) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Could not find user with id: " + id));
        userEntity.setPassword(customerPassword);
        userRepository.save(userEntity);
    }

    @Override
    public CustomerProfile getById(Integer id) throws UserNotFoundException, RoleNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Could not find user with id: " + id));
        RoleEntity roleEntity = roleRepository.findById(userEntity.getRoleId())
                .orElseThrow(()-> new RoleNotFoundException("Role not found with id: " + userEntity.getRoleId()));
        return new CustomerProfile(userEntity.getName(), userEntity.getPhone(), userEntity.getLogin(), userEntity.getPassword(), roleEntity.getName());
    }

    @Override
    public CustomerProfile getByLogin(String customerLogin) throws UserNotFoundException, RoleNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(customerLogin)
                .orElseThrow(()-> new UserNotFoundException("Could not find user with login: " + customerLogin));
        RoleEntity roleEntity = roleRepository.findById(userEntity.getRoleId())
                .orElseThrow(()-> new RoleNotFoundException("Role not found with id: " + userEntity.getRoleId()));
        return new CustomerProfile(userEntity.getName(), userEntity.getPhone(), userEntity.getLogin(), userEntity.getPassword(), roleEntity.getName());
    }

    @Override
    public List<CustomerProfile> getAll(Comparator<UserEntity> comparator) throws RoleNotFoundException {
        List<UserEntity> userEntities = Lists.newArrayList(userRepository.findAll());
        return processUser(userEntities, comparator);
    }

    private List<CustomerProfile> processUser(List<UserEntity> userEntities, Comparator<UserEntity> comparator) throws RoleNotFoundException {
        if (comparator != null) {
            userEntities.sort(comparator);
        }
        ArrayList<CustomerProfile> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            RoleEntity roleEntity = roleRepository.findById(userEntity.getRoleId())
                    .orElseThrow(()-> new RoleNotFoundException("Could not find Role with id:" + userEntity.getRoleId()));
            users.add(new CustomerProfile(userEntity.getName(), userEntity.getPhone(), userEntity.getLogin(), userEntity.getPassword(), roleEntity.getName()));
        }
        return users;
    }
}
