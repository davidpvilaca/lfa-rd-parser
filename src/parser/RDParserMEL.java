package parser;

import java.util.*;

enum ETypeSymbol {
    OPERATOR,
    NUMBER,
    DOT,
    EULER,
    PAREN_L,
    PAREN_R
}

class Symbol {
    public String token;
    public final ETypeSymbol type;

    public Symbol(Character token, ETypeSymbol type) {
        this.type = type;
        this.token = "";
        this.token += token;
    }

    public Symbol(String token, ETypeSymbol type) {
        this.type = type;
        this.token = token;
    }

    public void add(Character c) {
        this.token += c;
    }
}

public class RDParserMEL extends Parser {

    private ArrayList<Symbol> processList = new ArrayList<>();

    public RDParserMEL() {
        super();
    }

    public double parse(String tokens) {
        this.reset();
        this.setTokens(tokens);
        this.expr();
        if (this.symbol != null) {
            throw new Error("Not accept tokens");
        }
        return this.process();
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
                if (this.expectSumSub()) {
                    this.putSymbol(ETypeSymbol.OPERATOR);
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
            this.putSymbol(ETypeSymbol.OPERATOR);
            this.next();
            this.factor();
        }
    }

    private void manyFactor() {
        Character[] terms = { '*', '/', '%' };
        while (true) {
            try {
                if (Arrays.asList(terms).contains(this.symbol)) {
                    this.putSymbol(ETypeSymbol.OPERATOR);
                    this.next();
                    if (this.expect('/')) {
                        this.putSymbol(ETypeSymbol.OPERATOR);
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
        if (this.expectSumSub()) {
            this.putSymbol(ETypeSymbol.OPERATOR);
            this.next();
            this.base();
            return;
        }
        try {
            this.number();
            return;
        } catch (Error e) { }
        if (this.expect('(')) {
            this.putSymbol(ETypeSymbol.PAREN_L);
            this.next();
            this.expr();
            if (this.expect(')')) {
                this.putSymbol(ETypeSymbol.PAREN_R);
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
            this.putSymbol(ETypeSymbol.DOT);
            this.next();
            this.manyDigits();
        }
        if (this.expect('E') || this.expect('e')) {
            this.putSymbol(ETypeSymbol.EULER);
            this.next();
            if (this.expectSumSub()) {
                this.putSymbol(ETypeSymbol.OPERATOR);
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
            this.putSymbol(ETypeSymbol.NUMBER);
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

    private boolean expectSumSub() {
        return this.expect('+') || this.expect('-');
    }

    private void putSymbol(ETypeSymbol type) {
        Symbol s = this.processList.isEmpty() ? null : this.processList.get(this.processList.size() - 1);
        boolean sIsNumber = s != null && (s.type == ETypeSymbol.NUMBER || s.type == ETypeSymbol.EULER);
        boolean symbolIsNumber = type == ETypeSymbol.NUMBER || type == ETypeSymbol.EULER || type == ETypeSymbol.DOT;
        if (sIsNumber && symbolIsNumber) {
            s.add(this.symbol);
        } else {
            this.processList.add(new Symbol(this.symbol, type));
        }
    }

    private double process() {
        Character[] operations = { '^', '(', '*', '/', '%', '+', '-' };
        Symbol n1, n2, op;
        boolean isEnd = false;
        boolean hasOp, isIntDiv;
        int opIndex = 0;
        double result;

        while (!isEnd) {
            hasOp = false;
            for (int i = 0; i < this.processList.size(); i++) {
                op = this.processList.get(i);
                if (op.type == ETypeSymbol.OPERATOR && op.token.charAt(0) == operations[opIndex]) {
                    hasOp = true;
                    n1 = this.processList.get(i-1);
                    n2 = this.processList.get(i+1);
                    isIntDiv = n2.token.charAt(0) == '/';
                    if (isIntDiv) {
                        n2 = this.processList.get(i+2);
                        this.processList.remove(i+1);
                    }
                    this.processList.remove(i-1);
                    this.processList.remove(i-1);
                    this.processList.remove(i-1);
                    result = this.calc(n1.token, op.token.charAt(0), n2.token);
                    if (isIntDiv) {
                        result = (int) result;
                    }
                    this.processList.add(i-1, new Symbol(Double.toString(result),ETypeSymbol.NUMBER)
                    );
                }
            }
            isEnd = this.processList.size() < 2 || opIndex >= operations.length;
            if (!hasOp) {
                opIndex++;
            }
        }
        return Double.parseDouble(this.processList.get(0).token);
    }

    private double calc(String n1, Character operator, String n2) {
        double _n1 = Double.parseDouble(n1);
        double _n2 = Double.parseDouble(n2);
        switch (operator) {
            case '^':
                return Math.pow(_n1, _n2);
            case '*':
                return _n1 * _n2;
            case '/':
                return _n1 / _n2;
            case '%':
                return _n1 % _n2;
            case '+':
                return _n1 + _n2;
            case '-':
                return _n1 - _n2;
            default:
                throw new Error("Error on calc: Operator not valid");
        }
    }

}
