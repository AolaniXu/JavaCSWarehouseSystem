package service;

import java.util.Date;
import java.util.List;

import model.StockInDTO;
import model.StockInDetailDTO;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public StockInDTO findById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<StockInDTO> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

}
