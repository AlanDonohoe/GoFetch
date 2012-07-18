package com.gofetch.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.validator.NotNull;

/**
 * Entity implementation class for Entity: FaceBookData
 *
 */
@Entity
public class FaceBookData implements Serializable {

	   
	//TODO: finish the JPA annotation of this entity.....
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer fb_id;
	private static final long serialVersionUID = 1L;
		
	@Temporal(TemporalType.DATE)
	@NotNull private Date date;
	private int totalCount;
	private int likeCount;
	private int commentCount;
	private int shareCount;
	private int clickCount;

	public FaceBookData() {
		super();
	}   
	
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public int getLikeCount() {
		return likeCount;
	}


	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}


	public int getCommentCount() {
		return commentCount;
	}


	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}


	public int getShareCount() {
		return shareCount;
	}


	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}


	public int getClickCount() {
		return clickCount;
	}


	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}


	public Integer getId() {
		return this.fb_id;
	}

	public void setId(Integer id) {
		this.fb_id = id;
	}
   
}
