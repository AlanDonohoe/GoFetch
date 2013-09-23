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
	
	//new implementation of data structure suited to accordian menu	
	List <ClientAndTUrls> clientAndTUrlList;	
  
    public ClientTargetURLsBean(){
       	clientAndTUrlList = new ArrayList<ClientAndTUrls>();
       	
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
