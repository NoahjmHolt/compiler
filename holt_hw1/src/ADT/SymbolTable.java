package ADT;
import java.lang.String;
import java.io.FileWriter;
import java.io.IOException;

public class SymbolTable {

    int tableSize;
    String[][] symbolTable;
    int nextUp;

    public SymbolTable(int maxSize){

        tableSize = maxSize;
        symbolTable = new String[tableSize][4];
        nextUp = 0;

    } // General

    /*
        Triple Overloaded methods
        Storing into the table
        Table goes: Name, Use, Type, Value
     */
    public int AddSymbol(String symbol, char usage, int value){

        // first not over max
        if (nextUp == tableSize){ return -1;}

        // second check if in table
        int index = LookupSymbol(symbol);
        if (index < nextUp && index > -1){ return index; }

        //not equal add to table and
        symbolTable[nextUp][0] = symbol;
        symbolTable[nextUp][1] = String.valueOf(usage);
        symbolTable[nextUp][2] = String.valueOf('I');
        symbolTable[nextUp][3] = Integer.toString(value);

        // update next up
        nextUp = nextUp + 1;

        return (nextUp - 1);

    }
    public int AddSymbol(String symbol, char usage, double value){

        // first not over max
        if (nextUp == tableSize){ return -1;}

        // second check if in table
        int index = LookupSymbol(symbol);
        if (index < nextUp && index > -1){ return index; }

        //not equal add to table and
        symbolTable[nextUp][0] = symbol;
        symbolTable[nextUp][1] = String.valueOf(usage);
        symbolTable[nextUp][2] = String.valueOf('F');
        symbolTable[nextUp][3] = Double.toString(value);

        // update next up
        nextUp = nextUp + 1;

        return (nextUp - 1);

    }
    public int AddSymbol(String symbol, char usage, String value){

        // first not over max
        if (nextUp == tableSize){ return -1;}

        // second check if in table
        int index = LookupSymbol(symbol);
        if (index < nextUp && index > -1){ return index; }

        //not equal add to table and
        symbolTable[nextUp][0] = symbol;
        symbolTable[nextUp][1] = String.valueOf(usage);
        symbolTable[nextUp][2] = String.valueOf('S');
        symbolTable[nextUp][3] = value;

        // update next up
        nextUp = nextUp + 1;

        return (nextUp - 1);

    }
    // end all Add symbols

    /*
        index of symbol
     */
    public int LookupSymbol(String symbol){

        for(int i = 0; i < nextUp; i++) {
            if (symbol.compareToIgnoreCase(symbolTable[i][0]) == 0) {
                // exit and return i if equal
                return i;
            }
        }

        //not found if here
        return -1;

    } // Lookup Symbol

    /*
        Symbol at index
     */
    public String GetSymbol(int index){

        return "placeHolder";

    } // Get Symbol

    /*
        Usage at index
     */
    public char GetUsage(int index){

        return 'c';

    } // Get Usage

    /*
        Type at index
     */
    public char GetDataType(int index){

        return 'c';

    } // Get Data Type

    /*
        String at index
     */
    public String GetString(int index){

        return "Place Holdre";

    } // Get String

    /*
        int at index
     */
    public int GetInteger(int index){

        return 0;

    } // get int

    /*
        float at index
     */
    public double GetFloat(int index){

        return 0;

    } // get float

    /*
        Change values if needed
     */
    public void UpdateSymbol(int index, char usage, int value){

    }
    public void UpdateSymbol(int index, char usage, double value){

    }
    public void UpdateSymbol(int index, char usage, String value){

    }
    // end all Update Symbols

    /*
        print out symbol table
        only bits in use
        Format provided in example file
     */
    public void PrintSymbolTable(String fileName){



    } // Print Table


} // end class
