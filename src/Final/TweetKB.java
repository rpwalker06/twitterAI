package Final;

public class TweetKB 

{
	//here we will have all of the weights for each tweet digest category
		//user category weights
		public double tweet_count_w=1;
		public double follower_count_w=1;
		public double following_count_w=1;
		public double tweet_by_mobile_w=1;
		public double tweet_by_web_w=1;
                public double verified_user_w=1;
		
		//content weights
		public double nsfw_w;
			
		//here we define the thresholds that we will use to score each tweet
		public int high_tweet_count=2000;
		public int high_follower_count=1000;
		public int high_following_count=300;
		
		//popularity and exposure items
		public double retweet_w=1.5;
		public double favorite_w=2;
	
	public TweetKB()
	{
		System.out.println("Knowledge base created...");
	}
	
	//here we present a function to use the score of individual tweets to update the weights of the knowledge base
	public void updateWeights(Status_digest influence)
	{
		//read the ranking on a particular digested tweet object
		//this will subsequently affect the rankings in the overall knowledge base
		double rankingConstant=influence.relativeRank*0.60;
		
		//add or subtract the ranking constant to the weights based on 
                this.favorite_w = (influence.favorite_s > 1)? rankingConstant + influence.favorite_s: rankingConstant - influence.favorite_s;
                this.tweet_count_w = (influence.tweet_count_s > 1)? rankingConstant + influence.tweet_count_s: rankingConstant - influence.tweet_count_s;
                this.follower_count_w = (influence.follower_count_s > 1)? rankingConstant + influence.follower_count_s: rankingConstant - influence.follower_count_s;
                this.following_count_w = (influence.following_count_s > 1)? rankingConstant + influence.following_count_s: rankingConstant - influence.following_count_s;
                this.nsfw_w = (influence.nsfw_s > 1)? rankingConstant + influence.nsfw_s: rankingConstant - influence.nsfw_s;
                this.retweet_w = (influence.retweet_s > 1)? rankingConstant + influence.retweet_s: rankingConstant - influence.retweet_s;
                this.verified_user_w = (influence.verified_user_s > 1) ? rankingConstant + influence.verified_user_s : rankingConstant - influence.verified_user_s;
	};
	

	//method to output the values of each of the weights to the screen
	public void printWeights()
	{
		//print user categories
		System.out.println("User Categories\n===============================");
			System.out.println("Tweet_Count: " + tweet_count_w);
			System.out.println("Follower_Count: " + follower_count_w);
			System.out.println("Following_Count: " + following_count_w);
		System.out.println("================================================");
					
		//print content categories
		System.out.println("Content Categories\n===============================");
				System.out.println("Nsfw: " + nsfw_w);
		System.out.println("================================================");
				
		//popularity/exposure categories
		System.out.println("Popularity/Exposure Categories\n===============================");
			System.out.println("Favorite: " + favorite_w);
			System.out.println("Retweet: " + retweet_w);
		System.out.println("================================================");				
		
		return;
	};
}
