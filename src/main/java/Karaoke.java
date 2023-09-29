import Interpreter.ObjSystem.Entity;
import Interpreter.ObjSystem.Environment;
import Interpreter.*;
import Lexer.*;
import Parser.AST.Program;
import Parser.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import static Interpreter.Interpreter.NULL;

public class Karaoke {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: karaoke [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        Environment env = new Environment();
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()), env);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        Environment env = new Environment();
        for (;;) {
            System.out.print(">> ");
            String line = reader.readLine();
            if (Objects.equals(line, "")) break;
            run(line, env);
            /* Lexer Debug Code */
//            Lexer scan = new Lexer(line);
//            System.out.println(scan.tokenize());
        }
    }

    private static void run(String source, Environment env) {
        Lexer scanner = new Lexer(source);
        Parser par = new Parser(scanner);
        Program prg = par.parseProgram();
        Entity eval;
        if (!par.getErrors().isEmpty())
            par.getErrors().forEach(System.out::println);
        else {
            /*Parser Debug Code*/
//            System.out.println(prg);
            eval = Interpreter.eval(prg, env);
            System.out.println(eval.Inspect());
        }
    }
}




//    static boolean hadError = false;

//    static void error(int line, String msg) {
//        report(line, "", msg);
//    }
//
//    private static void report(int line, String where, String msg) {
//        System.err.println("[line "+ line +"] Error" + where +": " + msg);
//        hadError = true;
//    }