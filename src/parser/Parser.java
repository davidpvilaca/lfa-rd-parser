package parser;

abstract class Parser {

    protected Character symbol;
    private String tokens;
    private int index = 0;

    protected boolean accept(Character s) {
        return this.symbol == s;
    }

    protected boolean expect(char s) {
        if (this.accept(s)) {
            return true;
        }
        return false;
    }

    protected void next() {
        if (!this.endOfTokens()) {
            this.symbol = this.tokens.charAt(this.index++);
            if (this.symbol == ' ') {
                this.next();
            }
        } else {
            this.index = -1;
            this.symbol = null;
        }
    }

    protected boolean endOfTokens() {
        return this.index >= this.tokens.length();
    }

    protected void reset() {
        this.index = 0;
    }

    protected void setTokens(String tokens) {
        this.tokens = tokens;
        this.next();
    }

    protected String getTokens() {
        return this.tokens;
    }

}
