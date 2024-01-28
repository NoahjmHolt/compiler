package ADT;
import java.lang.String;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReserveTable {

    private List<String> stringList;
    private List<Integer> codeList;

    public ReserveTable(int maxSize)
    {

    }

    public int Add(String name, int code)
    {

        stringList.add(name);
        codeList.add(code);

        return stringList.indexOf(name);
    } // end add

    public int LookupName(String name)
    {
        // find the name
        int found = stringList.indexOf(name);
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

     

    } // end print table

} // end reserve table class
