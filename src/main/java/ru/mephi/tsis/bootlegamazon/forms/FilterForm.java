package ru.mephi.tsis.bootlegamazon.forms;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

public class FilterForm {
    private String categoryName;
    private Boolean inStock;
    @DecimalMin(value = "0.01")
    private Double priceFrom;
    @DecimalMax(value = "1000000.0")
    private Double priceTo;

    public FilterForm(String categoryName, Boolean inStock, Double priceFrom, Double priceTo) {
        this.categoryName = categoryName;
        this.inStock = inStock;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }

    public FilterForm() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    @Override
    public String toString() {
        return "FilterForm{" +
                "categoryName='" + categoryName + '\'' +
                ", inStock=" + inStock +
                ", priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                '}';
    }
}
