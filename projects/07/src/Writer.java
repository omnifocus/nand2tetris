import java.io.*;

public class Writer {
    private String filePath;
    private String fileName;
    private PrintWriter pw;
    private static int counter = 0;
    private static int counter_mem = 0;

    public Writer(String filePath) throws IOException {
        this.filePath = filePath;
        this.fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1,filePath.lastIndexOf("."));
        this.pw = new PrintWriter(new FileWriter(filePath));
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
        if ("static".equals(seg)) {
            String var = fileName+"."+index;
            pw.println("@SP");
            pw.println("AM=M-1");
            pw.println("D=M");
            pw.println("@"+var);
            pw.println("M=D");
            return;
        }
        pw.println("@SP");
        pw.println("AM=M-1");
        pw.println("D=M");
        pw.println("@addr"+counter_mem);
        pw.println("M=D");
        pw.println("@"+index);
        pw.println("D=A");
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
            pw.println("@SP");
            pw.println("A=M");
            pw.println("D=M");
            if (index == 0) {
                pw.println("@THIS");
            } else {
                pw.println("@THAT");
            }
            pw.println("M=D");
            //上面步骤已经对SP里内容-1了
            return;
        }
        else if ("temp".equals(seg)) {
            pw.println("@R5");
            //直接加地址
            pw.println("D=A+D");
        } else {
            pw.println("@" + seg);
            pw.println("D=M+D");
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
