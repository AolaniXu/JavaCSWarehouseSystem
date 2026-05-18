package model;

import java.util.Date;
import java.util.List;

public class StockInDTO {

    private Integer id;              
    private Integer warehouseId;
    private String invoiceNo;
    private String supplier;
    private String operator;
    private Date createTime;
    private Date bizTime;
    private Integer status;          

    private List<StockInDetailDTO> details;

    // 省略 getter/setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBizTime() {
        return bizTime;
    }

    public void setBizTime(Date bizTime) {
        this.bizTime = bizTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<StockInDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<StockInDetailDTO> details) {
        this.details = details;
    }


    
    
}
