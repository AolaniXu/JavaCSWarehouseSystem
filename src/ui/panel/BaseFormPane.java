package ui.panel;

import javax.swing.JPanel;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFormPane<T> extends JPanel {

    public BaseFormPane() {
        // 不做任何调用，避免调用到子类未初始化的成员
    }

    // 统一初始化入口（由子类主动调用）
    protected final void init() {
        initComponents();
        layoutComponents();
        bindEvents();
    }

    // ===== 生命周期方法 =====
    protected abstract void initComponents();
    protected abstract void layoutComponents();

    // 可选
    protected void bindEvents() {}

    // 可选通用能力
    public abstract void reset();

    
    public abstract T getData();
}
