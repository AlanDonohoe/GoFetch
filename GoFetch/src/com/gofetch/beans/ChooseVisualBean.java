package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author alandonohoe
 * bean deals with presenting user with selection of visualisations and reports
 * 
 * ex: visualisation = "LineChart", image in images/visualsreports/ = "LineChart.jpg"
 */

public class ChooseVisualBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<VisualisationReport> visualisationList = new ArrayList<VisualisationReport>();
	
	String selectedVisualisation;
	
	public ChooseVisualBean(){
		
		VisualisationReport visReport1 = new VisualisationReport();
		VisualisationReport visReport2 = new VisualisationReport();
		
		visReport1.setDescription("A simple line chart presentation of your data");
		visReport1.setName("Line Chart");
		visReport1.setImageName("LineChart.jpg");
		
		visReport2.setDescription("A Link audit and LinkSafe Visualisation");
		visReport2.setName("LinkSafe");
		visReport2.setImageName("LinkSafe.jpg");		

		visualisationList.add(visReport1); 
		visualisationList.add(visReport2);
	}

	public List<VisualisationReport> getVisualisationList() {
		return visualisationList;
	}

	public void setVisualisationList(List<VisualisationReport> visualisationList) {
		this.visualisationList = visualisationList;
	}

	public String getSelectedVisualisation() {
		return selectedVisualisation;
	}

	public void setSelectedVisualisation(String selectedVisualisation) {
		this.selectedVisualisation = selectedVisualisation;
	}
	

}
