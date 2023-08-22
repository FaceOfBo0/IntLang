import Scanner.Lexer;

public class MainProgram {

    public static void main(String[] args) {
        String test = "\nlet a = (b + 12) * 14; let tr = check == test";
        System.out.println(test);
        System.out.println("--------");
        Lexer scanner = new Lexer(test);
        scanner.tokenize().forEach(System.out::println);
        System.out.println(scanner.source);
    }
}