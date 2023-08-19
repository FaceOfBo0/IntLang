import Lexer.Lexer;

public class MainProgram {

    public static void main(String[] args) {
        String test = "let a = (b + 12) * 143";
        System.out.println(test);
        System.out.println("--------");
        Lexer.tokenize(test).forEach(System.out::println);
    }
}