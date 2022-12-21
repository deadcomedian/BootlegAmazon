package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.StatusEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.StatusRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.StatusNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Status;
import ru.mephi.tsis.bootlegamazon.services.StatusService;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    private StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public List<Status> getAll() {
        Iterable<StatusEntity> statusEntities = statusRepository.findAll();
        ArrayList<Status> statuses = new ArrayList<>();
        for (StatusEntity statusEntity :statusEntities){
            statuses.add(new Status(statusEntity.getId(), statusEntity.getName()));
        }
        return statuses;
    }

    @Override
    public Status getById(Integer statusId) throws StatusNotFoundException {
        StatusEntity statusEntity = statusRepository.findById(statusId)
                .orElseThrow(() -> new StatusNotFoundException("Status not found, id: " + statusId));
        return new Status(statusEntity.getId(), statusEntity.getName());
    }
}
