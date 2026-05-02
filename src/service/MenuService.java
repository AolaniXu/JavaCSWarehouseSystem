package service;

import javax.swing.*;

import java.awt.Window;
import java.awt.KeyboardFocusManager;
import java.util.*;
import model.MenuItem;
import ui.MainFrame;
import func.AbstractButtonFunction;

public class MenuService {

    public JMenuBar buildMenuBar(List<MenuItem> list) {

        JMenuBar menuBar = new JMenuBar();

        // 1. 先缓存所有 JMenu（用于找父菜单）
        Map<Integer, JMenu> menuMap = new HashMap<>();

        // 2. 第一步：创建所有“菜单（JMenu）”
        for (MenuItem item : list) {

            if (Boolean.TRUE.equals(item.getIsMenu())) {

                JMenu menu = new JMenu(item.getTitle());
                menuMap.put(item.getCode(), menu);

                // 判断是否一级菜单（简单规则：没有父级）
                if (isTopLevel(item.getCode(), list)) {
                    menuBar.add(menu);
                }
            }
        }

        // 3. 第二步：挂载菜单项（JMenuItem）
        for (MenuItem item : list) {

            if (Boolean.FALSE.equals(item.getIsMenu())) {

                JMenuItem menuItem = new JMenuItem(item.getTitle());

                // 点击事件（反射执行）
                menuItem.addActionListener(e -> {
                    invoke(item.getFuncClassName(), menuItem);
                });

                JMenu parent = findParentMenu(item.getCode(), menuMap);

                if (parent != null) {
                    parent.add(menuItem);
                }
            }
        }

        return menuBar;
    }

    // 判断一级菜单（没有父级）
    private boolean isTopLevel(Integer code, List<MenuItem> list) {

        for (MenuItem item : list) {

            if (!item.getCode().equals(code)) {

                // 如果存在“以它开头的更长code”，说明它是父菜单
                if (String.valueOf(item.getCode()).startsWith(String.valueOf(code))
                        && item.getCode().toString().length() > code.toString().length()) {
                    return true;
                }
            }
        }

        return false;
    }

    // 找父菜单
    private JMenu findParentMenu(Integer code, Map<Integer, JMenu> menuMap) {

        String codeStr = String.valueOf(code);

        // 从后往前截取，找最近父级
        for (int i = codeStr.length() - 1; i > 0; i--) {

            String parentCodeStr = codeStr.substring(0, i);
            Integer parentCode = Integer.parseInt(parentCodeStr);

            JMenu menu = menuMap.get(parentCode);

            if (menu != null) {
                return menu;
            }
        }

        return null;
    }

    // 反射执行
    private void invoke(String className, JMenuItem src) {

        try {
            Class<?> clazz = Class.forName(className);

            AbstractButtonFunction func = (AbstractButtonFunction) clazz.getDeclaredConstructor().newInstance();

            func.executeFunction(src);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
