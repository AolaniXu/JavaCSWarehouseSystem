package service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import dao.InventoryDao;
import dao.StockInDao;
import dao.StockOutDao;
import model.InventoryView;
import model.StockInView;
import model.StockOutView;

public class AIChatService {

    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    private static final String API_KEY = "sk-13ffdb50f5a24967aea8754d9a40dadc";

    // 系统提示词
    private static final String SYSTEM_PROMPT = "你是一个库存管理系统的智能助手。用户会询问关于库存、入库、出库等问题。\n" +
            "你可以帮助用户：\n" +
            "1. 查询库存情况\n" +
            "2. 分析出入库数据\n" +
            "3. 给出库存预警\n" +
            "4. 回答系统使用问题\n\n" +
            "请用简洁友好的语言回答用户问题。如果用户提供的数据为空，请如实告知。";

    public String getResponse(String userInput) {
        try {
            // 构建提示词（包含数据库上下文）
            String prompt = buildPrompt(userInput);

            // 调用 API
            String response = callDeepSeekAPI(prompt);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，服务暂时不可用，请稍后再试。\n错误信息：" + e.getMessage();
        }
    }

    private String buildPrompt(String userInput) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(SYSTEM_PROMPT).append("\n\n");

        // 添加当前数据库状态作为上下文
        prompt.append("【当前数据库状态】\n");

        // 库存情况
        InventoryDao inventoryDao = new InventoryDao();
        prompt.append("全部库存：\n");
        java.util.List<InventoryView> inventoryList = inventoryDao.findAllForTable();
        if (inventoryList.isEmpty()) {
            prompt.append("（暂无库存数据）\n");
        } else {
            for (InventoryView v : inventoryList) {
                prompt.append("- ").append(v.getProductCode())
                        .append(" (").append(v.getProductName() != null ? v.getProductName() : "").append(")")
                        .append("：").append(v.getQuantity()).append("\n");
            }
        }

        // 入库记录
        StockInDao stockInDao = new StockInDao();
        prompt.append("\n最近入库记录：\n");
        int count = 0;
        for (StockInView v : stockInDao.findAllForTable()) {
            if (count++ >= 5) break;
            String status = v.getStatus() == 1 ? "已审核" : "未审核";
            prompt.append("- 单号:").append(nullToEmpty(v.getInvoiceNo()))
                    .append(" 供应商:").append(nullToEmpty(v.getSupplier()))
                    .append(" 状态:").append(status).append("\n");
        }

        // 出库记录
        StockOutDao stockOutDao = new StockOutDao();
        prompt.append("\n最近出库记录：\n");
        count = 0;
        for (StockOutView v : stockOutDao.findAllForTable()) {
            if (count++ >= 5) break;
            String status = v.getStatus() == 1 ? "已审核" : "未审核";
            prompt.append("- 单号:").append(nullToEmpty(v.getInvoiceNo()))
                    .append(" 客户:").append(nullToEmpty(v.getCustomer()))
                    .append(" 状态:").append(status).append("\n");
        }

        prompt.append("\n【用户问题】\n").append(userInput);

        return prompt.toString();
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private String callDeepSeekAPI(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        // 构建 JSON 请求体
        String jsonBody = buildJsonBody(prompt);
        System.out.println("[AI] 发送请求体: " + jsonBody.substring(0, Math.min(200, jsonBody.length())) + "...");

        // 发送请求
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 读取响应
        int responseCode = conn.getResponseCode();
        System.out.println("[AI] 响应码: " + responseCode);

        if (responseCode != 200) {
            // 读取错误信息
            StringBuilder errorMsg = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    errorMsg.append(line);
                }
            }
            System.out.println("[AI] 错误响应: " + errorMsg);
            return "API 调用失败，错误码：" + responseCode + "\n错误信息：" + errorMsg;
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        String resp = response.toString();
        System.out.println("[AI] 原始响应: " + resp.substring(0, Math.min(500, resp.length())) + "...");

        // 解析 JSON
        return extractContentFromJson(resp);
    }

    private String buildJsonBody(String prompt) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"model\": \"deepseek-chat\",");
        json.append("\"messages\": [");
        json.append("{\"role\": \"user\", \"content\": ");
        json.append("\"").append(escapeJson(prompt)).append("\"");
        json.append("}],");
        json.append("\"temperature\": 0.7");
        json.append("}");
        return json.toString();
    }

    private String extractContentFromJson(String json) {
        int choicesStart = json.indexOf("\"choices\"");
        if (choicesStart == -1) return "无法解析响应";

        int contentKey = json.indexOf("\"content\"", choicesStart);
        if (contentKey == -1) return "无法解析内容";

        int colon = json.indexOf(':', contentKey);
        if (colon == -1) return "无法解析内容";

        int quoteStart = json.indexOf('"', colon);
        if (quoteStart == -1) return "无法解析内容";
        quoteStart++;

        int contentEnd = quoteStart;
        while (contentEnd < json.length()) {
            char c = json.charAt(contentEnd);
            if (c == '"' && json.charAt(contentEnd - 1) != '\\') {
                break;
            }
            contentEnd++;
        }
        if (contentEnd >= json.length()) return "无法解析内容";

        String content = json.substring(quoteStart, contentEnd);
        return unescapeJson(content);
    }

    private String unescapeJson(String value) {
        return value
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public void clearHistory() {
        // 暂时不需要历史记录
    }
}
