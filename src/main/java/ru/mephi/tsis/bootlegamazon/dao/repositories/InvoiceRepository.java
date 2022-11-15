package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.InvoiceEntity;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Integer> {
}
