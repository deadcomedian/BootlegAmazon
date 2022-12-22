package ru.mephi.tsis.bootlegamazon.forms;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InvoiceForm {

    private Integer articleId;

    @NotNull
    @Min(1)
    private Integer amount;

    public InvoiceForm(Integer articleId, Integer amount) {
        this.articleId = articleId;
        this.amount = amount;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "InvoiceForm{" +
                "articleId=" + articleId +
                ", amount=" + amount +
                '}';
    }
}
