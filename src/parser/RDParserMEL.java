package parser;

import java.util.Arrays;

public class RDParserMEL extends Parser {

    public RDParserMEL() {
        super();
    }

    public void parse(String tokens) {
        this.reset();
        this.setTokens(tokens);
        this.expr();
        if (this.symbol != null) {
            throw new Error("Not accept tokens");
        }
    }

    public void expr() {
        this.term();
        this.manyTerm();
    }

    public void term() {
        this.factor();
        this.manyFactor();
    }

    private void manyTerm() {
        while (true) {
            try {
                if (this.expect('+') || this.expect('-')) {
                    this.next();
                    this.term();
                } else {
                    break;
                }
            } catch (Error e) {
                break;
            }
        }
    }

    public void factor() {
        this.base();
        if (this.expect('^')) {
            this.next();
            this.factor();
        }
    }

    private void manyFactor() {
        Character[] terms = { '*', '/', '%' };
        while (true) {
            try {
                if (Arrays.asList(terms).contains(this.symbol)) {
                    this.next();
                    if (this.expect('/')) {
                        this.next();
                    }
                    this.factor();
                } else {
                    break;
                }
            } catch (Error e) {
                break;
            }
        }
    }

    public void base() {
        if (this.expect('+') || this.expect('-')) {
            this.next();
            this.base();
            return;
        }
        try {
            this.number();
            return;
        } catch (Error e) { }
        if (this.expect('(')) {
            this.next();
            this.expr();
            if (this.expect(')')) {
                this.next();
                return;
            }
        }
        throw new Error("Invalid base");
    }

    public void number() {
        this.digit();
        this.manyDigits();
        if (this.expect('.')) {
            this.next();
            this.manyDigits();
        }
        if (this.expect('E') || this.expect('e')) {
            this.next();
            if (this.expect('+') || this.expect('-')) {
                this.next();
            }
            this.digit();
            this.manyDigits();
        }
    }

    public void digit() {
        Character[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean contains = Arrays.asList(digits).contains(this.symbol);
        if (contains) {
            this.next();
        } else {
            throw new Error("Invalid digit");
        }
    }

    private void manyDigits() {
        while (true) {
            try {
                this.digit();
            } catch (Error e) {
                break;
            }
        }
    }

}
