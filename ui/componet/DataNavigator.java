package ui.componet;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DataNavigator extends JPanel {
    public JButton newButton, audiButton, saveButton, prevButton, nextButton;

    //回调接口，让外部页面处理逻辑
    public interface Listener {
        
        
    }

    private Listener listener;

    public DataNavigator() {
        //初始化数据导航条并布局
        initUI();
        bindEvents();

    }

    private void initUI(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
        newButton = new JButton("新建");
        saveButton = new JButton("提交");
        audiButton = new JButton("审核");
        prevButton = new JButton("上一条");
        nextButton = new JButton("下一条");

        this.add(newButton);
        this.add(saveButton);
        this.add(audiButton);
        this.add(prevButton);
        this.add(nextButton);

    }

    private void bindEvents(){

    }

    public void setListener(Listener listener){
        this.listener = listener;
    }



}
