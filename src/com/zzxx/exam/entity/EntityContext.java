package com.zzxx.exam.entity;

import com.zzxx.exam.util.Config;

import java.io.*;
import java.util.*;

/**
 * 实体数据管理, 用来读取数据文件放到内存集合当中
 */
public class EntityContext {

    //key - 用户编号id, value - 用户对象.
    private Map<String, User> users = new HashMap<>();
    //key - 试题难度级别, 难度级别对应的所有试题.
    private Map<Integer, List<Question>> questions = new HashMap<>();

    public EntityContext() throws IOException {
        loadUsers("./src/com/zzxx/exam/util/user.txt");
        loadQuestion("./src/com/zzxx/exam/util/corejava.txt");
    }

    /**
     * 读取user.txt文件，将其中的数据，封装为用户对象
     * 路径："./src/com/zzxx/exam/util/user.txt"
     */
    public void loadUsers(String filename) throws IOException {
        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String str;
        String[] userInfo;
        while ((str = fr.readLine()) != null) {
            userInfo = str.split(":");
            if (userInfo.length == 5) {
                User u = new User(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
                users.put(userInfo[0], u);
            }
        }
    }

    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * 读取corejava.txt文件, 将其中的数据封装为Question对象, 存储到集合中
     * 路径："./src/com/zzxx/exam/util/corejava.txt"
     */
    public void loadQuestion(String filename) throws IOException {
        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

        String str;
        while ((str = fr.readLine()) != null) {
            String[] ss = str.split(",");
            Question q = new Question();
            q.setScore(Integer.parseInt(ss[1].split("=")[1]));
            q.setLevel(Integer.parseInt(ss[2].split("=")[1]));
            if (ss[0].split("=")[1] == "1") {
                q.setType(Question.SINGLE_SELECTION);
            } else {
                q.setType(Question.MULTI_SELECTION);
            }
            q.setTitle(fr.readLine());

            List<String> list = new ArrayList<>();
            list.add(fr.readLine());
            list.add(fr.readLine());
            list.add(fr.readLine());
            list.add(fr.readLine());
            q.setOptions(list);

            int level = q.getLevel();
            List<Question> qus = this.questions.get(level);
            if (qus == null) {
                qus = new ArrayList<>();
            }
            qus.add(q);
            questions.put(level, qus);
        }
    }

    public List<Question> findQuestionByLevel(int level) {
        return new ArrayList<>(questions.get(level));
    }
}
