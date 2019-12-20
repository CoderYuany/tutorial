package com.github.dqqzj.athena.agent.asm.visitor;

import java.util.ArrayList;

import com.github.dqqzj.athena.agent.Advice;
import com.github.dqqzj.athena.agent.asm.visitor.method.TimeTunnelMethodVisitor;
import com.github.dqqzj.athena.agent.utils.Type;
import com.github.dqqzj.athena.agent.utils.TypeUtils;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;

import static org.springframework.asm.Opcodes.ASM5;

/**
 * @author wb-qzj584329
 * @create 2019/12/20 11:01
 * @description TODO
 * @since JDK1.8.0_211-b12
 */
public class TimeTunnelClassVisitor extends ClassVisitor {

    public String methodName ;
    public Advice advice;
    public String path ;


    public TimeTunnelClassVisitor(final ClassVisitor cv, String mn,Advice ad) {
        super(ASM5, cv);
        methodName = mn;
        advice = ad;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        //MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if (name.equals(methodName)) {
            ArrayList<Type> parInputITypes = TypeUtils.getInputParameterTypesByDesc(desc);
            //IType parOutputTypes = ASMTypeUtil.getOutputParameterTypesByDesc(desc);
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, new String[]{"java/lang/Exception"});
            return new TimeTunnelMethodVisitor(mv,name, parInputITypes,advice);
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

}
