package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
 
@ManagedBean
@RequestScoped
public class Collapsible implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Collapsible.class
			.getName());
 
    public Collapsible() {
    	
    	for(int i = 0; i < 5; i++){
    		RepeatRows row = new RepeatRows();
    		
    		row.setId(Integer.toString(i));
    		row.setTitle("Title" + Integer.toString(i));
    		row.setBody("Body" + Integer.toString(i));
    		
    		rows.add(row);
    	}
    }
     
    List<RepeatRows> rows =new ArrayList<RepeatRows>();
     
    public List<RepeatRows> getRows () {                
        // populate rows
        return rows;
    }
     
     
    public void setRows (List<RepeatRows> s) {
        rows = s;
    }
     
    public class RepeatRows  {        
        RepeatRows () {}
                 
        String id;
        String title;
        String body;
        boolean selected;
         
        // getters and setters
        
        
        public String getId () {
            return id;
        }
        public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		public void setId (String s) {
            id=s;
        }
        // Other getters and setters
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
        
        
    }    
}