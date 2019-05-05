package parser;

import java.util.*;

/**
 * ENUM for type of symbol
 * to process list
 */
enum ETypeSymbol {
    OPERATOR,
    NUMBER,
    DOT,
    EULER,
    PAREN_L,
    PAREN_R
}

/**
 * Symbol class to process list
 */
class Symbol {
    /**
     * Char or String expression number (example: 1, 1.1, 100.1)
     */
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

    // to calculate the result of the analyzed expression
    private ArrayList<Symbol> processList = new ArrayList<>();

    public RDParserMEL() {
        super();
    }

    /**
     * to validate input tokens and calculate the result of expression
     * @param tokens expression
     * @return result
     */
    public double parse(String tokens) {
        this.reset();
        this.setTokens(tokens);
        this.expr();
        if (this.symbol != null) {
            throw new Error("Not accept tokens");
        }
        return this.process(this.processList);
    }

    /**
     * expression
     */
    private void expr() {
        this.term();
        this.manyTerm();
    }

    /**
     * term expression
     */
    private void term() {
        this.factor();
        this.manyFactor();
    }

    /**
     * validate all terms in sequence
     */
    private void manyTerm() {
        while (true) {
            try {
                if (this.acceptSumSub()) {
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

    /**
     * factor expression
     */
    private void factor() {
        this.base();
        if (this.accept('^')) {
            this.putSymbol(ETypeSymbol.OPERATOR);
            this.next();
            this.factor();
        }
    }

    /**
     * validate all factors in sequence
     */
    private void manyFactor() {
        Character[] terms = { '*', '/', '%' };
        while (true) {
            try {
                if (Arrays.asList(terms).contains(this.symbol)) {
                    this.putSymbol(ETypeSymbol.OPERATOR);
                    this.next();
                    if (this.accept('/')) {
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

    /**
     * base expression
     */
    private void base() {
        if (this.acceptSumSub()) {
            this.putSymbol(ETypeSymbol.OPERATOR);
            this.next();
            this.base();
            return;
        }
        try {
            this.number();
            return;
        } catch (Error e) { }
        if (this.accept('(')) {
            this.putSymbol(ETypeSymbol.PAREN_L);
            this.next();
            this.expr();
            if (this.accept(')')) {
                this.putSymbol(ETypeSymbol.PAREN_R);
                this.next();
                return;
            }
        }
        throw new Error("Invalid base");
    }

    /**
     * number expression
     */
    private void number() {
        this.digit();
        this.manyDigits();
        if (this.accept('.')) {
            this.putSymbol(ETypeSymbol.DOT);
            this.next();
            this.manyDigits();
        }
        if (this.accept('E') || this.accept('e')) {
            this.putSymbol(ETypeSymbol.EULER);
            this.next();
            if (this.acceptSumSub()) {
                this.putSymbol(ETypeSymbol.OPERATOR);
                this.next();
            }
            this.digit();
            this.manyDigits();
        }
    }

    /**
     * digit expression
     */
    private void digit() {
        Character[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean contains = Arrays.asList(digits).contains(this.symbol);
        if (contains) {
            this.putSymbol(ETypeSymbol.NUMBER);
            this.next();
        } else {
            throw new Error("Invalid digit");
        }
    }

    /**
     * validate all digits in sequence
     */
    private void manyDigits() {
        while (true) {
            try {
                this.digit();
            } catch (Error e) {
                break;
            }
        }
    }

    /**
     * validate if actual char is plus or less
     * @return true if has '+' or '-'
     */
    private boolean acceptSumSub() {
        return this.accept('+') || this.accept('-');
    }

    /**
     * add symbol instance on process list
     * @param type type of symbol
     */
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

    /**
     * scroll through the list filled in parser validation
     * and calculate the result of the expression
     * @param list symbol list
     * @return result
     */
    private double process(ArrayList<Symbol> list) {
        @SuppressWarnings (value="unchecked")
        ArrayList<Symbol> _list = (ArrayList<Symbol>) list.clone();
        Character[] operations = { '(', '^', '*', '/', '%', '-', '+' };
        Symbol op;
        boolean isEnd = false;
        boolean hasOp;
        int opIndex = 0;

        while (!isEnd) {
            hasOp = false;
            for (int i = 0; i < _list.size(); i++) {
                op = _list.get(i);
                if (op.type == ETypeSymbol.OPERATOR && op.token.charAt(0) == operations[opIndex]) {
                    hasOp = true;
                    _list = this.processOne(_list, i);
                } else if (op.type == ETypeSymbol.PAREN_L) {
                    hasOp = true;
                    _list.remove(i);
                    ArrayList<Symbol> sub = new ArrayList<>();
                    Symbol symb = _list.get(i);
                    int countParenL = symb.type == ETypeSymbol.PAREN_L ? 1 : 0;
                    while (symb.type != ETypeSymbol.PAREN_R || countParenL > 0) {
                        sub.add(symb);
                        _list.remove(i);
                        symb = _list.get(i);
                        if(symb.type == ETypeSymbol.PAREN_L) {
                            countParenL++;
                        } else if (symb.type == ETypeSymbol.PAREN_R && countParenL > 0) {
                            countParenL--;
                            sub.add(symb);
                            _list.remove(i);
                            symb = _list.get(i);
                        }
                    }
                     _list.remove(i);
                    symb = new Symbol(Double.toString(this.process(sub)), ETypeSymbol.NUMBER);
                    _list.add(i, symb);
                }
            }
            isEnd = _list.size() < 2 || opIndex >= operations.length;
            if (!hasOp) {
                opIndex++;
            }
        }
        return Double.parseDouble(_list.get(0).token);
    }

    /**
     * process first expression of symbol list from index (example: "1 + 1")
     * @param symbols symbol list
     * @param i index
     * @return result symbol
     */
    private ArrayList<Symbol> processOne(ArrayList<Symbol> symbols, int i) {
        @SuppressWarnings (value="unchecked")
        ArrayList<Symbol> _list = (ArrayList<Symbol>)symbols.clone();
        Symbol n1, n2, op;
        boolean isIntDiv;
        double result;
        op = _list.get(i);
        n1 = _list.get(i-1);
        n2 = _list.get(i+1);
        isIntDiv = n2.token.charAt(0) == '/';
        if (isIntDiv) {
            n2 = _list.get(i+2);
            _list.remove(i+1);
        }
        _list.remove(i-1);
        _list.remove(i-1);
        _list.remove(i-1);
        result = this.calc(n1.token, op.token.charAt(0), n2.token);
            if (isIntDiv) {
            result = (int) result;
        }
        _list.add(i-1, new Symbol(Double.toString(result),ETypeSymbol.NUMBER));
        return _list;
    }

    /**
     * to calculate result of operation from n1 on n2
     * @param n1 expression (token of symbol with NUMBER type)
     * @param operator token of symbol wuth OPERATOR type
     * @param n2 expression (token of symbol with NUMBER type)
     * @return result
     */
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
