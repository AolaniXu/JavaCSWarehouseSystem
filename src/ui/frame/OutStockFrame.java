package ui.frame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import ui.panel.OutStockPane;
import ui.panel.OutStockTablePane;
import ui.componet.DataNavigator;
import model.StockOutDTO;
import model.StockOutDetailDTO;
import service.StockOutService;
import service.StockOutServiceImpl;

public class OutStockFrame extends BaseFrame implements DataNavigator.Listener {

    private OutStockPane formPane;
    private OutStockTablePane tablePane;
    private DataNavigator nav;
    private StockOutService service = new StockOutServiceImpl();

    // 编辑状态
    private boolean isNewMode = true;
    private int currentRecordId = -1;

    // 导航状态
    private List<Integer> recordIds;
    private int currentIndex = -1;

    public OutStockFrame() {

        System.out.println("Initializing OutStockFrame...");

        setTitle("出库管理");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {

        // 左：表单
        formPane = new OutStockPane();

        // 右：表格
        tablePane = new OutStockTablePane();
        tablePane.setOnRowSelectedListener(this::onRowSelected);

        // 下：导航条
        nav = new DataNavigator();
        nav.setListener(this);

        // 组装
        setLeft(formPane);
        setRight(tablePane);
        setBottom(nav);
    }

    // ===== 表格行选择回调 =====
    private void onRowSelected(StockOutDTO dto) {
        formPane.setData(dto);
        isNewMode = false;
        currentRecordId = dto.getId();
        nav.updateEditState(false);
        refreshNavigationState();
    }

    private void refreshNavigationState() {
        recordIds = service.getAllIds();
        currentIndex = recordIds.indexOf(currentRecordId);
        nav.updateNavigationState(
                currentIndex > 0,
                currentIndex < recordIds.size() - 1
        );
        nav.deleteButton.setEnabled(currentRecordId > 0);
    }

    private void loadRecord(int id) {
        StockOutDTO dto = service.findById(id);
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
        formPane.reset();
        isNewMode = true;
        currentRecordId = -1;
        currentIndex = -1;
        nav.updateEditState(true);
        nav.updateNavigationState(false, false);
        nav.deleteButton.setEnabled(false);
        tablePane.refresh();
    }

    @Override
    public void onFirst() {
        if (recordIds == null || recordIds.isEmpty()) return;
        loadRecord(recordIds.get(0));
    }

    @Override
    public void onPrev() {
        if (recordIds == null || currentIndex <= 0) return;
        loadRecord(recordIds.get(currentIndex - 1));
    }

    @Override
    public void onNext() {
        if (recordIds == null || currentIndex >= recordIds.size() - 1) return;
        loadRecord(recordIds.get(currentIndex + 1));
    }

    @Override
    public void onLast() {
        if (recordIds == null || recordIds.isEmpty()) return;
        loadRecord(recordIds.get(recordIds.size() - 1));
    }

    @Override
    public void onSave() {
        if (isNewMode) {
            insertData();
        } else {
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

                formPane.reset();
                isNewMode = true;
                currentRecordId = -1;
                currentIndex = -1;
                nav.updateEditState(true);
                nav.updateNavigationState(false, false);
                nav.deleteButton.setEnabled(false);

                refreshNavigationState();
                tablePane.refresh();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    @Override
    public void onAudit() {
        JOptionPane.showMessageDialog(this, "审核功能暂未实现");
    }

    // ===== 插入数据 =====
    private void insertData() {

        try {
            StockOutDTO dto = formPane.getData();

            if (dto == null) {
                JOptionPane.showMessageDialog(this, "数据获取失败");
                return;
            }

            if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请至少填写一条出库明细");
                return;
            }

            service.create(dto);

            JOptionPane.showMessageDialog(this, "提交成功");

            isNewMode = false;
            nav.updateEditState(false);
            refreshNavigationState();
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
            StockOutDTO dto = formPane.getData();

            if (dto == null) {
                JOptionPane.showMessageDialog(this, "数据获取失败");
                return;
            }

            if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请至少填写一条出库明细");
                return;
            }

            dto.setId(currentRecordId);
            dto.setStatus(0);

            service.update(dto);

            JOptionPane.showMessageDialog(this, "更新成功");
            tablePane.refresh();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请填写正确的数字：" + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
        }
    }
}
