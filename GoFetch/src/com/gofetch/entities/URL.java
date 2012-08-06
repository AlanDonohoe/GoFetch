package com.gofetch.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.hibernate.validator.NotNull;

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
	
	private String category;
	private String domain;
	private String doc_title;
	
	private boolean get_fb_data;
	private boolean get_twitter_data;
	private boolean get_backlinks;
	
	private Integer page_authority;
	private Integer domain_authority;
	
	private boolean seomoz_url;
	
//	@OneToOne(optional=true, cascade=CascadeType.ALL)
//	@JoinColumn(name="seomoz_id")
//	private SEOMozData seoMozObject;
//	@OneToMany(cascade=CascadeType.ALL)
//	private List <TwitterMention> twitterList;
//	@OneToMany(cascade=CascadeType.ALL)
//	private List <MiscSocialData> MiscSocialDataList;
	

	public URL() {
		super();
		
		get_fb_data = false;
		get_twitter_data = false;
		get_backlinks = false;
		seomoz_url = false;
	}   

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


	public boolean isGet_fb_data() {
		return get_fb_data;
	}


	public void setGet_fb_data(boolean get_fb_Data) {
		this.get_fb_data = get_fb_Data;
	}


	public boolean isGet_twitter_data() {
		return get_twitter_data;
	}


	public void setGet_twitter_data(boolean get_twitter_data) {
		this.get_twitter_data = get_twitter_data;
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
   
}
