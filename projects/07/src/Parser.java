import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private String filePath;
    private BufferedReader br;
    private String currentLine;
    private String[] parts;
    private Writer writer;

    public Parser(String filePath,Writer writer) throws FileNotFoundException {
        this.filePath = filePath;
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
                writer.writePop(arg1(),arg2());
            } else if (type == CommandType.C_PUSH) {
                writer.writePush(arg1(),arg2());
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
        return  Integer.parseInt(parts[2]);
    }
}
