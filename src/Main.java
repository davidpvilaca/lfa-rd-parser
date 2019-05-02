
import parser.RDParserMEL;

public class Main {

    public static void main(String[] args) {
        String tokens = "10 * 5 + 100 / 10 - 5 + 7 % 2";
        RDParserMEL parser = new RDParserMEL();
        double result = parser.parse(tokens);
        System.out.println("\"" + tokens + "\" > Accepted");
        System.out.println("Result: " + result);
    }

}
