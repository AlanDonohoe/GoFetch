package com.gofetch.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name="client_category")
public class ClientCategory implements Serializable {
	 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column( nullable=false )
	private Integer id;

	private String category;
	
	// points to the client's unique ID in the user table.
	@NotNull private int users_id;
	
	private boolean client_default;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getUser_id() {
		return users_id;
	}

	public void setUser_id(int user_id) {
		this.users_id = user_id;
	}

	public boolean isClient_default() {
		return client_default;
	}

	public void setClient_default(boolean client_default) {
		this.client_default = client_default;
	}
	
	
	

}