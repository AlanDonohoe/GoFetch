package com.gofetch.models;

/**
 * {@code enum} which represents the types of tree objects as either
 * "leaf", or "node".
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @version 1.0
 */

public enum TreeNodeType {
	 
    LEAF("leaf"), NODE("node");
    private String type;
 
    private TreeNodeType(final String type) {
        this.type = type;
    }
 
    @Override
    public String toString() {
        return type;
    }
 
    public String getType() {
        return type;
    }
}