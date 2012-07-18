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
	private String user_category;
	private String user_campaign;
	private String user_assigned_to;
	
	private boolean get_fb_Data;
	private boolean get_twitter_data;
	private boolean get_backlinks;
	
//	@OneToOne(optional=true, cascade=CascadeType.ALL)
//	@JoinColumn(name="seomoz_id")
//	private SEOMozData seoMozObject;
//	@OneToMany(cascade=CascadeType.ALL)
//	private List <TwitterMention> twitterList;
//	@OneToMany(cascade=CascadeType.ALL)
//	private List <FaceBookData> faceBookList;
	

	public URL() {
		super();
		
		get_fb_Data = false;
		get_twitter_data = false;
		get_backlinks = false;
	}   
	
	
	
//	public SEOMozData getSeoMozObject() {
//		return seoMozObject;
//	}
//
//
//
//	public void setSeoMozObject(SEOMozData seoMozObject) {
//		this.seoMozObject = seoMozObject;
//	}



//	public List<TwitterMention> getTwitterList() {
//		return twitterList;
//	}
//
//
//
//	public void setTwitterList(List<TwitterMention> twitterList) {
//		this.twitterList = twitterList;
//	}
//
//
//
//	public List<FaceBookData> getFaceBookList() {
//		return faceBookList;
//	}
//
//
//
//	public void setFaceBookList(List<FaceBookData> faceBookList) {
//		this.faceBookList = faceBookList;
//	}
//


	public Date getDate() {
		return date;
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


	public String getUser_category() {
		return user_category;
	}


	public void setUser_category(String user_category) {
		this.user_category = user_category;
	}


	public String getUser_campaign() {
		return user_campaign;
	}


	public void setUser_campaign(String user_campaign) {
		this.user_campaign = user_campaign;
	}


	public String getUser_assigned_to() {
		return user_assigned_to;
	}


	public void setUser_assigned_to(String user_assigned_to) {
		this.user_assigned_to = user_assigned_to;
	}


	public boolean isGet_fb_Data() {
		return get_fb_Data;
	}


	public void setGet_fb_Data(boolean get_fb_Data) {
		this.get_fb_Data = get_fb_Data;
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
