package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.InvoiceEntity;

public interface InvoiceRepository extends PagingAndSortingRepository<InvoiceEntity, Integer> {

    @Query("select i from InvoiceEntity i where i.articleId = ?1")
    Page<InvoiceEntity> findByArticleId(Pageable pageable, Integer articleId);

    @Override
    @Query("select i from InvoiceEntity i")
    Iterable<InvoiceEntity> findAll();

    @Query("select i from InvoiceEntity i")
    Page<InvoiceEntity> findAll(Pageable pageable);
}
