package com.github.dqqzj.athena.agent.utils;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 11:27
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
public class Type {
    /**
     * 加载该变量所需的Opcode
     */
    public int loadOpcode;
    /**
     * 变量描述
     */
    public String desc;
    /**
     * 变量类型,八种基本类型，保留原来的类型,eg. int ,short
     */
    public String classType;
    /**
     * 占用变量栈的大小
     */
    public int classTypeByteSize ;
    /**
     * 打印时所需的描述，在打印的时候，如果是一个Agent.test的类，是不能直接把Agent.test放在println中的desc中，
     *而是需要把Java.lang.Object放上去
     */
    public String printDesc ;

    /**
     * 保留拆箱和装箱的类型
     */
    public String BoxClassType;


    public Type(int loadOpcode, String desc, String dataType, int byteSize,String pd) {
        this.loadOpcode = loadOpcode;
        this.desc = desc;
        this.classType = dataType;
        this.classTypeByteSize = byteSize;
        this.printDesc = pd;
    }

    public int getLoadOpcode() {
        return loadOpcode;
    }

    public void setLoadOpcode(int loadOpcode) {
        this.loadOpcode = loadOpcode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String dataType) {
        this.classType = dataType;
    }

    public int getClassTypeByteSize() {
        return classTypeByteSize;
    }

    public void setClassTypeByteSize(int classTypeByteSize) {
        this.classTypeByteSize = classTypeByteSize;
    }

    public String getPrintDesc() {
        return printDesc;
    }

    public void setPrintDesc(String printDesc) {
        this.printDesc = printDesc;
    }
}
