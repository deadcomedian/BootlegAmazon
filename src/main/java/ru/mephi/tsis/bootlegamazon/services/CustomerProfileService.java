package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.dao.entities.UserEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.RoleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.UserNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.CustomerProfile;
import ru.mephi.tsis.bootlegamazon.models.Role;

import java.util.Comparator;
import java.util.List;

public interface CustomerProfileService {
    void createCustomerProfile(String customerName, String customerPhone, String customerLogin, String customerPassword, Role role);
    void deleteCustomerProfile(Integer id) throws UserNotFoundException;
    void updateName(Integer id, String customerName) throws UserNotFoundException;
    void updatePhone(Integer id, String customerPhone) throws UserNotFoundException;
    void updatePassword(Integer id, String customerPassword) throws UserNotFoundException;
    CustomerProfile getById(Integer id) throws UserNotFoundException, RoleNotFoundException;
    CustomerProfile getByLogin(String customerLogin) throws UserNotFoundException, RoleNotFoundException;
    List<CustomerProfile> getAll(Comparator<UserEntity> comparator) throws RoleNotFoundException;
}
