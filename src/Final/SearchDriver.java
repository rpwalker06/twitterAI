package Final;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import csvWriter.csvWriterSimple;
import java.util.HashMap;

public class SearchDriver {
	
            //Twitter API objects
            //Thank you Mr. Yamamoto!
            Query query;
            QueryResult result;
            List<Status> tweets; 
            List<Status_digest> collected;
            List<Status_digest> display_content;
            Status_digest user_display[];
            Twitter twitter;
            HashMap<Integer, Integer> displayAssociation = new HashMap<Integer, Integer>();
                        
            //driver output data
            static String outputName = "tweetOutPut";
            csvWriterSimple writeme = new csvWriterSimple(outputName, Status_digest.fieldNames);

            //knowledge base
            TweetKB knowing;

            //search specific items
            String searchString;
            int display_items;
            public static int TWEETS_TO_RETURN = 100;
            public static double SIMILARITY_THRESHOLD = 0.60;

            public void driveSearch( int repititions ) throws NumberFormatException, IOException
            {
                twitter = new TwitterFactory().getInstance();
                
                    for (int i = 0; i < repititions; i++)
                    {
                        tweets = performSearch(searchString);
                        collected = digestTweets(tweets);
                        display_content = collapseListContents(collected, displayAssociation);
                        showRankedTweets();
                        collectRankings();
                        updateKB();
                    }
                writeme.closeStream();
            }
            
            public SearchDriver(TweetKB brain, int items_display)
            {
               try {this.setupPrompt();}
               catch (IOException e) {e.printStackTrace();System.exit(-1);}

                //initialize the knowledge base
                knowing = brain;

                //set how many items we will present to the user to rank
                display_items = items_display;

                //initialize structures to hold results from Twitter API
                collected = new ArrayList<Status_digest>();
                user_display = new Status_digest[display_items];
            }
            
            public SearchDriver(TweetKB brain, int items_display, String search_term )
            {
                //initialize the knowledge base
                knowing = brain;

                //set how many items we will present to the user to rank
                display_items = items_display;

                //initialize structures to hold results from Twitter API
                collected = new ArrayList<Status_digest>();
                user_display = new Status_digest[display_items];
                
                searchString = search_term;
            }
            
            void setupPrompt() throws IOException
            {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                    //prompt the user to fill the situation
                    System.out.println("Please enter a string to search for.\n");
                    searchString=br.readLine();
                    searchString = URLEncoder.encode(searchString, "UTF-8");
            };

            public List<Status> performSearch(String stringSearch)
            {
                List<Status> returned_tweets = new ArrayList<Status>();
                //start collecting tweets
                try {
                        //set up the twitter search
                        query = new Query(stringSearch);
                        query.setCount(TWEETS_TO_RETURN);
                        query.setLang("en");

                        //now we query the twitter service and work on the results
                        result = twitter.search(query);
                        returned_tweets = result.getTweets();
                    }
                    catch (TwitterException te) 
                    {System.out.println("Failed to search tweets: " + te.getMessage());System.exit(-1);}
                
                return returned_tweets;
                
                
            };

    private List<Status_digest> digestTweets(List<Status> tweets_to_digest)
      {
        List<Status_digest> digested_tweets = new ArrayList<Status_digest>();
        //iterate through each of the returned tweets, digest them and determine the score 
        for (Status tweet : tweets_to_digest) 
        {
            Status_digest focus_status = new Status_digest(knowing, tweet);
            focus_status.digestTweet();
            focus_status.assignTweetScore();
            digested_tweets.add(focus_status);
            writeme.appendRow(focus_status.toString(false));
        }
        
        return digested_tweets;
      }
    
    private List<Status_digest> collapseListContents(List<Status_digest> allTweets, HashMap<Integer, Integer> associationDisplay)
    {
        List<Status_digest> results = new ArrayList<Status_digest>();
        double similarity_score;
        Status current_tweet;
        int results_index=0;
        boolean isduplicate;
        
        for (int i=0; i < allTweets.size(); ++i)
        {
            //check for similarity between strings to 
            current_tweet = allTweets.get(i).tweet;
            isduplicate = false;
            for (int j=0; j< results.size(); ++j)
            {
                //check the tweet's content relative to what we've already collected
                similarity_score = fuzzyStringMatcher.fuzzyMatch(current_tweet.getText(), results.get(j).tweet.getText());
                //we add to a hash map to provide an association between the two lists
                //we mark a duplicate for different handling in the outer loop
                if (similarity_score > SIMILARITY_THRESHOLD ) {associationDisplay.put(i, j);isduplicate = true;}
            }
            if (!isduplicate)
            {
                results.add(allTweets.get(i));
                associationDisplay.put(i, results_index);
                results_index++;
            }
        }
        return results;
    };
    
    void showRankedTweets()
            {
                //now show the user the tweets and give them the option to rank them.  The rankings will then be
                //used to recalculate the values of the weights
                System.out.println("When prompted please rank the tweet using a range from +" + display_items + " to -" + display_items);
                char select_letter;
                for (int i=0; i<display_items; i++)
                {
                    //provide offset for ASCII character
                    select_letter = (char) (i+65);
                    System.out.println(select_letter + ". " + display_content.get(i).tweet.getText());
                }
            }

            public void collectRankings() throws NumberFormatException, IOException
            {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    short holdRank = 0;
                    boolean validnum=false;
                    char select_letter;

                    //allow the user to enter a rank based on the the display size, either positive or negative
                    System.out.println("\nEnter a number rank for each letter.\nBetween +" + display_items + 
                                    " to -" + display_items);
                    for (int i=0; i<display_items; i++)
                    {
                            //provide offset for ASCII character
                            select_letter = (char) (i+65);
                            System.out.print(select_letter + ". " );
                            try {
                                    do {
                                      holdRank = Short.parseShort(br.readLine());
                                      validnum = validateRanking(holdRank);
                                    } while (!validnum);
                            } 
                            catch (NumberFormatException e) 
                            {
                                do 
                                {
                                    System.out.println("Not a valid number.  Please enter again.");
                                    System.out.print(select_letter + ". " );
                                    holdRank = Short.parseShort(br.readLine());
                                    validnum = validateRanking(holdRank);
                                } while (!validnum);
                            } 
                            catch (IOException e)   {e.printStackTrace();}
                            
                            display_content.get(i).relativeRank = holdRank;
                    }
            }

            //implemtation of how the ranking that the user entered is validated
            boolean validateRanking( int holdRank )
            {
                    return (holdRank >= -display_items && holdRank <= display_items ) ? true : false;
            }

            void updateKB()
            {	
                    //now that we have the user's preferences, we will apply the results of the rankings to the current knowledge base
                    for (int i = 0; i<display_items; i++)
                    {
                            knowing.updateWeights(display_content.get(i));
                    }

                    return;
            }
	}
	
