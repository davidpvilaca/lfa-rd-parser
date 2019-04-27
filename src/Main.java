
import parser.RDParser;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Character[] symbols = {
                new Character('0'),
                new Character('1'),
                new Character('2'),
                new Character('3'),
                new Character('4'),
                new Character('5'),
                new Character('6'),
                new Character('7'),
                new Character('8'),
                new Character('9')
        };
        RDParser parser = new RDParser(new ArrayList<Character>(Arrays.asList(symbols)));
        parser.setTokens("19");
        System.out.printf(parser.digit());
        parser.digit();
    }

}
