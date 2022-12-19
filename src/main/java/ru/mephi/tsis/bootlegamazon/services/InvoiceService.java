package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.InvoiceNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Invoice;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface InvoiceService {
    void createInvoice(Integer articleId, Integer articleCount);
    void updateArticleId(Integer id, Integer articleId) throws InvoiceNotFoundException;
    void updateArticleCount(Integer id, Integer articleCount) throws InvoiceNotFoundException;
    void updateInvoice(Integer id, Integer articleId, Integer articleCount) throws InvoiceNotFoundException;
    Invoice getById(Integer id) throws InvoiceNotFoundException;
    List<Invoice> getAll(Pageable pageable);
    List<Invoice> getByArticleId(Pageable pageable, Integer articleId) throws ArticleNotFoundException;
    int getTotalPages(Pageable pageable);
}
