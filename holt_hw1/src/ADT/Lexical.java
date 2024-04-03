/*
The following code is provided by the instructor for the completion of
PHASE 2 of the compiler project for CS4100/5100.

FALL 2023 version
STUDENTS ARE TO PROVIDE THE FOLLOWING CODE FOR THE COMPLETION OF THE ASSIGNMENT:

1) Initialize the 2 reserve tables, which are fields in the Lexical
class, named reserveWords and mnemonics. Create the following functions.

These calls are in the lexical constructor:

initReserveWords(reserveWords);

initMnemonics(mnemonics);

One-line examples are provided below

2) getIdentifier, getNumber, getString, and getOtherToken.
getOtherToken recognizes one and two-character tokens in the language.

PROVIDED UTILITY FUNCTIONS THAT STUDENT MAY NEED TO CALL-

1) YOU MUST NOT USE MAGIC NUMBERS, that is, numeric constants anywhere in
the code, like "if tokencode == 50". Instead, use the following:
// To get an integer for a given mnemonic, use
public int codeFor(String mnemonic) {
return mnemonics.LookupName(mnemonic);
}

// To get the full reserve word for a given mnemonic, use:
public String reserveFor(String mnemonic) {
return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
}

Other methods:
private void consoleShowError(String message)
private boolean isLetter(char ch)
private boolean isDigit(char ch)
private boolean isStringStart(char ch)
private boolean isWhitespace(char ch)
public char GetNextChar()

To check numeric formats of strings to see if they are valid, use:
public boolean doubleOK(String stin)
public boolean integerOK(String stin)

CALLING OTHER FUNCTIONS LIKE getNextLine COULD BREAK THE EXISTING CODE!
*/

package ADT;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/**
 *
 * @author nholt
 */

import java.io.*;

public class Lexical {
    private File file; //File to be read for input
    private FileReader filereader; //Reader, Java reqd
    private BufferedReader bufferedreader; //Buffered, Java reqd
    private String line; //Current line of input from file
    private int linePos; //Current character position
    // in the current line
    private SymbolTable saveSymbols; //SymbolTable used in Lexical
    // sent as parameter to construct
    private boolean EOF; //End Of File indicator
    private boolean echo; //true means echo each input line
    private boolean printToken; //true to print found tokens here
    private int lineCount; //line #in file, for echo-ing
    private boolean needLine; //track when to read a new line
    //Tables to hold the reserve words and the mnemonics for token codes
    private final int sizeReserveTable = 50;
    private ReserveTable reserveWords = new ReserveTable(sizeReserveTable);
    //a few more than # reserves
    private ReserveTable mnemonics = new ReserveTable(sizeReserveTable);
    //a few more than # reserves

    //constructor
    public Lexical(String filename, SymbolTable symbols, boolean echoOn) {
        saveSymbols = symbols; //map the initialized parameter to the local ST
        echo = echoOn; //store echo status
        lineCount = 0; //start the line number count
        line = ""; //line starts empty
        needLine = true; //need to read a line
        printToken = false; //default OFF, do not print tokesn here

        // within GetNextToken; call setPrintToken to
        // change it publicly.
        linePos = -1; //no chars read yet

        //call initializations of tables
        initReserveWords(reserveWords);
        initMnemonics(mnemonics);

        //set up the file access, get first character, line retrieved 1st time
        try {
            file = new File(filename); //creates a new file instance
            filereader = new FileReader(file); //reads the file
            bufferedreader = new BufferedReader(filereader); //creates a buffering character input stream
            EOF = false;
            currCh = GetNextChar();
        } catch (IOException e) {
            EOF = true;
            e.printStackTrace();
        }
    }

    // inner class "token" is declared here, no accessors needed
    public class token {
        public String lexeme;
        public int code;
        public String mnemonic;
        token() {
            lexeme = "";
            code = 0;
            mnemonic = "";
        }
    }

    // This is the DISCARDABLE dummy method for getting and returning single characters
    // STUDENT TURN-IN SHOULD NOT USE THIS!
    private token dummyGet() {
        token result = new token();
        result.lexeme = "" + currCh; //have the first char
        currCh = GetNextChar();
        result.code = 0;
        result.mnemonic = "DUMY";
        return result;
    }

    // ******************* PUBLIC USEFUL METHODS
    // These are nice for syntax to call later
    // given a mnemonic, find its token code value
    public int codeFor(String mnemonic) {
        return mnemonics.LookupName(mnemonic);
    }

    // given a mnemonic, return its reserve word
    public String reserveFor(String mnemonic) {
        return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
    }

    // Public access to the current End Of File status
    public boolean EOF() {
        return EOF;
    }

    // DEBUG enabler, turns on/OFF token printing inside of GetNextToken
    public void setPrintToken(boolean on) {
        printToken = on;
    }

    /* @@@ */
    // all given in table for handout
    // just some tedious copying over
    private void initReserveWords(ReserveTable reserveWords) {
    // Student must provide the rest
        reserveWords.Add("GOTO", 0);
        reserveWords.Add("INTEGER", 1);
        reserveWords.Add("TO", 2);
        reserveWords.Add("DO", 3);
        reserveWords.Add("if", 4);
        reserveWords.Add("THEN", 5);
        reserveWords.Add("ELSE", 6);
        reserveWords.Add("FOR", 7);
        reserveWords.Add("OF", 8);
        reserveWords.Add("WRITELN", 9);
        reserveWords.Add("READIN", 10);
        reserveWords.Add("BEGIN", 11);
        reserveWords.Add("END", 12);
        reserveWords.Add("VAR", 13);
        reserveWords.Add("WHILE", 14);
        reserveWords.Add("UNIT", 15);
        reserveWords.Add("LABEL", 16);
        reserveWords.Add("REPEAT", 17);
        reserveWords.Add("UNTIL", 18);
        reserveWords.Add("PROCEDURE", 19);
        reserveWords.Add("DOWNTO", 20);
        reserveWords.Add("FUNCTION", 21);
        reserveWords.Add("RETURN", 22);
        reserveWords.Add("REAL", 23);
        reserveWords.Add("STRING", 24);
        reserveWords.Add("ARRAY", 25);

        //1 and 2-char
        reserveWords.Add("/", 30);
        reserveWords.Add("*", 31);
        reserveWords.Add("+", 32);
        reserveWords.Add("-", 33);
        reserveWords.Add("(", 34);
        reserveWords.Add(")", 35);
        reserveWords.Add(";", 36);
        reserveWords.Add(":=", 37); // 2
        reserveWords.Add(">", 38);
        reserveWords.Add("<", 39);
        reserveWords.Add(">=", 40); // 2
        reserveWords.Add("<=", 41); // 2
        reserveWords.Add("=", 42);
        reserveWords.Add("<>", 43); // 2
        reserveWords.Add(",", 44);
        reserveWords.Add("[", 45);
        reserveWords.Add("]", 46);
        reserveWords.Add(":", 47);
        reserveWords.Add(".", 48);

        // all else
        reserveWords.Add("UNKN", 99);

    }

    /* @@@ */
    private void initMnemonics(ReserveTable mnemonics) {
        // Student must create their own 5-char mnemonics
        //                               4-char per announcment
        mnemonics.Add("GOTO", 0);
        mnemonics.Add("INTR", 1);
        mnemonics.Add("_TO_", 2);
        mnemonics.Add("_DO_", 3);
        mnemonics.Add("_if_", 4);
        mnemonics.Add("THEN", 5);
        mnemonics.Add("ELSE", 6);
        mnemonics.Add("_FOR", 7);
        mnemonics.Add("_OF_", 8);
        mnemonics.Add("WRLN", 9);
        mnemonics.Add("RDIN", 10);
        mnemonics.Add("BEGN", 11);
        mnemonics.Add("END", 12);
        mnemonics.Add("VAR", 13);
        mnemonics.Add("WHIL", 14);
        mnemonics.Add("UNIT", 15);
        mnemonics.Add("LABL", 16);
        mnemonics.Add("REPT", 17);
        mnemonics.Add("UNTL", 18);
        mnemonics.Add("PROC", 19);
        mnemonics.Add("DWTO", 20);
        mnemonics.Add("FUNC", 21);
        mnemonics.Add("RTRN", 22);
        mnemonics.Add("REAL", 23);
        mnemonics.Add("STNG", 24);

        mnemonics.Add("ARRY", 25);

        mnemonics.Add("DIVD", 30);
        mnemonics.Add("MULT", 31);
        mnemonics.Add("ADD_", 32);
        mnemonics.Add("SUBT", 33);
        mnemonics.Add("LFPR", 34);
        mnemonics.Add("RTPR", 35);
        mnemonics.Add("ENDL", 36);
        mnemonics.Add("ASGN", 37);
        mnemonics.Add("GRTR", 38);
        mnemonics.Add("LSEQ", 41);
        mnemonics.Add("EQUL", 42);

        mnemonics.Add("COMA", 44);
        mnemonics.Add("LFBR", 45);
        mnemonics.Add("RTBR", 46);
        mnemonics.Add("COLN", 47);
        mnemonics.Add("DOTS", 48);

        mnemonics.Add("IDNT", 50);
        mnemonics.Add("INTC", 51);
        mnemonics.Add("FLOT", 52);
        mnemonics.Add("STRG", 53);
        mnemonics.Add("OTHR", 66);
        mnemonics.Add("UNKN", 99);

        //1 and 2-char
        mnemonics.Add("NTEQ", 43);

    }

    //region Given Code From Instructor not changing

    // ********************** UTILITY FUNCTIONS
    private void consoleShowError(String message) {
        System.out.println("**** ERROR FOUND: " + message);
    }

    // Character category for alphabetic chars
    private boolean isLetter(char ch) {
        return (((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <=
                'z')));
    }

    // Character category for 0..9
    private boolean isDigit(char ch) {
        return ((ch >= '0') && (ch <= '9'));
    }

    // Category for any whitespace to be skipped over
    private boolean isWhitespace(char ch) {
    // SPACE, TAB, NEWLINE are white space
        return ((ch == ' ') || (ch == '\t') || (ch == '\n'));
    }

    // Returns the VALUE of the next character without removing it from the
    // input line. Useful for checking 2-character tokens that start with
    // a 1-character token.
    private char PeekNextChar() {
        char result = ' ';
        if ((needLine) || (EOF)) {
            result = ' '; //at end of line, so nothing
        } else //
        {
            if ((linePos + 1) < line.length()) { //have a char to peek
                result = line.charAt(linePos + 1);
            }
        }
        return result;
    }

    // Called by GetNextChar when the characters in the current line are used up.
    // STUDENT CODE SHOULD NOT EVER CALL THIS!
    private void GetNextLine() {
        try {
            line = bufferedreader.readLine();
            if ((line != null) && (echo)) {
                lineCount++;
                System.out.println(String.format("%04d", lineCount) + " "
                        + line);
            } // if not null

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line == null) { // The readLine returns null at EOF, set flag
                    EOF = true;
        }
        linePos = -1; // reset vars for new line if we have one
        needLine = false; // we have one, no need
        //the line is ready for the next call to get a character
    }

    // Called to get the next character from file, automatically gets a new
    // line when needed. CALL THIS TO GET CHARACTERS FOR GETIDENT etc.
    public char GetNextChar() {
        char result;
        if (needLine) //ran out last time we got a char, so get a new line
        {
            GetNextLine();
        } // if need line

        //try to get char from line buff
        if (EOF) {
            result = '\n';
            needLine = false;
        } else {
            if ((linePos < line.length() - 1)) { //have a character available
                linePos++;
                result = line.charAt(linePos);
            } else { //need a new line, but want to return eoln on this call first
                result = '\n';
                needLine = true; //will read a new line on next GetNextChar call
            }
        } // if EOF

        return result;
    }

    // The constants below allow flexible comment start/end characters
    final char commentStart_1 = '{';
    final char commentEnd_1 = '}';
    final char commentStart_2 = '(';
    final char commentPairChar = '*';
    final char commentEnd_2 = ')';
    // Skips past single and multi-line comments, and outputs UNTERMINATED
// COMMENT when end of line is reached before terminating
    String unterminatedComment = "Comment not terminated before End Of File";
    public char skipComment(char curr) {
        if (curr == commentStart_1) {
            curr = GetNextChar();

            while ((curr != commentEnd_1) && (!EOF)) {
                curr = GetNextChar();
            } // while

            if (EOF) {
                consoleShowError(unterminatedComment);
            } else {
                curr = GetNextChar();
            }

        } else {
            if ((curr == commentStart_2) && (PeekNextChar() == commentPairChar)) {
                curr = GetNextChar(); // get the second
                curr = GetNextChar(); // into comment or end of comment
                // while ((curr != commentPairChar) && (PeekNextChar() != commentEnd_2) &&(!EOF)) {
                while ((!((curr == commentPairChar) && (PeekNextChar() == commentEnd_2))) && (!EOF)) {
        // if (lineCount >=4) {
        // System.out.println("In Comment, curr,peek: "+curr+", "+PeekNextChar());}
                    curr = GetNextChar();
                } // while

                if (EOF) {
                    consoleShowError(unterminatedComment);
                } else {
                    curr = GetNextChar(); //must move past close
                    curr = GetNextChar(); //must get following
                } // if EOF
            } // if check next

        } // if current
        return (curr);
    } // skip comment

    // Reads past all whitespace as defined by isWhiteSpace
    // NOTE THAT COMMENTS ARE SKIPPED AS WHITESPACE AS WELL!
    public char skipWhiteSpace() {
        do {
            while ((isWhitespace(currCh)) && (!EOF)) {
                currCh = GetNextChar();
            }
            currCh = skipComment(currCh);

        } while (isWhitespace(currCh) && (!EOF));

        return currCh;
    }

    private boolean isPrefix(char ch) {
        return ((ch == ':') || (ch == '<') || (ch == '>'));
    }

    private boolean isStringStart(char ch) {
        return ch == '\"';
    }

    //endregion n

    //global char
    char currCh;

    /*  STARTED BY TEACHER
        changes added by me
     */
    private token getIdentifier(){
        token result = new token();
        result.lexeme = "" + currCh; //have the first char
        result.mnemonic = "IDNT";
        currCh = GetNextChar();

        int indentMax = 20;

        //NOTE: Below is not complete for SP23 identifier definition
        while (isLetter(currCh)||(isDigit(currCh)) || currCh == '_') {
            result.lexeme = result.lexeme + currCh; //extend lexeme
            currCh = GetNextChar();
        }

        // end of token, lookup or IDENT
        result.code = reserveWords.LookupName(result.lexeme);

        if (result.code == -1) {
            //result.code = IDENT_ID;
            // Identifiers need to be added to the symbol table after truncation
            // as needed

            // trunc string to 20 if needed
            if (result.lexeme.length() >= indentMax){
                consoleShowError("Ident Name too long, truncating to 20 chars!");
                result.lexeme = result.lexeme.substring(0, indentMax);
            }

            if(saveSymbols.LookupSymbol(result.lexeme) == -1){
                //saveSymbols.AddSymbol(result.lexeme, 'v', )
                saveSymbols.AddSymbol(result.lexeme, 'V', 0);

            } // if not on symbol table

            result.code = mnemonics.LookupName(result.mnemonic);

        } else { // is a reserve word

            result.mnemonic = mnemonics.LookupCode(result.code);

        }


            
        return result;
    } // get IDENT


    private token getNumber() {
        /* a number is: see token description! */
        token result = new token();
        result.lexeme = "" + currCh; //have the first char
        currCh = GetNextChar();

        int periodCounter = 0;
        int maxInt = 6;
        int maxFloat = 12;
        boolean wasFloat = false;

        while (periodCounter < 2 && (isDigit(currCh)) || currCh == '.') {
            result.lexeme = result.lexeme + currCh; //extend lexeme
            if (currCh == '.'){
                periodCounter += 1;
            }
            currCh = GetNextChar();
        }

        // Did we leave for Float?
        if (currCh == 'E'){
            result.lexeme = result.lexeme + currCh;
            currCh = GetNextChar();

            while (isDigit(currCh)) {
                result.lexeme = result.lexeme + currCh; //extend lexeme
                wasFloat = true; // must have number so moved into the while
                // now we must see 1 digit after E

                if (currCh == '.'){
                    periodCounter += 1;
                }
                currCh = GetNextChar();
            }
        }

        if (periodCounter == 0){

            // trunc
            if (result.lexeme.length() >= maxInt){
                consoleShowError("Integer too long, truncating to " + maxInt + " characters.");
                result.lexeme = result.lexeme.substring(0, maxInt);
            }

            if (integerOK(result.lexeme)) {
                result.mnemonic = "INTC";
                result.code = mnemonics.LookupName(result.mnemonic);

                if(saveSymbols.LookupSymbol(result.lexeme) == -1) {
                    //saveSymbols.AddSymbol(result.lexeme, 'v', )
                    saveSymbols.AddSymbol(result.lexeme, 'C', Integer.parseInt(result.lexeme));
                }

            } else {
                //error
                consoleShowError("Not a Valid Integer!");
            }
        } else if (wasFloat || periodCounter == 1){

            // trunc
            if (result.lexeme.length() >= maxFloat){
                consoleShowError("Integer too long, truncating to " + maxFloat + " characters.");
                result.lexeme = result.lexeme.substring(0, maxFloat);
            }

            if (doubleOK(result.lexeme)) {
                result.mnemonic = "FLOT";
                result.code = mnemonics.LookupName(result.mnemonic);

                if(saveSymbols.LookupSymbol(result.lexeme) == -1) {
                    //saveSymbols.AddSymbol(result.lexeme, 'v', )
                    saveSymbols.AddSymbol(result.lexeme, 'C', Float.parseFloat(result.lexeme));
                }

            } else {
                //error
                consoleShowError("Not a Valid Floating Point!");
            }

        } else {
            //error to many periods
            consoleShowError("Too many alternate characters!");
            
        }

        // in case of unknown issue
        // I say unknown, I am getting an issue so covering the bases
        if (result.mnemonic.isEmpty()){
            consoleShowError("Something seems wrong!");
            result.code = 99;
            result.mnemonic = mnemonics.LookupCode(result.code);
        }

        return result;
    } // get num


    private token getString() {

        token result = new token();
        result.mnemonic = "STRG";
        result.code = mnemonics.LookupName(result.mnemonic);
        // currChar = '
        // get next to begin getting actual contents
        currCh = GetNextChar();

        // check not finished and not the end
        while (!isStringStart(currCh) && currCh != ';') {
            result.lexeme = result.lexeme + currCh; //extend lexeme
            currCh = GetNextChar();
        }

        if (currCh == ';'){ // ie reached end of given line and not end of string
            consoleShowError("Unterminated String!");
            result.lexeme = "" + currCh;
            result.code = reserveWords.LookupName(result.lexeme);
            result.mnemonic = mnemonics.LookupCode(result.code);
        }

        //exited while currChar = '
        //want new before leaving
        currCh = GetNextChar();

        return result;
    } // get string


    private token getOtherToken() {

        token result = new token();
        result.lexeme = "" + currCh; //have the first char
        result.mnemonic = "OTHR";
        currCh = GetNextChar();

        while (!(isLetter(currCh)) && !(isDigit(currCh)) && (!EOF)) {

            // check if reserve is found and stop there.
            // also realize may need to catch the double marks
            //writing in assignment op for now, may want for others
            int size = result.lexeme.length();
            if (result.lexeme.charAt(size-1) == ':'){ //check if assign or just a col
                if (currCh == '='){ //then assignment and end
                    result.lexeme = result.lexeme + currCh; //extend lexeme
                    result.code = reserveWords.LookupName(result.lexeme);
                    result.mnemonic = mnemonics.LookupCode(result.code);
                    currCh = GetNextChar();
                    return result;
                    // this code gets repeated 2x
                    // thinking could make another func to do
                    // but somewhere in the instructions I thought
                    // I saw not to add others that aren't given.
                    // will try to reread and add if I can.
                }
            } else if (reserveWords.LookupName(result.lexeme) != -1){

                result.code = reserveWords.LookupName(result.lexeme);
                result.mnemonic = mnemonics.LookupCode(result.code);
                //currCh = GetNextChar();
                return result;

            } else {
                result.lexeme = result.lexeme + currCh; //extend lexeme
            }

            currCh = GetNextChar();
        }

        //now we have symbol check on reserve
        // or exit with error
        result.code = reserveWords.LookupName(result.lexeme);
        // if not on reserve then 99 unknown
        if (result.code == -1){
            result.code = 99; // actual unknown, maybe change lookup to be 99 if not
        }
        // else it was on the table or is now
        result.mnemonic = mnemonics.LookupCode(result.code);

        return result;
    }


    // Checks to see if a string contains a valid DOUBLE
    public boolean doubleOK(String stin) {
        boolean result;
        Double x;
        try {
            x = Double.parseDouble(stin);
            result = true;
        } catch (NumberFormatException ex) {
            result = false;
        }

        return result;
    } // double ok func

    // Checks the input string for a valid INTEGER
    public boolean integerOK(String stin) {
        boolean result;
        int x;
        try {
            x = Integer.parseInt(stin);
            result = true;
        } catch (NumberFormatException ex) {
            result = false;
        }

        return result;
    } // integer Ok func

    public token GetNextToken() {

        token result = new token();
        currCh = skipWhiteSpace();
        if (isLetter(currCh)) { //is identifier
            result = getIdentifier();
        } else if (isDigit(currCh)) { //is numeric
            result = getNumber();
        } else if (isStringStart(currCh)) { //string literal
            result = getString();
        } else //default char checks
        {
            result = getOtherToken();
        }

        if ((result.lexeme.equals("")) || (EOF)) {
            result = null;
        }

        //set the mnemonic
        if (result != null) {
            // THIS LINE REMOVED-- PUT BACK IN TO USE LOOKUP
            // result.mnemonic = mnemonics.LookupCode(result.code);
            if (printToken) {
                System.out.println("\t" + result.mnemonic + " | \t" + String.format("%04d", result.code) + " | \t" + result.lexeme);
            }
        }

        if (printToken) {
            System.out.println("\t" + result.mnemonic + " | \t" + String.format("%04d", result.code) + " | \t" + result.lexeme);
        }

        return result;
    } // get next token func



} // lexical class
