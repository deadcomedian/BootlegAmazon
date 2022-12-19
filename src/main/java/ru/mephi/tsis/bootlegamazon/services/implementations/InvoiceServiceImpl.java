package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.InvoiceEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.ArticleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.InvoiceRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.InvoiceNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.Invoice;
import ru.mephi.tsis.bootlegamazon.services.InvoiceService;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ArticleRepository articleRepository){
        this.invoiceRepository = invoiceRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void createInvoice(Integer articleId, Integer articleCount){
        InvoiceEntity invoiceEntity = new InvoiceEntity(null, articleId, articleCount);
        invoiceRepository.save(invoiceEntity);
    }

   @Override
    public void updateArticleId(Integer id, Integer articleId) throws InvoiceNotFoundException {
        InvoiceEntity invoiceEntity = invoiceRepository.findById(id)
                .orElseThrow(()-> new InvoiceNotFoundException("Could not find invoice with id:" + id));
        invoiceEntity.setArticleId(articleId);
        invoiceRepository.save(invoiceEntity);
   }

   @Override
    public void updateArticleCount(Integer id, Integer articleCount) throws InvoiceNotFoundException {
       InvoiceEntity invoiceEntity = invoiceRepository.findById(id)
               .orElseThrow(()-> new InvoiceNotFoundException("Could not find invoice with id:" + id));
       invoiceEntity.setCount(articleCount);
       invoiceRepository.save(invoiceEntity);
   }

   @Override
    public void updateInvoice(Integer id, Integer articleId, Integer articleCount) throws InvoiceNotFoundException {
       InvoiceEntity invoiceEntity = invoiceRepository.findById(id)
               .orElseThrow(()-> new InvoiceNotFoundException("Could not find invoice with id:" + id));
       invoiceEntity.setArticleId(articleId);
       invoiceEntity.setCount(articleCount);
       invoiceRepository.save(invoiceEntity);
   }

   @Override
    public Invoice getById(Integer id) throws InvoiceNotFoundException, ArticleNotFoundException {
       InvoiceEntity invoiceEntity = invoiceRepository.findById(id)
               .orElseThrow(()-> new InvoiceNotFoundException("Could not find invoice with id:" + id));
       ArticleEntity articleEntity = articleRepository.findById(invoiceEntity.getArticleId())
               .orElseThrow(()->new ArticleNotFoundException("Could not find Article with id: " + invoiceEntity.getArticleId()));
       ArticleCard articleCard = new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice());
       return new Invoice(invoiceEntity.getId(), articleCard, invoiceEntity.getCount());
   }

   @Override
    public List<Invoice> getAll(Pageable pageable) throws ArticleNotFoundException {
        Page<InvoiceEntity> invoiceEntities = invoiceRepository.findAll(pageable);
       return processInvoice(invoiceEntities);
   }

   @Override
   public List<Invoice> getByArticleId(Pageable pageable, Integer articleId) throws ArticleNotFoundException {
       Page<InvoiceEntity> invoiceEntities = invoiceRepository.findByArticleId(pageable, articleId);
       return processInvoice(invoiceEntities);
   }

   @Override
   public int getTotalPages(Pageable pageable) {
        return invoiceRepository.findAll(pageable).getTotalPages();
   }

   private List<Invoice> processInvoice(Page<InvoiceEntity> invoiceEntities) throws ArticleNotFoundException {
       ArrayList<Invoice> invoices = new ArrayList<>();
       for (InvoiceEntity invoiceEntity : invoiceEntities){
           ArticleEntity articleEntity = articleRepository.findById(invoiceEntity.getArticleId())
                   .orElseThrow(()->new ArticleNotFoundException("Could not find Article with id: " + invoiceEntity.getArticleId()));
           ArticleCard articleCard = new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice());
           invoices.add(new Invoice(invoiceEntity.getId(), articleCard, invoiceEntity.getCount()));
       }
       return invoices;
   }

}
