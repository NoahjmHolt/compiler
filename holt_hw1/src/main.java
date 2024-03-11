//import ADT.Lexical;

//import ADT.LexicalReserve;
import ADT.*;
//import ADT.Lexical;
/**
 *
 * @author nholt Spring 2024
 */
public class main {
    public static void main(String[] args) {
        String inFileAndPath = "LexicalTestSP24.txt"; //args[0]; //
        String outFileAndPath = "OutputLexical.txt"; //args[1];  //
        System.out.println("Lexical for " + inFileAndPath);
        boolean traceOn = true;

        // Create a symbol table to store appropriate ident, number,string
        // symbols found NO RESERVE WORDS GO IN THE SYMBOL TABLE!
        SymbolTable symbolList;
        symbolList = new SymbolTable(150);
        Lexical myLexer = new Lexical(inFileAndPath, symbolList, traceOn);
        Lexical.token currToken;
        currToken = myLexer.GetNextToken();

        while (currToken != null) {
            System.out.println("\t" + currToken.mnemonic + " | \t" +
                    String.format("%04d", currToken.code)
                    + " | \t" + currToken.lexeme);
            currToken = myLexer.GetNextToken();
        }

        symbolList.PrintSymbolTable(outFileAndPath);
        System.out.println("Done.");
    }
}