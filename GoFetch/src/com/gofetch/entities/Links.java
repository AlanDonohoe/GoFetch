package com.gofetch.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.sql.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Links
 *
 */
@Entity

public class Links implements Serializable {

	   
	//TODO: finish the JPA annotation of this entity.....
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private static final long serialVersionUID = 1L;
	
	private Integer target_id;
	private Integer source_id;
	
	private Date date_detected;
	private Date date_expired;
	

	public Links() {
		super();
	}   
	
	
	public Integer getTarget_id() {
		return target_id;
	}


	public void setTarget_id(Integer target_id) {
		this.target_id = target_id;
	}


	public Integer getSource_id() {
		return source_id;
	}


	public void setSource_id(Integer source_id) {
		this.source_id = source_id;
	}


	public Date getDate_detected() {
		return date_detected;
	}


	public void setDate_detected(Date date_detected) {
		this.date_detected = date_detected;
	}


	public Date getDate_expired() {
		return date_expired;
	}


	public void setDate_expired(Date date_expired) {
		this.date_expired = date_expired;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
   
}
