package com.gofetch.entities;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.validator.NotNull;

/**
 * Entity implementation class for Entity: TwitterMention
 * //Don't know if we need this now... as we can jsut access Topsy for historical data to make social ripple charts...
 */
@Entity
@Table(name="twitter_mention")
public class TwitterMention implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer twitter_id;
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.DATE)
	@NotNull private Date date;
	@NotNull private String tweeter;
	private String tweet_text;
	private Integer followers;
	private Integer followed;
	private Integer klout_score;
	

	public TwitterMention() {
		super();
	}   
	
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getTweeter() {
		return tweeter;
	}


	public void setTweeter(String tweeter) {
		this.tweeter = tweeter;
	}


	public String getTweet_text() {
		return tweet_text;
	}


	public void setTweet_text(String tweet_text) {
		this.tweet_text = tweet_text;
	}


	public Integer getFollowers() {
		return followers;
	}


	public void setFollowers(Integer followers) {
		this.followers = followers;
	}


	public Integer getFollowed() {
		return followed;
	}


	public void setFollowed(Integer followed) {
		this.followed = followed;
	}


	public Integer getKlout_score() {
		return klout_score;
	}


	public void setKlout_score(Integer klout_score) {
		this.klout_score = klout_score;
	}


	public Integer getId() {
		return this.twitter_id;
	}

	public void setId(Integer id) {
		this.twitter_id = id;
	}
   
}
