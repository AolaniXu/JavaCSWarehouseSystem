package model;

public class StockInDetailDTO {

    private Integer id;
    private Integer stockInId;
    private Integer productId;
    private String productCode; // 冗余字段，方便显示
    private int quantity;
    private double price;
    private double amount; // 计算字段，quantity * price

    // 省略 getter/setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getStockInId() {
        return stockInId;
    }
    public void setStockInId(Integer stockInId) {
        this.stockInId = stockInId;
    }
    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }



    
}
