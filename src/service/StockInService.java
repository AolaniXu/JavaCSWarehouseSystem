package service;
import model.StockInDTO;
import model.StockInView;

import java.util.List;

public interface StockInService {

    void create(StockInDTO dto);

    void update(StockInDTO dto);

    void delete(int id);

    StockInDTO findById(int id);

    List<StockInView> getStockInList();

}