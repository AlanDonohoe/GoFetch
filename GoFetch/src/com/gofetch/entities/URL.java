package com.gofetch.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.validator.NotNull;
//import java.util.ArrayList;
//import java.util.List;

import com.gofetch.GoFetchConstants;

/**
 * Entity implementation class for Entity: URL
 *
 */

@Entity
@Table(name="url")
public class URL implements Serializable {

	//TODO: finish the JPA annotation of this entity.....
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column( nullable=false )
	private Integer url_id;
	private static final long serialVersionUID = 1L;

	@NotNull private String url_address;
	@NotNull private String user_id;

	@Temporal(TemporalType.DATE) 
	@NotNull private Date date;

	@Temporal(TemporalType.DATE) 
	private Date social_data_date;
	
	private Integer social_data_freq;

	private String category; // directory, newsite, etc.
	private String domain;
	private String doc_title;

	private boolean get_social_data;
	private boolean get_backlinks;
	private boolean backlinks_got;

	private Integer page_authority;
	private Integer domain_authority;
	
	//private Integer no_of_layers;

	private Integer data_entered_by; // 0 = sourced from SeoMoz, 1 = user entered, 2 = ?? majestic ??
	private boolean seomoz_url;
	
	private Integer users_user_id; // points to a client's id, so that this url can be assigned to a client
	private Integer client_category_id; // points to a client-specific category, so this url can be assigned to a client's category
	private Integer client_category_users_user_id; // allows a client's category, to have many url's assigned to it...
	
	//private Integer client_category;


	// can not work out how to use JPAnnotation to make all the related objects delete when related url is deleted,
	//	so i'm doing it programmatically...
	//	@OneToOne(optional=true, cascade=CascadeType.ALL, mappedBy= "url")
	//	private SEOMozData seoMozObject;
	//	
	//	@OneToMany(cascade=CascadeType.ALL)
	//	private List <TwitterMention> twitterList;
	//	
	//	@OneToMany(cascade=CascadeType.ALL)
	//	private List <MiscSocialData> MiscSocialDataList;

	
	public URL() {
		super();

		get_social_data = false;
		get_backlinks = false;
		seomoz_url = false;

	}   
	
	
	
//	public Integer getClient_category() {
//		return client_category;
//	}
//
//
//
//	public void setClient_category(Integer client_category) {
//		this.client_category = client_category;
//	}



	public Integer getData_entered_by() {
		return data_entered_by;
	}



	public void setData_entered_by(Integer data_entered_by) {
		this.data_entered_by = data_entered_by;
	}



	public boolean isBacklinks_got() {
		return backlinks_got;
	}

	public void setBacklinks_got(boolean backlinks_got) {
		this.backlinks_got = backlinks_got;
	}

//	public Integer getNo_of_layers() {
//		return no_of_layers;
//	}
//
//	public void setNo_of_layers(Integer no_of_layers) {
//		this.no_of_layers = no_of_layers;
//	}

	public Date getDate() {
		return date;
	}

	public boolean isSeomoz_url() {
		return seomoz_url;
	}

	public void setSeomoz_url(boolean seomoz_url) {
		this.seomoz_url = seomoz_url;
	}

	public Integer getPage_authority() {
		return page_authority;
	}

	public void setPage_authority(Integer page_authority) {
		this.page_authority = page_authority;
	}

	public Integer getDomain_authority() {
		return domain_authority;
	}

	public void setDomain_authority(Integer domain_authority) {
		this.domain_authority = domain_authority;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDoc_title() {
		return doc_title;
	}

	public void setDoc_title(String doc_title) {
		this.doc_title = doc_title;
	}

	public Integer getUrl_id() {
		return url_id;
	}

	public void setUrl_id(Integer url_id) {
		this.url_id = url_id;
	}

	public boolean isGet_social_data() {
		return get_social_data;
	}

	public void setGet_social_data(boolean get_social_data) {
		this.get_social_data = get_social_data;
	}

	public boolean isGet_backlinks() {
		return get_backlinks;
	}

	public void setGet_backlinks(boolean get_backlinks) {
		this.get_backlinks = get_backlinks;
	}

	public String getUrl_address() {
		return this.url_address;
	}

	public void setUrl_address(String url_address) {
		this.url_address = url_address;
	}   
	public Integer getId() {
		return this.url_id;
	}

	public void setId(Integer id) {
		this.url_id = id;
	}


	public Date getSocial_data_date() {
		return social_data_date;
	}

	public void setSocial_data_date(Date social_data_date) {
		this.social_data_date = social_data_date;
	}

	public Integer getSocial_data_freq() {
		return social_data_freq;
	}

	public void setSocial_data_freq(Integer social_data_freq) {
		this.social_data_freq = social_data_freq;
	}
	
	

	public Integer getUsers_user_id() {
		return users_user_id;
	}



	public void setUsers_user_id(Integer users_user_id) {
		this.users_user_id = users_user_id;
	}



	public Integer getClient_category_id() {
		return client_category_id;
	}



	public void setClient_category_id(Integer client_category_id) {
		this.client_category_id = client_category_id;
	}



	public Integer getClient_category_users_user_id() {
		return client_category_users_user_id;
	}



	public void setClient_category_users_user_id(
			Integer client_category_users_user_id) {
		this.client_category_users_user_id = client_category_users_user_id;
	}



	@Override
	public String toString() {

		return String.format("\"URL\":\"%s\"", url_address);

	}

	/*
	 * decreases social_data_freq by 1. 
	 * if its already at max - then remains at max
	 */
	public void decreaseSocialCrawlFrequency() {
			
		if(GoFetchConstants.MONTHLY_FREQ == social_data_freq) // if at max, just return
			return;
		//else drop down to check at weekly or monthly.
		social_data_freq--;

	}

	/*
	 * increases social_data_freq by 1
	 * if its already checking at daily, 
	 */
	public void increaseSocialCrawlFrequency() {
			
		if(GoFetchConstants.DAILY_FREQ == social_data_freq ) // if already checking daily - just return
			return;
		
		social_data_freq++;

	}

}
