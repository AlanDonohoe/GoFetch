package com.gofetch.entities;

import java.io.Serializable;
import java.lang.Integer;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: SEOMozData
 *
 */
@Entity
@Table(name="seomoz_data")
public class SEOMozData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//TODO: finish the JPA annotation of this entity.....
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer seomoz_id;
	
	private Integer url_id;
	
	private Integer auditor_rank;
	private Integer auditor_id;
	private Integer last_question;
	private String comment;
	private String rga_score;

	public SEOMozData() {
		super();
	}   
	

	
	public Integer getSeomoz_id() {
		return seomoz_id;
	}



	public void setSeomoz_id(Integer seomoz_id) {
		this.seomoz_id = seomoz_id;
	}



	public Integer getUrl_id() {
		return url_id;
	}



	public void setUrl_id(Integer url_id) {
		this.url_id = url_id;
	}



	public Integer getAuditor_rank() {
		return auditor_rank;
	}


	public void setAuditor_rank(Integer auditor_rank) {
		this.auditor_rank = auditor_rank;
	}


	public Integer getAuditor_id() {
		return auditor_id;
	}


	public void setAuditor_id(Integer auditor_id) {
		this.auditor_id = auditor_id;
	}


	public Integer getLast_question() {
		return last_question;
	}


	public void setLast_question(Integer last_question) {
		this.last_question = last_question;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getRga_score() {
		return rga_score;
	}


	public void setRga_score(String rga_score) {
		this.rga_score = rga_score;
	}


	public Integer getId() {
		return this.seomoz_id;
	}

	public void setId(Integer id) {
		this.seomoz_id = id;
	}
   
}
