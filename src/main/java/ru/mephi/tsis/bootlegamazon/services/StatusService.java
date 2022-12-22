package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.exceptions.StatusNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Status;

import java.util.List;

public interface StatusService {

    List<Status> getAll();

    Status getById(Integer statusId) throws StatusNotFoundException;

}
