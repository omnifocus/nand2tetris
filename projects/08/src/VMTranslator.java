import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VMTranslator {
    public static void main(String[] args) throws IOException {
//        String fileName = args == null ? "" : args[0];
//        String fileName = "StackArithmetic/StackTest/StackTest.vm";
//        String fileName = "MemoryAccess/BasicTest/BasicTest.vm";
//        String fileName = "MemoryAccess/PointerTest/PointerTest.vm";
//        String fileName = "MemoryAccess/StaticTest/StaticTest.vm";
//         String fileName = "ProgramFlow/BasicLoop/BasicLoop.vm";
//         String fileName = "ProgramFlow/FibonacciSeries/FibonacciSeries.vm";
//         String fileName = "FunctionCalls/SimpleFunction/SimpleFunction.vm";

        //用文件夹命名输出的asm文件
//        String fileName = "FunctionCalls/NestedCall/sys.vm";

        //输入内容变为文件夹，搜索里面每个以vm结尾的文件，合并成文件夹名.asm

        String fileName = "FunctionCalls/FibonacciElement/";


        if ("".equals(fileName)) {
            System.out.println("必须指定一个文件名");
            return ;
        }



//        String nameFrag = fileName.substring(0,fileName.lastIndexOf("."));
//        String writePath = System.getProperty("user.dir") + File.separator + nameFrag + ".asm";
        String nameFrag = fileName.substring(0,fileName.lastIndexOf(File.separator));
        String _fileName = nameFrag.substring(nameFrag.indexOf(File.separator)+1);
        String writePath = System.getProperty("user.dir") + File.separator + nameFrag + File.separator + _fileName + ".asm";
        Writer writer = new Writer(writePath);

        //此次的fileName需要遍历得知
        List<String> fileNameList = getAllVMFiles(fileName);

        for (String realName : fileNameList) {
            Parser parser = new Parser(realName, writer);
            while (parser.hasMoreCommands()) {
                parser.advance();
            }
        }
        writer.close();

    }

    /***
     * 把sys.vm放在第一个解析
     * @param fileName vm文件所在的文件夹
     *
     * @return
     */
    private static List<String> getAllVMFiles(String fileName) {
        List<String> realNameList = new ArrayList<>();
        File[] files = new File(fileName).listFiles();
        List<File> fileList = new ArrayList(Arrays.asList(files));
        int index = -1;
        for (int i=0;i<fileList.size();i++) {
            String path = fileList.get(i).getAbsolutePath();
            if (!path.endsWith(".vm")) {
                continue;
            }
            if (path.endsWith("Sys.vm")) {
                index = i;
                continue;
            }
            realNameList.add(fileList.get(i).getAbsolutePath());
        }
       String vmFilePath = fileList.get(index).getAbsolutePath();
        realNameList.add(0,vmFilePath);
        System.out.println(realNameList);

        return realNameList;

    }



}
