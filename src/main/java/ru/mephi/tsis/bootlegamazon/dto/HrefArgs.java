package ru.mephi.tsis.bootlegamazon.dto;

public class HrefArgs {
    private int argsCount;
    private String sortMethod;
    private String categoryName;
    private Boolean inStock;
    private Double priceFrom;
    private Double priceTo;
    private String searchField;

    public void setSortMethod(String sortMethod) {
        this.sortMethod = sortMethod;
        this.argsCount +=1;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        this.argsCount +=1;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
        this.argsCount +=1;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
        this.argsCount +=1;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
        this.argsCount +=1;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
        this.argsCount +=1;
    }

    public String getHrefArgs(){
        if (this.argsCount > 0){
            StringBuilder stringBuilder = new StringBuilder();
            if (sortMethod != null){
                stringBuilder.append("&sort=").append(sortMethod);
            }
            if (categoryName != null){
                stringBuilder.append("&category=").append(categoryName);
            }
            if (inStock != null){
                stringBuilder.append("&instock=").append(inStock);
            }
            if (priceFrom != null){
                stringBuilder.append("&pricefrom=").append(priceFrom);
            }
            if (priceTo != null){
                stringBuilder.append("&priceto=").append(priceTo);
            }
            if (searchField != null){
                stringBuilder.append("&search=").append(searchField);
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public String getHrefArgsExcept(String parameterName){
        if (this.argsCount > 0){
            StringBuilder stringBuilder = new StringBuilder();
            if (sortMethod != null && !parameterName.equals("sort")){
                stringBuilder.append("&sort=").append(sortMethod);
            }
            if (categoryName != null && !parameterName.equals("category")){
                stringBuilder.append("&category=").append(categoryName);
            }
            if (inStock != null && !parameterName.equals("stock")){
                stringBuilder.append("&instock=").append(inStock);
            }
            if (priceFrom != null && !parameterName.equals("pricefrom")){
                stringBuilder.append("&pricefrom=").append(priceFrom);
            }
            if (priceTo != null && !parameterName.equals("priceto")){
                stringBuilder.append("&priceto=").append(priceTo);
            }
            if (searchField != null && !parameterName.equals("search")){
                stringBuilder.append("&search=").append(searchField);
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public String getSearchField() {
        if (searchField == null){
            return "";
        } else {
            return "?search=" + searchField;
        }
    }

    public String getHrefArgsExceptFiltration(){
        if (this.argsCount > 0){
            StringBuilder stringBuilder = new StringBuilder();
            if (sortMethod != null){
                stringBuilder.append("&sort=").append(sortMethod);
            }
            if (searchField != null){
                stringBuilder.append("&search=").append(searchField);
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }
}
