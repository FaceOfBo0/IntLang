import Scanner.Lexer;

public class MainProgram {

    public static void main(String[] args) {
        String test = "let a = (b + 12) * 14; let a = f == b; a != false";
        System.out.println(test);
        System.out.println("--------");
        Lexer scanner = new Lexer(test);
        scanner.tokenize().forEach(System.out::println);
    }
}