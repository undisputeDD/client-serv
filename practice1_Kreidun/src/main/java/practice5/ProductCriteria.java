package practice5;

public class ProductCriteria {

    private String name;
    private Double priceFrom;
    private Double priceTo;
    private Double amountFrom;
    private Double amountTo;

    public ProductCriteria() {

    }

    public ProductCriteria(String name, Double priceFrom, Double priceTo, Double amountFrom, Double amountTo) {
        this.name = name;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Double amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Double getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Double amountTo) {
        this.amountTo = amountTo;
    }
}
