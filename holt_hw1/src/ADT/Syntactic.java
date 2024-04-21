/*
STUDENTS MUST CHANGE THE 4-CHAR MNEMONICS USED HERE TO THEIR OWN MNEMONICS
SAMPLE syntactic CODE FOR SP24 Compiler Class.

See TEMPLATE at end of this file for the framework to be used for
ALL non-terminal methods created.

Four methods shown below are added to LEXICAL, where EOF, reserve and mnemonic
tables are accessible:
public boolean EOF(){
  //EOF is a private boolean in Lexical
  return EOF;
}
 
public int codeFor(String mnemonic){
    return mnemonics.LookupName(mnemonic);
}
Returns the Reserve Word for the given mnemonic
public String reserveFor(String mnemonic){
    return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
}

Allows control of whether tokens are printed within Lexical or not
public void setPrintToken(boolean on){
    printToken = on;
}

Add 2 lines which prints each token found by GetNextToken:
            if (printToken) {
                System.out.println("\t" + result.mnemonic + " | \t" + String.format("%04d", result.code) + " | \t" + result.lexeme);
            }

 */
package ADT;

/**
 *
 * @author abrouill
 * @editor nholt
 */
public class Syntactic {

    private String filein;              //The full file path to input file
    private SymbolTable symbolList;     //Symbol table storing ident/const
    private Lexical lex;                //Lexical analyzer 
    private Lexical.token token;        //Next Token retrieved 
    private boolean traceon;            //Controls tracing mode 
    private int level = 0;              //Controls indent for trace mode
    private boolean anyErrors;          //Set TRUE if an error happens 

    private final int symbolSize = 250;

    //region Given

    public Syntactic(String filename, boolean traceOn) {
        filein = filename;
        traceon = traceOn;
        symbolList = new SymbolTable(symbolSize);
        lex = new Lexical(filein, symbolList, true);
        lex.setPrintToken(traceOn);
        anyErrors = false;
    }

    //The interface to the syntax analyzer, initiates parsing
// Uses variable RECUR to get return values throughout the non-terminal methods    
    public void parse() {
        int recur = 0;
// prime the pump to get the first token to process
        token = lex.GetNextToken();
// call PROGRAM
        recur = Program();
    }

    //Non-Terminal PROG IDENTIFIER is fully implemented here, leave it as-is.
    private int ProgIdentifier() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        // This non-term is used to uniquely mark the program identifier
        if (token.code == lex.codeFor("IDNT")) {
            // Because this is the progIdentifier, it will get a 'P' type to prevent re-use as a var
            symbolList.UpdateSymbol(symbolList.LookupSymbol(token.lexeme), 'P', 0);
            //move on
            token = lex.GetNextToken();
        }
        return recur;
    }

    //Non Terminal PROGRAM is fully implemented here.
    private int Program() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Program", true);
        if (token.code == lex.codeFor("UNIT")) {
            token = lex.GetNextToken();
            recur = ProgIdentifier();
            if (token.code == lex.codeFor("ENDL")) {
                token = lex.GetNextToken();
                recur = Block();
                if (token.code == lex.codeFor("DOTS")) {
                    if (!anyErrors) {
                        System.out.println("Success.");
                    } else {
                        System.out.println("Compilation failed.");
                    }
                } else {
                    error(lex.reserveFor("DOTS"), token.lexeme);
                }
            } else {
                error(lex.reserveFor("ENDL"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("UNIT"), token.lexeme);
        }
        trace("Program", false);
        return recur;
    }

    //Non Terminal BLOCK is fully implemented here.
    // Will need to adjust a bit for part B
    private int Block() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Block", true);

        while (token.code == lex.codeFor("$VAR")) {
            recur = Variabledecsec();
        }

        if (anyErrors) {
            return -1;
        } else {
            recur = Blockbody();
        }

        trace("Block", false);
        return recur;
    }

    //Not a Non Terminal, but used to shorten Statement code body for readability.
    //<variable> $COLON-EQUALS <simple expression>
    // Now a part of part B
    private int handleAssignment() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("handleAssignment", true);
        //have ident already in order to get to here, handle as Variable
        recur = Variable();  //Variable moves ahead, next token ready

        if (token.code == lex.codeFor("ASGN")) {
            token = lex.GetNextToken();
            recur = SimpleExpression();
        } else {
            error(lex.reserveFor("ASGN"), token.lexeme);
        }

        trace("handleAssignment", false);
        return recur;
    }

    //endregion

    // NonTerminal This is dummied in to only work for an identifier.
    //  It will work with the SyntaxAMiniTest file having ASSIGNMENT statements
    //     containing only IDENTIFIERS.  TERM and FACTOR and numbers will be
    //     needed to complete Part A.
    // SimpleExpression MUST BE
    //  COMPLETED TO IMPLEMENT CFG for <simple expression>

    //region Part A

    // <SimpleExpression> => [<sign>] <term> [ <addop> <term]*
    private int SimpleExpression() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("SimpleExpression", true);

        // check for sign
        if (token.code == lex.codeFor("ADD_") || token.code == lex.codeFor("SUBT")) {
            token = lex.GetNextToken();
        }

        // call term at least once
        recur = Term();
        // then any number of times
        int addsub = Addop();
        while (addsub == 0) {
            //token = lex.GetNextToken();
            recur = Term();
            addsub = Addop();
        }

        trace("SimpleExpression", false);
        return recur;
    }


    // <term> => <factor> [<mulop> <factor>]*
    private int Term() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Term", true);

        // call factor once
        recur = Factor();
        // then any number of other factors after mult op
        int muldiv = Mulop();
        while (muldiv == 0) {
            //token = lex.GetNextToken();
            recur = Factor();
            muldiv = Mulop();
        }
        trace("Term", false);
        return recur;
    }


    // <factor> => <ident> || <number> || <SimpleExpression>
    private int Factor() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Factor", true);
        if (token.code == lex.codeFor("IDNT")) {
            //token = lex.GetNextToken();
            recur = Variable();
        } else if (token.code == lex.codeFor("INTC") || token.code == lex.codeFor("FLOT")) {
            recur = UnsignedConstant();
        } else {
            token = lex.GetNextToken();
            recur = SimpleExpression();
            token = lex.GetNextToken();
        }
        trace("Factor", false);
        return recur;
    }


    // <factor> => <ident> || <number> || <SimpleExpression>
    private int UnsignedConstant() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("UnsignedConstant", true);
        recur = UnsignedNumber();
        trace("UnsignedConstant", false);
        return recur;
    }


    // <factor> => <ident> || <number> || <SimpleExpression>
    private int UnsignedNumber() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("UnsignedNumber", true);
        if (token.code == lex.codeFor("INTC") || token.code == lex.codeFor("FLOT")) {
            token = lex.GetNextToken();
        }
        trace("UnsignedNumber", false);
        return recur;
    }


    private int Mulop() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        //trace("Mulop", true);
        if (token.code == lex.codeFor("MULT") || token.code == lex.codeFor("DIVD")) {
            token = lex.GetNextToken();
        } else {
            recur = -1;
        }
        //trace("Mulop", false);
        return recur;
    }

    private int Addop() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        //trace("Addop", true);
        if (token.code == lex.codeFor("ADD_") || token.code == lex.codeFor("SUBT")) {
            token = lex.GetNextToken();
        } else {
            recur = -1;
        }
        //trace("Addop", false);
        return recur;
    }

    //endregion


    //region Part B

    //Variabledecsec
    // $VAR <variable-declaration>
    private int Variabledecsec(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Variabledecsec", true);

        if (token.code == lex.codeFor("$VAR")){
            token = lex.GetNextToken();
            recur = Variabledeclaration();
        } else{
            recur = -1;
        }

        trace("Variabledecsec", false);
        return recur;
    }


    //Variabledeclaration
    // {<identifier> {$COMMA <identifier>}* $COLON <simple type> $SEMICOLON}+
    private int Variabledeclaration(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        //trace("Variabledeclaration", true);

        recur = identifier();

        // {$COMMA <identifier>}*
        if (recur == 0) {

            while (token.code == lex.codeFor("COMA")){
                token = lex.GetNextToken();
                identifier();
            }

        } else { //not valid Ident
            recur = -1;
        }

        // $COLON <simple type> $SEMICOLON}+
        // $COLON = COLN
        // $SEMICOLON = ENDL
        if (token.code == lex.codeFor("COLN")) {
            token = lex.GetNextToken();
        } else {
            return -1;
        }

        // will go min once if colon before
        while (recur == 0) {
            recur = Simpletype();
            if (token.code == lex.codeFor("ENDL")) {
                token = lex.GetNextToken();
            } else {
                recur = -1;
            }

        }

        //trace("Variabledeclaration", false);
        return recur;
    }


    //SimpleType
    // $INTEGER | $FLOAT | $STRING
    private int Simpletype(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Simpletype", true);

        if (token.code != lex.codeFor("INTC") && token.code != lex.codeFor("FLOT") && token.code != lex.codeFor("STRG")){
            recur = -1;
        }
        token = lex.GetNextToken();

        trace("Simpletype", false);
        return recur;
    }


    //Blockbody
    // This is the old block func
    // changed the old and replaced to call this later
    private int Blockbody(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Blockbody", true);
        if (token.code == lex.codeFor("BEGN")) {
            token = lex.GetNextToken();
            recur = Statement();
            while ((token.code == lex.codeFor("ENDL")) && (!lex.EOF()) && (!anyErrors)) {
                token = lex.GetNextToken();
                recur = Statement();
            }
            if (token.code == lex.codeFor("END_")) {
                token = lex.GetNextToken();
            } else {
                error(lex.reserveFor("END_"), token.lexeme);
            }

        } else {
            error(lex.reserveFor("BEGN"), token.lexeme);
        }
        trace("Blockbody", false);
        return recur;
    }

    // Eventually this will handle all possible statement starts in
    //    a nested if/else or switch structure. Only ASSIGNMENT is implemented now.
    //
    // Look at given notes for this, it is a lot
    private int Statement() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Statement", true);

        if (token.code == lex.codeFor("IDNT")) {  //must be an ASSIGNMENT
            recur = handleAssignment();
        } else {
            if (token.code == lex.codeFor("_IF_")) {  //must be an IF
                // this would handle the rest of the IF statment IN PART B
            } else
            // if/elses should look for the other possible statement starts...
            //  but not until PART B
            {
                error("Statement start", token.lexeme);
            }
        }

        trace("Statement", false);
        return recur;
    }

    //handleReadln
    //
    private int handleReadln(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleReadln", true);

        trace("handleReadln", false);
        return recur;
    }


    //handleAssignment is already here (cmd f)

    //handleFor
    //
    private int handleFor(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleFor", true);

        trace("handleFor", false);
        return recur;
    }


    //handleIf
    //
    private int handleIf(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleIf", true);

        trace("handleIf", false);
        return recur;
    }


    //Relop
    // $EQ | $LSS | $GTR | $NEQ | $LEQ | $GEQ
    private int Relop(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Relop", true);
        if (token.code >= 38 && token.code <= 42) {
            token = lex.GetNextToken();
        } else {
            recur = -1;
        }
        trace("Relop", false);
        return recur;
    }


    //handleWhile
    //
    private int handleWhile(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleWhile", true);

        trace("handleWhile", false);
        return recur;
    }

    //Relexpression
    // <simple expression> <relop> <simple expression>
    private int Relexpression(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Relexpression", true);

        recur = SimpleExpression();

        if (recur != -1) {
            recur = Relop();
        }

        if (recur != -1) {
            recur = SimpleExpression();
        }

        trace("Relexpression", false);
        return recur;
    }


    //handlePrintln
    //
    private int handlePrintln(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handlePrintln", true);

        trace("handlePrintln", false);
        return recur;
    }


    //identifier
    // $IDENTIFIER  code = 50
    // **note: <letter> {<letter> |<digit> | $ | _ }*
    private int identifier(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        //trace("identifier", true);

        if (token.code != lex.codeFor("IDNT")){
            recur = -1;
        }
        token = lex.GetNextToken();

        //trace("identifier", false);
        return recur;
    }


    //stringconst
    // $STRINGTYPE code = 53
    private int stringconst(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        //trace("stringconst", true);

        if (token.code != lex.codeFor("STRG")){
            recur = -1;
        }
        token = lex.GetNextToken();

        //trace("stringconst", false);
        return recur;
    }

    //
    //16 functions to edit and/or write
    //progress count 9 / 15
    //

    /**
     * *************************************************
     */

    //endregion




    //Non-terminal VARIABLE just looks for an IDENTIFIER.  Later, a
    //  type-check can verify compatible math ops, or if casting is required.
    private int Variable(){
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Variable", true);
        if ((token.code == lex.codeFor("IDNT"))) {
            // bookkeeping and move on
            token = lex.GetNextToken();
        }
        else
            error("Variable", token.lexeme);

        trace("Variable", false);
        return recur;

    }

    /**
     * *************************************************
     */
    /*     UTILITY FUNCTIONS USED THROUGHOUT THIS CLASS */
// error provides a simple way to print an error statement to standard output
//     and avoid reduncancy
    private void error(String wanted, String got) {
        anyErrors = true;
        System.out.println("ERROR: Expected " + wanted + " but found " + got);
    }

    // trace simply RETURNs if traceon is false; otherwise, it prints an
    // ENTERING or EXITING message using the proc string
    private void trace(String proc, boolean enter) {
        String tabs = "";

        if (!traceon) {
            return;
        }

        if (enter) {
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("--> Entering " + proc);
            level++;
        } else {
            if (level > 0) {
                level--;
            }
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("<-- Exiting " + proc);
        }
    }

    // repeatChar returns a string containing x repetitions of string s;
//    nice for making a varying indent format
    private String repeatChar(String s, int x) {
        int i;
        String result = "";
        for (i = 1; i <= x; i++) {
            result = result + s;
        }
        return result;
    }

    /*  Template for all the non-terminal method bodies
   // ALL OF THEM SHOULD LOOK LIKE THE FOLLOWING AT THE ENTRY/EXIT POINTS  
    private int exampleNonTerminal(){
        int recur = 0;   //Return value used later
        if (anyErrors) { // Error check for fast exit, error status -1
            return -1;
        }

        trace("NameOfThisMethod", true);
		
    // The unique non-terminal stuff goes here, assigning to "recur" based
    //     on recursive calls that were made
        
		trace("NameOfThisMethod", false);
    // Final result of assigning to "recur" in the body is returned
        return recur;

    }
    
    */
}