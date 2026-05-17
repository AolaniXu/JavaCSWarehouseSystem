package ui.panel;

import model.Product;
import model.ProductNode;
import service.ProductService;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;

public class ProductTreePane extends JPanel {

    public interface OnProductSelectedListener {
        void onProductSelected(Product product);
    }

    private JTree tree;
    private OnProductSelectedListener selectedListener;

    public ProductTreePane() {
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
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node == null || !node.isLeaf() || selectedListener == null) {
                    return;
                }
                Object userObject = node.getUserObject();
                if (userObject instanceof Product) {
                    selectedListener.onProductSelected((Product) userObject);
                }
            }
        });

        add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    public void setOnProductSelectedListener(OnProductSelectedListener listener) {
        this.selectedListener = listener;
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
