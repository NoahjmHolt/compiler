package ADT;
import java.lang.String;
import java.io.FileWriter;
import java.io.IOException;

public class SymbolTable {

    public SymbolTable(int maxSize){


    } // General

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

    public int LookupSymbol(String symbol){

        return 0;

    } // Lookup Symbol

    public String GetSymbol(int index){

        return "placeHolder";

    } // Get Symbol

    public char GetUsage(int index){

        return 'c';

    } // Get Usage

    public char GetDataType(int index){

        return 'c';

    } // Get Data Type

    public String GetString(int index){

        return "Place Holdre";

    } // Get String

    public int GetInteger(int index){

        return 0;

    } // get int

    public double GetFloat(int index){

        return 0;

    } // get float

    public void UpdateSymbol(int index, char usage, int value){

    }
    public void UpdateSymbol(int index, char usage, double value){

    }
    public void UpdateSymbol(int index, char usage, String value){

    }
    // end all Update Symbols

    public void PrintSymbolTable(String fileName){



    } // Print Table


} // end class
