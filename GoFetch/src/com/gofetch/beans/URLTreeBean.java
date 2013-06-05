package com.gofetch.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.gofetch.models.URLNodeImpl;

// extended model..
//import com.gofetch.models.URLNodeImpl;

@ManagedBean(name = "urlTreeBean")
@SessionScoped
public class URLTreeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(URLTreeBean.class.getName());

	// from:
	// http://javaevangelist.blogspot.co.uk/2012/07/custom-primefaces-tree-example.html

	private TreeNode root;
	//private TreeNode[] selectedNodes;
	private String targetURLAddress;
	
	private TreeNode selectedNode;
	
	

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void clearTree() {

		root.getChildren().clear();

	}

	// default constructor
	public URLTreeBean() {
		// extended version
		root = new URLNodeImpl("Root", null);
		
		// basic version:
		//root = new DefaultTreeNode("Root", null);
		// testing:
		root = new DefaultTreeNode("Root", null);  
        TreeNode node0 = new URLNodeImpl("Node 0", root);  
        TreeNode node1 = new URLNodeImpl("Node 1", root);  
        TreeNode node2 = new URLNodeImpl("Node 2", root);  
          
        TreeNode node00 = new URLNodeImpl("Node 0.0", node0);  
        TreeNode node01 = new URLNodeImpl("Node 0.1", node0);  
          
        TreeNode node10 = new URLNodeImpl("Node 1.0", node1);  
        TreeNode node11 = new URLNodeImpl("Node 1.1", node1);  
          
        TreeNode node000 = new URLNodeImpl("Node 0.0.0", node00);  
        TreeNode node001 = new URLNodeImpl("Node 0.0.1", node00);  
        TreeNode node010 = new URLNodeImpl("Node 0.1.0", node01);  
          
        TreeNode node100 = new URLNodeImpl("Node 1.0.0", node10); 

	}

	public URLTreeBean(Object data, TreeNode parent, String title) {
		
		// extended version
		root = new URLNodeImpl(title, null);
		
		// basic version:
		//root = new DefaultTreeNode(title, null);

	}
	
	

	public String getTargetURLAddress() {
		return targetURLAddress;
	}

	public void setTargetURLAddress(String targetURLAddress) {
		this.targetURLAddress = targetURLAddress;
	}

	/**
	 * This method returns the tree model based on the root node.
	 * 
	 * @return root node.
	 */
	public TreeNode getRoot() {
		return root;
	}

	/**
	 * {@inheritDoc }
	 * 
	 * Adds a {@link javax.faces.application.FacesMessage} with event data to
	 * the {@link javax.faces.context.FacesContext}.
	 */
	public void onNodeSelect(NodeSelectEvent event) {
		 System.out.println("NodeSelectEvent Fired");
		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 "Selected", event.getTreeNode().getData().toString());
		 FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(),
		 msg);
		
		log.info("In URLTreeBean::onNodeSelect()");
		log.info(event.getTreeNode().getData().toString());

	}

	public void onNodeUnSelect(NodeUnselectEvent event) {
		 System.out.println("NodeSelectEvent Fired");
		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 "Selected", event.getTreeNode().getData().toString());
		 FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(),
		 msg);
		
		log.info("In URLTreeBean::onNodeUnSelect()");
		log.info(event.getTreeNode().getData().toString());

	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Adds a {@link javax.faces.application.FacesMessage} with event data to
	 * the {@link javax.faces.context.FacesContext}.
	 */
	public void onNodeExpand(NodeExpandEvent event) {

		 System.out.println("NodeExpandEvent Fired");
		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 "Expanded", event.getTreeNode().getData().toString());
		 FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(),
		 msg);
		
		log.info("In URLTreeBean::onNodeExpand()");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Adds a {@link javax.faces.application.FacesMessage} with event data to
	 * the {@link javax.faces.context.FacesContext}.
	 */
	public void onNodeCollapse(NodeCollapseEvent event) {
		 System.out.println("NodeCollapseEvent Fired");
		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 "Collapsed", event.getTreeNode().getData().toString());
		 FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(),
		 msg);
		
		log.info("In URLTreeBean::onNodeCollapse()");
	}

//	public TreeNode[] getSelectedNodes() {
//		return selectedNodes;
//	}
//
//	public void setSelectedNodes(TreeNode[] selectedNodes) {
//		this.selectedNodes = selectedNodes;
//	}

}