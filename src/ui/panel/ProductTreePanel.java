package ui.panel;



import model.ProductNode;
import service.ProductService;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;

public class ProductTreePanel extends JPanel {

    private JTree tree;

    public ProductTreePanel() {
        System.out.println("Initializing ProductTreePanel...");
        setLayout(new BorderLayout());
        initTree();
    }

    private void initTree() {

        ProductService service = new ProductService();
        List<ProductNode> roots = service.buildTree();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("商品目录");

        for (ProductNode node : roots) {
            root.add(convert(node));
        }

        tree = new JTree(root);

        add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode convert(ProductNode node) {

        DefaultMutableTreeNode treeNode =
                new DefaultMutableTreeNode(node.getProduct());

        for (ProductNode child : node.getChildren()) {
            treeNode.add(convert(child));
        }

        return treeNode;
    }
}
