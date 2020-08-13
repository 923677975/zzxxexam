package com.zzxx.exam.ui;

import com.zzxx.exam.controller.ClientContext;
import com.zzxx.exam.entity.ExamInfo;
import com.zzxx.exam.entity.QuestionInfo;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ExamFrame extends JFrame {

    private ClientContext clientContext;
    private JTextArea questionArea;
    private JButton next;
    private JButton prev;
    private JLabel questionCount;
    private JLabel timer;
    private JLabel examInfo;

    private List<Integer> list = new ArrayList<Integer>();
    private List<Integer> list1 = new ArrayList<Integer>();

    // 选项集合, 方便答案读取的处理
    private Option[] options = new Option[4];

    public ExamFrame() {
        init();
    }

    private void init() {
        setTitle("指针信息在线测评");
        setSize(600, 380);
        setContentPane(createContentPane());        //设置JFrame的内容
        setLocationRelativeTo(null);        //窗口被放置在屏幕的中心
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);      //设置用户在此窗体上发起 "close" 时默认执行的操作
        //窗口监听,关闭窗口
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                clientContext.examFrameClose();
            }
        });
    }

    private JPanel createContentPane() {
        JPanel pane = new JPanel(new BorderLayout());   //new BorderLayout()创建边界布局
        pane.setBorder(new EmptyBorder(6, 6, 6, 6));//setBorder设置此组件的边框.    EmptyBorder一个提供一个空的透明边框的类，占用空间但不绘图。
        ImageIcon icon = new ImageIcon(getClass().getResource("pic/exam_title.png"));  //从图像绘制图标的图标界面的实现。  添加图片路径

        pane.add(BorderLayout.NORTH, new JLabel(icon));
        pane.add(BorderLayout.CENTER, createCenterPane());
        pane.add(BorderLayout.SOUTH, createToolsPane());
        return pane;
    }

    //创建中心内容    继续套用了边界布局
    private JPanel createCenterPane() {
        JPanel pane = new JPanel(new BorderLayout());
        // 注意!
        examInfo = new JLabel("姓名:XXX 考试:XXX 考试时间:XXX", JLabel.CENTER);
        pane.add(BorderLayout.NORTH, examInfo);
        pane.add(BorderLayout.CENTER, createQuestionPane());
        pane.add(BorderLayout.SOUTH, createOptionsPane());
        return pane;
    }

    //创建问题选项框
    private JPanel createOptionsPane() {
        JPanel pane = new JPanel();
        Option a = new Option(0, "A");
        Option b = new Option(1, "B");
        Option c = new Option(2, "C");
        Option d = new Option(3, "D");
        options[0] = a;
        options[1] = b;
        options[2] = c;
        options[3] = d;
        pane.add(a);
        pane.add(b);
        pane.add(c);
        pane.add(d);
        return pane;
    }

    //创建问题框
    private JScrollPane createQuestionPane() {
        JScrollPane pane = new JScrollPane();
        pane.setBorder(new TitledBorder("题目"));// 标题框

        // 注意!
        questionArea = new JTextArea();
        questionArea.setText("问题\nA.\nB.");
        questionArea.setLineWrap(true);// 允许折行显示
        questionArea.setEditable(false);// 不能够编辑内容
        // JTextArea 必须放到 JScrollPane 的视图区域中(Viewport)
        pane.getViewport().add(questionArea);
        return pane;
    }

    //读取当前的题号
    public void setQuestionCount(int questionCount) {
        this.questionCount.setText("当前题目: 第" + (questionCount + 1) + "题");
    }

    //读取剩余时间
    public void setTimer(int second) {
        this.timer.setText("剩余时间:" + second + "秒");
    }

    //底部信息栏
    private JPanel createToolsPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(new EmptyBorder(0, 10, 0, 10));
        // 注意!
        questionCount = new JLabel("当前题目: 第1题");

        timer = new JLabel("剩余时间:3600秒");

        pane.add(BorderLayout.WEST, questionCount);
        pane.add(BorderLayout.EAST, timer);
        pane.add(BorderLayout.CENTER, createBtnPane());
        return pane;
    }

    //创建底部选项框
    private JPanel createBtnPane() {
        JPanel pane = new JPanel(new FlowLayout());
        prev = new JButton("上一题");
        next = new JButton("下一题");
        JButton send = new JButton("交卷");

        pane.add(prev);
        pane.add(next);
        pane.add(send);

        //上一题按钮监听
        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientContext.questionPrev();
                list = clientContext.getOptions();
                int a, i = 0;
                while (i < list1.size() - 1) {
                    a = list.get(i);
                    options[a].setSelected(true);
                }
            }
        });

        //下一题按钮监听
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientContext.questionNext();
                for (int i = 0; i < 4; i++) {
                    if (options[i].isSelected()) {
                        list.add(options[i].value);
                        options[i].setSelected(false);
                    }
                }
                list1 = clientContext.setOptions(list);
                int a, i = 0;
                while (i < list1.size() - 1) {
                    a = list1.get(i);
                    options[a].setSelected(true);
                }
            }
        });

        //交卷按钮监听
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        return pane;
    }

    //更新上方的显示信息
    public void updateView(ExamInfo exam, QuestionInfo currentQuestionInfo) {
        examInfo.setText("姓名:" + exam.getUser().getName() + "  考试:" + exam.getTitle() + "  考试时间:" + exam.getTimeLimit() + "分钟");
        questionArea.setText(currentQuestionInfo.toString());
    }

    public void nextQuestion(QuestionInfo currentQuestionInfo) {
        questionArea.setText(currentQuestionInfo.toString());
    }

    /**
     * 使用内部类扩展了 JCheckBox 增加了val 属性, 代表答案值
     */
    class Option extends JCheckBox {
        int value;

        public Option(int val, String txt) {
            super(txt);
            this.value = val;
        }

        @Override
        public String toString() {
            return "Option{" +
                    "value=" + value +
                    '}';
        }
    }

    public void updateTime(long h, long m, long s) {
        String time = h + ":" + m + ":" + s;
        if (m < 5) {
            timer.setForeground(new Color(0xC85848));
        } else {
            timer.setForeground(Color.blue);
        }
        timer.setText(time);
    }

    public void setController(ClientContext clientContext) {
        this.clientContext = clientContext;
    }
}