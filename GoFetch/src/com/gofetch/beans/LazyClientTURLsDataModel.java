package com.gofetch.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;

public class LazyClientTURLsDataModel extends LazyDataModel<URL> implements
		SelectableDataModel<URL> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LazyClientTURLsDataModel.class
			.getName());

	private List<URL> datasource;
	private Integer clientID;

	// DB Comms:
	private URLDBService urlDB = new URLDBService();

	// DB entities:
	//private List<URL> tURLs = new ArrayList<URL>();

	public LazyClientTURLsDataModel() {

		this.datasource = new ArrayList<URL>();
		this.clientID = 0;

	}

	public LazyClientTURLsDataModel(Integer clientID) {
		this.datasource = new ArrayList<URL>();
		this.clientID = clientID;
	}
	
//	@Override
//	public List<URL> load(int first, int pageSize, String sortField,
//			SortOrder sortOrder, Map<String, String> filters) {
//		// TODO Auto-generated method stub
//		return super.load(first, pageSize, sortField, sortOrder, filters);
//	}

	@Override
	public List<URL> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {

		log.info("Entering LazyBackLinksDataModel::load(...). clientID = "
				+ Integer.toString(this.clientID));

		Integer rowCountInt;
		// Long rowCountLong;

		// may not need these are we are dealing directly with DB on any call
		// for new data - delete after testing...
		// data that acts as subset for the main dataset
		// data.clear();
		// subList.clear();

		// new: // can this be cached somehow??
		datasource.clear();

		// user has unselected all target URLs from target url tree
		if (0 == this.clientID) {
			this.setRowCount(0);
			return datasource;
		}

		// get the new data for the table...
		datasource.addAll(getBackLinkDataFromDB(this.clientID, first,
				pageSize));

		// NEw:
		// JUst use size of list....
		// rowCountInt = urlDB.noOfClientURLs(this.targetURLId);
		// urlDB.get
		// OLD:
		// rowCountLong = linkDB.noOfTimesURLIsTargetURL(this.targetURLId);
		// rowCountInt = rowCountLong != null ? rowCountLong.intValue() : null;
		// set the no of times this url is a target as the no of rows in
		// table...
		// this.setRowCount(rowCountInt);

		this.setRowCount(datasource.size());

		return datasource;
	}

	private List<URL> getBackLinkDataFromDB(Integer clientID, int first,
			int pageSize) {

		// TODO: implement any offseting later as req'd: using first and
		// pageSize

		return (urlDB.getClientsTargetURLs(clientID, true));
	}
	
	@Override
	public URL getRowData(String rowKey) {
		for (URL url : datasource) {
			if (url.getId().equals(rowKey))
				return url;
		}

		return null;
	}

	@Override
	public Object getRowKey(URL url) {
		return url.getId();
	}

	public Integer getTargetURLId() {
		return clientID;
	}

	public void setTargetURLId(Integer targetURLId) {
		this.clientID = targetURLId;
	}

}
