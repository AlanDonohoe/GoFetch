package com.gofetch.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;

import com.gofetch.entities.URLAndLinkData;

/**
 * Manages the data for the datatable holding backlinks 
 * @author alandonohoe
 *
 */
@ManagedBean
@SessionScoped
public class BackLinkTableBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BackLinkTableBean.class
			.getName());

	private LazyDataModel<URLAndLinkData> dataModel;

	private URLAndLinkData selectedBackLink;

	private URLAndLinkData[] selectedURLAndLinkDataArray;

	private String earliestLinkDate;
	private String latestLinkDate;

	public BackLinkTableBean() {

		dataModel = new LazyBackLinksDataModel();
		
		//TODO: temp hack until we get the tree -> datatable working...
		earliestLinkDate = "February 2013";
		latestLinkDate = "April 2013";
		

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

	public void onRowSelect(SelectEvent event) {
		selectedBackLink = (URLAndLinkData) event.getObject();
	}

	public URLAndLinkData[] getSelectedURLAndLinkDataArray() {
		return selectedURLAndLinkDataArray;
	}

	public void setSelectedURLAndLinkDataArray(
			URLAndLinkData[] selectedURLAndLinkDataArray) {
		this.selectedURLAndLinkDataArray = selectedURLAndLinkDataArray;
	}

	public void removeBackLinksFromTable(Integer urlID) {

		((LazyBackLinksDataModel) dataModel).removeBackLinksFromTable(urlID);

	}

	public void checkBoxSelected(SelectEvent se) {
//		int i = 0;
//		i++;
	}

	public void checkBoxUnselected(UnselectEvent se) {
//		int i = 0;
//		i++;
	}
	
	public String getEarliestLinkDate() {
		
		if(earliestLinkDate.isEmpty() && null != selectedURLAndLinkDataArray){
			extractDates();
		}

		return earliestLinkDate;
	}
	
	public String getLatestLinkDate() {
		
		if(latestLinkDate.isEmpty() && null != selectedURLAndLinkDataArray){
			extractDates();
		}
		
		return latestLinkDate;
	}

	public void setEarliestLinkDate(String earliestLinkDate) {
		this.earliestLinkDate = earliestLinkDate;
	}

	public void setLatestLinkDate(String latestLinkDate) {
		this.latestLinkDate = latestLinkDate;
	}

	/**
	 * Pulls earliest and latest links' dates from the selected array of url and link data
	 */
	private void extractDates() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		int noOfLinks = selectedURLAndLinkDataArray.length;
		Date[] dates = new Date[noOfLinks];
		String earliestDate, latestDate;
		
		for(int i = 0; i < noOfLinks; i++){
			
			dates[i]=selectedURLAndLinkDataArray[0].getLink().getDate_detected();
			
		}
		
		Arrays.sort(dates);
		
		earliestDate = dates[0].toString();
		latestDate= dates[--noOfLinks].toString(); 
		
	}

	public static void setLog(Logger log) {
		BackLinkTableBean.log = log;
	}


	public void addBackLinksToTable(List<Integer> selectedTargetURLIds) {
		
		log.info("BacklinkTabelBean::addBackLinksToTable(). Size: " + selectedTargetURLIds.size());
		
		((LazyBackLinksDataModel) dataModel).setTargetURLIds(selectedTargetURLIds);
		
	}
	
}
