//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String filePath = args[0];

    }

    private int handleForLoopConversion() {

        //removed recur and errors according to question
        //not sure if that is the right interpretation now
        //if not realize they were att end of each if as an error
        //return -1 if error occurred.
        token = lex.GetNextToken();

        // taken care of "FOR"
        System.out.print("for (");
        // print not print line so rest can be on the same line

        // assuming Statement and Simple print to same line also
        // so can right after to continue the statement

        // with variable being optional for general but not ours
        // I left it in here, leaving an assumption that Variable
        // also prints since variable calls identifier which is
        // non-terminal like the others.
        recur = Variable(); // prints i (general use var)

        if (token.code == lex.codeFor("ASGN")) {
            token = lex.GetNextToken();
            recur = SimpleExpression(); //simple prints according to question
            System.out.print("; ");
        }

        if (token.code == lex.codeFor("_TO_")) {
            token = lex.GetNextToken();
            recur = SimpleExpression(); //simple prints according to question
            System.out.print(")"); // end of for loop line
        }

        //bracket (like for functions can be after main body so println will do
        System.out.println("{");

        if (token.code == lex.codeFor("_DO_")) {
            token = lex.GetNextToken();
            Statement(); //Statement prints according to question
        }

        //finish for loop with closing bracket
        System.out.println("}");

        return 0;

    }

}