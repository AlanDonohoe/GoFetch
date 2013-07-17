package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

import com.gofetch.charts.GoogleChartsWrapper;
import com.gofetch.entities.MiscSocialData;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLAndLinkData;
import com.gofetch.entities.URLDBService;
import com.gofetch.entities.User;
import com.gofetch.entities.UserDBService;

/* bean that backs dashboard.xhtml
 * 
 */
@ManagedBean
@SessionScoped
public class FullScreenDashboardBean implements Serializable {

	// ////////////
	//
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(FullScreenDashboardBean.class
			.getName());
	//
	// /////////////
	
	private URLDBService urlDBService = new URLDBService();
	
	//////
	// refs to other beans.. could have them as nested members??
	
    @ManagedProperty("#{clientTargetURLsBean}")
	private ClientTargetURLsBean clientTargetURLsBean;
	//
	////////

	// /////////////
	// Entities:
    
    //////
    // Just for testing - delete when done...
    
    private List<Integer> testList = new ArrayList<Integer>();
    
    private List<Integer> innerTestList = new ArrayList<Integer>();
    
	public List<Integer> getTestList() {
		
		
		
		return testList;
	}

	public void setTestList(List<Integer> testList) {
		
		
		this.testList = testList;
	}
	


	public List<Integer> getInnerTestList() {
		

		
		return innerTestList;
	}

	public void setInnerTestList(List<Integer> innerTestList) {
		this.innerTestList = innerTestList;
	}

    
    
    //
    /////////






	private List<User> clientsFromDB = null; // new ArrayList<User>(); // pulls
												// all clients from the DB. may
												// want to break this
												// functionality out...
	private List<User> clientsSelectedByUser = new ArrayList<User>();
	
	private List<Integer> selectedTargetURLIds = new ArrayList<Integer>();
	//private List<URL> sourceURLs = new ArrayList<URL>();
	//private List<Link> links = new ArrayList<Link>();

	// may need just one or the other - let the lazy loading happen in
	// URLAndLinkDataModel ?? and remove URLandLinkData from here?
	// let user select lots of URLAndLinkData rows, saving each link and URL id
	// in a separate list... - so dont need to pull all
	// the URLs and links to memory at once....
	//private List<URLAndLinkData> urlAndLinks = new ArrayList<URLAndLinkData>(); // or
																				// just
																				// use
																				// this
																				// list
																				// to
																				// hold
																				// all
																				// the
																				// URLAndLinks
																				// that
																				// the
																				// user
																				// has
																				// selected
																				// for
																				// subsequent
																				// report
																				// generation....
	private LazyDataModel<URLAndLinkData> urlAndLinksLazyModel;
	private URLAndLinkData selectedURLAndLinkData;
	
	private TreeNode[] selectedNodes;
	private TreeNode selectedNode;

	private List<MiscSocialData> socialData = new ArrayList<MiscSocialData>();
	// private List<URL> targetURLs;// = new ArrayList<URL>(); // - initialize
	// after we make call to DB.

	//
	// /////////////

	// ////////////////
	// Helper objects

	private GoogleChartsWrapper googleSocialChart = new GoogleChartsWrapper();
	private GoogleChartsWrapper googleBackLinksChart = new GoogleChartsWrapper();
	private BackLinkTableBean backLinkTableBean = new BackLinkTableBean();
	//private ClientTargetURLsBean clientTargetURLsBean = new ClientTargetURLsBean();

	// ////////////////////////////////////////////////////////
	// Data structures for drop downs, menus, etc.

	// 1: Clients:
	private LinkedHashMap<String, Integer> clientsMenu = null; // contents of
																// the client's
																// menu
	private List<String> selectedClients; // clients that the user has selected
	private String selectedClientsKey; // clients that the user has selected
	private List<Integer> clientsIDs; // id's back from the DB id???

	// /////
	// Flags:

	// TODO: delete when tested - move all these flags to local JS file.
	// private boolean showTargetURLs; // if true - client's target URLs are
	// visible as accordian stack /tree.
	// of the two fields below: only one can be true at a time: acts as a switch
	// showing relevant content of dashboard
	private boolean showClientsDashboard; // if true - client-focused version of
											// dashboard is visible
	private boolean showContactsDashboard; // if true - contacts-focused version
											// of dashboard is visible

	private boolean showTree; // may need to keep this one - as not too sure how
								// to deal with empty tree- it shows up
								// as an ugly bar on screen
	
	/////////////////////////
	//TODO: delete when done....
	private boolean testBoolean;// temp - just for testing ajax issue... 
	
	public boolean isTestBoolean() {
		return testBoolean;
	}

	public void setTestBoolean(boolean testBoolean) {
		this.testBoolean = testBoolean;
	}
	

	//
	//////////////////////

	// flags end
	// /////////////////////////////////////////////////////


	private Integer urlEntry;

	// ////
	// client's target and associated competitors' URLs:

//	private URLTreeBean urlTreeBean = new URLTreeBean();

	public FullScreenDashboardBean() {

		showTree = false;
		
		//////
		// testing:
		
		for (int i = 0; i < 5; i++){
			
			testList.add(i);
		}

		for(int a = 0; a < 5; a++){
			innerTestList.add(a);
			
		}
	
		
		//
		/////////

	}
	
	//////
	//TODO: DELETE!
	
//	private void addMandSToClientList(){
//		
//		List<URL> targetURLs = null;
//
//	
//	Integer clientFromDBID, clientIDFromUserSel;
//
//	// loop through the clients getting adding them to list if selected by
//	// user
//	for (int a = 0; a < clientsFromDB.size(); a++) {
//
//		for (int b = 0; b < clientsIDs.size(); b++) {
//
//			clientFromDBID = clientsFromDB.get(a).getId();
//			clientIDFromUserSel = clientsIDs.get(b);
//
//			if (clientFromDBID.equals(clientIDFromUserSel)) {
//
//				clientsSelectedByUser.add(clientsFromDB.get(a));
//
//
//			}
//		}
//
//	}
//
//	
//	//Create list to hold all selected clients and associated T URls:
//	List<ClientAndTUrls> clientAndURLsList = new ArrayList<ClientAndTUrls>();
//	
//		for(User selectedClient: clientsSelectedByUser){
//			
//			//create single new  ClientandURLs object and :
//			ClientAndTUrls clientAndURLs = new ClientAndTUrls();
//			
//			// 1. set user to the client selected;
//			clientAndURLs.setUser(selectedClient);
//			
//			// 2. get URLs: later move this to dynamically call it when user opens client tab..
//			targetURLs = urlDBService.getClientsTargetURLs(selectedClient.getId(), true);
//			
//			//3. add these, as unselected Target urls to the client_url object
//			
//			// need to create new URLAndBoolSelection object for each- can not cast... inefficient... replace..
//			
//			for(URL url: targetURLs){
//				URLAndBoolSelection urlAndBoolSel = new URLAndBoolSelection();
//				urlAndBoolSel.setUrl(url);
//				clientAndURLs.getUrls().add(urlAndBoolSel);
//			}
//			
//			clientAndURLsList.add(clientAndURLs);
//			
//		}
//		
//		// then add: to the bean in the accordion:
//		clientTargetURLsBean.setClientAndTUrlList(clientAndURLsList);
//	}
	
	//
	///////////

	// //////
	// action controllers:

	public void clientSelectionChange() {
		

		log.info("Entering FullScreenDashboard::clientSelectionChange()");
		
		List<Integer> clientsIDs = new ArrayList<Integer>();
		
		for (String clientID : selectedClients) {

			clientsIDs.add(Integer.valueOf(clientID));

		}
		
		List<URL> targetURLs = null;

		// TODO: might be a more optimised way than just clearing
		clientsSelectedByUser.clear();
			//urlTreeBean.clearTree();

		Integer clientFromDBID, clientIDFromUserSel;

		// loop through the clients getting adding them to list if selected by
		// user
		for (int a = 0; a < clientsFromDB.size(); a++) {

			for (int b = 0; b < clientsIDs.size(); b++) {

				clientFromDBID = clientsFromDB.get(a).getId();
				clientIDFromUserSel = clientsIDs.get(b);

				if (clientFromDBID.equals(clientIDFromUserSel)) {

					clientsSelectedByUser.add(clientsFromDB.get(a));
		

					// then need to get that client's target URLs / competitor

//					targetURLs = urlDBService.getClientsTargetURLs(
//							clientsSelectedByUser.get(0).getId(), true);

				}
			}

		}
//		if(null != targetURLs){
			//Integer targetURLId = targetURLs.get(0).getId();
			
			/*TODO:  NEw - 11-6-13:
			 * user selects client(s) from drop down menu...
			 * now have selected client id's in: 
			 * clientsSelectedByUser
			 * 
			 * check if users in clientsSelectedByUser are also in the clientTargetURLsBean.getClientAndTUrlList
			 * 
			 * but for now - just add the newly selected user..
			*/
		
		//Create list to hold all selected clients and associated T URls:
		List<ClientAndTUrls> clientAndURLsList = new ArrayList<ClientAndTUrls>();
		
			for(User selectedClient: clientsSelectedByUser){
				
				//create single new  ClientandURLs object and :
				ClientAndTUrls clientAndURLs = new ClientAndTUrls();
				
				// 1. set user to the client selected;
				clientAndURLs.setUser(selectedClient);
				
				// 2. get URLs: later move this to dynamically call it when user opens client tab..
				targetURLs = urlDBService.getClientsTargetURLs(selectedClient.getId(), true);
				
				//3. add these, as unselected Target urls to the client_url object
				
				// need to create new URLAndBoolSelection object for each- can not cast... inefficient... replace..
				
				for(URL url: targetURLs){
					URLAndBoolSelection urlAndBoolSel = new URLAndBoolSelection();
					urlAndBoolSel.setUrl(url);
					clientAndURLs.getUrls().add(urlAndBoolSel);
				}
				
				// now add this to the list of all clients selected:
				//TODO; need to add / remove any clients here...
				
				clientAndURLsList.add(clientAndURLs);
				
			}
			
			// then add: to the bean in the accordion:
			clientTargetURLsBean.setClientAndTUrlList(clientAndURLsList);
			
			//clientTargetURLsBean.getClientAndTUrlList()clientFromDBID..
			//( (LazyClientTURLsDataModel)clientTargetURLsBean.getDataModel()).setTargetURLId(targetURLId); 
			//((LazyBackLinksDataModel)clientTargetURLsBean.getDataModel()).setTargetURLId(targetURLId);
			//((LazyBackLinksDataModel)backLinkTableBean.getDataModel()).setTargetURLId(targetURLId);
//		}
		
		
//		((LazyClientTURLsDataModel) dataModel).setTargetURLId(targetURLId);
		//clientTargetURLsBean
//		((LazyBackLinksDataModel) dataModel).setTargetURLId(targetURLId);
//		clientTargetURLsBean.setTargetURLAddress("");
		//((LazyBackLinksDataModel)clientTargetURLsBean.getDataModel()).setTargetURLId(targetURLId);
		
		//////
		// OLD

//		List<Integer> clientsIDs = new ArrayList<Integer>();
//
//		URLDBService urlDBService = new URLDBService();
//
//		List<URL> targetURLs;
//
//		// ////
//		//
//
//		// nothing is selected in the drop down menu...
//		if (selectedClients.isEmpty()) {
//
//			if(null != urlTreeBean){
//				urlTreeBean.clearTree();
//				//urlTreeBean.getRoot().
//			}
//			
//			showTree = false;
//			return;
//		}
//
//		// run through all selected clients, getting their id's as integers
//
//		for (String clientID : selectedClients) {
//
//			clientsIDs.add(Integer.valueOf(clientID));
//
//		}
//
//		// TODO: might be a more optimised way than just clearing
//		clientsSelectedByUser.clear();
//			urlTreeBean.clearTree();
//
//		Integer clientFromDBID, clientIDFromUserSel;
//
//		// loop through the clients getting adding them to list if selected by
//		// user
//		for (int a = 0; a < clientsFromDB.size(); a++) {
//
//			for (int b = 0; b < clientsIDs.size(); b++) {
//
//				clientFromDBID = clientsFromDB.get(a).getId();
//				clientIDFromUserSel = clientsIDs.get(b);
//
//				if (clientFromDBID.equals(clientIDFromUserSel)) {
//
//					clientsSelectedByUser.add(clientsFromDB.get(a));
//
//					// and add to the tree:
//
//					// Move this to URLTreeBean constructor
//					// NEW:
//					//urlTreeBean = new URLTreeBean(clientsSelectedByUser);
//					urlTreeBean.clearTreeAndAddNewNodes(clientsSelectedByUser);
					
					// OLD
					// extended version of the tree impl
//					URLNodeImpl newNode = new URLNodeImpl(clientsFromDB.get(a)
//							.getDisplayed_name(), urlTreeBean.getRoot());
					
					//default:
//					DefaultTreeNode newNode = new DefaultTreeNode(clientsFromDB.get(a)
//							.getDisplayed_name(), urlTreeBean.getRoot());
					
					/////////////////
					// add ajax programmitcally too:
					// but really - do we want to add the ajax to teh TREE after the nodes are added..??
					
					// access tree from its id:
					
					/*
					 * ((DataTable)(FacesContext.getCurrentInstance().getViewRoot().findComponent(
"centerTabIds:dataId:tableViewFormId:tableViewId"))).setFirst(0);
					 */
					
//					<p:ajax event="expand" update=":leftContentForm:growl1" listener="#{fullScreenDashboardBean.onNodeExpand}" />
//			        <p:ajax event="collapse" update=":leftContentForm:growl1" listener="#{fullScreenDashboardBean.onNodeCollapse}" />
//			        <p:ajax event="select" update=":leftContentForm:growl1" listener="#{fullScreenDashboardBean.onNodeSelect}" />
//			        <p:ajax event="unselect" update=":leftContentForm:growl1" listener="#{fullScreenDashboardBean.onNodeUnselect}" />
					
//					FacesContext fc = FacesContext.getCurrentInstance();
//					Application application = fc.getApplication();
//					ExpressionFactory ef = fc.getApplication().getExpressionFactory();
//					
//					// v hacky - what if the front end ids change..???	
//					Tree tree = (Tree) fc.getViewRoot().findComponent("leftContentForm:tree1");
					
					/////
					// 1. select event class:
					
//					Class[] classArray = new Class[1];
//					classArray[0] =  BehaviorEvent.class;
					//classArray[1] = NodeExpandEvent.class;
					
//					MethodExpression meSelect = ef.createMethodExpression( fc.getELContext(), "#{fullScreenDashboardBean.onNodeSelect}", null,  new Class[]{NodeSelectEvent.class});
//					// or: MethodExpressionActionListener actionListener = new MethodExpressionActionListener(meSelect);
//					AjaxBehavior pajaxSelect = new AjaxBehavior();
//					pajaxSelect.setListener( meSelect );
//					pajaxSelect.setProcess("@this");
//					pajaxSelect.setUpdate( "@form" );
//					//newNode.addClientBehavior( "select", pajaxSelect );
//					//its the tree we want to give ajax to: - not the nodes..
//					tree.addClientBehavior( "select", pajaxSelect );
					//
					////////////
					
					////////
					// 2 expand event class:
					//classArray[1] = NodeExpandEvent.class;
					
//					MethodExpression meExpand = ef.createMethodExpression( fc.getELContext(), "#{fullScreenDashboardBean.onNodeExpand}", null,  new Class[]{NodeExpandEvent.class});
//					
//					AjaxBehavior pajaxExpand = new AjaxBehavior();
//					pajaxExpand.setListener( meExpand );
//					pajaxExpand.setProcess("@this");
//					pajaxExpand.setUpdate( "@form" );
//					//its the tree we want to give ajax to:
//					//newNode.addClientBehavior( "expand", pajaxExpand );
//					tree.addClientBehavior( "expand", pajaxExpand );
					
					
					
					//
					///////////
					

					// then need to get that client's target URLs / competitor
					// URLs and present as children under that client:
					// New:
					
					//OLD:
//					targetURLs = urlDBService.getClientsTargetURLs(
//							clientsFromDB.get(a).getId(), true);
//
//					for (int c = 0; c < targetURLs.size(); c++) {
//						
//						// extended version of the tree impl
//						new URLNodeImpl(targetURLs.get(c).getUrl_address(),
//								newNode);
						
						//trying to add ajax.... - but move this to p:tree, NOT p:node...
//						newChild.addClientBehavior( "select", pajaxSelect );
//						newChild.addClientBehavior( "expand", pajaxExpand );
//						//default:
//						new DefaultTreeNode(targetURLs.get(c).getUrl_address(),
//								newNode);
//
//					}
//				}
//			}
//
//		}
		

		showTree = true;
		
		// new version: just update the viewscoped bean, in the left content form, this will then access this
		//	bean's selectedClients array and create new tree accordingly...
//		RequestContext context = RequestContext.getCurrentInstance();  
//		context.update("leftContentForm");

		// testing email here...
		// EmailWrapper.SendEmail("Alan@propellernet.co.uk", "Alan Donohoe",
		// "robot@gofetchdata.appspotmail.com", "GoFetch Robot",
		// "If you get this message, then our robot in the cloud has learned how to email.",
		// "Hello From The GoFetch Robot");
		
		
	}

	public void switchToClientsDashboard() {
		// for now this is all handled by JS
		showClientsDashboard = true;
		showContactsDashboard = false;

	}

	public void switchToContactsDashboard() {
		// for now this is all handled by JS
		showClientsDashboard = false;
		showContactsDashboard = true;

	}
	
	@PostConstruct
	public void init() {

		// showTargetURLs = true;

		showClientsDashboard = true; // start with client-facing dashboard
										// first... - can later set this in
										// session object...
		showContactsDashboard = false;

		clientsMenu = new LinkedHashMap<String, Integer>();

		UserDBService usersDB = new UserDBService();

		clientsFromDB = usersDB.getAllClients();

		for (User user : clientsFromDB) {
			clientsMenu.put(user.getDisplayed_name(), user.getId());
		}

	}
	
	
	////////
	// temp listener stuff:
	
	public void onNodeExpand(NodeExpandEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "FULLSCREENDASHBOARD:: Expanded", event.getTreeNode().toString());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
  
    public void onNodeCollapse(NodeCollapseEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "FULLSCREENDASHBOARD:: Collapsed", event.getTreeNode().toString());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
    
    public void testAjaxSelectBtn(){
    	
    	log.info("Entering FullScreenDashboard::testAjaxSelectBtn()");
//
//    	 FacesContext context = FacesContext.getCurrentInstance();  
//         
//         context.addMessage(null, new FacesMessage("Successful", "testAjaxSelectBtn FIRED"));  
//    	
//    	updateTURLs();
    }
    
    public void onTabChange(TabChangeEvent tabChangeEvent){
    	log.info("Entering FullScreenDashboard::onTabChange()");
    	
    }
    

    
    public void onNodeSelect(javax.faces.event.AjaxBehaviorEvent event){
    	
    	log.info("Entering FullScreenDashboard::onNodeSelect()");
    	
    	// clear data model... may be way to cache this...
    	//backLinkTableBean.setDataModel(null);
    	
    	//updateTURLs();
    	
    	// go through all selected users....
//    	for(ClientAndTUrls clientsAndURLs: clientTargetURLsBean.getClientAndTUrlList()){
//    		
//    		// got through all the selected users' selected T URLs
//    		for(URLAndBoolSelection urlAndSelection: clientsAndURLs.getUrls()){
//    			
//    			//backLinkTableBean.getDataModel()
//    			
//    			if(urlAndSelection.isSelected()){
//    				
//    				// add to list if not already added:
//    				if(!selectedTargetURLIds.contains(urlAndSelection.getUrl().getId()))
//    					selectedTargetURLIds.add(urlAndSelection.getUrl().getId());
//    				
//    			}
//    			else{ // user has unselected this T URL - must be a more efficient way of doing this...
//    				if(selectedTargetURLIds.contains(urlAndSelection.getUrl().getId()))
//    					selectedTargetURLIds.remove(urlAndSelection.getUrl().getId());
//    			}
//    		}
//    		
//    	}
//    	
//    	// update the data model with the ID of the target URL...
//		backLinkTableBean.addBackLinksToTable(selectedTargetURLIds);
		
		log.info("selectedTargetURLIds = " + selectedTargetURLIds.toString());
		
    }
    
    //////
    // Testing:
    public String testButtonAction() {
    	log.info("testButtonAction invoked");
        return "";
    }
    public void testButtonActionListener(){
    	log.info("testButtonActionListener() invoked");
    }

    public void testButtonActionListener(AjaxBehaviorEvent event) throws AbortProcessingException{
    	log.info("testButtonActionListener(AjaxBehaviorEvent event) invoked");
    }
    
    public void updateTURLs(){
    	log.info("- - - - - -- -  -- - Entering updateTURLs()");
    }
    
    public String testInnerButtonAction() {
    	log.info("testInnerButtonAction invoked");
        return "";
    }
    public void testInnerButtonActionListener(){
    	log.info("testInnerButtonActionListener() invoked");
    }

    public void testInnerButtonActionListener(AjaxBehaviorEvent event) throws AbortProcessingException{
    	log.info("testInnerButtonActionListener(AjaxBehaviorEvent event) invoked");
    }
    
    public void updateInnerTURLs(){
    	log.info("- - - - - -- -  -- - Entering updateInnerTURLs()");
    }
    //
    /////////
    // but only event param signature - when working with... <f:ajax ..../>
   // public void processAjaxBehavior(javax.faces.event.AjaxBehaviorEvent event)
    public void updateTURLs(AjaxBehaviorEvent event) throws AbortProcessingException{
    	
    	log.info("- - - - - -- -  -- - Entering updateTURLs(AjaxBehaviorEvent event)");
    	
    	// go through all selected users....
    	for(ClientAndTUrls clientsAndURLs: clientTargetURLsBean.getClientAndTUrlList()){
    		
    		// got through all the selected users' selected T URLs
    		for(URLAndBoolSelection urlAndSelection: clientsAndURLs.getUrls()){
    			
    			//backLinkTableBean.getDataModel()
    			
    			if(urlAndSelection.isSelected()){
    				
    				// add to list if not already added:
    				if(!selectedTargetURLIds.contains(urlAndSelection.getUrl().getId()))
    					selectedTargetURLIds.add(urlAndSelection.getUrl().getId());
    				
    			}
    			else{ // user has unselected this T URL - must be a more efficient way of doing this...
    				if(selectedTargetURLIds.contains(urlAndSelection.getUrl().getId()))
    					selectedTargetURLIds.remove(urlAndSelection.getUrl().getId());
    			}
    		}
    	}
    	
    	// update the data model with the ID of the target URL...
		backLinkTableBean.addBackLinksToTable(selectedTargetURLIds);
    	
    }
  
//    public void onNodeSelect(NodeSelectEvent event) {  
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "FULLSCREENDASHBOARD:: Selected", event.getTreeNode().toString());  
//  
//        FacesContext.getCurrentInstance().addMessage(null, message);  
//        
////        String selectedNodeData = urlTreeBean.getSelectedNode().getData().toString();
////        
////        FacesMessage message2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "FULLSCREENDASHBOARD:: selectedNodeData = ", selectedNodeData);  
////        FacesContext.getCurrentInstance().addMessage(null, message2);  
//        
//        URLDBService urlDB = new URLDBService();
//        String urlAddress = event.getTreeNode().toString();
//     // Get backlink datatable data from DB.
//		Integer urlID = urlDB.getURLIDFromAddress(urlAddress);
//		
//		backLinkTableBean.addBackLinksToTable(urlID);
//        
//    }  
  
    public void onNodeUnselect(NodeUnselectEvent event) {  
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "FULLSCREENDASHBOARD:: Unselected", event.getTreeNode().toString());  
  
       // FacesContext.getCurrentInstance().addMessage(null, message);  
    	
    	log.info("Entering FullScreenDashboard::onNodeUnselect()");
        
        String urlAddress;
		Integer urlID;
		
		URLDBService urlDB = new URLDBService();
		
		urlAddress = event.getTreeNode().getData().toString();
		urlID = urlDB.getURLIDFromAddress(urlAddress);
		
		backLinkTableBean.removeBackLinksFromTable(urlID);
    }  
	
	
	
	//
	////////////
	
	/*
	 * We want to get all backlink data for the table and chart and all
	 * historical social data for table of currently selected
	 * node->target_url...
	 * 
	 *  - TIMEOUT HERE ON SERVER....
	 *  
	 *  issue - pulling back tonnes of row on the server at once - causes a time out..
	 *  
	 *  make one call to get the total no of links - have this presented somewhere - or used to fill the table with dummy data??, 
	 *  and then another call to limit the now of URLs and links to the size of the page.
	 *  
	 *  1 idea - only call backlink data here... then use jquery callback to call the social data once this is complete..???
	 *  
	 *  first case: user selects a node with no other nodes selected prev which has 100 backlinks... 
	 *  - get the count back of 100 and only 10 rows in the table are displayed - DONE.
	 *  
	 *  2nd case: node with children nodes...
	 */
//	public void onNodeSelect(NodeSelectEvent event) {	
//		
//		log.info("Entering FullScreenDashboard::onNodeSelect(...)");
//		
//		Integer urlID;
//		String urlAddress;
//
//		URLDBService urlDB = new URLDBService();
//		//LinkDBService linkDB = new LinkDBService();
//		MiscSocialDataDBService socialDB = new MiscSocialDataDBService();
//		
//		urlAddress = event.getTreeNode().getData().toString();
//		
//		log.info("FullScreenDashboard::onNodeSelect: urlAddress = " + urlAddress);
//		
//		// Get backlink datatable data from DB.
//		urlID = urlDB.getURLIDFromAddress(urlAddress);
//		
//		log.info("FullScreenDashboard::onNodeSelect: urlID = " + Integer.toString(urlID));
//		
//		// if there's no urlID - assume we are dealing with a client parent node
//		if(0 == urlID){
//			
//			if(event.getTreeNode().getChildCount() > 0){
//				//TODO: implement when node selected is parent node - need to get all urls associated with that parent URL's data
//				//	the method below works - but takes too long and produces timeouts..
//				//addChildrenToDataTable(event.getTreeNode(), urlDB);
//
//				
//			}else{ // just have a url with no children - but is a client node
//				//TODO: present summary of no of backlinks and cumulative social data???
//			}
//			
//		}else{ // we have a node thats an actual url - not just a client name...
//		
//			
//			// add it's data to the table and... all its children...
//			
//			backLinkTableBean.addBackLinksToTable(urlID);
//
//			//TODO: case: current node is  a target URL and has children nodes.... need to implement this in some ajax way....
////			if(event.getTreeNode().getChildCount() > 0){
////				addChildrenToDataTable(event.getTreeNode(), urlDB);
////			}
//			
//		}
//		
//		
//			
//		// all this below is if we just have 1 node selected - not a parent node...
//		
//
//		// BackLink Data -  this call - below - resets all data - dont really want to call this..
//		//backLinkTableBean.updateBackLinkData(urlID);
//
//		// Google backlink chart - at the moment just time vs - no of links...
//		// shall we do a count in mysql or - get all links, order by date....???
//		// and then count total links in each month?
//		// if some flag - could change the chart to show... 1: no of links vs
//		// time, 2: DA over time, 3. some other chart...
//		
//		/////////
//		// - all this below - should be moved to other functions and called separately....
//
//		//TODO: uncomment and implement somewhere else??
////		googleBackLinksChart.setBackLinkDataString(googleBackLinksChart
////				.parseBackLinkData(urlAddress,
////						backLinkTableBean.getBackLinkData()));
//
//		//TODO: move somewhere else - to speed up loading - define a function and call it via callback when the backlinks are first loaded into the table
//		// Social Data Chart
////		socialData = socialDB.getAllSocialData(urlID);
////
////		googleSocialChart.setSocialDataString(googleSocialChart
////				.parseSocialDataToString(socialData));
//
//		//
//		//////////////////////
//	}
	
	//TODO: delete after testing....
//	private void addChildrenToDataTable(TreeNode treeNode, URLDBService urlDB) {
//		
//		String urlAddress;
//		Integer urlID;
//		
//		List<TreeNode> children = treeNode.getChildren();
//		
//		for(int i =0; i < children.size(); i++ ){
//			
//			urlAddress = children.get(i).getData().toString();
//			urlID = urlDB.getURLIDFromAddress(urlAddress);
//			
//			backLinkTableBean.addBackLinksToTable(urlID); 
//		}
//		
//	}

	/* 
	 * this needs to remove the  backlinks that point to this target URL from the datatable...	
	 */
//	public void onNodeUnSelect(NodeUnselectEvent event){
//		
//		log.info("Entering FullScreenDashboard::onNodeUnSelect(...)");
//
//		
//		String urlAddress;
//		Integer urlID;
//		
//		URLDBService urlDB = new URLDBService();
//		
//		urlAddress = event.getTreeNode().getData().toString();
//		urlID = urlDB.getURLIDFromAddress(urlAddress);
//		
//		backLinkTableBean.removeBackLinksFromTable(urlID);
//	}
	//
	// ///////

	// action controllers - end
	// //////


	// ///
	// getters and setters:

	public Integer getUrlEntry() {
		return urlEntry;
	}

	public void setUrlEntry(Integer urlEntry) {
		this.urlEntry = urlEntry;
	}

	// public List<URL> getTargetURLs() {
	// return targetURLs;
	// }
	//
	// public void setTargetURLs(List<URL> targetURLs) {
	// this.targetURLs = targetURLs;
	// }

	// public boolean isShowTargetURLs() {
	// return showTargetURLs;
	// }
	// public void setShowTargetURLs(boolean showTargetURLs) {
	// this.showTargetURLs = showTargetURLs;
	// }

	public List<User> getClientsFromDB() {
		return clientsFromDB;
	}

	public void setClientsFromDB(List<User> clientsFromDB) {
		this.clientsFromDB = clientsFromDB;
	}

	public LinkedHashMap<String, Integer> getClientsMenu() {
		return clientsMenu;
	}

	public void setClientsMenu(LinkedHashMap<String, Integer> clientsMenu) {
		this.clientsMenu = clientsMenu;
	}

	public List<String> getSelectedClients() {
		return selectedClients;
	}

	public void setSelectedClients(List<String> selectedClients) {
		this.selectedClients = selectedClients;
	}

	public String getSelectedClientsKey() {
		return selectedClientsKey;
	}

	public void setSelectedClientsKey(String selectedClientsKey) {
		this.selectedClientsKey = selectedClientsKey;
	}

	public boolean isShowClientsDashboard() {
		return showClientsDashboard;
	}


	// TODO: this is all being handled in the client JS now.. can remove this..????
	public void setShowClientsDashboard(boolean showClientDashboard) {
		this.showClientsDashboard = showClientDashboard;

	}

	public boolean isShowContactsDashboard() {
		return showContactsDashboard;
	}

	public void setShowContactsDashboard(boolean showContactsDashboard) {
		this.showContactsDashboard = showContactsDashboard;

	}


	public boolean isShowTree() {
		return showTree;
	}

	public void setShowTree(boolean showTree) {
		this.showTree = showTree;
	}

	public GoogleChartsWrapper getGoogleSocialChart() {
		return googleSocialChart;
	}

	public void setGoogleSocialChart(GoogleChartsWrapper googleChart) {
		this.googleSocialChart = googleChart;
	}

	public LazyDataModel<URLAndLinkData> getUrlAndLinksLazyModel() {
		return urlAndLinksLazyModel;
	}

	// public void setUrlAndLinksLazyModel(LazyDataModel<URLAndLinkData>
	// urlAndLinksLazyModel) {
	// this.urlAndLinksLazyModel = urlAndLinksLazyModel;
	// }

	public URLAndLinkData getSelectedURLAndLinkData() {
		return selectedURLAndLinkData;
	}

	public void setSelectedURLAndLinkData(URLAndLinkData selectedURLAndLinkData) {
		this.selectedURLAndLinkData = selectedURLAndLinkData;
	}

	public BackLinkTableBean getBackLinkTableBean() {
		return backLinkTableBean;
	}

	public void setBackLinkTableBean(BackLinkTableBean backLinkTableBean) {
		this.backLinkTableBean = backLinkTableBean;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	// for single tree node selection mode:
	
	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public List<User> getClientsSelectedByUser() {
		return clientsSelectedByUser;
	}

	public void setClientsSelectedByUser(List<User> clientsSelectedByUser) {
		this.clientsSelectedByUser = clientsSelectedByUser;
	}

	public ClientTargetURLsBean getClientTargetURLsBean() {
		return clientTargetURLsBean;
	}

	public void setClientTargetURLsBean(ClientTargetURLsBean clientTargetURLsBean) {
		this.clientTargetURLsBean = clientTargetURLsBean;
	}


	


	// getters & setters end....
	// /////

	

}
