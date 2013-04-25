package com.gofetch.fileio;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.FileUploadEvent;

import com.gofetch.ExcelConstants;
import com.gofetch.beans.ManualLink;
import com.gofetch.entities.ClientCategory;
import com.gofetch.entities.ClientCategoryDBService;
import com.gofetch.entities.User;
import com.gofetch.entities.UserDBService;
import com.gofetch.utils.TextUtil;

public class ExcelWrapper {

	private static Logger log = Logger.getLogger(ExcelWrapper.class.getName());

	private class LinksHeaderColumns {

		private int backLinkCol;
		private int targetURLCol;
		private int anchorTextCol;
		private int dateCol;
		private int linkActivityCol;

		public int getBackLinkCol() {
			return backLinkCol;
		}

		public void setBackLinkCol(int backLinkCol) {
			this.backLinkCol = backLinkCol;
		}

		public int getTargetURLCol() {
			return targetURLCol;
		}

		public void setTargetURLCol(int targetURLCol) {
			this.targetURLCol = targetURLCol;
		}

		public int getAnchorTextCol() {
			return anchorTextCol;
		}

		public void setAnchorTextCol(int anchorTextCol) {
			this.anchorTextCol = anchorTextCol;
		}

		public int getDateCol() {
			return dateCol;
		}

		public void setDateCol(int dateCol) {
			this.dateCol = dateCol;
		}

		public int getLinkActivityCol() {
			return linkActivityCol;
		}

		public void setLinkActivityCol(int linkActivityCol) {
			this.linkActivityCol = linkActivityCol;
		}

	}

	private LinksHeaderColumns linksHeaderCols = new LinksHeaderColumns();

	/*
	 * Method parses the excel workbook within the "event" parameter, extracting
	 * "objectType" of objects, and returns them in a List.
	 * uses the "linkActivity" linkedHashMap to check that link building activity is valid and then assign key value
	 * to each link.linkactivitykey
	 */
	public List<ManualLink> SheetToLinksArray(FileUploadEvent event, LinkedHashMap<String, Integer> linkActivityList, LinkedHashMap<String, Integer> campaignsList)
			throws Exception {

		// see: http://poi.apache.org/spreadsheet/quick-guide.html
		// http://www.avajava.com/tutorials/lessons/how-do-i-read-from-an-excel-file-using-poi.html?page=2
		// http://www.java-tutorial.ch/java-server-faces/file-upload-with-primefaces
		// https://developers.google.com/appengine/kb/java

		InputStream inputStream;
		String workBookFileName, clientNameFromWB, clientDisplayedName = "", selectedClientName = "";
		int index, noOFSheets, clientID = 0;
		HSSFWorkbook workBook;

		FacesMessage msgFaces = new FacesMessage();

		UserDBService userDB = new UserDBService();

		List<User> clientsDB = userDB.getAllClients();

		List<ManualLink> linksInWorkBook = new ArrayList<ManualLink>();
		List<ManualLink> linksInSheet;
		
		List<ClientCategory> clientsCategories = null;
		
		
		
		// /////////////////////////
		// 1. get the title of the work book and search for similar clients in
		// DB - set this as the client.
		// - give feedback to user to check that its the correct client.

		workBookFileName = event.getFile().getFileName();

		if (workBookFileName.isEmpty()) {
			Exception e = new Exception("Can not determine workbook name");
			throw (e);
		}
		
		//TODO: implement xlsx parsing - until then throw exception
		
		if(workBookFileName.endsWith("xlsx")){
			Exception e = new Exception("Can not currently work with xlxs format.");
			throw (e);
		}
		//
		// ////////////////////

		// //////////////////////////////////
		// use the substring of the first x chars to search for clients
		clientNameFromWB = workBookFileName.substring(0, ExcelConstants.noOfDigitsToCheckClientName).toLowerCase();
		//and remove whitespace, so we're just comparing the lowercase text
		clientNameFromWB = clientNameFromWB.replaceAll("\\s","");
		
		// look through database for similar named client:
		for (User user : clientsDB) {

			clientDisplayedName = user.getDisplayed_name();
			clientDisplayedName = clientDisplayedName.toLowerCase();
			clientDisplayedName = clientDisplayedName.replaceAll("\\s","");
			
			if (clientDisplayedName
					.startsWith(clientNameFromWB)) {
				selectedClientName = user.getDisplayed_name();
				clientID = user.getId();
			}
		}

		// if there's no match - pass exception up the chain:

		if (0 == clientID) {
			Exception e = new Exception(
					"Can not find client in DB to associate with: "
							+ workBookFileName
							+ ". Make sure client name and file name share the same first few letters.");
			throw (e);
		}
		//
		// ////////////////////////
		
		
		// get campaigns:
		
		if(campaignsList.isEmpty()){
			ClientCategoryDBService clientCategoryDBSevice = new ClientCategoryDBService();
			clientsCategories = clientCategoryDBSevice.getClientsCategories(clientID);
			
			//clientsCategories.isEmpty();
			
			//campaignsList = clientsCategories; // dont work - mistype...
			
			
			for (ClientCategory category : clientsCategories) {

				campaignsList.put(category.getCategory(), category.getId());
				 
			}
		}
			 	

		// ////////////////////////
		// get the data out of the workbook

		try {
			inputStream = event.getFile().getInputstream();

			workBook = new HSSFWorkbook(inputStream);

		} catch (Exception e) {

			String msg = "Exception in ExcelWrapper: SheetToLinksArray. ";

			log.warning(msg + e.getMessage());

			throw (e);
		}

		//
		// ///////////////////////////

		// now we have data - proceed with the processing...

		// 2. for each sheet in the book, run through and parse sheet's data.

		noOFSheets = workBook.getNumberOfSheets();

		boolean atEndOfWB = false;

		//while (!atEndOfWB) {
		for (int i = 0; i < noOFSheets && (!atEndOfWB); i++) {

			HSSFSheet workSheet = workBook.getSheetAt(i);
			String sheetTitle = "";

			// check that sheet is not empty. - at least the 1st 10 rows...
			if (IsSheetEmpty(workSheet, 10)) {

				if (0 == i) {// first sheet is empty

					Exception e = new Exception(workBookFileName
							+ " is empty.");
					throw (e);
				}

				// else we're just at the end of the workbook
				atEndOfWB = true;
			} else {

				// //////
				// there's some data in the current sheet, find the headers
				// we are interested in.

				// do this in the first sheet only:
				if (0 == i) {
					// find the backlink column, in the first 2 rows
					try {

						linksHeaderCols
						.setBackLinkCol(getColIndexWithHeaderText(
								workSheet,
								ExcelConstants.backLinkHeaderText,
								1));
						linksHeaderCols
						.setTargetURLCol(getColIndexWithHeaderText(
								workSheet,
								ExcelConstants.targetURLHeaderText,
								1));
						linksHeaderCols
						.setAnchorTextCol(getColIndexWithHeaderText(
								workSheet,
								ExcelConstants.anchorTextHeaderText,
								1));
						linksHeaderCols
						.setDateCol(getColIndexWithHeaderText(
								workSheet,
								ExcelConstants.dateHeaderText, 1));
						linksHeaderCols
						.setLinkActivityCol(getColIndexWithHeaderText(
								workSheet,
								ExcelConstants.linkActivityHeaderText,
								1));

					} catch (Exception e) {
						// have not found the nec headers...
						throw (e);
					}
				} // end of if only on 1st sheet

				// ///
				// now we have the index of each column we are interested
				// in.

				// match the sheet title with a campaign in the DB assoc
				// with this client.
				sheetTitle = workSheet.getSheetName();

				String campaign = matchCategoryToSheetTitle(sheetTitle,
						campaignsList, clientID);
				//////////
				//Case: where client has no campaigns, and sheet has not been named, ie: it retains default name: 'sheet1,2,3,etc'
				if (campaign.isEmpty() && (sheetTitle.toLowerCase().startsWith("sheet")) && (1 == campaignsList.size())) {
					
					campaign = campaignsList.entrySet().iterator().next().getKey(); // this will be the only category, which will be the default category
				}
				
				//
				/////////////
				if (campaign.isEmpty()) {
					Exception e = new Exception(
							"Could not match worksheet: " + sheetTitle
							+ " to a " + selectedClientName
							+ " campaign."); 
					throw (e);

				}
				
				Integer campaignID = campaignsList.get(campaign);

				// ////////////////
				// now we have the campaign for this sheet, the client - run
				// through all the rows getting link data for each....

				linksInSheet = getAllLinksFromSheet(workSheet, campaign, campaignID, linkActivityList, selectedClientName);

				linksInWorkBook.addAll(linksInSheet);
			}

		}
		

		return linksInWorkBook;

	}

	private List<ManualLink> getAllLinksFromSheet(HSSFSheet workSheet,
			String campaign, Integer campaignID, LinkedHashMap<String, Integer> linkActivityList, String clientName) throws Exception {

		List<ManualLink> links = new ArrayList<ManualLink>();
		String dateString, sourceURL, targetURL, cellLinkActivity, cellValue = "";
		Date date;
		int noOfRows = workSheet.getLastRowNum();
		int i = 1;

		// need to make sure that linksHeaderCols is not empty

		if (0 == linksHeaderCols.getAnchorTextCol()
				+ linksHeaderCols.getBackLinkCol())
			return links; // return empty links... //TODO: throw exception??

		// starting at 2nd row (int i = 1)- allowing for the header row, get each of the
		// metrics for each individual link - until we hit an empty row - then
		// exit

		try{
			for (; i < noOfRows; i++) {

				ManualLink currentLink = new ManualLink();
				
				currentLink.setClient(clientName);

				//check source url - HAS to be present for valid link - if not, then consider this the end of the links in this sheet
				// if its empty - then exception is thrown....
				try{
					sourceURL = workSheet.getRow(i).getCell(linksHeaderCols.getBackLinkCol()).getStringCellValue();
				}catch(Exception e){
					sourceURL = "";
				}

				if (!sourceURL.isEmpty()){
					
					//clear white space from begining and end or url - trim doesnt seem to work....
					//sourceURL = sourceURL.trim();
					
					sourceURL = TextUtil.removeWSFromStartAndEnd(sourceURL);
					
					// make sure url is not missing http:// or https://
					if(!sourceURL.startsWith("http"))
						sourceURL = TextUtil.addHTTPToURL(sourceURL);

					currentLink.setSourceUrl(sourceURL);

					/////////////
					//anchor text - can sometimes be numerical data....

					switch(workSheet.getRow(i)
							.getCell(linksHeaderCols.getAnchorTextCol())
							.getCellType()){
							case Cell.CELL_TYPE_NUMERIC:

								cellValue = Double.toString(workSheet.getRow(i)
										.getCell(linksHeaderCols.getAnchorTextCol())
										.getNumericCellValue());
								break;

							case Cell.CELL_TYPE_STRING:

								cellValue = workSheet.getRow(i)
								.getCell(linksHeaderCols.getAnchorTextCol())
								.getStringCellValue();
								break;

					}
					
					// remove WS from begining and end of anchor text:
					
					cellValue = TextUtil.removeWSFromStartAndEnd(cellValue);
					
					currentLink.setAnchorText(cellValue);

					//
					///////////////////
					
					cellLinkActivity = workSheet.getRow(i)
							.getCell(linksHeaderCols.getLinkActivityCol())
							.getStringCellValue();
					
					// validate - that the activity from cell is in the allowed list from DB
					if(linkActivityList.containsKey(cellLinkActivity)){
						
						currentLink.setLinkActivity(cellLinkActivity);
						
						currentLink.setLinkActivityKey(linkActivityList.get(cellLinkActivity));
					}else{
						throw(new Exception(cellLinkActivity + "  - not in Link Building Activity List"));
					}
	
					/////////////////////////
					//NEW Code - 4-2-13:
					targetURL = workSheet.getRow(i)
							.getCell(linksHeaderCols.getTargetURLCol())
							.getStringCellValue();
					
					//clear white space from begining and end or url - Issue - trim doesnt seem to clear the WS
					//targetURL = targetURL.trim();
					
					targetURL = TextUtil.removeWSFromStartAndEnd(targetURL);
					
					// make sure url is not missing http:// or https://
					if(!targetURL.startsWith("http"))
						targetURL = TextUtil.addHTTPToURL(targetURL);
					
					currentLink.setTargetUrl(targetURL);
					//
					/////////////////////////
					
					/////////////////////////
//					OLD CODE:
//					currentLink.setTargetUrl(workSheet.getRow(i)
//							.getCell(linksHeaderCols.getTargetURLCol())
//							.getStringCellValue());
					/////////////////////////

					date = workSheet.getRow(i).getCell(linksHeaderCols.getDateCol()).getDateCellValue();

					currentLink.setDate(date);
					
					currentLink.setCampaign(campaign);
					
					currentLink.setCampaignID(campaignID);
					
					currentLink.setSocialData(true);

					links.add(currentLink);
				}
				else
					return links;

			}

			return links;
			
		}catch(Exception e){
			
			Exception ex = null;
			
			String amendedMsg = e.getMessage();
			
			Integer problemRow = ++i;
			
			amendedMsg += ". Check Row: " + problemRow + " in sheet: " + workSheet.getSheetName();
			
			ex = new Exception(amendedMsg);
			
			throw (ex);
		}

	}

	private String matchCategoryToSheetTitle(String sheetTitle, LinkedHashMap<String, Integer> campaignsList, Integer clientID) {
		
		String sheetTitleLowerCase = sheetTitle.toLowerCase();
		//new..... 
		if(!campaignsList.isEmpty()){
			if(campaignsList.containsKey(sheetTitleLowerCase))
				return sheetTitleLowerCase;
			if(campaignsList.containsKey(sheetTitle))
				return sheetTitle;
			else
				return "";
		}
		

		//old method.............		 case insensitive - but made another hit to the DB...
		ClientCategoryDBService clientCategoryDBSevice = new ClientCategoryDBService();
		//
		// old: List<ClientCategory> clientsCategories = clientCategoryDBSevice
		//		.getClientsCategories(campaignsList);
		
		// new:
		List<ClientCategory> clientsCategories = clientCategoryDBSevice.getClientsCategories(clientID); 
		
		for (ClientCategory category : clientsCategories) {

			if (0 == category.getCategory().compareToIgnoreCase(
					sheetTitleLowerCase)){
							
				return category.getCategory();
			}
		}
		
		return "";

	}

	private boolean IsSheetEmpty(HSSFSheet workSheet, int noOfRowsToCheck) {

		int counter = 0;
		String cellContents = "";

		// if no of rows to check exceeds the no of rows in the worksheet or
		// user passed 0 - check whole sheet is empty.
		if (noOfRowsToCheck > workSheet.getPhysicalNumberOfRows()
				|| (0 == noOfRowsToCheck))
			noOfRowsToCheck = workSheet.getPhysicalNumberOfRows();

		// if no of rows = 0, then sheet is empty...
		if(0 == noOfRowsToCheck)
			return true;

		for (Row myrow : workSheet) {
			for (Cell mycell : myrow) {
				cellContents = mycell.getStringCellValue();
				// if we have any data that is not empty return true
				if (!cellContents.isEmpty())
					return false;
			}
			counter++;
			if (counter >= noOfRowsToCheck) {

				if (cellContents.isEmpty())
					return true;
				else
					return false;
			}
		}
		return false;
	}

	/*
	 * 
	 * returns the first column index encountered - when any of the cell
	 * contents within the noOfRowsToCheck no of rows matches the headerText -
	 * case insensitive
	 */
	private int getColIndexWithHeaderText(HSSFSheet workSheet,
			String headerText, int noOfRowsToCheck) throws Exception {

		String cellContents;
		int counter = 0;

		for (Row myrow : workSheet) {
			for (Cell mycell : myrow) {
				cellContents = mycell.getStringCellValue();

				if (!cellContents.isEmpty()) {

					if (0 == cellContents.compareToIgnoreCase(headerText)) {
						return mycell.getColumnIndex();
					}
					//
					// if(cellContents.substring(0, 2).toLowerCase() ==
					// headerText.substring(0, 2).toLowerCase()) // .. the first
					// 3 digits match
					// return mycell.getColumnIndex();
				}
			}
			counter++; // count that we have moved down a row

			if (counter >= noOfRowsToCheck) { // then we have checked all cells
				// in rows and not found
				// headerText

				throw (new Exception("\"" + headerText + "\""
						+ " column not found in first sheet of workbook"));

			}

		}
		return 0;
	}

}
