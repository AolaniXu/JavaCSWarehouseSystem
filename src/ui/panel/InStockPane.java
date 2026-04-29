package ui.panel;

import model.StockInDTO;
import model.StockInDetailDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

public class InStockPane extends BaseFormPane {

    // ===== 输入框 =====
    private JTextField txtWarehouse;
    private JTextField txtDate;
    private JTextField txtInvoice;
    private JTextField txtCode;
    private JTextField txtName;
    private JTextField txtSpec;
    private JTextField txtType;
    private JTextField txtUnit;
    private JTextField txtQty;
    private JTextField txtPrice;
    private JTextField txtAmount;
    private JTextField txtSupplier;
    private JTextField txtOperator;

    private JPanel formPanel;

    public InStockPane() {
        super();
        init(); // ✔ 关键：由子类主动初始化
    }

    @Override
    protected void initComponents() {

        formPanel = new JPanel(new GridBagLayout());

        txtWarehouse = new JTextField(15);
        txtDate = new JTextField(15);
        txtInvoice = new JTextField(15);
        txtCode = new JTextField(15);
        txtName = new JTextField(15);
        txtSpec = new JTextField(15);
        txtType = new JTextField(15);
        txtUnit = new JTextField(15);
        txtQty = new JTextField(15);
        txtPrice = new JTextField(15);
        txtAmount = new JTextField(15);
        txtSupplier = new JTextField(15);
        txtOperator = new JTextField(15);
    }

    @Override
    protected void layoutComponents() {

        this.setLayout(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        addRow(formPanel, gbc, row++, "仓库名：", txtWarehouse);
        addRow(formPanel, gbc, row++, "日期：", txtDate);
        addRow(formPanel, gbc, row++, "发票号码：", txtInvoice);
        addRow(formPanel, gbc, row++, "编号：", txtCode);
        addRow(formPanel, gbc, row++, "名称：", txtName);
        addRow(formPanel, gbc, row++, "规格型号：", txtSpec);
        addRow(formPanel, gbc, row++, "类型：", txtType);
        addRow(formPanel, gbc, row++, "单位：", txtUnit);
        addRow(formPanel, gbc, row++, "数量：", txtQty);
        addRow(formPanel, gbc, row++, "单价：", txtPrice);
        addRow(formPanel, gbc, row++, "金额：", txtAmount);
        addRow(formPanel, gbc, row++, "供应商：", txtSupplier);
        addRow(formPanel, gbc, row++, "仓管员：", txtOperator);

        this.add(formPanel, BorderLayout.NORTH);
    }

    // ===== 封装：一行“标签 + 输入框” =====
    private void addRow(JPanel panel, GridBagConstraints gbc, int y,
            String labelText, JComponent field) {

        // 左列：标签
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(labelText), gbc);

        // 右列：输入框
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
        txtType.setText("");
        txtUnit.setText("");
        txtQty.setText("");
        txtPrice.setText("");
        txtAmount.setText("");
        txtSupplier.setText("");
        txtOperator.setText("");
    }

    @Override
    public StockInDTO getData() {

        StockInDTO dto = new StockInDTO();

        // ===== 表头 =====
        dto.setInvoiceNo(txtInvoice.getText());
        dto.setSupplier(txtSupplier.getText());
        dto.setOperator(txtOperator.getText());
        dto.setCreateTime(new Date());

        // ===== 单条明细 =====
        List<StockInDetailDTO> list = new ArrayList<>();

        StockInDetailDTO d = new StockInDetailDTO();

        d.setProductCode(txtCode.getText());
        d.setQuantity(Integer.parseInt(txtQty.getText()));
        d.setPrice(Double.parseDouble(txtPrice.getText()));

        list.add(d);

        dto.setDetails(list);

        return dto;
    }
}
