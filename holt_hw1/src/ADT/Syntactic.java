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

    //new
    private final int quadSize = 1000;
    private int Minus1Index;
    private int Plus1Index;
    private QuadTable quads;
    private Interpreter interp;

    //region Given

    public Syntactic(String filename, boolean traceOn) {
        filein = filename;
        traceon = traceOn;
        symbolList = new SymbolTable(symbolSize);
        lex = new Lexical(filein, symbolList, true);
        lex.setPrintToken(traceOn);
        anyErrors = false;

        //
        // Add these to symbol table to accommodate sign flips
        //
        Minus1Index = symbolList.AddSymbol("-1", 'c', -1);
        Plus1Index = symbolList.AddSymbol("1", 'c', 1);

        quads = new QuadTable(quadSize);
        interp = new Interpreter();


    }


    //region Final Givens

    //Interface to the syntax analyzer, initiates parsing
    public void parse() {

        //Use source filename as pattern for symbol table and quad table output later
        String filenameBase = filein.substring(0, filein.length() - 4);
        System.out.println(filenameBase);
        int recur = 0;
        //Prime the pump, get first token
        token = lex.GetNextToken();
        //Call PROGRAM
        recur = Program();
        //Done with recursion, so add the final STOP quad
        quads.AddQuad(0, 0, 0, 0);

        //Print SymbolTable, QuadTable before execute
        symbolList.PrintSymbolTable(filenameBase + "ST-before.txt");
        quads.PrintQuadTable(filenameBase + "QUADS.txt");
        //interpret
        if (!anyErrors) {
            interp.InterpretQuads(quads, symbolList, false, filenameBase + "TRACE.txt");
        } else {
            System.out.println("Errors, unable to run program.");
        }
        symbolList.PrintSymbolTable(filenameBase + "ST-after.txt");

    }

    //endregion


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
            recur = symbolList.LookupSymbol(token.lexeme);
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
        int left;
        int right;

        left = Variable();  //Variable moves ahead, next token ready

        if (token.code == lex.codeFor("ASGN")) {
            token = lex.GetNextToken();
            right = SimpleExpression();
            quads.AddQuad(5, right, 0, left);
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

        //new vars
        int left, right, signval, temp, opcode;

        // check for sign
        if (token.code == lex.codeFor("ADD_")) {
            signval = Plus1Index;
            token = lex.GetNextToken();
        } else if (token.code == lex.codeFor("SUBT")) {
            signval = Minus1Index;
            token = lex.GetNextToken();
        } else {
            signval = 0;
        }

        // call term at least once
        left = Term();

        if(signval == -1){
            quads.AddQuad(2, left, signval, left);
        }

        // then any number of times
        int addsub = Addop();
        while (addsub == 0) {

            if(signval != -1){
                opcode = 4;
            } else {
                opcode = 3;
            }

            right = Term();
            temp = symbolList.AddSymbol("temp", 'c', 0);
            quads.AddQuad(opcode, left, right, temp);
            left = temp;
            addsub = Addop();
        }

        trace("SimpleExpression", false);
        return left;
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
            // return the s.t. index
            recur = symbolList.LookupSymbol(token.lexeme);
            token = lex.GetNextToken();
        } else {
            error("Integer or Floating Point Number", token.lexeme);
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
    private int Variabledeclaration() {

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        //trace("Variabledeclaration", true);

        recur = identifier();

        // {$COMMA <identifier>}*
        if (recur == 0) {

            while (token.code == lex.codeFor("COMA")) {
                token = lex.GetNextToken();
                identifier();
            }

        } else { //not valid Ident
            recur = -1;
        }

        // $COLON <simple type> $SEMICOLON}+
        // $COLON = COLN
        // $SEMICOLON = ENDL
        while (recur == 0) {
            if (token.code == lex.codeFor("COLN")) {
                token = lex.GetNextToken();
            } else {
                return -1;
            }

            // will go min once if colon before

            recur = Simpletype();
            token = lex.GetNextToken();
            if (token.code != lex.codeFor("ENDL")) {
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

        if (token.code != lex.codeFor("INTR") && token.code != lex.codeFor("INTC") && token.code != lex.codeFor("FLOT") && token.code != lex.codeFor("STRG")){
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

        int saveTop, branchQuad, patchElse;


        if (token.code == lex.codeFor("IDNT")) { // <var> assign do (stuff
                recur = handleAssignment();

        } else if (token.code == lex.codeFor("_IF_")) { // $If call handle if

            token = lex.GetNextToken();
            branchQuad = Relexpression();

            if (token.code == lex.codeFor("THEN")) {

                token = lex.GetNextToken();
                Statement();

                if (token.code == lex.codeFor("ELSE")) {

                    token = lex.GetNextToken();
                    patchElse = quads.NextQuad();

                    quads.AddQuad(8, 0,0,0);
                    quads.UpdateJump(branchQuad, quads.NextQuad());

                    Statement();
                    quads.UpdateJump(patchElse, quads.NextQuad());

                } else {
                    quads.UpdateJump(branchQuad, quads.NextQuad());
                }

            } else {
                error("THEN", token.lexeme);
            }

        } else if (token.code == lex.codeFor("WHIL")) { // $WHILE call handle while

            token = lex.GetNextToken();

            saveTop = quads.NextQuad();
            branchQuad = Relexpression();

            if (token.code == lex.codeFor("_DO_")){
                token = lex.GetNextToken();
                Statement();

                quads.AddQuad(8, 0,0,saveTop);
                quads.UpdateJump(branchQuad, quads.NextQuad());
            } else {
                error("DO", token.lexeme);
            }

        } else if (token.code == lex.codeFor("_FOR")) { // $For handle for
                recur = handleFor();

        } else if (token.code == lex.codeFor("WRLN")) { // $WriteLn Println
                recur = handleWriteln();

        } else if (token.code == lex.codeFor("READ")) { // $ReadLn handleReadLn
                recur = handleReadln();

        } else if (token.code == lex.codeFor("ENDL")) {
                token = lex.GetNextToken();
                recur = -1;

        } else if (token.code == lex.codeFor("BEGN")) {
                recur = Blockbody();

        } else if (token.code == lex.codeFor("END_")) {
                recur = -1;

        }else {
                error("Statement start", token.lexeme);
                return  -1;
        }

        recur = 0;

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
        token = lex.GetNextToken();

        // Format: (IDNT)
        if (token.code == lex.codeFor("LFPR")){
            token = lex.GetNextToken();
            // READ SOMETHING HERE
            recur = identifier();
        } else {
            error("Need to strat with left parenthesis", token.lexeme);
            return -1;
        }

        // finish with )
        if (token.code == lex.codeFor("RTPR")){
            token = lex.GetNextToken();
        } else {
            error("Need to strat with right parenthesis", token.lexeme);
            return -1;
        }

        //end line after done
        if (token.code == lex.codeFor("ENDL")){
            //thats good
        } else {
            error("End Line with semicolon", token.lexeme);
            return -1;
        }


        trace("handleReadln", false);
        return recur;
    }


    //handleAssignment is already here (cmd f)

    //handleFor
    // <variable> $ASSIGN <simple expression>
    // $TO <simple expression> $DO <statement>
    private int handleFor(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleFor", true);
        token = lex.GetNextToken();

        recur = Variable();
        if (token.code == lex.codeFor("ASGN")){
            token = lex.GetNextToken();
            recur = SimpleExpression();
        } else {
            error("Assignment expected", token.lexeme);
        }

        if (token.code == lex.codeFor("_TO_")){
            token = lex.GetNextToken();
            recur = SimpleExpression();
        } else {
            error("TO expected", token.lexeme);
        }

        if (token.code == lex.codeFor("_DO_")){
            token = lex.GetNextToken();
            recur = Statement();
        } else {
            error("DO expected", token.lexeme);
        }

        trace("handleFor", false);
        return recur;
    }


    //handleIf
    // <relexpression> $THEN <statement> [$ELSE <statement>]
    private int handleIf(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleIf", true);
        token = lex.GetNextToken();

        recur = Relexpression();

        if (token.code == lex.codeFor("THEN") && recur != -1){
            token = lex.GetNextToken();
            recur = Statement();

            if (token.code == lex.codeFor("ELSE") && recur != -1){
                token = lex.GetNextToken();
                recur = Statement();
            } else {
                error("Else wanted", token.lexeme);
            }

        } else {
            error("Then wanted", token.lexeme);
        }

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

    private int relopToOpcode(int relop){

        int result = -1;

        switch (relop)
        {

            case 42: // equal
                result = 12;
                break;

            case 43: // not equal
                result = 9;
                break;

            case 39: // less
                result = 14;
                break;

            case 38: // greater
                result = 13;
                break;

            case 40: // greater equal
                result = 11;
                break;

            case 41: // less equal
                result = 10;
                break;

        }

        return result;

    }

    //handleWhile
    // <relexpression> DO <statement>
    private int handleWhile(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleWhile", true);
        token = lex.GetNextToken();

        recur = Relexpression();
        if (token.code == lex.codeFor("_DO_")) {
            token = lex.GetNextToken();
            recur = Statement();
        } else {
            error("No do found", token.lexeme);
        }

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

        int left, right, saverelop, result, temp;

        left = SimpleExpression();
        saverelop = Relop();
        right = SimpleExpression();
        temp = symbolList.AddSymbol("temp", 'c', 0);

        quads.AddQuad(3, left, right, temp);
        result = quads.NextQuad();
        quads.AddQuad(relopToOpcode(saverelop), temp, 0, 0);

        trace("Relexpression", false);
        return result;
    }


    //handlePrintln
    //
    private int handleWriteln() {
        int recur = 0;
        int toprint = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleWriteln", true);
        //got here from a WRITELN token, move past it...
        token = lex.GetNextToken();
        //look for ( stringconst, ident, or simpleexp )
        if (token.code == lex.codeFor("LFPR")) {
            //move on
            token = lex.GetNextToken();
            if ((token.code == lex.codeFor("STNG")) || (token.code == lex.codeFor("IDNT"))) {
                // save index for string literal or identifier
                toprint = symbolList.LookupSymbol(token.lexeme);
                //move on
                token = lex.GetNextToken();
            } else {
                toprint = SimpleExpression();
            }
            quads.AddQuad(6, 0, 0, toprint);
            //now need right ")"
            if (token.code == lex.codeFor("RTPR")) {
                //move on
                token = lex.GetNextToken();
            } else {
                error(lex.reserveFor("RTPR"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("LFPR"), token.lexeme);
        }
        // end lpar group

        trace("handlePrintn", false);
        return recur;

    }


    //handleRepeat
    //
    private int handleRepeat(){

        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("handleRepeat", true);
        token = lex.GetNextToken();

        boolean keepGoing = true;
        while (keepGoing){

            recur = Statement();

            if (token.code == lex.codeFor("UNTL")){
                token = lex.GetNextToken();
                recur = Relexpression();
                keepGoing = false;
            }

        }

        token = lex.GetNextToken();
        trace("handleRepeat", false);
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
    //progress count 15 / 16
    //

    /**
     * *************************************************
     */

    //endregion




    //Non-terminal just looks for an IDENTIFIER.  Later, a
    //  type-check can verify compatible math ops, or if casting is required.
    private int Variable(){
        int result = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Variable", true);
        if ((token.code == lex.codeFor("IDNT"))) {
            //return the location of this variable for Quad use
            result = symbolList.LookupSymbol(token.lexeme);
            // bookkeeping and move on
            token = lex.GetNextToken();
        }
        else
            error("Variable", token.lexeme);

        trace("Variable", false);
        return result;

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