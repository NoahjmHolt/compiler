
import ADT.*;

/**
 *
 * @author abrouill SPRING 2024
 *  implimented by nholt
 */
public class main {

    public static void main(String[] args) {
        String filePath = args[0];
        boolean traceon = true;
        System.out.println("Student Name, Last 4 of student number, CS4100/5100, SPRING 2024");
        System.out.println("INPUT FILE TO PROCESS IS: "+filePath);

        Syntactic parser = new Syntactic(filePath, traceon);
        parser.parse();
        System.out.println("Done.");
    }

}
