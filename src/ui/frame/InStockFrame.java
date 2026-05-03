package ui.frame;

import javax.swing.*;
import java.awt.*;

import ui.panel.InStockPane;
import ui.panel.StockInTablePane;
import ui.componet.DataNavigator;
import model.StockInDTO;
import model.StockInDetailDTO;
import service.StockInService;
import service.StockInServiceImpl;

public class InStockFrame extends BaseFrame implements DataNavigator.Listener {

    private InStockPane formPane;
    private StockInTablePane tablePane;  // 保存表格引用
    private DataNavigator nav;
    private StockInService service = new StockInServiceImpl();

    // 编辑状态
    private boolean isNewMode = true;     // true=新增模式, false=编辑模式
    private int currentRecordId = -1;     // 当前记录的 ID

    public InStockFrame() {

        setTitle("入库管理");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {

        // ===== 左：单据表单 =====
        formPane = new InStockPane();

        // ===== 右：表格 =====
        tablePane = new StockInTablePane();
        // 设置行选择监听器
        tablePane.setOnRowSelectedListener(this::onRowSelected);

        // ===== 下：数据导航条 =====
        nav = new DataNavigator();
        nav.setListener(this);  // 设置监听器

        // ===== 组装 =====
        setLeft(formPane);
        setRight(tablePane);
        setBottom(nav);
    }

    // ===== 表格行选择回调 =====
    private void onRowSelected(StockInDTO dto) {
        // 加载数据到表单
        formPane.setData(dto);

        // 切换到编辑模式
        isNewMode = false;
        currentRecordId = dto.getId();
        nav.updateEditState(false);  // 按钮文字变成"更新"

        // 启用浏览按钮
        nav.updateNavigationState(true, true);
    }

    // ===== DataNavigator.Listener 实现 =====

    @Override
    public void onNew() {
        // 新建：清空表单，切换到新增模式
        formPane.reset();
        isNewMode = true;
        currentRecordId = -1;
        nav.updateEditState(true);

        // 清空后浏览按钮应该禁用
        nav.updateNavigationState(false, false);

        // 清除表格选择
        tablePane.refresh();
    }

    @Override
    public void onSave() {
        if (isNewMode) {
            // 新增模式：插入数据
            insertData();
        } else {
            // 编辑模式：更新数据（暂未实现）
            JOptionPane.showMessageDialog(this, "更新功能暂未实现");
        }
    }

    // ===== 暂未实现的接口方法 =====
    @Override
    public void onFirst() {
        JOptionPane.showMessageDialog(this, "首条功能暂未实现");
    }

    @Override
    public void onPrev() {
        JOptionPane.showMessageDialog(this, "上一条功能暂未实现");
    }

    @Override
    public void onNext() {
        JOptionPane.showMessageDialog(this, "下一条功能暂未实现");
    }

    @Override
    public void onLast() {
        JOptionPane.showMessageDialog(this, "末条功能暂未实现");
    }

    @Override
    public void onDelete() {
        JOptionPane.showMessageDialog(this, "删除功能暂未实现");
    }

    @Override
    public void onAudit() {
        JOptionPane.showMessageDialog(this, "审核功能暂未实现");
    }

    // ===== 核心：插入数据 =====
    private void insertData() {

        try {
            // 1. 收集数据
            StockInDTO dto = formPane.getData();

            // 2. 基本校验
            if (dto == null) {
                JOptionPane.showMessageDialog(this, "数据获取失败");
                return;
            }

            if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请至少填写一条入库明细");
                return;
            }

            // 3. 调用业务层插入
            service.create(dto);

            // 4. 成功提示
            JOptionPane.showMessageDialog(this, "提交成功");

            // 5. 保存成功后，切换到编辑模式
            isNewMode = false;
            nav.updateEditState(false);

            // 6. 刷新右侧表格
            tablePane.refresh();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请填写正确的数字：" + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "提交失败：" + ex.getMessage());
        }
    }
}