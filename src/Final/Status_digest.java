package Final;

import twitter4j.*;

public class Status_digest {

	TweetKB knowledge;
	Status tweet;
	
	//user category items
	short tweet_count_s=1;
	short follower_count_s=1;
	short following_count_s=1;
	short tweet_by_mobile_s =1;
	short tweet_by_web_s =1;
        short verified_user_s=1;
		
	//content items
	short nsfw_s =1;  //Twitter automatically flags tweets that may contain offensive language
		
	//popularity and exposure items
	short retweet_s =1;
	short favorite_s =1;
		
	short relativeRank;
	public double tweetScore;
        public static String fieldNames = createHeading();
	
	public Status_digest(TweetKB known, Status focus)
	{
		knowledge = known;
		tweet = focus;
                
	};
        
        private static String createHeading()
        {
            String heading="";
            
            heading+="Tweet ID\t";
            heading+="Tweet Time\t";
            heading+="Tweet Source\t";
            heading+="Tweet Username\t";
            heading+="Tweet User Screen Name\t";
            heading+="Tweet User Follower Count\t";
            heading+="Tweet User Following Count\t";
            heading+="Tweet User Tweets Count\t";
            heading+="Tweet User Is Verified (Famous)\t";
            heading+="Is Tweet a Retweet\t";
            heading+="Is Tweet Favorited\t";
            heading+="Is There Cussing?\t";
            heading+="Tweet Score\t";
            heading+="Tweet Content\t";
            heading+="\n";
            
            return heading;
        }
        
     public String toString(boolean header)
        {
            //give an idea of the status of the tweet, this can be considered an interface element
            String heading="";
            
            String digested="";
            
            //Print the tweet ID
            heading+="Tweet ID\t";
            digested+= tweet.getId() +"\t";
            
            //Print the tweet ID
            heading+="Tweet Time\t";
            digested+= tweet.getCreatedAt().toString() +"\t";
            
            heading+="Tweet Source\t";
            digested+= tweet.getSource() +"\t";
            
            heading+="Tweet Username\t";
            digested+= tweet.getUser().getName() +"\t";
            
            heading+="Tweet User Screen Name\t";
            digested+= tweet.getUser().getScreenName() +"\t";
            
            heading+="Tweet User Follower Count\t";
            digested+= tweet.getUser().getFollowersCount() +"\t";
            
            heading+="Tweet User Following Count\t";
            digested+= tweet.getUser().getFriendsCount() +"\t";
            
            heading+="Tweet User Tweets Count\t";
            digested+= tweet.getUser().getStatusesCount() +"\t";
            
            heading+="Tweet User Is Verified (Famous)\t";
            digested+= tweet.getUser().isVerified() +"\t";
            
            heading+="Is Tweet a Retweet\t";
            digested+= Boolean.toString(tweet.isRetweet()) +"\t";
            
            heading+="Is Tweet Favorited\t";
            digested+= Boolean.toString(tweet.isFavorited()) +"\t";
            
            heading+="Is There Cussing?\t";
            digested+= Boolean.toString(tweet.isPossiblySensitive()) +"\t";
            
            heading+="Tweet Score\t";
            digested+= tweetScore +"\t";
            
            heading+="Tweet Content\t";
            digested+= tweet.getText() +"\t";
            
            
            
            heading+="\n";
            
            return header ? heading : digested;
         }
        
	
	//digest function to assign category scores based on the weights in the knowledge base
	public void digestTweet()
	{
		//assign scores for user categories
			//score for high user tweet count
			if (tweet.getUser().getStatusesCount() >= knowledge.high_tweet_count )	tweet_count_s = 2;
			
			//score for high follower count
			if (tweet.getUser().getFollowersCount() >= knowledge.high_follower_count)	follower_count_s = 2;
			
			//score for following count
			if (tweet.getUser().getFriendsCount() >= knowledge.high_following_count)	following_count_s = 2;
			
		//assign scores for content categories
			//score nsfw
			if (tweet.isPossiblySensitive()) nsfw_s = 2;
			
		//assign scores for popularity/exposure categories
			//score favorite.  Note that this score is worth more b/c of the rarity of a tweet being a favorite
			if (tweet.isFavorited()) favorite_s = 3;
			
			//score retweet
			if (tweet.isRetweet()) retweet_s = 2;
			
			if (tweet.getUser().isVerified()) verified_user_s = 2;
	}
	
	public void assignTweetScore()
	{
		tweetScore=0;
		
		//add scores and weights for user categories 
			tweetScore+=tweet_count_s*knowledge.tweet_count_w;
			tweetScore+=follower_count_s*knowledge.follower_count_w;
			tweetScore+=following_count_s*knowledge.following_count_w;
			tweetScore+=tweet_by_mobile_s*knowledge.tweet_by_mobile_w;
			tweetScore+=tweet_by_web_s*knowledge.tweet_by_web_w;
	
		//add scores and weights for content categories 
			tweetScore+=nsfw_s*knowledge.nsfw_w;
			
		//add scores and weights for popularity/exposure categories
			tweetScore+=retweet_s*knowledge.retweet_w;
			tweetScore+=favorite_s*knowledge.favorite_w;
                        tweetScore+=verified_user_s*knowledge.verified_user_w;
		
	}
}