package com.zzxx.exam.controller;

import com.zzxx.exam.entity.ExamInfo;
import com.zzxx.exam.entity.QuestionInfo;
import com.zzxx.exam.entity.User;
import com.zzxx.exam.service.ExamService;
import com.zzxx.exam.service.IdOrPwdException;
import com.zzxx.exam.ui.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端控制器：进行页面和业务模型之间的数据传递/交互
 */
public class ClientContext {
    private ClientContext clientContext;

    private LoginFrame loginFrame;
    private MenuFrame menuFrame;
    private ExamFrame examFrame;
    private WelcomeWindow welcomeWindow;
    private MsgFrame msgFrame;

    private User user;//记录登录的用户

    private ExamService examService;

    //登录,并跳转到主页面
    public void login() {
        String id = loginFrame.getIdField().getText();
        String pwd = loginFrame.getPwdField().getText();
        try {
            user = examService.login(id, pwd);
            menuFrame.setInfo(user.getName());
            loginFrame.setVisible(false);
            menuFrame.setVisible(true);
        } catch (IdOrPwdException e) {
            loginFrame.updateMessage(e.getMessage());
        }
    }

    //显示考试规则
    public void msgStart() throws IOException {
        msgFrame.setVisible(true);
        String str = examService.getShowMsg("./src/com/zzxx/exam/util/rule.txt");
        msgFrame.showMsg(str);
    }

    //开始考试
    public void start() {
        //获取考试信息
        ExamInfo examInfo = examService.startExam(user);
        //获取第一道考题
        currentQuestionInfo = examService.getQuestionFormPaper(0);
        examFrame.updateView(examInfo, currentQuestionInfo);
        menuFrame.setVisible(false);
        examFrame.setVisible(true);
        clientContext.setTimer(examInfo);
    }

    // 记录正在作答的题目信息
    private QuestionInfo currentQuestionInfo;

    private int questionIndex = 0; // -- 可以使用currentQuestionInfo.getQuestionIndex()取代

    //上一题
    public void questionPrev() {
        if (questionIndex == 0) {
            JOptionPane.showMessageDialog(null, "已经是第一题了！");
        } else {
            questionIndex--;
            currentQuestionInfo = examService.getQuestionFormPaper(questionIndex);
            clientContext.setQuestionIndex(questionIndex);
            examFrame.nextQuestion(currentQuestionInfo);

        }
    }

    public List<Integer> setOptions(List<Integer> list) {
        examService.getQuestionFormPaper(questionIndex).setUserAnswers(list);
        return examService.getQuestionFormPaper(questionIndex + 1).getUserAnswers();
    }

    public List<Integer> getOptions() {
        return examService.getQuestionFormPaper(questionIndex ).getUserAnswers();
    }

    //下一题
    public void questionNext() {
        try {
            questionIndex++;
            currentQuestionInfo = examService.getQuestionFormPaper(questionIndex);
            clientContext.setQuestionIndex(questionIndex);
            examFrame.nextQuestion(currentQuestionInfo);
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "题目已经做完！");
        }
    }

    public void setTimer(ExamInfo exam) {
        int second = exam.getTimeLimit() * 60;
        new Thread() {
            @Override
            public void run() {
                for (int i = second; i > 0; i--) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        examFrame.setTimer(i);
                    }
                }
            }
        }.start();

    }

    //设置左下角的题号
    public void setQuestionIndex(int questionIndex) {
        examFrame.setQuestionCount(questionIndex);
    }

    //关闭msg框
    public void msgClose() {
        msgFrame.setVisible(false);
    }

    //启动程序时,延时启动的登录页面
    public void show() {
        welcomeWindow.setVisible(true);
        try {
            Robot r = new Robot();
            r.delay(2000);
        } catch (AWTException e) {

        }
        welcomeWindow.setVisible(false);
        loginFrame.setVisible(true);
    }

    //返回登录页面
    public void backLogin() {
        menuFrame.setVisible(false);
        loginFrame.setVisible(true);
    }

    //关闭考试页面
    public void examFrameClose() {
        examFrame.setVisible(false);
        menuFrame.setVisible(true);
    }

    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    public void setMenuFrame(MenuFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    public void setExamService(ExamService examService) {
        this.examService = examService;
    }

    public void setExamFrame(ExamFrame examFrame) {
        this.examFrame = examFrame;
    }

    public void setMsgFrame(MsgFrame msgFrame) {
        this.msgFrame = msgFrame;
    }

    public void setWelcomeWindow(WelcomeWindow welcomeWindow) {
        this.welcomeWindow = welcomeWindow;
    }

    public void setClientContext(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

}
