package parser;

import java.util.ArrayList;
import java.util.Arrays;

public class RDParser extends Parser {

    public RDParser(ArrayList<Character> symbols) {
        super(symbols);
    }

    public String digit() {
        int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        if (Arrays.asList(digits).contains(this.symbol)) {
            this.next();
            return "Accepted";
        }
        throw new Error("Invalid digit");
    }

}
