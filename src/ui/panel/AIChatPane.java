package ui.panel;

import javax.swing.*;

import service.AIChatService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AIChatPane extends JPanel {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private AIChatService chatService;

    public AIChatPane() {
        chatService = new AIChatService();
        initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(5, 5));

        // 聊天记录区
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder("对话记录"));

        // 输入区
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputField.addActionListener(e -> sendMessage());

        sendButton = new JButton("发送");
        sendButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        sendButton.addActionListener(e -> sendMessage());

        clearButton = new JButton("清空");
        clearButton.addActionListener(e -> {
            chatArea.setText("");
            chatService.clearHistory();
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(clearButton, BorderLayout.WEST);

        // 欢迎语
        chatArea.setText("=== 库存智能查询助手 ===\n\n");
        chatArea.append("您好！我是库存智能查询助手，可以帮您：\n");
        chatArea.append("• 查询库存情况\n");
        chatArea.append("• 分析出入库数据\n");
        chatArea.append("• 库存预警提醒\n\n");
        chatArea.append("请输入您的问题，例如：\n");
        chatArea.append("  \"查询库存低于10的商品\"\n");
        chatArea.append("  \"哪些商品需要补货\"\n");
        chatArea.append("  \"本月入库统计\"\n\n");

        this.add(chatScroll, BorderLayout.CENTER);
        this.add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) {
            return;
        }

        // 显示用户输入
        chatArea.append("【您】" + userInput + "\n\n");
        inputField.setText("");
        System.out.println("[Pane] 用户输入完成");

        // 记录等待提示开始位置
        final int waitStartPos = chatArea.getText().length();
        chatArea.append("【助手】正在思考...\n\n");
        chatArea.setCaretPosition(chatArea.getText().length());
        System.out.println("[Pane] 显示等待提示完成，等待位置: " + waitStartPos);

        // 后台调用 AI
        new Thread(() -> {
            String response = chatService.getResponse(userInput);
            System.out.println("[Pane] AI 响应收到，长度: " + response.length());

            // 在 UI 线程更新
            SwingUtilities.invokeLater(() -> {
                String currentText = chatArea.getText();
                System.out.println("[Pane] 当前文本长度: " + currentText.length());

                // 从等待提示开始位置替换到末尾
                String beforeWait = currentText.substring(0, waitStartPos);
                String newText = beforeWait + "【助手】" + response + "\n\n";
                chatArea.setText(newText);
                chatArea.setCaretPosition(chatArea.getText().length());
                System.out.println("[Pane] UI更新完成，新文本长度: " + chatArea.getText().length());
            });
        }).start();
    }
}