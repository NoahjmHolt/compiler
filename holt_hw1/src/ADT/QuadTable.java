package ADT;
import java.lang.String;
import java.io.FileWriter;
import java.io.IOException;

public class QuadTable {

    int tableSize;
    int nextUp;

    /*
        initialize vars and get things ready
     */
    public QuadTable(int maxSize){
        tableSize = maxSize;
        nextUp = 0;
    } // general method

    int[][] quadTable = new int[tableSize][4];

    /*
        returns the index of next empty slot in the table
     */
    public int NextQuad(){

        return nextUp;

    } // next quad

    /*
        add new quad and increments next up
     */
    public void AddQuad(int opcode, int op1, int op2, int op3){

        quadTable[nextUp][0] = opcode;
        quadTable[nextUp][1] = op1;
        quadTable[nextUp][2] = op2;
        quadTable[nextUp][3] = op3;

        nextUp = nextUp +1;

    } // add quad

    /*
        using index, return the quad
     */
    public int[] GetQuad(int index){

        return quadTable[index];

    } // Get Quad

    /*
        update op 3 of specified index
     */
    public void UpdateJump(int index, int op3){

        quadTable[index][3] = op3;

    } // Update Jump

    /*
        print out quad table
        only bits in use
        Format provided in example file
     */
    public void PrintQuadTable(String fileName){

        try {
            FileWriter myWriter = new FileWriter(fileName);

            myWriter.write(String.format("| %-6s | %-8s | %5s | %5s | %5s |%n", "Index", "Opcode", "Op 1", "Op 2", "Op 3"));

            //print per element
            for (int i = 0; i < nextUp; i++) {
                myWriter.write(String.format("| %-6d | %-8d | %5d | %5d | %5d |%n", i, quadTable[i][0], quadTable[i][1], quadTable[i][2], quadTable[i][3]));
            }

            myWriter.close();

            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

    } // Print Quad

} // end class
