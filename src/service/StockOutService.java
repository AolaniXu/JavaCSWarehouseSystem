package service;

import model.StockOutDTO;
import model.StockOutView;
import java.util.List;

public interface StockOutService {

    void create(StockOutDTO dto);

    void update(StockOutDTO dto);

    void delete(int id);

    StockOutDTO findById(int id);

    List<StockOutView> getStockOutList();

    List<Integer> getAllIds();
}

