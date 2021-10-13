import com.sun.tools.javac.util.Assert;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.util.*;

public class MyAssembler {
    private static BufferedReader br;
    private static String outfilePath ;
    private static StringBuffer sb;
    private static Map<String,Integer> symbolMap ;
    private static Map<String,Integer> varMap;
    private static int initVarAddr = 16;



    /**
     * 输出的文件跟输入的文件所在目录一致，且只是后缀从.asm改为.hack
     */
    static {
      InputStream is =  MyAssembler.class.getClassLoader().getResourceAsStream("file.prop");
        Properties properties = new Properties();
        try {
            properties.load(is);
           String fullpath =  properties.getProperty("name");
          String filename =  fullpath.substring(fullpath.lastIndexOf("/")+1,fullpath.lastIndexOf("."));
          br = new BufferedReader(new InputStreamReader(new FileInputStream(fullpath)));
          outfilePath = fullpath.substring(0,fullpath.lastIndexOf("/") + 1) + filename + ".hack";
          sb = new StringBuffer();
            varMap = new HashMap<>();
            symbolMap = new HashMap<>();
            symbolMap.put("R0",0);
            symbolMap.put("R1",1);
            symbolMap.put("R2",2);
            symbolMap.put("R3",3);
            symbolMap.put("R4",4);
            symbolMap.put("R5",5);
            symbolMap.put("R6",6);
            symbolMap.put("R7",7);
            symbolMap.put("R8",8);
            symbolMap.put("R9",9);
            symbolMap.put("R10",10);
            symbolMap.put("R11",11);
            symbolMap.put("R12",12);
            symbolMap.put("R13",13);
            symbolMap.put("R14",14);
            symbolMap.put("R15",15);
            symbolMap.put("SCREEN",16384);
            symbolMap.put("KBD",24576);
            symbolMap.put("SP",0);
            symbolMap.put("LCL",1);
            symbolMap.put("ARG",2);
            symbolMap.put("THIS",3);
            symbolMap.put("THAT",4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String res2;

    public static void main(String[] args) {


//        String str = "@R10";
//        System.out.println(str.replace("R1","1"));
        //1. remove symbol address
            // blank
            assemble();
                //做了三件事儿
                // predefined  @R0
                // variable @i
                // LOOP ()


        // 2. parse A and C

//        System.out.println(sb.toString());

//        String syn = "MD=D+1;JEQ";
//        System.out.println(parseAC(syn));
//        System.out.println("1110011111011010".equals(parseAC(syn)));


    }

//其实只需要@R1 中的R1替换为1，之后的R15会自动换为15... String的replace足矣
    private static String removePredefined(String line) {
        if ("".equals(line)) return "";
//        System.out.println("before rp: " + line);
        for (Map.Entry<String,Integer> entry : symbolMap.entrySet()) {
           line = line.replace(entry.getKey(),String.valueOf(entry.getValue()));
        }
        return line;
//        System.out.println("after rp: " +  line);
    }

    private void sustitueLOOP() {

    }

    private static String removeVar(String line) {
        if ("".equals(line)) return "";
        System.out.println("rV之前：" + line);


        //@开头 在以上map找不到的,addr++,替换；能找到，直接替换
        if (line.startsWith("@")) {
            String word =  line.substring(line.indexOf("@")+1);

            //如果symbolMap中的，已经替换为数字了,则不会报异常，直接过；否则按普通变量处理
            try {
                Integer.parseInt(word);
            } catch (Exception e) {
                if (varMap.get(word) == null) {
                    varMap.put(word,initVarAddr);
                    //新值替换
                    line = line.replace(word, String.valueOf(initVarAddr));
                    initVarAddr ++;
                } else {
                    //原值替换
                    line = line.replace(word, String.valueOf(varMap.get(word)));
                }
            }

        }
        System.out.println("rV之后：" + line);
        return line;

    }

    private static String removeBlank(String line) {
        if (line.contains("//")) {
            line = line.substring(0,line.indexOf("//"));
        }
        line = line.replace(" ","");
        return line;
    }

    /**
     * 挨行读，直到读到的是空为止。
         * 如果有 //
         *  截掉//之后的内容
         * 把" "换成""
         * 如果最终是""，继续下一行；否则输出该行并加\n
     *
     *
     */
    private static void assemble() {
        List<String> list = new ArrayList<>();
        try {
            String line;

            while ((line = br.readLine()) != null) {

                String res1 = removeBlank(line);
                if (!"".equals(res1))
                    list.add(res1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> newList = removeParenthesis(list);

        for (String line : newList) {
            String res2 = removePredefined(line);
            String res3 = removeVar(res2);
            String res4 = parseAC(res3);
            if (!"".equals(res4))   sb.append(res4+"\n");

        }
//        删掉最后的换行
        try {
            sb.delete(sb.length()-1,sb.length());
            FileWriter fw = new FileWriter(outfilePath);
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 把(XX)替换为下一行的行号
     * @param list
     * @return
     */
    private static List<String> removeParenthesis(List<String> list) {
        Iterator<String> iterator = list.iterator();
        int count = 0;
        List<String> newList = new ArrayList<>();
        while (iterator.hasNext()) {
            String line = iterator.next();
            //去(),没值加到map里，地址++，删掉当前行，有值直接删掉当前行
            if (line.contains("(") || line.contains(")")) {
                String word =  line.substring(line.indexOf("(")+1,line.indexOf(")"));
                varMap.put(word,count);
                //不加这行
            } else {
                newList.add(line);
                count++;
            }

        }
        return newList;
    }

    private static String parseAC(String res3) {
        if ("".equals(res3)) return "";
        String finalVForA = "";
        if (res3.startsWith("@")) {
            //转成0+15位的二进制
            String num = res3.substring(1);
            String v = Integer.toBinaryString(Integer.parseInt(num));
            if (v.length() < 16) {
                StringBuffer sb = new StringBuffer();
                for (int i=0;i<16-v.length();i++) {
                    sb.append("0");
                }
                sb.append(v);
                finalVForA = sb.toString();
            }

//            System.out.println("parseA之前：" + res3);
//            System.out.println("parseA之后：" + finalVForA);
            return finalVForA;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("111");
        //parts不可能为空
       String[] parts =  res3.split(";");
       //parts[0] 可能没 = ,即computeString
       String[] destParts = parts[0].split("=");
        String destString = "";
        String computeString = "";
        String jumpString = "";

       if (destParts.length == 1) {
           computeString = destParts[0];
       } else {
           destString = destParts[0];
           computeString = destParts[1];
       }

       if (parts.length > 1) {
           jumpString = parts[1];
       }

       //按顺序解析
       dealWithCompute(computeString,sb);
       dealWithDest(destString,sb);
       dealWithJump(jumpString,sb);


        return sb.toString();

    }

    private static void dealWithJump(String jumpString, StringBuffer sb) {
        if (jumpString == null || "".equals(jumpString)) {
            sb.append("000");
            return;
        }
        switch (jumpString) {
            case "JGT":
                sb.append("001");break;
            case "JEQ":
                sb.append("010");break;
            case "JGE":
                sb.append("011");break;
            case "JLT":
                sb.append("100");break;
            case "JNE":
                sb.append("101");break;
            case "JLE":
                sb.append("110");break;
            case "JMP":
                sb.append("111");break;
        }
    }

    private static void dealWithCompute(String computeString, StringBuffer sb) {
        if (computeString == null || "".equals(computeString)) {
            sb.append("000");
            return;
        }
    //有M出现，必然是1
        if (computeString.contains("M")) {
            sb.append("1");
        } else {
            sb.append("0");
        }

        switch (computeString) {
            case "0" :
                sb.append("101010");break;
            case "1":
                sb.append("111111");break;
            case "-1":
                sb.append("111010");break;
            case "D":
                sb.append("001100");break;
            case "A":
            case "M":
                sb.append("110000");break;
            case "!D":
                sb.append("001101");break;
            case "!A":
            case "!M":
                sb.append("110001");break;
            case "-D":
                sb.append("001111");break;
            case "-A":
            case "-M":
                sb.append("110011");break;
            case "D+1":
                sb.append("011111");break;
            case "A+1":
            case "M+1":
                sb.append("110111");break;
            case "D-1":
                sb.append("001110");break;
            case "A-1":
            case "M-1":
                sb.append("110010");break;
            case "D+A":
            case "D+M":
                sb.append("000010");break;
            case "D-A":
            case "D-M":
                sb.append("010011");break;
            case "A-D":
            case "M-D":
                sb.append("000111");break;
            case "D&A":
            case "D&M":
                sb.append("000000");break;
            case "D|A":
            case "D|M":
                sb.append("010101");break;

        }


    }

    private static void dealWithDest(String destString, StringBuffer sb) {
        if (destString == null || "".equals(destString)) {
            sb.append("000");
            return;
        }
        switch (destString) {
            case "M":
                sb.append("001");break;
            case "D":
                sb.append("010");break;
            case "MD":
                sb.append("011");break;
            case "A":
                sb.append("100");break;
            case "AM":
                sb.append("101");break;
            case "AD":
                sb.append("110");break;
            case "AMD":
                sb.append("111");break;
        }
    }


}
