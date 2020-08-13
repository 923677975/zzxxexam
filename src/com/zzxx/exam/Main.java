package com.zzxx.exam;

import com.zzxx.exam.controller.ClientContext;
import com.zzxx.exam.entity.EntityContext;
import com.zzxx.exam.entity.Question;
import com.zzxx.exam.entity.QuestionInfo;
import com.zzxx.exam.service.ExamService;
import com.zzxx.exam.ui.*;

import java.io.IOException;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws IOException {
        ClientContext clientContext = new ClientContext();

        LoginFrame loginFrame = new LoginFrame();
        MenuFrame menuFrame = new MenuFrame();
        ExamFrame examFrame = new ExamFrame();
        MsgFrame msgFrame = new MsgFrame();
        WelcomeWindow welcomeWindow = new WelcomeWindow();

        ExamService examService = new ExamService();

        EntityContext entityContext = new EntityContext();
        QuestionInfo questionInfo = new QuestionInfo();

        //注入依赖
        clientContext.setLoginFrame(loginFrame);
        clientContext.setMenuFrame(menuFrame);
        clientContext.setExamFrame(examFrame);
        clientContext.setMsgFrame(msgFrame);
        clientContext.setWelcomeWindow(welcomeWindow);
        clientContext.setClientContext(clientContext);

        menuFrame.setController(clientContext);
        msgFrame.setController(clientContext);
        loginFrame.setController(clientContext);
        examFrame.setController(clientContext);

        examService.setClientContext(clientContext);
        clientContext.setExamService(examService);

        examService.setEntityContext(entityContext);

        clientContext.show();
    }
}
