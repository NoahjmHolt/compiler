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

    // Initializes what is needed
    public Interpreter(){

        initReserve(operation);

    }

    // Builds tables for Factorial func
        //
    // With provided main, needed to make public to call from main
    public boolean initializeFactorialTest(SymbolTable symbtable, QuadTable quadtable){

        InitSTforFactorial(symbtable);
        InitQTforFactorial(quadtable);

        return true; //placeholder until filled out

    }

    // Builds tables for summation func
        //
    // With provided main, needed to make public to call from main
    public boolean initializeSummationTest(SymbolTable symbtable, QuadTable quadtable){


        return true; // placeholder until func written
        
    }

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
    }

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
    public static void InitSTforFactorial(SymbolTable st) {
        st.AddSymbol("n", 'V', 10);
        st.AddSymbol("i", 'V', 0);

        //... put the rest of the Symbol table entries below...


    }

    //factorial Quads
    public void InitQTforFactorial(QuadTable qt) {
        qt.AddQuad(5, 3, 0, 2); //MOV
        qt.AddQuad(5, 3, 0, 1); //MOV
        qt.AddQuad(3, 1, 0, 4); //SUB

        //... put the rest of the Quad table entries below...


    }

} // end class



