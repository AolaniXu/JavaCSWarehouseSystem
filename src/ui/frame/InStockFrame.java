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
    private StockInTablePane tablePane;
    private DataNavigator nav;
    private StockInService service = new StockInServiceImpl();

    // 编辑状态
    private boolean isNewMode = true;
    private int currentRecordId = -1;
    private int currentStatus = 0;  // 0=未审核, 1=已审核

    // 导航状态
    private List<Integer> recordIds;
    private int currentIndex = -1;

    public InStockFrame() {

        setTitle("入库管理");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {

        // 左：表单
        formPane = new InStockPane();

        // 右：表格
        tablePane = new StockInTablePane();
        tablePane.setOnRowSelectedListener(this::onRowSelected);

        // 下：导航条
        nav = new DataNavigator();
        nav.setListener(this);

        // 组装
        setLeft(formPane);
        setRight(tablePane);
        setBottom(nav);
    }

    // 表格行选择回调
    private void onRowSelected(StockInDTO dto) {
        formPane.setData(dto);
        isNewMode = false;
        currentRecordId = dto.getId();
        currentStatus = dto.getStatus();  // 加载状态
        nav.updateEditState(false);
        refreshNavigationState();
        updateButtonState();  // 根据状态更新按钮
    }

    // 根据审核状态更新按钮
    private void updateButtonState() {
        if (currentStatus == 1) {
            // 已审核：禁用修改、删除按钮
            nav.saveButton.setEnabled(false);
            nav.deleteButton.setEnabled(false);
            nav.auditButton.setText("已审核");
            nav.auditButton.setEnabled(false);
        } else {
            // 未审核：启用修改、删除按钮
            nav.saveButton.setEnabled(true);
            nav.deleteButton.setEnabled(currentRecordId > 0);
            nav.auditButton.setText("审核");
            nav.auditButton.setEnabled(currentRecordId > 0);
        }
    }

    // 刷新导航状态
    private void refreshNavigationState() {
        recordIds = service.getAllIds();
        currentIndex = recordIds.indexOf(currentRecordId);
        nav.updateNavigationState(
                currentIndex > 0,
                currentIndex < recordIds.size() - 1
        );
    }

    // 加载记录
    private void loadRecord(int id) {
        StockInDTO dto = service.findById(id);
        if (dto != null) {
            formPane.setData(dto);
            currentRecordId = id;
            currentStatus = dto.getStatus();
            isNewMode = false;
            nav.updateEditState(false);
            refreshNavigationState();
            updateButtonState();
        }
    }

    // ===== DataNavigator.Listener 实现 =====

    @Override
    public void onNew() {
        formPane.reset();
        isNewMode = true;
        currentRecordId = -1;
        currentStatus = 0;
        currentIndex = -1;
        nav.updateEditState(true);
        nav.updateNavigationState(false, false);
        nav.saveButton.setEnabled(true);
        nav.deleteButton.setEnabled(false);
        nav.auditButton.setText("审核");
        nav.auditButton.setEnabled(false);
        tablePane.refresh();
    }

    @Override
    public void onSave() {
        if (currentStatus == 1) {
            JOptionPane.showMessageDialog(this, "已审核的单据不能修改");
            return;
        }
        if (isNewMode) {
            insertData();
        } else {
            updateData();
        }
    }

    @Override
    public void onDelete() {
        if (currentStatus == 1) {
            JOptionPane.showMessageDialog(this, "已审核的单据不能删除");
            return;
        }
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
                onNew();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    @Override
    public void onAudit() {
        if (currentRecordId <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要审核的单据");
            return;
        }
        if (currentStatus == 1) {
            JOptionPane.showMessageDialog(this, "该单据已经审核过了");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "审核通过后，库存将自动增加。确定要审核吗？",
                "确认审核",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.audit(currentRecordId);
                JOptionPane.showMessageDialog(this, "审核成功，库存已更新");

                // 重新加载当前记录，刷新状态
                loadRecord(currentRecordId);
                tablePane.refresh();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "审核失败：" + ex.getMessage());
            }
        }
    }

    // 导航方法
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

    // 插入数据
    private void insertData() {
        try {
            StockInDTO dto = formPane.getData();
            if (dto == null) {
                JOptionPane.showMessageDialog(this, "数据获取失败");
                return;
            }
            if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请至少填写一条入库明细");
                return;
            }

            service.create(dto);
            JOptionPane.showMessageDialog(this, "提交成功（未审核）");

            // 保存成功后加载这条记录
            // 注意：insert 后返回的 ID 没有保存，这里简单处理
            currentRecordId = dto.getId() != null ? dto.getId() : -1;
            isNewMode = false;
            nav.updateEditState(false);
            refreshNavigationState();
            updateButtonState();
            tablePane.refresh();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请填写正确的数字：" + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "提交失败：" + ex.getMessage());
        }
    }

    // 更新数据
    private void updateData() {
        if (currentRecordId <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择要更新的记录");
            return;
        }

        try {
            StockInDTO dto = formPane.getData();
            if (dto == null) {
                JOptionPane.showMessageDialog(this, "数据获取失败");
                return;
            }
            if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请至少填写一条入库明细");
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