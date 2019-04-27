package parser;

import java.util.ArrayList;

abstract class Parser {

    protected final ArrayList<Character> symbols;
    protected Character symbol;
    protected String tokens;
    private int index = 0;

    public Parser(ArrayList<Character> symbols) {
        ArrayList<Character> _arr = (ArrayList<Character>) symbols.clone();
        _arr.replaceAll(c -> new Character(Character.toLowerCase(c)));
        this.symbols = _arr;
    }

    protected boolean accept(Character s) {
        return this.symbols.contains(s);
    }

    protected boolean expect(char s) {
        if (this.accept(s)) {
            return true;
        }
        return false;
    }

    protected void next() {
        this.symbol = this.tokens.charAt(this.index);
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
        this.next();
    }

}
