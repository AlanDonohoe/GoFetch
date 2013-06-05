package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;

import com.gofetch.entities.Link;
import com.gofetch.entities.LinkDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLAndLinkData;
import com.gofetch.entities.URLDBService;

@ManagedBean
@SessionScoped
public class BackLinkTableBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BackLinkTableBean.class.getName());

	private LazyDataModel<URLAndLinkData> dataModel;

	private URLAndLinkData selectedBackLink;
	
	//private List<URLAndLinkData> selectedURLAndLinkData = new ArrayList<URLAndLinkData>();

	private URLAndLinkData[] selectedURLAndLinkDataArray;

	//private Integer urlId; // the id of the target url selected by the user.

	////////////
	// moved all the db stuff to the datamodel.... delete when tested...
	// DB Comms
//	private LinkDBService linkDB = new LinkDBService();
//	private URLDBService urlDB = new URLDBService();
	
	// DB entities
//	private List<URL> sourceURLs = new ArrayList<URL>();
//	private List<Link> links = new ArrayList<Link>();
	//private List<URLAndLinkData> backLinkData = new ArrayList<URLAndLinkData>();

	public BackLinkTableBean() {
		
		dataModel =  new LazyBackLinksDataModel();

	}

	// now in the data model - will only access DB via that - set datamodel target url id, delete when tested...
//	public void updateBackLinkData(Integer targetURLId) {
//
//		//backLinkData = getBackLinkDataFromDB(targetURLId);
//		//dataModel = new LazyBackLinksDataModel(backLinkData);
//
//	}
	
	public void addBackLinksToTable(Integer targetURLId) {
		
		log.info("Entering BacklinkTableBean::addBackLinksToTable(...) targetURLId = " + Integer.toString(targetURLId));
		
		//backLinkData.clear();
		// move to default constrcutor
//		if(null == dataModel)
//			dataModel =  new LazyBackLinksDataModel();
		
		// old:
		//backLinkData = getBackLinkDataFromDB(targetURLId);
		//((LazyBackLinksDataModel)dataModel).addBackLinksToData(backLinkData);
		
		// new - now when load gets called - we just make lazy load calls to DB there... based on page size, etc...
		((LazyBackLinksDataModel)dataModel).setTargetURLId(targetURLId);
	}


	public URLAndLinkData getSelectedBackLink() {
		return selectedBackLink;
	}

	public void setSelectedBackLink(URLAndLinkData selectedBackLink) {
		this.selectedBackLink = selectedBackLink;
	}

	public LazyDataModel<URLAndLinkData> getDataModel() {
		return dataModel;
	}

	public void setDataModel(LazyDataModel<URLAndLinkData> dataModel) {
		this.dataModel = dataModel;
	}

//	public Integer getUrlId() {
//		return urlId;
//	}
//
//	public void setUrlId(Integer urlId) {
//		this.urlId = urlId;
//	}

	// move this to the datamodel....???
//	public List<URLAndLinkData> getBackLinkDataFromDB(Integer targetURLId) {
//
//		sourceURLs = urlDB.getBackLinkURLs(targetURLId);
//		links = linkDB.getAllLinks(targetURLId);
//
//		for (int i = 0; i < sourceURLs.size(); i++) {
//
//			URLAndLinkData urlAndLink = new URLAndLinkData();
//
//			urlAndLink.setUrl(sourceURLs.get(i));
//			urlAndLink.setLink(links.get(i));
//			
//			urlAndLink.setTargetURLId(targetURLId);
//
//			backLinkData.add(urlAndLink);
//
//		}
//
//		return backLinkData;
//
//	}

//	public List<URLAndLinkData> getBackLinkData() {
//		return backLinkData;
//	}
//
//	public void setBackLinkData(List<URLAndLinkData> backLinkData) {
//		this.backLinkData = backLinkData;
//	}	

	public void onRowSelect(SelectEvent event){
		selectedBackLink = (URLAndLinkData) event.getObject();
	}

//	public List<URLAndLinkData> getSelectedURLAndLinkData() {
//		return selectedURLAndLinkData;
//	}
//
//	public void setSelectedURLAndLinkData(
//			List<URLAndLinkData> selectedURLAndLinkData) {
//		this.selectedURLAndLinkData = selectedURLAndLinkData;
//	}

//	public List<URL> getSourceURLs() {
//		return sourceURLs;
//	}
//
//	public void setSourceURLs(List<URL> sourceURLs) {
//		this.sourceURLs = sourceURLs;
//	}
//
//	public List<Link> getLinks() {
//		return links;
//	}
//
//	public void setLinks(List<Link> links) {
//		this.links = links;
//	}

	public URLAndLinkData[] getSelectedURLAndLinkDataArray() {
		return selectedURLAndLinkDataArray;
	}

	public void setSelectedURLAndLinkDataArray(
			URLAndLinkData[] selectedURLAndLinkDataArray) {
		this.selectedURLAndLinkDataArray = selectedURLAndLinkDataArray;
	}
	
	public void removeBackLinksFromTable(Integer urlID) {
		
		((LazyBackLinksDataModel)dataModel).removeBackLinksFromTable(urlID);
		
	}
	
	public void checkBoxSelected(SelectEvent se){
		
		int i =0;
		i++;
		
	}
	
public void checkBoxUnselected(UnselectEvent se){
		
		int i =0;
		i++;
		
	}
	
}
