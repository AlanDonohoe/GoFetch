package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.TabChangeEvent;

@ManagedBean(name = "clientTargetURLsBean")
@SessionScoped
public class ClientTargetURLsBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	  //////
    // Just for testing - delete when done...
    
//    private List<Integer> testList = new ArrayList<Integer>();
//    
//    private List<Integer> innerTestList = new ArrayList<Integer>();
//    
//	public List<Integer> getTestList() {
//		
//		
//		
//		return testList;
//	}
//
//	public void setTestList(List<Integer> testList) {
//		
//		
//		this.testList = testList;
//	}
//	
//
//
//	public List<Integer> getInnerTestList() {
//		
//
//		
//		return innerTestList;
//	}
//
//	public void setInnerTestList(List<Integer> innerTestList) {
//		this.innerTestList = innerTestList;
//	}
	// testing - end
	//////////
	
	//new implementation of data structure suited to accordian menu	
	List <ClientAndTUrls> clientAndTUrlList;	
  
    public ClientTargetURLsBean(){
       	clientAndTUrlList = new ArrayList<ClientAndTUrls>();
       	
    //////
    		// testing:
    		
//    		for (int i = 0; i < 5; i++){
//    			
//    			testList.add(i);
//    		}
//
//    		for(int a = 0; a < 5; a++){
//    			innerTestList.add(a);
//    			
//    		}
    	
    		
    		//
    		/////////
    }

	public List<ClientAndTUrls> getClientAndTUrlList() {
		return clientAndTUrlList;
	}


	public void setClientAndTUrlList(List<ClientAndTUrls> clientAndTUrlList) {
		this.clientAndTUrlList = clientAndTUrlList;
	}
	
	public void onTabChange(TabChangeEvent event ){
		
//		int i = 0;
//		i++;
		
	}
    
}
