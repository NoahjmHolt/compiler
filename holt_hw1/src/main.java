//import ADT.SymbolTable;
//import ADT.Lexical;
import ADT.*;
/**
 *
 * @author nholt SP2024
 */
public class main {
    public static void main(String[] args) {
        String filePath = args[0];
        System.out.println("Code Generation SP 2024, by Noah Holt");
        System.out.println("Parsing "+filePath);
        boolean traceon = false; //true;//
        Syntactic parser = new Syntactic(filePath, traceon);
        parser.parse();
        System.out.println("Done.");
    }
}
