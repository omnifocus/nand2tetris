import java.io.*;

public class Parser {
    private String filePath;
    private BufferedReader br;
    private String currentLine;
    private String[] parts;
    private Writer writer;
    //获取当前解析的文件名，static变量名用
    private String fileName;

    public Parser(String filePath,Writer writer) throws FileNotFoundException {
        this.filePath = filePath;
        this.fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1,filePath.lastIndexOf("."));

        br = new BufferedReader(new FileReader(filePath));
        this.writer = writer;
    }

    boolean hasMoreCommands() throws IOException {
        currentLine = br.readLine();
        return null != currentLine;
    }

    void advance() throws IOException {

        //去掉空格
        removeBlank();

//        System.out.println("commandType:" + commandType() + "; arg1: " + arg1() + "; arg2: " + arg2());
       CommandType type =  commandType();
        if (parts != null) {
            String command = parts[0];
            if (type == CommandType.C_ARITHMETIC) {
                writer.writeArithmetic(command);
            } else if (type == CommandType.C_POP) {
                writer.writePop(fileName,arg1(),arg2());
            } else if (type == CommandType.C_PUSH) {
                writer.writePush(fileName,arg1(),arg2());
            } else if (type == CommandType.C_PROGRAMFLOW) {
                writer.writeProgramFlow(parts[0],arg1());
            } else if (type == CommandType.C_FUNCTION) {
                writer.writeFunction(parts[0],arg1(),arg2());
            }
        }
    }

    private void removeBlank() {
        if(currentLine.contains("//")) {
           currentLine = currentLine.substring(0,currentLine.indexOf("//"));
        }

    }

    //先调用该方法，parts就有值了
    CommandType commandType() {
        if (null == currentLine || "".equals(currentLine)) {
            return null;
        }
       parts =  currentLine.split(" ");
        CommandType type ;
        switch (parts[0]) {
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not":
                type = CommandType.C_ARITHMETIC;break;
            case "pop":
                type = CommandType.C_POP;break;
            case "push":
                type = CommandType.C_PUSH; break;
            case "label":
            case "if-goto":
            case "goto":
                type = CommandType.C_PROGRAMFLOW;break;
            case "return":
            case "call":
            case "function":
                type = CommandType.C_FUNCTION;break;

            default:
                throw new IllegalStateException("Unexpected value: " + parts[0]);
        }
       return type;

    }

    String arg1() {
        if (commandType() == null || parts.length == 1)
            return null;
       return  parts[1];
    }

    Integer arg2() {
        if (commandType() == null || parts.length <= 2) {
            return null;
        }
        //trim 去掉 pop local 0 //
        // 0后注释之前的空格要去掉
        return  Integer.parseInt(parts[2].trim());
    }
}
