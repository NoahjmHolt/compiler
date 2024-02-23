/*
HERE is code that goes in the Interpreter class
so that all the trace outputs match the intended result

    private String makeTraceString(int pc, int opcode,int op1,int op2,int op3 ){
        String result = "";
        result = "PC = "+String.format("%04d", pc)+": "+(optable.LookupCode(opcode)+"     ").substring(0,6)+String.format("%02d",op1)+
                                ", "+String.format("%02d",op2)+", "+String.format("%02d",op3);
        return result;
    }


*/

package ADT;


public class Interpreter {

    ReserveTable operation;

    // factorial
    QuadTable quad4Fact;
    SymbolTable symb4Fact;

    // summation
    QuadTable quad4Summ;
    SymbolTable symb4Summ;

    // Initializes what is needed
    public Interpreter(){

        initReserve(operation);

        initializeFactorialTest(symb4Fact, quad4Fact);
        initializeSummationTest(symb4Summ, quad4Summ);

    } // end method

    // Builds tables for Factorial func
        //
    // With provided main, needed to make public to call from main
    public boolean initializeFactorialTest(SymbolTable symbtable, QuadTable quadtable){

        initSTforFactorial(symbtable);
        initQTforFactorial(quadtable);

        return true; //placeholder until filled out

    } // end fact test

    // Builds tables for summation func
        //
    // With provided main, needed to make public to call from main
    public boolean initializeSummationTest(SymbolTable symbtable, QuadTable quadtable){

        initSTforSummation(symbtable);
        initQTforSummation(quadtable);

        return true; // placeholder until func written

    } // end Summ test

    //
    public void InterpretQuads(QuadTable quadTable, SymbolTable symbtable, boolean TraceOn, String filename){

        if (TraceOn){ //Trace the program by printing op mnemonics

            System.out.println("on");

        } else {

            System.out.println("off");

        }

    } // end Interpret Quads


    // Start of given Code

    private String makeTraceString(int pc, int opcode,int op1,int op2,int op3 ){
        String result = "";
        result = "PC = "+String.format("%04d", pc)+": "+(operation.LookupCode(opcode)+"     ").substring(0,6)+String.format("%02d",op1)+
                ", "+String.format("%02d",op2)+", "+String.format("%02d",op3);
        return result;
    } // end Trace

    //HERE is a free opcode table initialization for a created ReserveTable
    private void initReserve(ReserveTable optable){
        optable.Add("STOP", 0);
        optable.Add("DIV", 1);
        optable.Add("MUL", 2);
        optable.Add("SUB", 3);
        optable.Add("ADD", 4);
        optable.Add("MOV", 5);
        optable.Add("PRINT", 6);
        optable.Add("READ", 7);
        optable.Add("JMP", 8);
        optable.Add("JZ", 9);
        optable.Add("JP", 10);
        optable.Add("JN", 11);
        optable.Add("JNZ", 12);
        optable.Add("JNP", 13);
        optable.Add("JNN", 14);
        optable.Add("JINDR", 15);
    }

    // HERE IS THE FACTORIAL INITIALIZATION STUFF...

    //factorial Symbols
    public static void initSTforFactorial(SymbolTable st) {
        st.AddSymbol("n", 'V', 10);
        st.AddSymbol("i", 'V', 0);

        //... put the rest of the Symbol table entries below...
        st.AddSymbol("product", 'v', 0);
        st.AddSymbol("1", 'c', 1);
        st.AddSymbol("$temp", 'v', 0);

    } // end fact symbol

    //factorial Quads
    public void initQTforFactorial(QuadTable qt) {
        qt.AddQuad(5, 3, 0, 2); //MOV
        qt.AddQuad(5, 3, 0, 1); //MOV
        qt.AddQuad(3, 1, 0, 4); //SUB

        //... put the rest of the Quad table entries below...
        qt.AddQuad(10, 4, 0, 7); //JP exit loop
        qt.AddQuad(2, 2, 1, 2); //MUL product = product * i
        qt.AddQuad(8, 0, 0, 2); //JMP loop back through
        qt.AddQuad(6, 2, 0, 0); //PRINT


    } // end fact quad

    public static void initSTforSummation(SymbolTable st) {
        st.AddSymbol("n", 'V', 10);
        st.AddSymbol("i", 'V', 0);

        //... put the rest of the Symbol table entries below...


    } // end sum symbol

    //factorial Quads
    public void initQTforSummation(QuadTable qt) {
        qt.AddQuad(5, 3, 0, 2); //MOV prod = 1
        qt.AddQuad(5, 3, 0, 1); //MOV i = 1
        qt.AddQuad(3, 1, 0, 4); //SUB Compare $temp = i-n (in loop)

        //... put the rest of the Quad table entries below...


    } // end sum quad

} // end class



