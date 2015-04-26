package NLPProject;

import java.io.*;
import java.util.*;

import twitter4j.Status;
import twitter4j.TwitterException;

public class DataSet_Date_Extraction {
	public static String input_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\output_celeb_inf.txt";
	public static String input_ninf = "C:\\Users\\Anurag\\Desktop\\NLP\\output_celeb_non_inf.txt";
	public static String Dataset_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\Dataset_inf";
	public static String Dataset_ninf = "C:\\Users\\Anurag\\Desktop\\NLP\\Dataset_ninf";
	
	public static void main(String args[]) throws IOException, TwitterException{
		Scanner s = new Scanner(new File(input_inf));
		FileWriter fw = new FileWriter(new File(Dataset_inf+"\\tweet_inf.txt"));
		TweetsExtraction tweet_exac = new TweetsExtraction();
		while(s.hasNextLine()){
			String temp = s.nextLine();
			temp = temp.substring(1);
			//fw.write(temp+"   ");
			ArrayList<Status> tweets_list = new ArrayList<Status>();
			tweets_list =tweet_exac.generateTweets(temp);
			for(Status s1 : tweets_list){
				if(!s1.isRetweet()){
					fw.write(temp+"   "+s1.getCreatedAt()+"   "+s1.getText());
					fw.write("\n");
					
				}
				
			}
		}
		fw.close();
		/*
		Scanner sc = new Scanner(new File(input_ninf));
		FileWriter fw1 = new FileWriter(new File(Dataset_ninf+"\\tweet_ninf.txt"));
		//TweetsExtraction tweet_exac = new TweetsExtraction();
		while(s.hasNextLine()){
			String temp = sc.nextLine();
			temp = temp.substring(1);
			//fw1.write(temp+"   ");
			ArrayList<Status> tweets_list = new ArrayList<Status>();
			tweets_list =tweet_exac.generateTweets(temp);
			for(Status s1 : tweets_list){
				if(!s1.isRetweet()){
					fw1.write(temp+"   "+s1.getCreatedAt()+"   "+s1.getText());
					fw1.write("\n");
					
				}
				
			}
		}
		fw1.close();
		
		*/
		
		
		
		
	}
}
