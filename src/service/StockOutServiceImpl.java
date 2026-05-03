package service;

import java.util.Date;
import java.util.List;

import model.StockOutDTO;
import model.StockOutDetailDTO;
import model.StockOutView;
import dao.ProductDao;
import dao.StockOutDao;
import dao.StockOutDetailDao;

public class StockOutServiceImpl implements StockOutService {

    private StockOutDao stockOutDao = new StockOutDao();
    private StockOutDetailDao detailDao = new StockOutDetailDao();
    private ProductDao productDao = new ProductDao();

    @Override
    public void create(StockOutDTO dto) {

        // 1. 基本处理
        dto.setWarehouseId(1);
        dto.setStatus(0);
        dto.setCreateTime(new Date());

        // 2. 保存主表 → 拿到 id
        int stockOutId = stockOutDao.insert(dto);

        // 3. 处理明细
        for (StockOutDetailDTO d : dto.getDetails()) {

            if (d.getProductId() == null) {
                int productId = productDao.findIdByCode(d.getProductCode());
                d.setProductId(productId);
            }

            double amount = d.getQuantity() * d.getPrice();
            d.setAmount(amount);
            d.setStockOutId(stockOutId);

            detailDao.insert(d);
        }
    }

    @Override
    public void update(StockOutDTO dto) {
        // 1. 更新主表
        stockOutDao.update(dto);

        // 2. 删除旧明细
        detailDao.deleteByStockOutId(dto.getId());

        // 3. 插入新明细
        for (StockOutDetailDTO d : dto.getDetails()) {
            if (d.getProductId() == null) {
                int productId = productDao.findIdByCode(d.getProductCode());
                d.setProductId(productId);
            }
            double amount = d.getQuantity() * d.getPrice();
            d.setAmount(amount);
            d.setStockOutId(dto.getId());
            detailDao.insert(d);
        }
    }

    @Override
    public void delete(int id) {
        // 1. 先删明细
        detailDao.deleteByStockOutId(id);
        // 2. 再删主表
        stockOutDao.delete(id);
    }

    @Override
    public StockOutDTO findById(int id) {
        // 1. 查询主表
        StockOutView view = stockOutDao.findById(id);
        if (view == null) {
            return null;
        }

        // 2. 构建 DTO
        StockOutDTO dto = new StockOutDTO();
        dto.setId(view.getId());
        dto.setWarehouseId(view.getWarehouseId());
        dto.setInvoiceNo(view.getInvoiceNo());
        dto.setCustomer(view.getCustomer());
        dto.setOperator(view.getOperator());
        dto.setStatus(view.getStatus());

        // 3. 查询明细列表
        dto.setDetails(detailDao.findByStockOutId(id));

        return dto;
    }

    @Override
    public List<StockOutView> getStockOutList() {
        return stockOutDao.findAllForTable();
    }

    @Override
    public List<Integer> getAllIds() {
        return stockOutDao.findAllIds();
    }
}
