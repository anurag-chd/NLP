package NLPProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class HashtagExtraction {
	
	public static String input_folder_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\Influential_tweets"; 
	public static String input_folder_non_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\non_influential_tweets";
	public static String testing_folder_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_inf";
	public static String hashtag_file = "C:\\Users\\Anurag\\Desktop\\NLP\\HashTag_count.csv";
	public static String output_file_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\dataset_tweets\\output_inf.txt";
	public static String output_file_non_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\dataset_tweets\\output_non_inf.txt";
	public static String output_inf_file = "C:\\Users\\Anurag\\Desktop\\NLP\\dataset_tweets\\output_celeb_inf.txt";
	public static String output_non_inf_file = "C:\\Users\\Anurag\\Desktop\\NLP\\dataset_tweets\\output_celeb_non_inf.txt";
	
	public static String output_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\HashTags_inf_map.txt";
	public static String output_non_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\HashTags_non_inf_map.txt";
	
	public static void populate_tagmap(File[] TrainingFiles,Map<String,Long> hashtag_count,Map<String,Long> hashtagcount_map,Map<String,Long> hashtagpop_map,String file_str) throws FileNotFoundException, UnsupportedEncodingException{
		
		PrintWriter writer_temp = new PrintWriter(file_str, "UTF-8");

		
		 
		for(File file:TrainingFiles){
			
			Scanner input = null;
			try {
				input = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	
			while(input.hasNextLine()) {
			    String nextLine = input.nextLine();
			    //System.out.println( nextLine );
			    String[] s = nextLine.split("\\:-",2);
			    if(s.length == 2){
			    	String username = s[0].trim();
			    	String tweet = s[1];
			    
			    
			    Pattern p = Pattern.compile("#\\w*");
			    Matcher m = p.matcher(tweet);
			    while(m.find()){
			       //System.out.println(m.group().substring(1).trim().toLowerCase());
			       if(hashtagpop_map.containsKey(username)){
			    	   Long hashpop = hashtagpop_map.get(username);
			    	   hashpop = hashpop + (hashtag_count.get(m.group().substring(1).trim().toLowerCase()) == null?0:hashtag_count.get(m.group().substring(1).trim().toLowerCase()));
			    	   hashtagpop_map.put(username, hashpop);
			    	   
			    	   Long hashcount = hashtagcount_map.get(username);
			    	   hashcount = hashcount + 1;			    	   
			    	   hashtagcount_map.put(username, hashcount);
			       }
			       else {
			    	   Long hashpop = new Long(hashtag_count.get(m.group().substring(1).trim().toLowerCase()) == null?0:hashtag_count.get(m.group().substring(1).trim().toLowerCase()));			    	   
			    	   hashtagpop_map.put(username, hashpop);
			    	   
			    	   Long hashcount = 1L;
			    	   hashtagcount_map.put(username, hashcount);
			    	   writer_temp.println(username);
			       }
			    }			    
			 
			   } 
			}			
			
		}
		
		writer_temp.close();
		return;
		
	}
	
	public static void populate_tagmap2(File TestingFile,Map<String,Long> hashtag_count,Map<String,Long> hashtagcount_map,Map<String,Long> hashtagpop_map) throws FileNotFoundException, UnsupportedEncodingException{
		Scanner input = null;
		try {
			input = new Scanner(TestingFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		while(input.hasNextLine()) {
			String nextLine = input.nextLine();
			//System.out.println( nextLine );
			String[] s = nextLine.split("\\:-",2);
			if(s.length == 2){
				String username = s[0].trim();
				String tweet = s[1];
				Pattern p = Pattern.compile("#\\w*");
				Matcher m = p.matcher(tweet);
				while(m.find()){
					//System.out.println(m.group().substring(1).trim().toLowerCase());
					if(hashtagpop_map.containsKey(username)){
						Long hashpop = hashtagpop_map.get(username);
						hashpop = hashpop + (hashtag_count.get(m.group().substring(1).trim().toLowerCase()) == null?0:hashtag_count.get(m.group().substring(1).trim().toLowerCase()));
						hashtagpop_map.put(username, hashpop);
						Long hashcount = hashtagcount_map.get(username);
						hashcount = hashcount + 1;			    	   
						hashtagcount_map.put(username, hashcount);
					}
					else {
						Long hashpop = new Long(hashtag_count.get(m.group().substring(1).trim().toLowerCase()) == null?0:hashtag_count.get(m.group().substring(1).trim().toLowerCase()));			    	   
						hashtagpop_map.put(username, hashpop);
						Long hashcount = 1L;
						hashtagcount_map.put(username, hashcount);
	 	   
			       }
			   }			    
			}
		}			
		return;
	}
	
	public static void main(String[] args) throws IOException {
		File folder_inf = new File(input_folder_inf);
		File folder_non_inf = new File(input_folder_non_inf);
		File[] TrainingFiles_inf = folder_inf.listFiles();
		File[] TrainingFiles_non_inf = folder_non_inf.listFiles();

		File tag_file = new File(hashtag_file);
		Map<String,Long> hashtag_count = new HashMap<String,Long> ();
		Scanner input_tag = null;
		try {
			input_tag = new Scanner(tag_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while(input_tag.hasNextLine()) {
			String nextLine = input_tag.nextLine();
		    String[] s = nextLine.split(",",2);
		    hashtag_count.put(s[0].trim(), new Long(s[1].trim()));		    
		}
		
		Map<String,Long> hashtagcount_map_inf = new HashMap<String,Long>();		
		Map<String,Long> hashtagpop_map_inf = new HashMap<String,Long>();
		Map<String,Long> hashtagcount_map_non_inf = new HashMap<String,Long>();		
		Map<String,Long> hashtagpop_map_non_inf = new HashMap<String,Long>();
		System.out.println("Hiii");
		populate_tagmap(TrainingFiles_inf,hashtag_count,hashtagcount_map_inf,hashtagpop_map_inf,output_inf_file);
		populate_tagmap(TrainingFiles_non_inf,hashtag_count,hashtagcount_map_non_inf,hashtagpop_map_non_inf,output_non_inf_file);
		
		System.out.println("Hiii");
		PrintWriter writer = new PrintWriter(output_file_inf, "UTF-8");
		for(String user:hashtagpop_map_inf.keySet()){
			writer.println(user + " : " + hashtagcount_map_inf.get(user) + " , " + hashtagpop_map_inf.get(user));
		}
		writer.close();
		System.out.println("Hiii");
		
		PrintWriter writer1 = new PrintWriter(output_file_non_inf, "UTF-8");
		for(String user:hashtagpop_map_non_inf.keySet()){
			writer1.println(user + " : " + hashtagcount_map_non_inf.get(user) + " , " + hashtagpop_map_non_inf.get(user));
		}
		writer1.close();
		
		// avg calac
		
		float tag_tweet_avg_inf = 0;
		float tag_pop_avg_inf = 0;
		float tag_tweet_avg_non_inf = 0;
		float tag_pop_avg_non_inf = 0;
		long sum =0;
		int count =0;
		for(String s: hashtagcount_map_inf.keySet()){
			long temp =hashtagcount_map_inf.get(s);
			if(count <48 && temp >50){
				sum = sum+temp;
				count ++;
			}
			else if(temp <=50){
				
			}
			else{
				break;
			}
			
			
			//System.out.println(sum);
		}
		System.out.println(sum);
		//tag_tweet_avg_inf =(float)sum/(300*(float)hashtagcount_map_inf.size());
		tag_tweet_avg_inf =(float)sum/*(300*(float)48)*/;
		System.out.println(hashtagcount_map_inf.size()+"   "+ hashtagpop_map_inf.size());
		
		sum =0;
		
		for(String s: hashtagcount_map_non_inf.keySet()){
			sum = sum+hashtagcount_map_non_inf.get(s);
		}
		System.out.println(sum);
		tag_tweet_avg_non_inf =(float)sum/*(300*hashtagcount_map_non_inf.size())*/;
		System.out.println(hashtagcount_map_non_inf.size()+"   "+ hashtagpop_map_non_inf.size());
		
		sum =0;
		count =0;
		for(String s: hashtagcount_map_inf.keySet()){
			long temp_pop= hashtagpop_map_inf.get(s);
		
			if(count <48 && temp_pop >5000){
				sum = sum+(temp_pop/hashtagcount_map_inf.get(s));
				count++;
			}
			else if(temp_pop<=100000){
				
			}
			else{
				break;
			}
		}
		//tag_pop_avg_inf = sum/hashtagcount_map_inf.size() ;
		tag_pop_avg_inf = sum/48 ;
		
		
		
		sum =0;
		for(String s: hashtagcount_map_non_inf.keySet()){
			long temp = hashtagpop_map_non_inf.get(s);
			if(temp>200000){
				temp = 10000;
			}
			
			sum = sum+(temp/hashtagcount_map_non_inf.get(s));
		}
		tag_pop_avg_non_inf = sum/hashtagcount_map_non_inf.size() ;
		
		System.out.println("Influential tag per tweet avg: "+tag_tweet_avg_inf+" popularity per tag: "+tag_pop_avg_inf);
		System.out.println("Non Influential tag per tweet avg: "+tag_tweet_avg_non_inf+" popularity per tag: "+tag_pop_avg_non_inf);
		
		//Populate map
		
		File map_inf_file  = new File(output_inf_map);
		File map_non_inf_file  = new File(output_non_inf_map);
	
		FileWriter out_inf  = new FileWriter(map_inf_file);
		out_inf.write("TagPerTweet"+"~#"+(int)tag_tweet_avg_inf+"\n");
		out_inf.write("PopPerTag"+"~#"+(int)tag_pop_avg_inf+"\n");
		out_inf.close();
		
		FileWriter out_inf1  = new FileWriter(map_non_inf_file);
		out_inf1.write("TagPerTweet"+"~#"+(int)tag_tweet_avg_non_inf+"\n");
		out_inf1.write("PopPerTag"+"~#"+(int)tag_pop_avg_non_inf+"\n");
		out_inf1.close();
		
	}
	
	public static int[] populateTagFeatures(String input_file,String hash_file) throws IOException, UnsupportedEncodingException{
		File input = new File(input_file);
		File tag_file = new File(hash_file);
		
		Map<String,Long> hashtag_count = new HashMap<String,Long> ();
		Scanner input_tag = null;
		try {
			input_tag = new Scanner(tag_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while(input_tag.hasNextLine()) {
			String nextLine = input_tag.nextLine();
		    String[] s = nextLine.split(",",2);
		    hashtag_count.put(s[0].trim(), new Long(s[1].trim()));		    
		}
		
		Map<String,Long> hashtagcount_map = new HashMap<String,Long>();		
		Map<String,Long> hashtagpop_map = new HashMap<String,Long>();
		
		
		populate_tagmap2(input,hashtag_count,hashtagcount_map,hashtagpop_map);
		
		
		// avg calac
		
		float tag_tweet_avg_inf = 0;
		float tag_pop_avg_inf = 0;
		float tag_tweet_avg_non_inf = 0;
		float tag_pop_avg_non_inf = 0;
		long sum =0;
		int count =0;
		for(String s: hashtagcount_map.keySet()){
			long temp =hashtagcount_map.get(s);
			
				sum = sum+temp;
			
		}
			
			
			
			//System.out.println(sum);
		
		System.out.println(sum);
		//tag_tweet_avg_inf =(float)sum/(300*(float)hashtagcount_map_inf.size());
		tag_tweet_avg_inf =(float)sum/*(300*(float)48)*/;
		System.out.println(hashtagcount_map.size()+"   "+ hashtagpop_map.size());
		
		sum =0;
		
		
		
		
		for(String s: hashtagcount_map.keySet()){
			long temp_pop= hashtagpop_map.get(s);
		
			sum = sum+(temp_pop/hashtagcount_map.get(s));
			}
		//tag_pop_avg_inf = sum/hashtagcount_map_inf.size() ;
		tag_pop_avg_inf = sum/48 ;
		
		
		
		sum =0;
		int result_arr[] = new int[2];
		result_arr[0] = (int)(tag_tweet_avg_inf);
		result_arr[1] = (int)(tag_pop_avg_inf);
		return result_arr;
		
		
	}

}
