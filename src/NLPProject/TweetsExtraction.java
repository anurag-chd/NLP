package NLPProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class TweetsExtraction {
    /**
     * Usage: java twitter4j.examples.timeline.GetHomeTimeline
     *
     * @param args String[]
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        try {

           ArrayList<Status> tweets = generateTweets("iPoonampandey");
            
            for (Status status : tweets){
            	
            	status.getUser().getFollowersCount();
            	status.getUser().isVerified();
            	
            	//if(statu)
            	//status.getUser().is
            	if(!status.isRetweet())
            		System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()+"   Dated : "+status.getCreatedAt());
            }
            	
        	
        	
        	//generateTweets_querry("iPoonampandey ");
        	
        	
        	}
        catch (Exception te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }

public static ArrayList<Status> generateTweets(String username) throws TwitterException{
	  Twitter twitter = new TwitterFactory().getInstance();
	  Map<String,RateLimitStatus> rateLimitStatus=  twitter.getRateLimitStatus();
      
	  int numberOfTweets = 100;
      long lastID = Long.MAX_VALUE;
      ArrayList<Status> tweets = new ArrayList<Status>();
      int i =1;
      for (String endpoint : rateLimitStatus.keySet()) {
          RateLimitStatus status = rateLimitStatus.get(endpoint);
          System.err.println("Endpoint: " + endpoint);
          System.err.println(" Limit: " + status.getLimit());
          System.err.println(" Remaining: " + status.getRemaining());
          System.err.println(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
          System.err.println(" SecondsUntilReset: " + status.getSecondsUntilReset());
          System.out.println();
      }
      
   	  while(i<5){  
      
   	    try {
   	    	Paging paging = new Paging(i, 100);
   	    	
   	    	System.out.println("\n\n");
   	    	
   	    	ResponseList<Status> result = twitter.getUserTimeline(username, paging);
   	    	
   	    	if(result.size()>0){
   	    		if(result.get(0).getUser().isProtected()){
   	    			return tweets;
   	    		}
   	    		System.out.println();
   	    		tweets.addAll(result);
   	    		System.out.println("Gathered " + tweets.size() + " tweets");
   	    		i++;
   	    	}
   	    	else{
   	    		break;
   	    	}
   	      
   	    }
   	     catch (TwitterException te) {
   	    	System.out.println("Couldn't connect: " + te);
   	    	return null;
   	      }; 
   	     
   	    }
   	    

      return tweets;
}

public static ArrayList<Status> generateTweets_querry(String username){
	
	Twitter twitter = new TwitterFactory().getInstance();
	Query query = new Query("from:"+username);
	
	ArrayList<Status> tweets = new ArrayList<Status>();
    
    QueryResult result;
	try {
		result = twitter.search(query);
		
		System.out.println("results size: " + result.getTweets().size());
		 for (Status status : result.getTweets()) {
		        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		    }
		 
	} 
	catch (TwitterException e) {
		
		e.printStackTrace();
	}
	   
 	
 	      
 	    
 	     	    

    return tweets;
}










}