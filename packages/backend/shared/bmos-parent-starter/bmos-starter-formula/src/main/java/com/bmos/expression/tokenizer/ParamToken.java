package com.bmos.expression.tokenizer;

public class ParamToken extends Token {

    private String name;

    /**
     * Get the name of the setVariable
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Create a new instance
     *
     * @param name the name of the setVariable
     */
    public ParamToken(String name) {
        super(TOKEN_PARAM);
        this.name = name;
    }
}
