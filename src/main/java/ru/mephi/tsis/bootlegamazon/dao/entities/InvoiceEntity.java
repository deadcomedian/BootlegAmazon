package ru.mephi.tsis.bootlegamazon.dao.entities;

import javax.persistence.*;

@Entity
@Table(name = "t_invoice")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "invoice_count")
    private Integer count;

    public InvoiceEntity() {
    }

    public InvoiceEntity(Integer id, Integer articleId, Integer count) {
        this.id = id;
        this.articleId = articleId;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public Integer getCount() {
        return count;
    }
}
