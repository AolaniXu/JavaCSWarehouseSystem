package ui.componet;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DataNavigator extends JPanel {
    public JButton newButton;
    public JButton firstButton;
    public JButton prevButton;
    public JButton nextButton;
    public JButton lastButton;
    public JButton saveButton;
    public JButton deleteButton;
    public JButton auditButton;

    // 回调接口，让外部页面处理逻辑
    public interface Listener {
        void onNew();        // 新建
        void onFirst();      // 首条
        void onPrev();       // 上一条
        void onNext();       // 下一条
        void onLast();       // 末条
        void onSave();       // 提交
        void onDelete();     // 删除
        void onAudit();      // 审核
    }

    private Listener listener;

    public DataNavigator() {
        // 初始化数据导航条并布局
        initUI();
        bindEvents();
    }

    private void initUI() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        newButton = new JButton("新建");
        firstButton = new JButton("首条");
        prevButton = new JButton("上一条");
        nextButton = new JButton("下一条");
        lastButton = new JButton("末条");
        saveButton = new JButton("提交");
        deleteButton = new JButton("删除");
        auditButton = new JButton("审核");

        this.add(newButton);
        this.add(firstButton);
        this.add(prevButton);
        this.add(nextButton);
        this.add(lastButton);
        this.add(saveButton);
        this.add(deleteButton);
        this.add(auditButton);

        // 初始状态：浏览按钮不可用（没有数据时）
        firstButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        lastButton.setEnabled(false);
        deleteButton.setEnabled(false);
        auditButton.setEnabled(false);
    }

    private void bindEvents() {
        newButton.addActionListener(e -> {
            if (listener != null) listener.onNew();
        });
        firstButton.addActionListener(e -> {
            if (listener != null) listener.onFirst();
        });
        prevButton.addActionListener(e -> {
            if (listener != null) listener.onPrev();
        });
        nextButton.addActionListener(e -> {
            if (listener != null) listener.onNext();
        });
        lastButton.addActionListener(e -> {
            if (listener != null) listener.onLast();
        });
        saveButton.addActionListener(e -> {
            if (listener != null) listener.onSave();
        });
        deleteButton.addActionListener(e -> {
            if (listener != null) listener.onDelete();
        });
        auditButton.addActionListener(e -> {
            if (listener != null) listener.onAudit();
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    // 更新导航按钮状态
    public void updateNavigationState(boolean hasPrev, boolean hasNext) {
        prevButton.setEnabled(hasPrev);
        nextButton.setEnabled(hasNext);
        firstButton.setEnabled(hasPrev);
        lastButton.setEnabled(hasNext);
    }

    // 更新编辑按钮状态（浏览模式/编辑模式）
    public void updateEditState(boolean isNewMode) {
        if (isNewMode) {
            saveButton.setText("提交");
        } else {
            saveButton.setText("更新");
        }
    }
}
