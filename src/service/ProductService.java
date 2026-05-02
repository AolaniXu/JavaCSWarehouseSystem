package service;



import dao.ProductDao;
import model.Product;
import model.ProductNode;

import java.util.*;

public class ProductService {

    private ProductDao dao = new ProductDao();

    public List<ProductNode> buildTree() {

        List<Product> list = dao.findAll();

        Map<String, ProductNode> map = new HashMap<>();
        List<ProductNode> roots = new ArrayList<>();

        // 1. 放入map
        for (Product p : list) {
            map.put(p.getCode(), new ProductNode(p));
        }

        // 2. 建立父子关系
        for (Product p : list) {

            String code = p.getCode();
            String parentCode = getParentCode(code);

            ProductNode node = map.get(code);

            if (parentCode == null) {
                roots.add(node);
            } else {
                ProductNode parent = map.get(parentCode);
                if (parent != null) {
                    parent.addChild(node);
                }
            }
        }

        return roots;
    }

    private String getParentCode(String code) {
        if (code.length() == 1) return null;
        return code.substring(0, code.length() - 2);
    }
}
