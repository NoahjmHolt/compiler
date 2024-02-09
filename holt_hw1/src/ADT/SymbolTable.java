package ADT;
import java.lang.String;
import java.io.FileWriter;
import java.io.IOException;

public class SymbolTable {

    int tableSize;
    String[][] symbolTable;

    public SymbolTable(int maxSize){

        tableSize = maxSize;
        symbolTable = new String[tableSize][4];

    } // General

    /*
        Triple Overloaded methods

     */
    public int AddSymbol(String symbol, char usage, int value){
        return 0;
    }
    public int AddSymbol(String symbol, char usage, double value){

        return 0;
    }
    public int AddSymbol(String symbol, char usage, String value){

        return 0;
    }
    // end all Add symbols

    /*
        index of symbol
     */
    public int LookupSymbol(String symbol){

        return 0;

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
