package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.tree.Tree;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.entities.User;
import com.gofetch.models.URLNodeImpl;

// extended model..
//import com.gofetch.models.URLNodeImpl;

@ManagedBean(name = "urlTreeBean")
@ViewScoped
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
	
	
	@ManagedProperty("#{fullScreenDashboardBean}")
	private FullScreenDashboardBean fullScreenDashboardBean;
	
	
	
	//transient private Tree bindingTree;
	
	/////
	// 20-5-13:
	

	public FullScreenDashboardBean getFullScreenDashboardBean() {
		return fullScreenDashboardBean;
	}

	public void setFullScreenDashboardBean(
			FullScreenDashboardBean fullScreenDashboardBean) {
		this.fullScreenDashboardBean = fullScreenDashboardBean;
	}


	List<Integer> clientsIDs = new ArrayList<Integer>();

	URLDBService urlDBService = new URLDBService();

	List<URL> targetURLs;
	
	///
	//////
	
	/////
	// 20-5-13:
	// avoid constructor...
//	public void clearTreeAndAddNewNodes(List<User> clientsSelectedByUser){
//		
//		//TODO: here!! - need to clear the child nodes.....
//		//root.getChildren().clear();
//		
//		//or:
//		
//		root.getChildren().clear();  
//		//root.getParent().getChildren().remove(root);  
//		//root.setParent(null);  
//	          
//		root = null;  
//		root = new DefaultTreeNode("Root", null);
//		
//		// not working on server...
////		if(null != bindingTree)
////			bindingTree.setRowKey(null);
//		// OR:
//		//NOT working...
////		List<TreeNode> nodes = this.root.getChildren();
////		
////		Iterator<TreeNode> i = nodes.iterator();
////		while (i.hasNext()) {
////		   //TreeNode node = i.next(); 
////		   // Use isLeaf() method to check doesn't have childs.
////		   i.remove();
////		}
//		
//		/*
//		 * node.getParent().getChildren().remove(node);  
//
//  node.setParent(null);  
//		 */
//		
//		targetURLs = urlDBService.getClientsTargetURLs(clientsSelectedByUser.get(0).getId(), true);
//
//		for (int c = 0; c < targetURLs.size(); c++) {
//			
//			// extended version of the tree impl
//			new DefaultTreeNode(targetURLs.get(c).getUrl_address(),
//					root);
//		}
//		
//	}

	@PostConstruct
	void init() {
			
		root = new DefaultTreeNode("Root", null);

		if (!fullScreenDashboardBean.getClientsSelectedByUser().isEmpty()) {

			targetURLs = urlDBService.getClientsTargetURLs(
					fullScreenDashboardBean.getClientsSelectedByUser().get(0)
							.getId(), true);

			for (int c = 0; c < targetURLs.size(); c++) {

				// extended version of the tree impl
				new DefaultTreeNode(targetURLs.get(c).getUrl_address(), root);
			}
		}
		
		//root = new DefaultTreeNode("Root", null); ??
		
//		if(null ==fullScreenDashboardBean.getClientsSelectedByUser().get(0))
//			return;
//		
//		targetURLs = urlDBService.getClientsTargetURLs(fullScreenDashboardBean.getClientsSelectedByUser().get(0).getId(), true);
//
//		for (int c = 0; c < targetURLs.size(); c++) {
//			
//			// extended version of the tree impl
//			new DefaultTreeNode(targetURLs.get(c).getUrl_address(),
//					root);
//		}
		
	}
	
	// 20-5-13:
	public URLTreeBean(List<User> clientsSelectedByUser){
		
		if(null == root)
			root = new DefaultTreeNode("Root", null);
		else
			root.getChildren().clear();
		
		targetURLs = urlDBService.getClientsTargetURLs(clientsSelectedByUser.get(0).getId(), true);

		for (int c = 0; c < targetURLs.size(); c++) {
			
			// extended version of the tree impl
			new DefaultTreeNode(targetURLs.get(c).getUrl_address(),
					root);
			
			//trying to add ajax.... - but move this to p:tree, NOT p:node...
//			newChild.addClientBehavior( "select", pajaxSelect );
//			newChild.addClientBehavior( "expand", pajaxExpand );
			//default:
//			new DefaultTreeNode(targetURLs.get(c).getUrl_address(),
//					newNode);

		}
		
	}

	
	public void clearTree() {

		root.getChildren().clear();
		
	}

	// default constructor
	public URLTreeBean() {
		// extended version
		//root = new URLNodeImpl("Root", null);
		
		// basic version: // moved this to post construct..???
//		root = new DefaultTreeNode("Root", null);
	
		
		// see: http://stackoverflow.com/questions/6660458/how-to-create-a-dynamic-tree
		
		
		//root.getChildren().clear();
		// testing1:
		//root = new DefaultTreeNode("Root", null);  
//        TreeNode node0 = new DefaultTreeNode("Node 0", root);  
//        TreeNode node1 = new DefaultTreeNode("Node 1", root);  
//        TreeNode node2 = new DefaultTreeNode("Node 2", root);  
//          
//        TreeNode node00 = new DefaultTreeNode("Node 0.0", node0);  
//        TreeNode node01 = new DefaultTreeNode("Node 0.1", node0);  
//          
//        TreeNode node10 = new DefaultTreeNode("Node 1.0", node1);  
//        TreeNode node11 = new DefaultTreeNode("Node 1.1", node1);  
//          
//        TreeNode node000 = new DefaultTreeNode("Node 0.0.0", node00);  
//        TreeNode node001 = new DefaultTreeNode("Node 0.0.1", node00);  
//        TreeNode node010 = new DefaultTreeNode("Node 0.1.0", node01);  
//          
//        TreeNode node100 = new DefaultTreeNode("Node 1.0.0", node10); 
		
		// testing 2:
		
//        TreeNode node0 = new URLNodeImpl("Node abc", root);  
//        TreeNode node1 = new URLNodeImpl("Node fdsadf", root);  
//        TreeNode node2 = new URLNodeImpl("Node fdsafdsa", root);  
//          
//        TreeNode node00 = new URLNodeImpl("Node fdsafdsafdsa", node0);  
//        TreeNode node01 = new URLNodeImpl("Node fdsafdsa", node0);  
//          
//        TreeNode node10 = new URLNodeImpl("Node fdsafdsa", node1);  
//        TreeNode node11 = new URLNodeImpl("Node dsafdsafdsafdsa", node1);  
//          
//        TreeNode node000 = new URLNodeImpl("Node fdsafdsafdsa", node00);  
//        TreeNode node001 = new URLNodeImpl("Node fdsafdsafdsa", node00);  
//        TreeNode node010 = new URLNodeImpl("Node fdsafdsafdsa", node01);  
//          
//        TreeNode node100 = new URLNodeImpl("Node fdsafdsafdsa", node10); 

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
		 log.info("URLTreeBean - NodeSelectEvent");
//		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
//		 "URLTreeBean - Selected", event.getTreeNode().getData().toString());
//		 FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(),
//		 msg);
//		
		String nodeData = selectedNode.getData().toString();
		 
		 log.info("URLTreeBean - NodeSelectEvent");
			
			FacesMessage msg0 = new FacesMessage(FacesMessage.SEVERITY_INFO,
					 "URL Tree - onNodeExpand", "");
			 
			 FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO,
					 "URLTreeBean - Expanded", "");
			 
			 FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_INFO,
					 nodeData, nodeData);
			 
			 
			 FacesContext.getCurrentInstance().addMessage("1", msg0);

			 FacesContext.getCurrentInstance().addMessage("1", msg1);
			 
			 FacesContext.getCurrentInstance().addMessage("1", msg2);
		 
		log.info("In URLTreeBean::onNodeSelect()");
		//log.info(event.getTreeNode().getData().toString());

	}

	public void onNodeUnSelect(NodeUnselectEvent event) {
		 System.out.println("NodeSelectEvent Fired");
		 
		 
		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 "URLTreeBean - Selected", event.getTreeNode().getData().toString());
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
//		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
//		 "URLTreeBean - Expanded", event.getTreeNode().getData().toString());
//		 FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(),
//		 msg);
		 
		String nodeData = selectedNode.getData().toString();
		
		FacesMessage msg0 = new FacesMessage(FacesMessage.SEVERITY_INFO,
				 "URL Tree - onNodeExpand", "");
		 
		 FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO,
				 "URLTreeBean - Expanded", "");
		 
		 FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_INFO,
				 "nodeData", "");
		 
		 
		 FacesContext.getCurrentInstance().addMessage("1", msg0);

		 FacesContext.getCurrentInstance().addMessage("1", msg1);
		 
		 FacesContext.getCurrentInstance().addMessage("1", msg2);
		 
		
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
		 "URLTreeBean - Collapsed", event.getTreeNode().getData().toString());
		 FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(),
		 msg);
		
		log.info("In URLTreeBean::onNodeCollapse()");
	}


	public TreeNode getSelectedNode() {
		return selectedNode;
	}


	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

//	public Tree getBindingTree() {
//		return bindingTree;
//	}
//
//	public void setBindingTree(Tree bindingTree) {
//		this.bindingTree = bindingTree;
//	}


//	public void setRoot(TreeNode root) {
//		this.root = root;
//	}

//	public TreeNode[] getSelectedNodes() {
//		return selectedNodes;
//	}
//
//	public void setSelectedNodes(TreeNode[] selectedNodes) {
//		this.selectedNodes = selectedNodes;
//	}
	
	
	

}