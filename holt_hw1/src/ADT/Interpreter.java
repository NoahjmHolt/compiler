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

    int maxSize;
    ReserveTable operation;

    // factorial
    QuadTable quad4Fact;
    QuadTable quad4Sum;
    SymbolTable symb4All;

    // Initializes what is needed
    public Interpreter(){

        maxSize = 35;

        operation = new ReserveTable(maxSize);
        initReserve(operation);

        symb4All = new SymbolTable(maxSize);
        quad4Fact = new QuadTable(maxSize);
        quad4Sum = new QuadTable(maxSize);

        initializeFactorialTest(symb4All, quad4Fact);
        initializeSummationTest(symb4All, quad4Sum);

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

        int programCount = 0;

        while (programCount < maxSize){

            int[] currentQuad = new int[4];
            currentQuad = quadTable.GetQuad(programCount);

            if (TraceOn){
                makeTraceString(programCount, currentQuad[0], currentQuad[1], currentQuad[2], currentQuad[3]);
            }

            if (!operation.LookupCode(currentQuad[0]).isEmpty()){

                char keep = symbtable.GetUsage(currentQuad[3]);

                switch (currentQuad[0]){

                    case 0: // STOP
                        System.out.println("Execution terminated by program STOP");
                        programCount = maxSize;
                        break;

                    case 1: // DIV
                        int upper = symbtable.GetInteger(currentQuad[1]);
                        int lower = symbtable.GetInteger(currentQuad[2]);

                        int division = upper / lower; // putting

                        symbtable.UpdateSymbol(currentQuad[3], keep, division);

                        programCount += 1;

                        break;

                    case 2: // MUL

                        int mul1 = symbtable.GetInteger(currentQuad[1]);
                        int mul2 = symbtable.GetInteger(currentQuad[2]);

                        int multi = mul2 * mul1; // putting in

                        symbtable.UpdateSymbol(currentQuad[3], keep, multi);

                        programCount += 1;

                        break;

                    case 3: // SUB

                        int big = symbtable.GetInteger(currentQuad[1]);
                        int lil = symbtable.GetInteger(currentQuad[2]);

                        int subtract = big - lil; // putting in

                        symbtable.UpdateSymbol(currentQuad[3], keep, subtract);

                        programCount += 1;

                        break;

                    case 4: // ADD

                        int first = symbtable.GetInteger(currentQuad[1]);
                        int second = symbtable.GetInteger(currentQuad[2]);

                        int addition = first + second; // putting in

                        symbtable.UpdateSymbol(currentQuad[3], keep, addition);

                        programCount += 1;

                        break;

                    case 5: //MOV

                        int moveSomewhere = symbtable.GetInteger(currentQuad[1]);
                        symbtable.UpdateSymbol(currentQuad[3], keep, moveSomewhere);

                        break;

                    case 6: // PRINT

                        int outcome = symbtable.GetInteger(currentQuad[3]);

                        System.out.println("Outcome = " + outcome);

                        break;

                    case 7: // READ

                        break;

                    case 8: // JMP

                        programCount = currentQuad[3];

                        break;

                    case 9: // JZ

                        if (symbtable.GetInteger(currentQuad[1]) == 0){
                            programCount = currentQuad[3];
                        } else {
                            programCount += 1;
                        }

                        break;

                    case 10: // JP

                        programCount = currentQuad[3];

                        break;

                    case 11: // JN

                        if (symbtable.GetInteger(currentQuad[1]) != 0){
                            programCount = currentQuad[3];
                        } else {
                            programCount += 1;
                        }

                        break;

                    case 12: // JNZ

                        if (symbtable.GetInteger(currentQuad[1]) != 0){
                            programCount = currentQuad[3];
                        } else {
                            programCount += 1;
                        }

                        break;

                    case 13: // JNP not positive?

                        if (symbtable.GetInteger(currentQuad[1]) <= 0){
                            programCount = currentQuad[3];
                        } else {
                            programCount += 1;
                        }

                        break;

                    case 14: // JNN not neg?

                        if (symbtable.GetInteger(currentQuad[1]) >= 0){
                            programCount = currentQuad[3];
                        } else {
                            programCount += 1;
                        }

                        break;

                    case 15: // JINDR ???

                        // no idea so ...
                        programCount += 1;

                        break;

                    default: // something went wrong
                        System.out.println("Opcode Not Found");
                        programCount = maxSize;

                } // end switch

            } // end if

        } // end while

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
        st.AddSymbol("n", 'V', 10); //1
        st.AddSymbol("i", 'V', 0); //2

        //... put the rest of the Symbol table entries below...
        st.AddSymbol("product", 'v', 0); //3
        st.AddSymbol("1", 'c', 1); // 4
        st.AddSymbol("$temp", 'v', 0); // 5

    } // end fact symbol

    //factorial Quads
    public void initQTforFactorial(QuadTable qt) {
        qt.AddQuad(5, 3, 0, 2); //MOV
        qt.AddQuad(5, 3, 0, 1); //MOV
        qt.AddQuad(3, 1, 0, 4); //SUB

        //... put the rest of the Quad table entries below...
        qt.AddQuad(10, 4, 0, 7); //JP exit loop
        qt.AddQuad(2, 2, 1, 2); //MUL product = product * i
        qt.AddQuad(4, 1, 3, 1); //ADD
        qt.AddQuad(8, 0, 0, 2); //JMP loop back through
        qt.AddQuad(6, 2, 0, 0); //PRINT
        qt.AddQuad(0, 0, 0, 0); //STOP


    } // end fact quad

    public static void initSTforSummation(SymbolTable st) {
        //st.AddSymbol("n", 'V', 10); already on symbol table (see quad section) 1
        //st.AddSymbol("i", 'V', 0);  already on symbol table (see quad section) 2

        //... put the rest of the Symbol table entries below...
        st.AddSymbol("sum", 'v', 0);  // 6
        st.AddSymbol("1", 'c', 1);
        st.AddSymbol("$temp", 'v', 0);

    } // end sum symbol

    //factorial Quads
    public void initQTforSummation(QuadTable qt) {
        qt.AddQuad(5, 5, 0, 2); //MOV sum = 1
        qt.AddQuad(5, 3, 0, 1); //MOV i = 1
        qt.AddQuad(3, 1, 0, 4); //SUB Compare $temp = i-n (in loop)

        //... put the rest of the Quad table entries below...
        qt.AddQuad(10, 4, 0, 7); //JP exit loop
        qt.AddQuad(4, 2, 1, 2); //ADD product = product * i
        qt.AddQuad(4, 1, 3, 1); //ADD
        qt.AddQuad(8, 0, 0, 2); //JMP loop back through
        qt.AddQuad(6, 2, 0, 0); //PRINT
        qt.AddQuad(0, 0, 0, 0); //STOP

    } // end sum quad

} // end class



