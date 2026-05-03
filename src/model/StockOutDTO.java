package model;

import java.util.Date;
import java.util.List;

public class StockOutDTO {

    private Integer id;
    private Integer warehouseId;
    private String invoiceNo;
    private String customer;      // 客户（出库对应供应商）
    private String operator;
    private Date createTime;
    private Integer status;

    private List<StockOutDetailDTO> details;

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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<StockOutDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<StockOutDetailDTO> details) {
        this.details = details;
    }
}