package NLPProject;
import java.io.*;
import java.util.*;

import twitter4j.Status;
public class DatasetExtraction {

	public static void main(String args[]){
		String input_file = "C:\\Users\\Anurag\\Desktop\\NLP\\celebrities_profiles_comp.txt";
		String input_file1 = "C:\\Users\\Anurag\\Desktop\\NLP\\Twiiter_name_Treust_rank\\Temp_twitter_name_inf.txt";
		String output_file = "C:\\Users\\Anurag\\Desktop\\NLP\\Influential_tweets\\celeb_tweets19.txt";
		//createDataSet(input_file,output_file);
		createDataSet2(input_file1,output_file);
	}
	
	public static void createDataSet(String input_file, String output_file){
		try{
			File input = new File(input_file);
			File output2 = new File("C:\\Users\\Anurag\\Desktop\\NLP\\celeb_tweet_name.txt");
			File output = new File(output_file);
			FileWriter fw = new FileWriter(output);
			FileWriter fw2 = new FileWriter(output2);
			BufferedReader br = new BufferedReader(new FileReader(input));
			TweetsExtraction tweet_exac = new TweetsExtraction();
			String temp;
			while((temp =br.readLine())!=null){
				String temp2[] = temp.split("\\s+");
				int count =0;
				for(String s1 : temp2 ){
					//System.out.println(s1);
					if(count ==1){
						if(!(s1.equals("False") || s1.equals("None") || s1.equals("True"))){
							System.out.println(s1);
							fw2.write(s1+"\n");
							ArrayList<Status> tweets_list = new ArrayList<Status>();
							tweets_list =tweet_exac.generateTweets(s1);
							for(Status s : tweets_list){
								if(!s.isRetweet()){
									fw.write(s.getText());
									fw.write("\n");
									
								}
								
							}
							
							count =0;
						}
						else{
							count =0;
						}
						
					}
					if(s1.contains("gif") || s1.contains("png") || s1.contains("jpg"))
						count = 1;
					
				}
			
			}
			fw2.close();
			fw.close();
			
		}	
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void createDataSet2(String input_file,String output_file){
		try{
			File input = new File(input_file);
			File output = new File(output_file);
			Scanner s = new Scanner(input);
			FileWriter fw = new FileWriter(output);
			TweetsExtraction tweet_exac = new TweetsExtraction();
			int acc_count = 0;
			while(s.hasNextLine()){
				if(acc_count < 50){
					String temp = s.nextLine();
					String temp2[] = temp.split("\\s+");
					System.out.println(temp2[0]);
					ArrayList<Status> tweets_list = new ArrayList<Status>();
					
					
					tweets_list =tweet_exac.generateTweets(temp2[0]);
					if(tweets_list == null){
						continue;
					}
					for(Status st : tweets_list){
						if(!st.isRetweet()){
							fw.write("@"+st.getUser().getScreenName()+" :-"+st.getText());
							fw.write("\n");
							
						}
					
					}
				acc_count++;
				}
				else{
					break;
				}
				
			}
			fw.close();
		}	
			
		catch(Exception e){
			e.printStackTrace();
			
		}
		
	}
	
}
