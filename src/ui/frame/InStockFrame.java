package ui.frame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

    // 导航状态
    private List<Integer> recordIds;       // 所有记录的 ID 列表
    private int currentIndex = -1;       // 当前记录的索引位置

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

        // 更新导航状态
        refreshNavigationState();
    }

    // 刷新导航状态（重新加载 ID 列表并定位当前记录）
    private void refreshNavigationState() {
        recordIds = service.getAllIds();

        // 找到当前记录的位置
        currentIndex = recordIds.indexOf(currentRecordId);

        // 更新按钮状态
        nav.updateNavigationState(
                currentIndex > 0,                    // 有上一条
                currentIndex < recordIds.size() - 1  // 有下一条
        );

        // 启用删除按钮
        nav.deleteButton.setEnabled(currentRecordId > 0);
    }

    // 加载指定 ID 的记录到表单
    private void loadRecord(int id) {
        StockInDTO dto = service.findById(id);
        if (dto != null) {
            formPane.setData(dto);
            currentRecordId = id;
            isNewMode = false;
            nav.updateEditState(false);
            refreshNavigationState();
        }
    }

    // ===== DataNavigator.Listener 实现 =====

    @Override
    public void onNew() {
        // 新建：清空表单，切换到新增模式
        formPane.reset();
        isNewMode = true;
        currentRecordId = -1;
        currentIndex = -1;
        nav.updateEditState(true);

        // 清空后浏览按钮应该禁用
        nav.updateNavigationState(false, false);
        nav.deleteButton.setEnabled(false);

        // 刷新表格
        tablePane.refresh();
    }

    @Override
    public void onSave() {
        if (isNewMode) {
            // 新增模式：插入数据
            insertData();
        } else {
            // 编辑模式：更新数据
            updateData();
        }
    }

    @Override
    public void onDelete() {
        if (currentRecordId <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的记录");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除这条记录吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.delete(currentRecordId);
                JOptionPane.showMessageDialog(this, "删除成功");

                // 清空表单
                formPane.reset();
                isNewMode = true;
                currentRecordId = -1;
                currentIndex = -1;
                nav.updateEditState(true);
                nav.updateNavigationState(false, false);
                nav.deleteButton.setEnabled(false);

                // 刷新导航状态（更新 ID 列表）
                refreshNavigationState();

                // 刷新表格
                tablePane.refresh();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    // ===== 导航方法 =====
    @Override
    public void onFirst() {
        if (recordIds == null || recordIds.isEmpty()) {
            return;
        }
        loadRecord(recordIds.get(0));
    }

    @Override
    public void onPrev() {
        if (recordIds == null || currentIndex <= 0) {
            return;
        }
        loadRecord(recordIds.get(currentIndex - 1));
    }

    @Override
    public void onNext() {
        if (recordIds == null || currentIndex >= recordIds.size() - 1) {
            return;
        }
        loadRecord(recordIds.get(currentIndex + 1));
    }

    @Override
    public void onLast() {
        if (recordIds == null || recordIds.isEmpty()) {
            return;
        }
        loadRecord(recordIds.get(recordIds.size() - 1));
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

    // ===== 更新数据 =====
    private void updateData() {

        if (currentRecordId <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要更新的记录");
            return;
        }

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

            // 3. 设置 ID 和状态
            dto.setId(currentRecordId);
            dto.setStatus(0);  // 保持原状态

            // 4. 调用业务层更新
            service.update(dto);

            // 5. 成功提示
            JOptionPane.showMessageDialog(this, "更新成功");

            // 6. 刷新右侧表格
            tablePane.refresh();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请填写正确的数字：" + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
        }
    }
}