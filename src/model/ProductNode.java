package model;



import java.util.ArrayList;
import java.util.List;

public class ProductNode {

    private Product product;
    private List<ProductNode> children = new ArrayList<>();

    public ProductNode(Product product) {
        this.product = product;
    }

    public void addChild(ProductNode node) {
        children.add(node);
    }

    public List<ProductNode> getChildren() {
        return children;
    }

    public Product getProduct() {
        return product;
    }
}