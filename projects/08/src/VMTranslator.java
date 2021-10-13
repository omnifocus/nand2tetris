import java.io.File;
import java.io.IOException;

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
        String fileName = "FunctionCalls/NestedCall/sys.vm";
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

        String realPath = System.getProperty("user.dir") + File.separator + fileName;
        Parser parser = new Parser(realPath,writer);
        while(parser.hasMoreCommands()) {
            parser.advance();
        }
        writer.close();

    }
}
