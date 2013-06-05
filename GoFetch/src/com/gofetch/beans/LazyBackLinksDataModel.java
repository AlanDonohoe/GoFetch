package com.gofetch.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//import org.mortbay.log.Log;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import com.gofetch.entities.Link;
import com.gofetch.entities.LinkBuildingActivity;
import com.gofetch.entities.LinkBuildingActivityDBService;
import com.gofetch.entities.LinkDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLAndLinkData;
import com.gofetch.entities.URLDBService;

/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real
 * datasource like a database.
 */
@SuppressWarnings("serial")
public class LazyBackLinksDataModel extends LazyDataModel<URLAndLinkData>
		implements SelectableDataModel<URLAndLinkData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LazyBackLinksDataModel.class.getName());


	// data back from the DB - on every call of load(...)
	private List<URLAndLinkData> datasource;
	private Integer targetURLId; 
	
	//Don't know if we need these now we are updated data directly from DB on each call...
	//private List<URLAndLinkData> subList = new ArrayList<URLAndLinkData>();
	//private List<URLAndLinkData> data = new ArrayList<URLAndLinkData>();
	
	// testing the new lazy loading pagination:
	//private Integer totalNoOfLinks; // this will be sum of all no of links by all selected target urls

	// DB comms
	private LinkDBService linkDB = new LinkDBService();
	private URLDBService urlDB = new URLDBService();
	private LinkBuildingActivityDBService linkBuildingDB = new LinkBuildingActivityDBService();

	// DB entities
	private List<URL> sourceURLs = new ArrayList<URL>();
	private List<Link> links = new ArrayList<Link>();
	// wraps both the above into one.
	private List<URLAndLinkData> backLinkData = new ArrayList<URLAndLinkData>();
	
	private List<LinkBuildingActivity> linkBuildingActivity = new ArrayList<LinkBuildingActivity>(); 

	// Datasource - the data model thats displayed in the datatable  - is now refreshed everytime from the DB, via load(...) call
	// so this is redundant?
//	public LazyBackLinksDataModel(List<URLAndLinkData> datasource,
//			Integer targetURLId) {
//		this.datasource = datasource;
//	}

	public LazyBackLinksDataModel(Integer targetURLId) {
		this.targetURLId = targetURLId;

		// Datasource - the data model thats displayed in the datatable  - is now refreshed everytime from the DB, via load(...) call
		// so this is redundant?
		// here is where we get info from the DB:
		// datasource = getBackLinkDataFromDB(targetURLId);
	}

//	public LazyBackLinksDataModel(List<URLAndLinkData> datasource) {
//		this.datasource = datasource;
//	}

	public LazyBackLinksDataModel() {
		this.datasource = new ArrayList<URLAndLinkData>();
		this.targetURLId= 0;
	}

	public List<URLAndLinkData> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<URLAndLinkData> datasource) {
		this.datasource = datasource;
	}

	@Override
	public URLAndLinkData getRowData(String rowKey) {
		for (URLAndLinkData backLink : datasource) {
			if (backLink.getLink().getLinks_id().equals(rowKey))
				return backLink;
		}

		return null;
	}

	@Override
	public Object getRowKey(URLAndLinkData backLink) {
		return backLink.getLink().getLinks_id();
	}

	@Override
	public List<URLAndLinkData> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {
		
		log.info("Entering LazyBackLinksDataModel::load(...). URL_id = " + Integer.toString(this.targetURLId));
		
		Integer rowCountInt;
		Long rowCountLong;
		
		
		// may not need these are we are dealing directly with DB on any call for new data - delete after testing...
		// data that acts as subset for the main dataset
		//data.clear();
		//subList.clear();
		
		// new: // can this be cached somehow??
		datasource.clear();
		
		// user has unselected all target URLs from target url tree
		if(0==this.targetURLId){
			this.setRowCount(0);
			return datasource;
		}
		
		rowCountLong = linkDB.noOfTimesURLIsTargetURL(this.targetURLId);
		rowCountInt = rowCountLong != null ? rowCountLong.intValue() : null;
		
		log.info("No of backlinks / rows in total table: " + Integer.toString(rowCountInt));	
		
		// set the no of times this url is a target as the no of rows in table...
		this.setRowCount(rowCountInt);
		
		// get the new data for the table...
		datasource.addAll(getBackLinkDataFromDB(this.targetURLId, first, pageSize));
		
		
		//TODO: implement filter  will need to call DB here...
//		for (URLAndLinkData backLink : datasource) {
//			boolean match = true;
//			
//			//TODO: hacky bugfix: need to work this out at some other point
////			if(null == backLink.getTidyURLAddress())
////				backLink.getTidyURLAddress();
//
//			for (Iterator<String> it = filters.keySet().iterator(); it
//					.hasNext();) {
//				try {
//					String filterProperty = it.next();
//					
//					////////////////
//					//temp fix: filterProperty for ex: = link.anchor_text needs to be anchor_text
//					//Integer dotIndex =  filterProperty.indexOf(".");
//					//filterProperty = filterProperty.substring(dotIndex++);
//					//
//					//////////////
//					
//					String filterValue = filters.get(filterProperty);
//					Field field = backLink.getClass().getDeclaredField(filterProperty); 
//					field.setAccessible(true);
//					String fieldValue = String.valueOf(field.get(backLink));
//					
////					String fieldValue = String.valueOf(backLink.getClass()
////							.getField(filterProperty).get(backLink));
//
//					if (filterValue == null
//							|| fieldValue.contains(filterValue)) {
//						match = true;
//					} else {
//						match = false;
//						break;
//					}
//				} catch (Exception e) {
//					//String msg = e.getMessage();
//					match = false;
//				}
//			}
//
//			if (match) {
//				//TODO: replace this with call to DB
//				//data.add(backLink);
//			}
//		}
//
//		// sort
//		if (sortField != null) {
//			 Collections.sort(data, new LazySorter(sortField, sortOrder));
//		}

		// rowCount - old code - irrellevant?? - but what about when we add more than one target to it..???
		//int dataSize = data.size();
		//TODO: may have to delete this... called above....
		//this.setRowCount(dataSize);

		// paginate // OLD code - pagination is done now via calling hte DB directly... // delete this code after testing...
//		if (dataSize > pageSize) {
//			try {
//				//BUG - 
//				// orig: return data.subList(first, first + pageSize);
//				// soln, see:
//				// http://journeymanjournal.blogspot.co.uk/2005/07/collections-returns-non.html
//				 subList.addAll( data.subList(first, first + pageSize));
//				 // testing
//				// subList.clear();
//				return subList;
//				//return data;
//				
//			} catch (IndexOutOfBoundsException e) {
//
//				subList.addAll(data.subList(first, first + (dataSize % pageSize)));
//				return subList;
//				//return data;
//				// orig: return data.subList(first, first + (dataSize %
//				// pageSize));
//			}
//		} else {
//			return data;
//		}
		
		//OLD:
		//return data;
		
		// new:
		return datasource;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return super.getRowCount();
	}

	public Integer getTargetURLId() {
		return targetURLId;
	}

	public void setTargetURLId(Integer targetURLId) {
		this.targetURLId = targetURLId;
	}

	public List<URL> getSourceURLs() {
		return sourceURLs;
	}

	public void setSourceURLs(List<URL> sourceURLs) {
		this.sourceURLs = sourceURLs;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<URLAndLinkData> getBackLinkData() {
		return backLinkData;
	}

	public void setBackLinkData(List<URLAndLinkData> backLinkData) {
		this.backLinkData = backLinkData;
	}

	public void addBackLinksToData(List<URLAndLinkData> newBackLinks) {

		datasource.addAll(newBackLinks);
	}
	
	public void addBackLinksToData(Integer urlID){
		// actually set the datasource here - using the count of total links pointing to the target URL in links table
		// and then retreiving just the rows that will be displayed in the page.
		// so limit the rows to to page no - no matter what  - then subsequent calls 
		// see: http://stackoverflow.com/questions/13972193/how-to-query-data-for-primefaces-datatable-with-lazy-loading-and-pagination
	
		
		//super.setRowCount();; // called in load...
	}

	public void removeBackLinksFromTable(Integer urlID) {

		// removing objects from arraylist: from:
		// http://stackoverflow.com/questions/5145135/java-util-concurrentmodificationexception-on-arraylist

		Iterator<URLAndLinkData> it = datasource.iterator();

		while (it.hasNext()) {

			URLAndLinkData urlAndLinkData = it.next();

			// check target ID
			if (urlID.equals(urlAndLinkData.getTargetURLId())) {
				it.remove();
			}
		}
		
		// remove the target URL id from the (in the future) list of target URL ids we are presenting in the table... 
		// for now - just clear it 
		this.targetURLId = 0;
	}
	
	public List<URLAndLinkData> getBackLinkDataFromDB(int targetId, int limitStart, Integer limitEnd) {

		// test this - might need to order by - to make sure we're getting the links that correspond to urls we're pulling back....
		sourceURLs = urlDB.getBackLinkURLs(targetId,limitStart, limitEnd );
		links = linkDB.getAllLinks(targetId, limitStart, limitEnd);
		
		// testing
		
		linkBuildingActivity =  linkBuildingDB.getAllLinkActivities();

		// now put them in the urlANDLink wrapper...... 
		//TODO: THIS IS NOT PUTTING THE CORRECT URLS AND LINKS TOGETHER - REQUIRES FURTHER TESTING...
		for (int i = 0; i < sourceURLs.size(); i++) {

			URLAndLinkData urlAndLink = new URLAndLinkData();

			urlAndLink.setUrl(sourceURLs.get(i));
			urlAndLink.setLink(links.get(i));
			
			urlAndLink.setTargetURLId(targetId);

			backLinkData.add(urlAndLink);

		}

		return backLinkData;

	}

}
