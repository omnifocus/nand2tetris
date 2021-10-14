import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 一上来就写出栈顶值
 */
public class Writer {
    private String filePath;
    private String fileName;
    private PrintWriter pw;
    private static int counter = 0;
    private static int counter_mem = 0;
    private static Map<String,Integer> callCountMap;
    String funcName = "";
//    private static int counter_func = 0;
    private int counter_lcl = 0;

    static {
        callCountMap = new HashMap<>();

    }

    public  void writePreCommands() {
        pw.println("@261");
        pw.println("D=A");
        pw.println("@SP");
        pw.println("M=D");
        pw.println("@LCL");
        pw.println("M=D");
        pw.println("@ARG");
        pw.println("M=D");
        pw.println("@4000");
        pw.println("D=A");
        pw.println("@THIS");
        pw.println("M=D");
        pw.println("@5000");
        pw.println("D=A");
        pw.println("@THAT");
        pw.println("M=D");
        pw.println("@Sys.init");
        pw.println("0;JMP");
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }
    public Writer(String filePath) throws IOException {
        this.filePath = filePath;
        this.fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1,filePath.lastIndexOf("."));
        this.pw = new PrintWriter(new FileWriter(filePath));
        //new完之后，立马写
        writePreCommands();
    }

    void writeFunction(String type,String arg1,Integer arg2) {

        switch (type) {
            case "function":

                /*
                   生成括号标签,代码就一份
                 */
                pw.println("("+arg1+")");
                funcName = arg1;
                //LCL清零参
                //果然不能用视频中
                for (int i=0;i<arg2;i++) {
                    this.pw.println("@"+i);
                    this.pw.println("D=A");
                    this.pw.println("@LCL");
                    this.pw.println("D=D+M");
                    this.pw.println("A=D");
                    this.pw.println("M=0");
                }
                //有局部变量才这样走
                if (arg2 > 0) {
                    //TODO SP挪到LCL+N位的下一位  要不把LCL 放的值都覆盖了
                    this.pw.println("@SP");
                    this.pw.println("M=D+1");
                }


                break;
            case "call":
                /*
                1. 放5个参数以保存
                    问题：如何设定返回地址的变量名 -> call的时候怎么知道在哪个func里调的？
                    答：必然从case function进来 (不能放在该方法中，否则永远是""，而要放局部变量中)，保存住，在return之后清空(并把map的值+1)。
                2. 放置ARG，LCL
                3. LCL 变量清零
                    问：怎么知道有几个LCL ?
                    答：挪到function中处理
                4. goto
                 */
                //根据call的参数 判断return 会不会 覆盖返回地址
                    //没用，因为一个方法可能调用另一个call，而不是直接会调用return
                Integer v = callCountMap.get(arg1) == null ? 0 : callCountMap.get(arg1) ;
                String retAddress = funcName + "$ret." + v;
                callCountMap.put(arg1,v+1);
                this.pw.println("@"+retAddress);
                this.pw.println("D=A");
                //保存return address
                this.pw.println("@SP");
                this.pw.println("A=M");
                this.pw.println("M=D");
                this.pw.println("@SP");
                this.pw.println("M=M+1");

                //保存LCL
                this.pw.println("@LCL");
                this.pw.println("D=M");
                this.pw.println("@SP");
                this.pw.println("A=M");
                this.pw.println("M=D");
                this.pw.println("@SP");
                this.pw.println("M=M+1");

                //保存ARG
                this.pw.println("@ARG");
                this.pw.println("D=M");
                this.pw.println("@SP");
                this.pw.println("A=M");
                this.pw.println("M=D");
                this.pw.println("@SP");
                this.pw.println("M=M+1");

                //保存THIS
                this.pw.println("@THIS");
                this.pw.println("D=M");
                this.pw.println("@SP");
                this.pw.println("A=M");
                this.pw.println("M=D");
                this.pw.println("@SP");
                this.pw.println("M=M+1");

                //保存THAT
                this.pw.println("@THAT");
                this.pw.println("D=M");
                this.pw.println("@SP");
                this.pw.println("A=M");
                this.pw.println("M=D");
                this.pw.println("@SP");
                this.pw.println("M=M+1");
                //放置当前ARG
                this.pw.println("@"+(arg2+5));
                this.pw.println("D=A");
                this.pw.println("@SP");
                //不能！
//                this.pw.println("A=M");
                this.pw.println("D=M-D");
                this.pw.println("@ARG");
                this.pw.println("M=D");
                //放置当前LCL
                this.pw.println("@SP");
                //不能
//                this.pw.println("A=M");
                this.pw.println("D=M");
                this.pw.println("@LCL");
                this.pw.println("M=D");


                this.pw.println("@"+ arg1);
                this.pw.println("0;JMP");
                //填上返回地址
                this.pw.println("(" + retAddress + ")");
                break;
            case "return":
                // +1
                callCountMap.put(funcName,callCountMap.get(funcName) == null ? 0 : callCountMap.get(funcName) + 1);

                /*
                @SP
                A=M-1
                D=M
                
                @Arg
                A=M
                M=D
                
                @LCL
                AM=M-1
                D=M
                
                @THAT
                M=D
                
                @LCL
                AM=M-1
                D=M
                
                @THIS
                M=D
                
                @LCL
                AM=M-1
                D=M
                
                @ARG
                M=D
                
                @LCL
                AM=M-1
                D=M
                
                @LCL
                M=D
                
                @LCL
                A=M-1
                D=M

                 */

                /*
                copy 栈顶值 到 ARG
                ,SP指向ARG + 1,
                恢复各变量，
                跳转
                 */



      //TODO      //判断是否有局部变量
                //不判断了，直接把returnAddr先取出来

                pw.println("@LCL");
                pw.println("D=M");
                pw.println("@R14");
                pw.println("M=D");

                pw.println("@5");
                pw.println("D=A");
                pw.println("@R14");
                pw.println("A=M-D");
                pw.println("D=M");
                //把returnAddr保存起来
                pw.println("@TT");
                pw.println("M=D");


                pw.println("@SP");
                pw.println("A=M-1");
                pw.println("D=M");

                pw.println("@ARG");
                pw.println("A=M");
                pw.println("M=D");

                pw.println("@ARG");
                pw.println("D=M");
                pw.println("@SP");
                pw.println("M=D+1");



                    pw.println("@1");
                    pw.println("D=A");
                    pw.println("@R14");
                    pw.println("A=M-D");
                    pw.println("D=M");

                    pw.println("@THAT");
                    pw.println("M=D");

                    pw.println("@2");
                    pw.println("D=A");
                    pw.println("@R14");
                    pw.println("A=M-D");
                    pw.println("D=M");

                    pw.println("@THIS");
                    pw.println("M=D");

                    pw.println("@3");
                    pw.println("D=A");
                    pw.println("@R14");
                    pw.println("A=M-D");
                    pw.println("D=M");

                    pw.println("@ARG");
                    pw.println("M=D");

                    pw.println("@4");
                    pw.println("D=A");
                    pw.println("@R14");
                    pw.println("A=M-D");
                    pw.println("D=M");

                    pw.println("@LCL");
                    pw.println("M=D");


                    pw.println("@TT");
                    pw.println("A=M");
                    //直接跳转
                    pw.println("0;JMP");

                //感觉这里不用赋空
//                funcName = "";

                break;
        }
    }

    void writeProgramFlow(String type,String arg1) {
        switch (type) {
            case "label":
                this.pw.println("(" + arg1 + ")");
                break;
            case "if-goto":
                //不等0才满足条件
                this.pw.println("@SP");
                //push的用于判断是否为0的值已经没用...
                this.pw.println("AM=M-1");
                this.pw.println("D=M");
                this.pw.println("@"+arg1);
                this.pw.println("D;JNE");
                break;
            case "goto":
                this.pw.println("@"+arg1);
                this.pw.println("0;JMP");
                break;
        }
    }

    void writeArithmetic(String type) throws IOException {
        switch (type) {
            case "add":
                dealWithTwoOprands("add");
                break;
            case "sub":
                dealWithTwoOprands("sub");
                break;
            case "neg":
                dealWithOneOprand("neg");
                break;
            case "eq":
                dealWithTwoOprands("eq");
                break;
            case "gt":
                dealWithTwoOprands("gt");
                break;
            case "lt":
                dealWithTwoOprands("lt");
                break;
            case "and":
                dealWithTwoOprands("and");
                break;
            case "or":
                dealWithTwoOprands("or");
                break;
            case "not":
                dealWithOneOprand("not");
                break;

        }

    }

    /**
     * @seg
     * D=M
     *
     * @index
     * AD=D+A
     * D=M
     *
     * @SP
     * A=M
     * M=D
     *
     * @SP
     * M=M+1
     *
     *
     * @param seg
     * @param index
     */
    void writePush(String seg,int index) {
        if ("constant".equals(seg)) {
            pw.println("@"+index);
            pw.println("D=A");
            pw.println("@SP");
            pw.println("A=M");
            pw.println("M=D");
            pw.println("@SP");
            pw.println("M=M+1");
            return;
        }
        switch (seg){
            case "local":
                seg = "LCL";
                break;
            case "argument":
                seg = "ARG";
                break;
            case "this":
                seg = "THIS";
                break;
            case "that":
                seg = "THAT";
                break;

        }
        if ("static".equals(seg)) {
            String var = fileName+"."+index;
            pw.println("@"+var);
            pw.println("D=M");
            pw.println("@SP");
            pw.println("A=M");
            pw.println("M=D");
            pw.println("@SP");
            pw.println("M=M+1");
            return;
        } else if ("pointer".equals(seg)) {
            if (index == 0) {
                pw.println("@THIS");
            } else {
                pw.println("@THAT");
            }
            pw.println("D=M");
            pw.println("@SP");
            pw.println("A=M");
            pw.println("M=D");
            pw.println("@SP");
            pw.println("M=M+1");
            return;
        } else if ("temp".equals(seg)) {
            pw.println("@R5");
            pw.println("D=A");
        } else {
            pw.println("@" + seg);
            pw.println("D=M");
        }
        pw.println("@"+index);
        pw.println("A=D+A");
        pw.println("D=M");
        pw.println("@SP");
        pw.println("A=M");
        pw.println("M=D");
        pw.println("@SP");
        pw.println("M=M+1");
    }


    /**
     * @SP
     * A=M-1
     * D=M
     * @addr
     * M=D
     * @index
     * D=A
     * @seg
     * D=M+D
     * @addr2
     * M=D
     * @addr
     * D=M
     * @addr2
     * A=M
     * M=D
     * @param seg
     * @param index
     */
    void writePop(String seg, int index) {
        pw.println("@SP");
        pw.println("AM=M-1");
        pw.println("D=M");
        if ("static".equals(seg)) {
            String var = fileName+"."+index;
            pw.println("@"+var);
            pw.println("M=D");
            return;
        }

        switch (seg){
            case "local":
                seg = "LCL";
                break;
            case "argument":
                seg = "ARG";
                break;
            case "this":
                seg = "THIS";
                break;
            case "that":
                seg = "THAT";
                break;
        }
         if ("pointer".equals(seg)) {
            if (index == 0) {
                pw.println("@THIS");
            } else {
                pw.println("@THAT");
            }
            pw.println("M=D");
            //上面步骤已经对SP里内容-1了
            return;
        }
        else {
             pw.println("@addr"+counter_mem);
             pw.println("M=D");
             pw.println("@"+index);
             pw.println("D=A");
             if ("temp".equals(seg)) {
                 pw.println("@R5");
                 //直接加地址
                 pw.println("D=A+D");
             } else {
                 pw.println("@" + seg);
                 pw.println("D=M+D");
             }
         }
        pw.println("@addr_" +counter_mem);
        pw.println("M=D");
        pw.println("@addr" + counter_mem);
        pw.println("D=M");
        pw.println("@addr_" +counter_mem);
        pw.println("A=M");
        pw.println("M=D");

        counter_mem++;
    }

    /**
     * M-D才是对的顺序，先放的-后放的
     * @param type
     * @throws IOException
     */
    private void dealWithTwoOprands(String type) throws IOException {
        
        pw.println("@SP");
        
        pw.println("AM=M-1");
        
        pw.println("D=M");
        
        pw.println("@SP");
        //SP上面减一次就ok了
        pw.println("AM=M-1");
        
        switch (type) {
            case "add":
                pw.println("M=D+M");
                appendTail();
                break;
            case "sub":
                pw.println("M=M-D");
                appendTail();
                break;
            case "and":
                pw.println("M=D&M");
                appendTail();
                break;
            case "or":
                pw.println("M=D|M");
                appendTail();
                break;
//逻辑运算符 -1为true 0 为 false， 突然间想到了跳转功能
            case "eq":
                pw.println("D=M-D");
                pw.println("@CMD_FALSE" + counter);
                pw.println("D;JNE");

                appendLogicTail();

                break;
            case "gt":
                pw.println("D=M-D");
                pw.println("@CMD_FALSE" + counter);
                pw.println("D;JLE");
                appendLogicTail();


                break;
            case "lt":
                pw.println("D=M-D");
                pw.println("@CMD_FALSE" + counter);
                pw.println("D;JGE");
                appendLogicTail();

                break;

        }

        counter++;


        
    }

    private void appendTail() {
        pw.println("@SP");
        pw.println("M=M+1");
    }

    private void appendLogicTail() {
        //都会牵涉TRUE FALSE的跳转，而Assemble code自带了根据条件跳转的功能
        this.pw.println("(CMD_TRUE"+counter+")");
        this.pw.println("@SP");
        this.pw.println("A=M");
        this.pw.println("M=-1");
        this.pw.println("@CUR_CMD"+counter+"");
        this.pw.println("0;JMP");

        this.pw.println("(CMD_FALSE"+counter+")");
        this.pw.println("@SP");
        this.pw.println("A=M");
        this.pw.println("M=0");
        pw.println("(CUR_CMD"+counter+")");
        pw.println("@SP");
        pw.println("M=M+1");
    }


    private void dealWithOneOprand(String type) throws IOException {
        pw.println("@SP");
        
        pw.println("AM=M-1");
        

        switch (type) {
            case "not":
                pw.println("M=!M");
                
                break;
            case "neg":
                pw.println("M=-M");
                
                break;
        }
        pw.println("@SP");
        
        pw.println("M=M+1");
        
    }




    void close() throws IOException {
        pw.flush();
    }
}
