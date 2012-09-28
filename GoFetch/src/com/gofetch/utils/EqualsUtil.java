package com.gofetch.utils;

/**
 *  from: http://www.javapractices.com/topic/TopicAction.do?Id=17
 * @author alandonohoe:
 * utility class that helps implement the overriding of equals for user defined classes.
 *
 */


public final class EqualsUtil {

	  static public boolean areEqual(boolean aThis, boolean aThat){
	    
	    return aThis == aThat;
	  }

	  static public boolean areEqual(char aThis, char aThat){
	    
	    return aThis == aThat;
	  }

	  static public boolean areEqual(long aThis, long aThat){
	    /*
	    * Implementation Note
	    * Note that byte, short, and int are handled by this method, through
	    * implicit conversion.
	    */
	    
	    return aThis == aThat;
	  }

	  static public boolean areEqual(float aThis, float aThat){
	    
	    return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
	  }

	  static public boolean areEqual(double aThis, double aThat){
	    
	    return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
	  }
}