package Final;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AI_Final {

	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		//UNCOMMENT IF THE APPLICATION GIVES YOU ACCESS ISSUES
		//generateAccessToken token = new generateAccessToken();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String keep_going="n";
		heading();
		TweetKB foo = new TweetKB();
		SearchDriver go;
		
		do 
		{
			go = new SearchDriver(foo, 5);
			go.driveSearch(getRepetitions(br));
			
			System.out.println("Would you like to enter another search query?\n" +
								"Enter a lowercase n for no, or any other number for yes");
			keep_going = br.readLine();
			
						
		} while (!keep_going.equals("n"));
                
              foo.printWeights();
		
		System.exit(0);
	}
	
	//print heading function
	static void heading()
	{
		//title
		System.out.println("Automated Tweet Feed Application");
		System.out.println("Robert Walker @02635553");
		System.out.println("Intro to AI - Fall 2012\n");
		
		return;
	}
	
	static void intro()
	{
		//title
		System.out.println("\nThis application will allow you to search for a string on Twitter\n" +
							"and subsequently try to deliver you the tweets that match your preferences based\n" +
							"on how you rank the resulting tweets.  You will be prompted for a string to search for\n" +
							"as well as how many times you want to train the knowledge base.  The knowledge base will\n" +
							"remember your preferences across searches.\n" +
							"=============================================================================================");		
		return;
	}
	
	static int getRepetitions(BufferedReader br) throws NumberFormatException, IOException
	{
		//title
		System.out.println("\nHow many times would you like the application to learn with this search?");
		return Integer.parseInt(br.readLine());
	}
}