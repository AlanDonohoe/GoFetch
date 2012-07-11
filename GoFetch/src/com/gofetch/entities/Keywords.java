package com.gofetch.entities;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Keywords
 *
 */
@Entity
@Table(name="keywords")
public class Keywords implements Serializable {

	   
	@Id
	private String keyword;
	private static final long serialVersionUID = 1L;
	
	private Integer search_volume;
	private String vertical;

	public Keywords() {
		super();
	}   
	
	public Integer getSearch_volume() {
		return search_volume;
	}

	public void setSearch_volume(Integer search_volume) {
		this.search_volume = search_volume;
	}


	public String getVertical() {
		return vertical;
	}


	public void setVertical(String vertical) {
		this.vertical = vertical;
	}


	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
   
}
