package parser;

abstract class Parser {

    protected Character symbol;
    private String tokens;
    private int index = 0;

    /**
     * accept char
     * @param s expected char
     * @return actual character is s
     */
    protected boolean accept(char s) {
        return this.symbol != null && this.symbol == s;
    }

    /**
     * next char of tokens
     */
    protected void next() {
        if (!this.endOfTokens()) {
            this.symbol = this.tokens.charAt(this.index++);
            while (this.symbol == ' ') {
                this.next();
            }
        } else {
            this.index = -1;
            this.symbol = null;
        }
    }

    /**
     * ran the entire string of tokens
     * @return not has next char
     */
    protected boolean endOfTokens() {
        return this.index >= this.tokens.length();
    }

    /**
     * reset index to scroll tokens
     */
    protected void reset() {
        this.index = 0;
    }

    /**
     * set tokens
     * @param tokens string expression
     */
    protected void setTokens(String tokens) {
        this.tokens = tokens;
        this.next();
    }

    /**
     * get string expression tokens
     * @return string expression
     */
    protected String getTokens() {
        return this.tokens;
    }

}
