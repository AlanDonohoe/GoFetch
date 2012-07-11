package com.gofetch.seomoz;
/**
 * 
 * @author alandonohoe
 * 
 * Wraps up the SEOMoz URL data - DA, PA.
 *
 */
public class SEOMozURLData {
	
    private String score; // red, green or amber
    
    private Integer backLinkPA; // Page Authority
    private Integer backLinkDA; // domain authority
    
    private static String targetUrlDA; //class wide member - this backlink's target's DA
    private static String targetUrlPA; //class wide member - this backlink's target's PA

    private Integer auditorRank;	// either 0: link not scored, 1: scored by algo, 2: scored by out-sourced auditor, 3: scored by in house team, 4: scored/checked by account manager 
    private Integer auditorID; 		// will ID the auditor if any issues - QA measure.
    private Integer lastQuestion; 	// the last question answered before the link was assigned a RGA score
    private String comment;			// used by auditor to add a comment - for ex: no submission policy - for use by account manager when querying audit results.
    								// 	use to send messages higher up the tree....
    
    

    public Integer getAuditorRank() {
		return auditorRank;
	}
	public void setAuditorRank(Integer auditorRank) {
		this.auditorRank = auditorRank;
	}
	public Integer getAuditorID() {
		return auditorID;
	}
	public void setAuditorID(Integer auditorID) {
		this.auditorID = auditorID;
	}
	public Integer getLastQuestion() {
		return lastQuestion;
	}
	public void setLastQuestion(Integer lastQuestion) {
		this.lastQuestion = lastQuestion;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
    public String getBackLinkPAString(){
    	return String.valueOf(backLinkPA);
    }
	public String getBackLinkDAString(){
    	return String.valueOf(backLinkDA);
    }
    public Integer getBackLinkPA() {
		return backLinkPA;
	}
	public void setBackLinkPA(Integer backLinkPA) {
		this.backLinkPA = backLinkPA;
	}
	public Integer getBackLinkDA() {
		return backLinkDA;
	}
	public void setBackLinkDA(Integer backLinkDA) {
		this.backLinkDA = backLinkDA;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public static String getTargetUrlDA() {
		return targetUrlDA;
	}
	public static void setTargetUrlDA(String targetUrlDA) {
		SEOMozURLData.targetUrlDA = targetUrlDA;
	}
	public static String getTargetUrlPA() {
		return targetUrlPA;
	}
	public static void setTargetUrlPA(String targetUrlPA) {
		SEOMozURLData.targetUrlPA = targetUrlPA;
	}
}
