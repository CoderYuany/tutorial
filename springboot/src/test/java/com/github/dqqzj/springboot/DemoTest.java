package com.github.dqqzj.springboot;

import java.util.Date;

/**
 * author: qinzhongjian
 * jdk: 11.0.4
 * date: 2019/11/16 09:41
 * description: TODO
 */
public class DemoTest {
    public static void main(String[] args){
        Student d1 = new Student("s1");
        nextDateUpdate(d1);
        System.out.println(d1.toString());
        Student d2 = new Student("s2");
        nextDateReplace(d2);
        System.out.println(d2);
    }

    private static void nextDateUpdate(Student student) {
        student.setName("s11");
        System.out.println(student.toString());
    }
    private static void nextDateReplace(Student student) {
        student = new Student("s22");
        System.out.println(student.toString());
    }
   static class Student{
        String name;

        public Student(String name) {
            this.name = name;
        }
       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       @Override
       public String toString() {
           return "Student{" +
                   "name='" + name + '\'' +
                   '}';
       }
   }
}
