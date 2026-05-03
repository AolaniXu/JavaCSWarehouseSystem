package service;

import java.util.Date;
import java.util.List;

import model.StockInDTO;
import model.StockInDetailDTO;
import model.StockInView;
import dao.ProductDao;
import dao.StockInDao;
import dao.StockInDetailDao;

public class StockInServiceImpl implements StockInService {

    private StockInDao stockInDao = new StockInDao();
    private StockInDetailDao detailDao = new StockInDetailDao();
    private ProductDao productDao = new ProductDao(); // 如果你用 productCode

    @Override
    public void create(StockInDTO dto) {

        // 1. 基本处理
        dto.setWarehouseId(1);
        dto.setStatus(0);
        dto.setCreateTime(new Date());

        // 2. 保存主表 → 拿到 id
        int stockInId = stockInDao.insert(dto);

        // 3. 处理明细
        for (StockInDetailDTO d : dto.getDetails()) {

            // （如果你用 productCode）
            if (d.getProductId() == null) {
                int productId = productDao.findIdByCode(d.getProductCode());
                d.setProductId(productId);
            }

            // 计算金额（关键）
            double amount = d.getQuantity() * d.getPrice();
            d.setAmount(amount);

            // 绑定主表 id
            d.setStockInId(stockInId);

            // 插入
            detailDao.insert(d);
        }
    }

    @Override
    public void update(StockInDTO dto) {
        // 1. 更新主表
        stockInDao.update(dto);

        // 2. 删除旧明细
        detailDao.deleteByStockInId(dto.getId());

        // 3. 插入新明细
        for (StockInDetailDTO d : dto.getDetails()) {
            if (d.getProductId() == null) {
                int productId = productDao.findIdByCode(d.getProductCode());
                d.setProductId(productId);
            }
            double amount = d.getQuantity() * d.getPrice();
            d.setAmount(amount);
            d.setStockInId(dto.getId());
            detailDao.insert(d);
        }
    }

    @Override
    public void delete(int id) {
        // 1. 先删明细
        detailDao.deleteByStockInId(id);
        // 2. 再删主表
        stockInDao.delete(id);
    }

    @Override
    public StockInDTO findById(int id) {
        // 1. 查询主表
        StockInView view = stockInDao.findById(id);
        if (view == null) {
            return null;
        }

        // 2. 构建 DTO
        StockInDTO dto = new StockInDTO();
        dto.setId(view.getId());
        dto.setWarehouseId(view.getWarehouseId());
        dto.setInvoiceNo(view.getInvoiceNo());
        dto.setSupplier(view.getSupplier());
        dto.setOperator(view.getOperator());
        dto.setStatus(view.getStatus());

        // 3. 查询明细列表
        dto.setDetails(detailDao.findByStockInId(id));

        return dto;
    }

    private StockInDao dao = new StockInDao();

    @Override
    public List<StockInView> getStockInList() {
        return dao.findAllForTable();
    }

    @Override
    public List<Integer> getAllIds() {
        return dao.findAllIds();
    }

}
