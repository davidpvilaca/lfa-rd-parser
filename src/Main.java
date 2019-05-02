
import parser.RDParserMEL;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String tokens = new Scanner(System.in).nextLine(); // Ex: "10 * 5 + 100 / 10 - 5 + 7 % 2"
        RDParserMEL parser = new RDParserMEL();
        double result = parser.parse(tokens);
        System.out.println(result);

    }

}
