package com.gofetch.entities;

// see:
// for datastore and images

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

import com.google.appengine.api.blobstore.BlobKey;
/**
 * 
 * @author alandonohoe
 * represents an image, which points to either a URL, link, client or contact
 */
@Table(name = "image")
@Entity
public class Image {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@NotNull
	private String name; // format: id of object it points to, version no (plus date. ?)
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date date;
	
	// Points to either a URL, Link or contact object.
	private Integer object_id;
	
	//1 = first image with no annotations or user added effects, 2 = first version affected by the users, 3... etc
	@NotNull
	private Integer version;
	
	// 1 = 70x50 (mini thumnbail), 2 = 140x100 (thumbnail),  3 = 336x336 (small image), 4 = 504x504 (med), 5 = 768x578 (large), 6 = full screen webshot
	@NotNull
	private Integer size;
	
	//Img and img flags...
	// For these 2 below - set flag, set - this image can only ever be saved locally or remotely - set flag when blobkey or urlAddress is set
	// if saved on our server
	private BlobKey blobKey;
	
	// if saved on remote server:
	private String imgURLAddress;
	
	//True if img if saved and pointed to by: imgURLAddress. else img is saved locally and pointed to by blobkey
	private boolean remoteSavedImg; 
	
	//this data format... could be one of 3 things below.... depends on future use cases...
	private byte[] imgData;
	
	// old way - saving to mysql
//	@Lob
//	private byte[] content;
	
	// new way - saving to GAE datastore
//	@Persistent
//    Blob image;

	public Image(){	
		version = 0;
	}
	
	public Image(String name){
		this.name = name;
		//this.image = image;
		version = 0;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getObject_id() {
		return object_id;
	}

	public void setObject_id(Integer object_id) {
		this.object_id = object_id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

//	public byte[] getContent() {
//		return content;
//	}
//
//	public void setContent(byte[] content) {
//		this.content = content;
//	}
	
//	public Blob getImage() {
//		return image;
//	}
//
//	public void setImage(Blob image) {
//		this.image = image;
//	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public BlobKey getBlobKey() {
		return blobKey;
	}

	public void setBlobKey(BlobKey blobKey) {
		this.blobKey = blobKey;
	}

	public String getImgURLAddress() {
		return imgURLAddress;
	}

	public void setImgURLAddress(String imgURLAddress) {
		this.imgURLAddress = imgURLAddress;
	}

	public boolean isRemoteSavedImg() {
		return remoteSavedImg;
	}

	public void setRemoteSavedImg(boolean remoteSavedImg) {
		this.remoteSavedImg = remoteSavedImg;
	}

	public byte[] getImgData() {
		return imgData;
	}

	public void setImgData(byte[] imgData) {
		this.imgData = imgData;
	}
}
