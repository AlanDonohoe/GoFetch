package com.gofetch.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

import com.gofetch.utils.DateUtil;
import com.gofetch.utils.EqualsUtil;

/**
 * Used when one source returns all the social data in one call.
 * esp. refering to SharedCount API
 * @author alandonohoe
 *
 */
@Entity
@Table(name="misc_social_data")
public class MiscSocialData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer social_data_id;

	@Temporal(TemporalType.DATE)
	@NotNull private Date date;
	
	@NotNull private int url_id;
	//facebook
	private int fb_total_Count;
	private int fb_like_Count;
	private int fb_comment_Count;
	private int fb_share_Count;
	private int fb_click_Count;
	private int fb_commentsbox_count;
	//
	private int stumble_upon;
	private int delicious;
	private int google_plus_one;
	private int pinterest;
	private int linkedin;
	private int twitter;

	@Override
	public String toString() {

		String fbTotal = Integer.toString(fb_total_Count);
		String fbLikes = Integer.toString(fb_like_Count);
		String fbComments = Integer.toString(fb_comment_Count);
		String fbShares = Integer.toString(fb_share_Count);
		String fbClicks = Integer.toString(fb_click_Count);
		String fbCommentBox = Integer.toString(fb_commentsbox_count);
		
		String str_stumble_upon = Integer.toString(stumble_upon);
		String str_delicious  = Integer.toString(delicious);
		String str_google_plus_one  = Integer.toString(google_plus_one);
		String str_pinterest  = Integer.toString(pinterest);
		String str_linkedin  = Integer.toString(linkedin);
		String str_twitter  = Integer.toString(twitter);
		
		
		String strDate = DateUtil.getFormattedDate(getDate());
		
		//return(strDate + " - " + fbTotal + " - " + fbLikes);

		return String.format("{\"Date\": \"%s\", \"FaceBookTotal\": %s, \"FaceBookLikes\": %s,\"FaceBookComments\": %s,\"FaceBookShares\": %s,\"FaceBookClicks\": %s,\"FaceBookCommentBox\": %s,\"StumbleUpon\": %s,\"Delicious\": %s,\"GooglePlus\": %s,\"Pinterest\": %s,\"LinkedIn\": %s,\"Twitter\": %s }", 
							  strDate, 	 			fbTotal, 			fbLikes,		  		fbComments,		  		fbShares,			   fbClicks,		  		fbCommentBox, 			str_stumble_upon,	str_delicious,		str_google_plus_one, str_pinterest, str_linkedin, 	str_twitter);
		
	}


	public Integer getSocial_data_id() {
		return social_data_id;
	}
	public void setSocial_data_id(Integer social_data_id) {
		this.social_data_id = social_data_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getUrl_id() {
		return url_id;
	}
	public void setUrl_id(int url_id) {
		this.url_id = url_id;
	}
	public int getFb_total_Count() {
		return fb_total_Count;
	}
	public void setFb_total_Count(int fb_total_Count) {
		this.fb_total_Count = fb_total_Count;
	}
	public int getFb_like_Count() {
		return fb_like_Count;
	}
	public void setFb_like_Count(int fb_like_Count) {
		this.fb_like_Count = fb_like_Count;
	}
	public int getFb_comment_Count() {
		return fb_comment_Count;
	}
	public void setFb_comment_Count(int fb_comment_Count) {
		this.fb_comment_Count = fb_comment_Count;
	}
	public int getFb_share_Count() {
		return fb_share_Count;
	}
	public void setFb_share_Count(int fb_share_Count) {
		this.fb_share_Count = fb_share_Count;
	}
	public int getFb_click_Count() {
		return fb_click_Count;
	}
	public void setFb_click_Count(int fb_click_Count) {
		this.fb_click_Count = fb_click_Count;
	}

	public int getFb_commentsbox_count() {
		return fb_commentsbox_count;
	}
	public void setFb_commentsbox_count(int fb_commentsbox_count) {
		this.fb_commentsbox_count = fb_commentsbox_count;
	}
	public int getLinkedin() {
		return linkedin;
	}
	public void setLinkedin(int linkedin) {
		this.linkedin = linkedin;
	}
	public int getStumble_upon() {
		return stumble_upon;
	}
	public void setStumble_upon(int stumble_upon) {
		this.stumble_upon = stumble_upon;
	}
	public int getDelicious() {
		return delicious;
	}
	public void setDelicious(int delicious) {
		this.delicious = delicious;
	}
	public int getGoogle_plus_one() {
		return google_plus_one;
	}
	public void setGoogle_plus_one(int google_plus_one) {
		this.google_plus_one = google_plus_one;
	}

	public int getPinterest() {
		return pinterest;
	}
	public void setPinterest(int pinterest) {
		this.pinterest = pinterest;
	}

	public int getTwitter() {
		return twitter;
	}
	public void setTwitter(int twitter) {
		this.twitter = twitter;
	}

	/** 
	 * 
	 * @param compareTo = MiscSocialData object to compare to this.
	 * @returns true, ONLY if ALL fields of the social data object are the equal. 
	 */
	public boolean equals(MiscSocialData compareTo){


		return 
				EqualsUtil.areEqual(fb_total_Count, compareTo.getFb_total_Count()) &&
				EqualsUtil.areEqual(fb_like_Count, compareTo.getFb_like_Count()) &&
				EqualsUtil.areEqual(fb_comment_Count, compareTo.getFb_comment_Count()) &&
				EqualsUtil.areEqual(fb_share_Count, compareTo.getFb_share_Count()) &&
				EqualsUtil.areEqual(fb_click_Count, compareTo.getFb_click_Count()) &&
				EqualsUtil.areEqual(stumble_upon, compareTo.getStumble_upon()) &&
				EqualsUtil.areEqual(delicious, compareTo.getDelicious()) &&
				EqualsUtil.areEqual(google_plus_one, compareTo.getGoogle_plus_one()) &&
				EqualsUtil.areEqual(pinterest, compareTo.getPinterest()) &&
				EqualsUtil.areEqual(linkedin, compareTo.getLinkedin()) &&
				EqualsUtil.areEqual(twitter, compareTo.getTwitter());
	}

}
