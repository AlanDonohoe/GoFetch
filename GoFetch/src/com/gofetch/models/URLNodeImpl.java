package com.gofetch.models;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/*
 * from: http://javaevangelist.blogspot.co.uk/2012/07/custom-primefaces-tree-example.html
 * 
 */

public class URLNodeImpl extends DefaultTreeNode {
	
	  private static final long serialVersionUID = 5333810777428638968L;
	  
	  
	  /**
	     *
	     * @param type The type of node this represents.
	     * @param data {@code Object} value stored in the node.
	     * @param parent the {@link org.primefaces.model.TreeNode} which is the
	     * parent to this object, or {@code null} if this is the "root"
	     * node.
	     */
	    public URLNodeImpl(TreeNodeType type, Object data, TreeNode parent) {
	        super(type.getType(), data, parent);
	    }
	 
	    /**
	     * Constructor which sets {@code Object} data, and parent node.
	     *
	     * @param data {@code Object} value stored in the node.
	     * @param parent parent the {@link org.primefaces.model.TreeNode} which is
	     * the parent to this object, or {@code null} if this is the
	     * "root" node.
	     */
	    public URLNodeImpl(Object data, TreeNode parent) {
	        super(data, parent);
	    }
	 
	    /**
	     * This method returns {@link com.bluelotussoftware.example.jsf.TreeNodeType#getType()}
	     * depending on whether the node is a "leaf" node which contains
	     * no children, or a "node" if it contains children.
	     *
	     * @return {@link com.bluelotussoftware.example.jsf.TreeNodeType#getType()}
	     * based on whether this node has child objects.
	     */
//	    @Override
//	    public String getType() {
//	        if (isLeaf()) {
//	            return TreeNodeType.LEAF.getType();
//	        } else {
//	            return TreeNodeType.NODE.getType();
//	        }
//	    }
	    
//	    
//	    public void setChildren(List<TreeNode> children){
//	    	super.setChildren(children); //addChild(childNode);
//	    }

}
