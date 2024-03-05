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
            } else {
                return -1;
            }
        }

        //not found if here
        return -1;

    } // Lookup Symbol

    /*
        Symbol at index
        symbol is [index][0]
     */
    public String GetSymbol(int index){

        return symbolTable[index][0];

    } // Get Symbol

    /*
        Usage at index
        usage is [index][1]
     */
    public char GetUsage(int index){

        return (symbolTable[index][1]).charAt(0);

    } // Get Usage

    /*
        Type at index
        type is [index][2]
     */
    public char GetDataType(int index){

        return (symbolTable[index][2]).charAt(0);

    } // Get Data Type

    /*
        String at index
        string is [index][3]
     */
    public String GetString(int index){

        return symbolTable[index][3];

    } // Get String

    /*
        int at index
        int is [index][3]
        cast back to int (stored as string)
     */
    public int GetInteger(int index){

        return Integer.parseInt(symbolTable[index][3]);

    } // get int

    /*
        float at index
        float is [index][3]
        Cast back to float (stored as string)
     */
    public double GetFloat(int index){

        return Double.parseDouble(symbolTable[index][3]);

    } // get float

    /*
        Change values if needed
        Table goes: Name, Use, Type, Value
     */
    public void UpdateSymbol(int index, char usage, int value){

        symbolTable[index][1] = String.valueOf(usage);
        symbolTable[index][2] = String.valueOf('I');
        symbolTable[index][3] = Integer.toString(value);

    }
    public void UpdateSymbol(int index, char usage, double value){

        symbolTable[index][1] = String.valueOf(usage);
        symbolTable[index][2] = String.valueOf('F');
        symbolTable[index][3] = Double.toString(value);

    }
    public void UpdateSymbol(int index, char usage, String value){

        symbolTable[index][1] = String.valueOf(usage);
        symbolTable[index][2] = String.valueOf('S');
        symbolTable[index][3] = value;

    }
    // end all Update Symbols

    /*
        print out symbol table
        only bits in use
        Format provided in example file
     */
    public void PrintSymbolTable(String fileName){

        try {
            FileWriter myWriter = new FileWriter(fileName);

            myWriter.write(String.format("| %6s | %25s | %5s | %6s | %30s |%n", "Index", "Name", "Use", "Type", "Value"));

            //print per element
            for (int i = 0; i < nextUp; i++) {
                myWriter.write(String.format("| %6s | %25s | %5s | %6s | %30s |%n", i, symbolTable[i][0], symbolTable[i][1], symbolTable[i][2], symbolTable[i][3]));
            }

            myWriter.close();

            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

    } // Print Table


} // end class
