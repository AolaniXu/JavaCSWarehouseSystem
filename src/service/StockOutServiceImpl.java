package service;

import java.util.Date;
import java.util.List;

import model.StockOutDTO;
import model.StockOutDetailDTO;
import model.StockOutView;
import dao.ProductDao;
import dao.StockOutDao;
import dao.StockOutDetailDao;
import dao.InventoryDao;

public class StockOutServiceImpl implements StockOutService {

    private StockOutDao stockOutDao = new StockOutDao();
    private StockOutDetailDao detailDao = new StockOutDetailDao();
    private ProductDao productDao = new ProductDao();
    private InventoryDao inventoryDao = new InventoryDao();

    @Override
    public void create(StockOutDTO dto) {

        // 1. 基本处理
        if (dto.getWarehouseId() == null) {
            dto.setWarehouseId(1);
        }
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

    @Override
    public void audit(int id) {
        // 1. 获取出库单明细
        StockOutDTO dto = findById(id);
        if (dto == null) {
            throw new RuntimeException("出库单不存在");
        }

        // 2. 扣减库存
        for (StockOutDetailDTO detail : dto.getDetails()) {
            // 如果没有 productId，通过 code 查找
            if (detail.getProductId() == null) {
                detail.setProductId(productDao.findIdByCode(detail.getProductCode()));
            }
            inventoryDao.decrease(detail.getProductId(), detail.getQuantity());
        }

        // 3. 更新状态为已审核
        stockOutDao.updateStatus(id, 1);
    }

}