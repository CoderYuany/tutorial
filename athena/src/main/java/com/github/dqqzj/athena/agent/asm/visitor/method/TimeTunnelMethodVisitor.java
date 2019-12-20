package com.github.dqqzj.athena.agent.asm.visitor.method;

import java.util.ArrayList;

import com.github.dqqzj.athena.agent.Advice;
import com.github.dqqzj.athena.agent.collect.TimeTunnelCollect;
import com.github.dqqzj.athena.agent.utils.Type;
import com.github.dqqzj.athena.agent.utils.TypeUtils;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import static org.springframework.asm.Opcodes.AALOAD;
import static org.springframework.asm.Opcodes.AASTORE;
import static org.springframework.asm.Opcodes.ALOAD;
import static org.springframework.asm.Opcodes.ANEWARRAY;
import static org.springframework.asm.Opcodes.ASTORE;
import static org.springframework.asm.Opcodes.BIPUSH;
import static org.springframework.asm.Opcodes.DUP;
import static org.springframework.asm.Opcodes.GETSTATIC;
import static org.springframework.asm.Opcodes.ICONST_0;
import static org.springframework.asm.Opcodes.ICONST_1;
import static org.springframework.asm.Opcodes.INVOKESPECIAL;
import static org.springframework.asm.Opcodes.INVOKESTATIC;
import static org.springframework.asm.Opcodes.INVOKEVIRTUAL;
import static org.springframework.asm.TypeReference.NEW;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 11:05
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
public class TimeTunnelMethodVisitor extends MethodVisitor {

    public String name ;
    /**
     * 存放入参的类型Type
     */
    public ArrayList<Type> parInputITypes;
    /**
     *  存放最后一个变量出栈的位置，即为返回值的下标！！
     */
    public int lastVar ;
    /**
     * 存放输出类型
     */
    public Type parOutType;
    public String path;
    public Advice advice;
    public TimeTunnelMethodVisitor(MethodVisitor mv, String n, ArrayList<Type> ITypes,Advice ad) {
        super(Opcodes.ASM5, mv);
        name = n;
        parInputITypes = ITypes;
        advice = ad;
    }

    @Override
    public void visitCode() {
        try {
            dumpAdvice( "./" + advice.getTargetClass().getName() + "@" + advice.getMethodName());
        }catch (Exception e){}

        int nextIndex = getNextIndex();

        /**
         * @see {String clazz = Thread.currentThread() .getStackTrace()[1].getClassName();}
         */
        int clazzNameIndex = nextIndex++;
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
        mv.visitInsn(ICONST_1);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StackTraceElement", "getClassName", "()Ljava/lang/String;", false);
        mv.visitVarInsn(ASTORE, clazzNameIndex);
        /**
         * @see {String method = Thread.currentThread() .getStackTrace()[1].getMethodName();}
         */
        int methodNameIndex = nextIndex++;
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
        mv.visitInsn(ICONST_1);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StackTraceElement", "getMethodName", "()Ljava/lang/String;", false);
        mv.visitVarInsn(ASTORE, methodNameIndex);
        /**
         * @see {String path = clazz+"@"+method+"#"+System.currentTimeMillis();}
         */
        int pathIndex = nextIndex++;
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitVarInsn(ALOAD, clazzNameIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        mv.visitLdcInsn("@");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        mv.visitVarInsn(ALOAD, methodNameIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        mv.visitLdcInsn("#");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitVarInsn(ASTORE, pathIndex);
        /**
         * @see {ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path),true));}
         */
        int outFileIndex = nextIndex++;
        mv.visitTypeInsn(NEW, "java/io/ObjectOutputStream");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/io/FileOutputStream");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/io/File");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, pathIndex);
        mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ICONST_1);
        mv.visitMethodInsn(INVOKESPECIAL, "java/io/FileOutputStream", "<init>", "(Ljava/io/File;Z)V", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/io/ObjectOutputStream", "<init>", "(Ljava/io/OutputStream;)V", false);
        mv.visitVarInsn(ASTORE, outFileIndex);


        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("开始采集方法："+name + "\n");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        // 计算申请来的数组存放的位置
        int objectArrayIndex = nextIndex++;
        if(parInputITypes !=null && parInputITypes.size()>0) {


            /**申请空间advice保留请求参数的object[]**/

            //object[] 数组大小为parInputType的大小
            mv.visitIntInsn(BIPUSH, parInputITypes.size());
            mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
            mv.visitVarInsn(ASTORE, objectArrayIndex);
            int index = 1;
            for (int i = 0; i < parInputITypes.size(); i++) {

                Type iType = parInputITypes.get(i);

                mv.visitVarInsn(ALOAD, objectArrayIndex);
                // object[]的下标
                mv.visitInsn(ICONST_0+i);
                // 局部变量的下标
                mv.visitVarInsn(iType.getLoadOpcode(), index);
                // 如果是8种常量类型，则需要调用String.valueOf的方法
                if(TypeUtils.isBasicType(iType.classType)) {
                    String owner = TypeUtils.getBoxType(iType.classType);
                    String descriptor = "(" + iType.desc + ")L" + owner + ";";
                    mv.visitMethodInsn(INVOKESTATIC, owner, "valueOf", descriptor, false);
                }
                mv.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/JSON", "toJSONString", "(Ljava/lang/Object;)Ljava/lang/String;", false);
                mv.visitInsn(AASTORE);

                index += iType.classTypeByteSize;

            }
        }


        mv.visitVarInsn(ALOAD, outFileIndex);
        mv.visitVarInsn(ALOAD, objectArrayIndex);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/ObjectOutputStream", "writeObject", "(Ljava/lang/Object;)V", false);

        super.visitCode();
    }


    public void dumpAdvice(String path) throws Exception{
        TimeTunnelCollect.collect(advice,path);
    }


    @Override
    public void visitVarInsn(int opcode, int var) {
        lastVar = var;
        super.visitVarInsn(opcode,var);
    }


    public int getNextIndex(){
        int x = 0;
        for(int i=0; i<parInputITypes.size(); i++) {
            x +=parInputITypes.get(i).classTypeByteSize;
        }
        return x+1;
    }

}
