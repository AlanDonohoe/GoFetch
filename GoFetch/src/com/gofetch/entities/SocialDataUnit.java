package com.gofetch.entities;

/*
 * Used as a getter/setter utility class for:
 * twitter, stumbel_upon, reddit, delicious, buzz, pinterest, linkedin, google+,
 * 	uses: so can return 0, if social metric count = 0 and null if there's a problem retrieving the data 
 */
public class SocialDataUnit {
	
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}



}
