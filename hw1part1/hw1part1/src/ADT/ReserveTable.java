package ADT;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReserveTable {

    private ArrayList<String> stringList = new ArrayList<String>();
    private ArrayList<Integer> codeList = new ArrayList<Integer>();

    public ReserveTable(int maxSize)
    {

    }

    public int Add(String name, int code)
    {

        stringList.add(name.toLowerCase());
        codeList.add(code);

        return stringList.indexOf(name.toLowerCase());
    } // end add

    public int LookupName(String name)
    {
        // find the name
        int found = stringList.indexOf(name.toLowerCase());
        //if found return code associated
        if (found != -1) {
            return codeList.get(found);
        }

        return -1; //not found return -1
    } // end name look up

    public String LookupCode(int code)
    {
        // find the code
        int found = codeList.indexOf(code);
        // if found return name associated
        if (found != -1){
            return stringList.get(found);
        }

        return ""; //not found return nothing
    } // end code look up

    public void PrintReserveTable(String filename)
    {

        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

    } // end print table

} // end reserve table class
