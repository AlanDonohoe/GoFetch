package com.gofetch.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * 
 * @author alandonohoe
 * Represents a single entry in the premier league table
 */
public class LeagueEntryBean {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	User brand; // Client/Brand - saved in users’ table ??? - will also have img associated with accessed via here...
	String campaign;  // in client_category table. ?
	
	@OneToOne (fetch=FetchType.LAZY)
	@PrimaryKeyJoinColumn(name="url_id")
	URL url;
	
	//Social Data: 2 options:
	//1: Don't know if we need this ??? just need call every week to get current social data "live" and no need to persist..???
	@OneToMany(cascade=CascadeType.ALL)
	private List <MiscSocialData> miscSocialDataList;
	
	//2: just call social data every week and dont save...
	MiscSocialData miscSocialData;
	
	// Images: 
	// will have 2: users/client's logo - stored locally and accessed throgh user object above...
	// .. and... img of url - saved on linkpeek.com
	Image imgURL;
	
	Integer totalNoOfLinks;
	Integer rocketFuel;

	
}
