package ui.frame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import ui.panel.InStockPane;
import ui.componet.DataNavigator;
import model.StockInDTO;
import model.StockInDetailDTO;
import service.StockInService;
import service.StockInServiceImpl;

public class InStockFrame extends BaseFrame {

    private InStockPane formPane;
    private JPanel listPlaceholder; // 右侧占位
    private DataNavigator nav;
    private StockInService service = new StockInServiceImpl();

    JButton saveButton;

    public InStockFrame() {

        setTitle("入库管理");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        bindEvents();

        setVisible(true);
    }

    private void initComponents() {

        // ===== 左：单据 =====
        formPane = new InStockPane();

        // ===== 右：占位（以后换成ListPane）=====
        listPlaceholder = new JPanel(new BorderLayout());
        listPlaceholder.add(
                new JLabel("（列表区，待实现）", SwingConstants.CENTER),
                BorderLayout.CENTER);

        // ===== 下：导航栏 =====
        // nav = new DataNavigator();
        saveButton = new JButton("提交");

        // ===== 组装 =====
        setLeft(formPane);
        setRight(listPlaceholder);
        // setBottom(nav);
        setBottom(saveButton);
    }

    private void bindEvents() {
        saveButton.addActionListener(e -> save());

    }

    // ===== 核心：保存 =====
    private void save() {

        try {
            // 1. 收集数据
            StockInDTO dto = formPane.getData();

            // 2. 基本校验（防止空数据）
            if (dto == null) {
                JOptionPane.showMessageDialog(this, "数据获取失败");
                return;
            }

            if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请至少填写一条入库明细");
                return;
            }

            // 3. 调用业务
            service.create(dto);

            // 4. 成功提示
            JOptionPane.showMessageDialog(this, "提交成功");

        } catch (Exception ex) {

            ex.printStackTrace(); // 控制台打印（调试用）

            // 5. 失败提示（避免程序崩溃）
            JOptionPane.showMessageDialog(this, "提交失败：" + ex.getMessage());
        }
    }
}