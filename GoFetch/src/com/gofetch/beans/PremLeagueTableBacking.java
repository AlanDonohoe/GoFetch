package com.gofetch.beans;
import java.io.Serializable;
import java.util.List;

import com.gofetch.entities.LeagueEntryBean;
/**
 * 
 * @author alandonohoe
 *
 * Bean that contains all the logic for the premier league table page
 */
public class PremLeagueTableBacking implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<LeagueEntryBean> leagueTableEntries;

}
