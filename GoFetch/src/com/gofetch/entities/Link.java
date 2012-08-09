package com.gofetch.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.validator.NotNull;

import com.gofetch.seomoz.Constants;

/**
 * Entity implementation class for Entity: Links
 *
 */
@Entity
@Table(name="links")
public class Link implements Serializable {

	//TODO: finish the JPA annotation of this entity.....
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer links_id;
	private static final long serialVersionUID = 1L;

	@NotNull private Integer target_id;
	@NotNull private Integer source_id;

	@NotNull private String anchor_text;

	@Temporal(TemporalType.DATE)
	@NotNull private Date date_detected;

	@Temporal(TemporalType.DATE)
	private Date date_expired;
	
	@NotNull
	private String final_target_url;


	public Link() {
		super();
	}   


	public String getFinal_target_url() {
		return final_target_url;
	}


	public void setFinal_target_url(String final_target_url) {
		this.final_target_url = final_target_url;
	}


	public String getAnchor_text() {
		return anchor_text;
	}


	public void setAnchor_text(String anchor_text) {

		if(anchor_text.length() > Constants.MAX_ANCHOR_TEXT_LENGTH)
			this.anchor_text = anchor_text.substring(0, Constants.MAX_ANCHOR_TEXT_LENGTH);
		else
			this.anchor_text = anchor_text;
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


	public Integer getLinks_id() {
		return links_id;
	}


	public void setLinks_id(Integer links_id) {
		this.links_id = links_id;
	}



}
