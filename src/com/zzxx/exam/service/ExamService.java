package com.zzxx.exam.service;

import com.zzxx.exam.controller.ClientContext;
import com.zzxx.exam.entity.*;
import com.zzxx.exam.ui.MsgFrame;
import com.zzxx.exam.util.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 所有业务模型：登录，开始考试，查看规则，交卷，上一题，下一题....
 */

public class ExamService {
    private EntityContext entityContext;
    private ClientContext clientContext;
    private MsgFrame msgFrame;

    public User login(String id, String pwd) throws IdOrPwdException {
        Map<String, User> users = entityContext.getUsers();
        User user = users.get(id);
        if (user != null) {
            if (pwd.equals(user.getPassword()) && id.equals(user.getId())) {
                return user;
            }
        }
        throw new IdOrPwdException("编号/密码错误!");
    }

    public void setEntityContext(EntityContext entityContext) {
        this.entityContext = entityContext;
    }

    public void setClientContext(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    public ExamInfo startExam(User user) {
        ExamInfo info = new ExamInfo();
        Config config = new Config("config.properties");

        info.setQuestionCount(config.getInt("QuestionNumber"));
        info.setTimeLimit(config.getInt("TimeLimit"));
        info.setTitle(config.getString("PaperTitle"));
        info.setUser(user);

        creatExamPaper();
        return info;
    }

    //生成一套试卷
    private void creatExamPaper() {
        Random r = new Random();
        int index = 0;
        for (int level = Question.LEVEL1; level <= Question.LEVEL10; level++) {
            List<Question> list = entityContext.findQuestionByLevel(level);
            Question q1 = list.remove(r.nextInt(list.size()));
            Question q2 = list.remove(r.nextInt(list.size()));
            paper.add(new QuestionInfo(index++, q1));
            paper.add(new QuestionInfo(index++, q2));
        }
    }

    // 定义一套试卷
    private List<QuestionInfo> paper = new ArrayList<>();

    //返回一套试题
    public QuestionInfo getQuestionFormPaper(int i) {
        return paper.get(i);
    }

    //显示考试规则信息
    public String getShowMsg(String filename) throws IOException {
        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String str;
        String s = new String();
        while ((str = fr.readLine()) != null) {
            s += str + "\n";
        }
        return s;
    }
}
