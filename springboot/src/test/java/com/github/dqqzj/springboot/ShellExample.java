package com.github.dqqzj.springboot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShellExample {

    public static void main(String[] args) throws IOException {
        String file = "/Users/qinzhongjian/IdeaProjects/tutorial/springboot/src/test/java/com/github/dqqzj/springboot/a.log";
        List<String> lines = shellForCat(file);
        //grep abc，相当于filter
        lines.stream().filter(s -> s.contains("abc"))
        //sort 按照字典序从小到大排序
                .sorted()
                .distinct() //uniq
                .collect(Collectors.toList())
        .forEach(System.out::println);
    }
    /**
     * @author qinzhongjian
     * @date 2019/12/26 22:39
     * @param fileName
     * @return java.util.List<java.lang.String>
     * @description 根据文件名全路径获取数据
     * cat命令，相当于是读取文件中的所有行
     */
    private static List<String> shellForCat(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            lines.add(str);
        }
        return lines;
    }
}
