package ui.panel;

import dao.WarehouseDao;
import model.Product;
import model.StockOutDTO;
import model.StockOutDetailDTO;
import model.Warehouse;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OutStockPane extends BaseFormPane {

    // 主表输入框
    private JTextField txtWarehouse;
    private JTextField txtDate;
    private JTextField txtInvoice;
    private JTextField txtCustomer;
    private JTextField txtOperator;

    // 明细输入框（商品字段只读）
    private JTextField txtCode;
    private JTextField txtName;
    private JTextField txtSpec;
    private JTextField txtUnit;
    private JTextField txtQty;
    private JTextField txtPrice;
    private JTextField txtAmount;

    private JPanel formPanel;
    private WarehouseDao warehouseDao = new WarehouseDao();
    private Integer selectedWarehouseId;

    public OutStockPane() {
        super();
        init();
    }

    @Override
    protected void initComponents() {

        formPanel = new JPanel(new GridBagLayout());

        // 主表字段
        txtWarehouse = new JTextField(15);
        txtWarehouse.setEditable(false);  // 只读，通过选择器选择
        txtDate = new JTextField(15);
        txtInvoice = new JTextField(15);
        txtCustomer = new JTextField(15);
        txtOperator = new JTextField(15);

        // 明细字段（商品字段只读，通过选择器选择）
        txtCode = new JTextField(15);
        txtCode.setEditable(false);
        txtName = new JTextField(15);
        txtName.setEditable(false);
        txtSpec = new JTextField(15);
        txtSpec.setEditable(false);
        txtUnit = new JTextField(15);
        txtUnit.setEditable(false);
        txtQty = new JTextField(15);
        txtPrice = new JTextField(15);
        txtAmount = new JTextField(15);
        txtAmount.setEditable(false);  // 金额自动计算，只读

        // 数量和单价变化时自动计算金额
        DocumentListener amountListener = new DocumentListener() {
            private void calcAmount() {
                try {
                    int qty = Integer.parseInt(txtQty.getText());
                    double price = Double.parseDouble(txtPrice.getText());
                    txtAmount.setText(String.valueOf(qty * price));
                } catch (NumberFormatException e) {
                    txtAmount.setText("");
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) { calcAmount(); }
            @Override
            public void removeUpdate(DocumentEvent e) { calcAmount(); }
            @Override
            public void changedUpdate(DocumentEvent e) { calcAmount(); }
        };
        txtQty.getDocument().addDocumentListener(amountListener);
        txtPrice.getDocument().addDocumentListener(amountListener);
    }

    @Override
    protected void layoutComponents() {

        this.setLayout(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // 主表字段
        addRow(formPanel, gbc, row++, "仓库名：", txtWarehouse);
        addRow(formPanel, gbc, row++, "时间：", txtDate);
        addRow(formPanel, gbc, row++, "发票号码：", txtInvoice);

        // 明细字段
        addRow(formPanel, gbc, row++, "编号：", txtCode);
        addRow(formPanel, gbc, row++, "名称：", txtName);
        addRow(formPanel, gbc, row++, "规格型号：", txtSpec);
        addRow(formPanel, gbc, row++, "单位：", txtUnit);
        addRow(formPanel, gbc, row++, "数量：", txtQty);
        addRow(formPanel, gbc, row++, "单价：", txtPrice);
        addRow(formPanel, gbc, row++, "金额：", txtAmount);

        // 主表字段（续）
        addRow(formPanel, gbc, row++, "客户：", txtCustomer);
        addRow(formPanel, gbc, row++, "操作员：", txtOperator);

        this.add(formPanel, BorderLayout.NORTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y,
            String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    @Override
    public void reset() {
        txtWarehouse.setText("");
        txtDate.setText("");
        txtInvoice.setText("");
        txtCode.setText("");
        txtName.setText("");
        txtSpec.setText("");
        txtUnit.setText("");
        txtQty.setText("");
        txtPrice.setText("");
        txtAmount.setText("");
        txtCustomer.setText("");
        txtOperator.setText("");
        selectedWarehouseId = null;
    }

    public void setWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            selectedWarehouseId = null;
            txtWarehouse.setText("");
            return;
        }
        selectedWarehouseId = warehouse.getId();
        txtWarehouse.setText(warehouse.getName() != null ? warehouse.getName() : "");
    }

    public void setProduct(Product product) {
        if (product == null) {
            return;
        }
        txtCode.setText(product.getCode() != null ? product.getCode() : "");
        txtName.setText(product.getName() != null ? product.getName() : "");
        txtSpec.setText(product.getSpec() != null ? product.getSpec() : "");
        txtUnit.setText(product.getUnit() != null ? product.getUnit() : "");
    }

    @Override
    public StockOutDTO getData() {

        StockOutDTO dto = new StockOutDTO();

        // 主表
        dto.setWarehouseId(selectedWarehouseId);
        dto.setInvoiceNo(txtInvoice.getText());
        dto.setCustomer(txtCustomer.getText());
        dto.setOperator(txtOperator.getText());
        dto.setBizTime(parseBizTime(txtDate.getText()));

        // 明细
        List<StockOutDetailDTO> list = new ArrayList<>();
        StockOutDetailDTO d = new StockOutDetailDTO();
        d.setProductCode(txtCode.getText());
        d.setQuantity(Integer.parseInt(txtQty.getText()));
        d.setPrice(Double.parseDouble(txtPrice.getText()));
        list.add(d);

        dto.setDetails(list);

        return dto;
    }

    // 加载数据到表单
    public void setData(StockOutDTO dto) {
        // 主表
        selectedWarehouseId = dto.getWarehouseId();
        if (selectedWarehouseId != null) {
            Warehouse warehouse = warehouseDao.findById(selectedWarehouseId);
            txtWarehouse.setText(warehouse != null ? warehouse.getName() : "");
        } else {
            txtWarehouse.setText("");
        }
        txtInvoice.setText(dto.getInvoiceNo() != null ? dto.getInvoiceNo() : "");
        txtCustomer.setText(dto.getCustomer() != null ? dto.getCustomer() : "");
        txtOperator.setText(dto.getOperator() != null ? dto.getOperator() : "");
        txtDate.setText(formatBizTime(dto.getBizTime()));

        // 明细（只取第一条）
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            StockOutDetailDTO detail = dto.getDetails().get(0);
            txtCode.setText(detail.getProductCode() != null ? detail.getProductCode() : "");
            txtQty.setText(String.valueOf(detail.getQuantity()));
            txtPrice.setText(String.valueOf(detail.getPrice()));
            txtAmount.setText(String.valueOf(detail.getQuantity() * detail.getPrice()));
        }
    }

    private java.util.Date parseBizTime(String text) {
        String value = text == null ? "" : text.trim();
        if (value.isEmpty()) {
            return null;
        }
        try {
            if (value.length() == 10) {
                value += " 00:00:00";
            }
            return Timestamp.valueOf(value);
        } catch (IllegalArgumentException ex) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(value);
            } catch (ParseException parseException) {
                throw new IllegalArgumentException("时间格式应为 yyyy-MM-dd HH:mm:ss");
            }
        }
    }

    private String formatBizTime(java.util.Date time) {
        return time == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }
}
