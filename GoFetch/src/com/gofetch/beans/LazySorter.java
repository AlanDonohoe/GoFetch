package com.gofetch.beans;

import java.util.Comparator;
import org.primefaces.model.SortOrder;

import com.gofetch.entities.URLAndLinkData;

public class LazySorter implements Comparator<URLAndLinkData> {
	
	
	private String sortField;
    
    private SortOrder sortOrder;
    
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

	@Override
	public int compare(URLAndLinkData urlAndLinkData1, URLAndLinkData urlAndLinkData2) {
		try {
            Object value1 = URLAndLinkData.class.getField(this.sortField).get(urlAndLinkData1);
            Object value2 = URLAndLinkData.class.getField(this.sortField).get(urlAndLinkData2);

            int value = ((Comparable)value1).compareTo(value2);
            
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
	}

}
