package NLPProject;
import classifier.*;

import java.util.*;
import java.io.*;

import twitter4j.Status;
import twitter4j.TwitterException;
public class FVCreation {
	public static String input_folder_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\Influential_tweets"; 
	public static String input_folder_non_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\non_influential_tweets";
	public static String testing_folder_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_inf";
	public static String testing_folder_non_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_ninf";
	public static String input_tweet_file = "C:\\Users\\Anurag\\Desktop\\NLP\\Input_twitter_name.txt";
	public static String output_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\Fv_inf_map.txt";
	public static String output_non_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\Fv_non_inf_map.txt";
	public static String input_file1 = "C:\\Users\\Anurag\\Desktop\\NLP\\Twiiter_name_Treust_rank\\Temp_twitter_name.txt";
	//C:\Users\Anurag\Desktop\NLP\Twiiter_name_Treust_rank\Temp_twitter_name.txt
	public static void main (String args[]) throws TwitterException, IOException{
		/// test data creation	
		//createTestingSet(input_file1,testing_folder_non_inf);
		///
		
		
		
		
		
		/// Unigram testing////
		
		/// populating training data////
		
		File folder_inf = new File(input_folder_inf);
		File folder_non_inf = new File(input_folder_non_inf);
		Map<String,Integer> unigram_count_inf =  new HashMap<String,Integer>();
		Map<String,Integer> unigram_count_non_inf =  new HashMap<String,Integer>();
		File[] TrainingFiles_inf = folder_inf.listFiles();
		File[] TrainingFiles_non_inf = folder_non_inf.listFiles();
		LanguageModel.set_stopwords();
		//File[] fileTrainingSetpos = LanguageModel.getFiles(folder_pos,trainingSet);
		LanguageModel.populate_unigram_map(folder_inf,TrainingFiles_inf,unigram_count_inf,true);
		LanguageModel.populate_unigram_map(folder_non_inf,TrainingFiles_non_inf,unigram_count_non_inf,true);
		
		//Tags_Emoticons_Extract.run();
		
		// Writing mpas in file
		//inf map
		File f = new File(output_inf_map);
		FileWriter out = new FileWriter(f);
		for(String s : unigram_count_inf.keySet()){
			out.write(s+"~#"+unigram_count_inf.get(s)+"\n");
		}
		out.close();
		
		// Non inf file
		
		File f2 = new File(output_non_inf_map);
		FileWriter out2 = new FileWriter(f2);
		for(String s : unigram_count_non_inf.keySet()){
			out2.write(s+"~#"+unigram_count_non_inf.get(s)+"\n");
		}
		out2.close();
		
		
		
		/////////////////////////////////////
		
		// populating the test data//
		
		
		
		File folder = new File(testing_folder_inf);
		File[] listOfFiles = folder.listFiles();
		int correct_results =0, totalresults = listOfFiles.length;
		for(File testfile:listOfFiles) {
			File inter = rmName(testfile.getAbsolutePath());
			if(classify_unigram(inter,unigram_count_inf,unigram_count_non_inf,testing_folder_non_inf,true))
					correct_results++;		 
			}
		
		System.out.println("Total results: "+ totalresults + " Correct results: " + correct_results);
		
	}
	
	
	
	public static void createTestingSet(String input_file, String output_folder){
		try{
			File in = new File(input_file);
			
			
			Scanner s = new Scanner(in);
			int test_acc = 0;// 15 accounts
			FileWriter fw = null;
			while(s.hasNextLine()){
				if(test_acc<50){
					String temp = s.nextLine();
					String temp2[] = temp.split("\\s+");
					fw = new FileWriter(new File(output_folder+"\\"+temp2[0]+".txt"));
					ArrayList<Status> tweets_list = new ArrayList<Status>();
					tweets_list = TweetsExtraction.generateTweets(temp2[0]);
					for(Status st : tweets_list){
						if(!st.isRetweet()){
							fw.write("@"+st.getUser().getScreenName()+" :-"+st.getText());
							fw.write("\n");
						}
					}
					test_acc++;
				}
				else{
					break;
				}
				
			}
			fw.close();
			s.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	
	
	public static boolean classify_unigram(File file, Map<String,Integer> pos_unigram_map, Map<String,Integer> neg_unigram_map,String Training_set_type,boolean stop_words){
        HashMap<String,Integer> test_unigram_map = new HashMap<String,Integer>();
        LanguageModel.getUnigramMap(file,test_unigram_map,stop_words);
        
        double total_sum_inf =0;
        for(String str : pos_unigram_map.keySet()){
        	total_sum_inf = total_sum_inf + pos_unigram_map.get(str);
        }
        total_sum_inf = total_sum_inf + Tags_Emoticons_Extract.num_pos_emoticons_inf+Tags_Emoticons_Extract.num_neg_emoticons_inf+Tags_Emoticons_Extract.num_slang_inf+Tags_Emoticons_Extract.num_excl_inf;
  
        double total_sum_ninf =0;
        for(String str : neg_unigram_map.keySet()){
        	total_sum_ninf = total_sum_ninf + neg_unigram_map.get(str);
        }
        total_sum_ninf = total_sum_ninf + Tags_Emoticons_Extract.num_pos_emoticons_ninf+Tags_Emoticons_Extract.num_neg_emoticons_ninf+Tags_Emoticons_Extract.num_slang_ninf+Tags_Emoticons_Extract.num_excl_ninf;
        
        double inf_sent = Math.log( total_sum_inf/ (total_sum_inf + total_sum_ninf) )  +  getSentiment(test_unigram_map,pos_unigram_map);
        double ninf_sent = Math.log( total_sum_ninf/ (total_sum_inf + total_sum_ninf) ) + getSentiment(test_unigram_map,neg_unigram_map);
        
        /*
        
        System.out.println("Positive value " + pos_sent+ " " +Math.log( total_sum_pos/ (total_sum_pos + total_sum_neg) ) +
        		"\n" +"Negative value " + neg_sent + " " +Math.log( total_sum_neg/ (total_sum_pos + total_sum_neg) ));
        if(pos_sent > neg_sent) {
            System.out.println("Document is positive");
           
        }
       
        else{
            System.out.println("Document is negative");
        }
        
        */ 
        //System.out.println("Positive sentiment: " + pos_sent + "Negative sentiment: " + neg_sent);
        
        if(Training_set_type.equals(testing_folder_inf)){
        	return (inf_sent > ninf_sent);
        }
        else{
        	return (inf_sent < ninf_sent);
        }
        
       
    }
	
    public static double getSentiment(Map<String,Integer> test_gram_map,Map<String,Integer> gram_map){
        double result = 0;
        double total_sum =0,vocab_size=0;
        Set<String> pos_key_string = gram_map.keySet();
        for(String str : pos_key_string){
            total_sum = total_sum + gram_map.get(str);
            vocab_size++;
        }

        Set<String> key_string = test_gram_map.keySet();
        for(String str : key_string ){
            
                result = result + test_gram_map.get(str) *  Math.log(( ( gram_map.get(str)==null?0:gram_map.get(str) )  + 1) /(total_sum + vocab_size)) ;
                //System.out.println(str + " -- " + test_gram_map.get(str) + "  "+ Math.log(( ( gram_map.get(str)==null?0:gram_map.get(str) )  + 1) /(total_sum + vocab_size)) + " " + result);                    
        }
        
     return result;   
    }
	
	
	

	public static Map<String, Float> populateFeatures(Map<String, Float> map , String input_file){
		File in_folder = new File(input_file);
		File[] input_files = in_folder.listFiles();
		try{
			for(File f: input_files){
				File inter = rmName(f.getAbsolutePath());
				Scanner s = new Scanner(inter);
				s.useDelimiter("\\s+");
				while(s.hasNext()){
					String temp = s.next();
					if(map.containsKey(temp)){
						float i = map.get(temp);
						map.put(temp, ++i);
						
					}
				}
				
			}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	public static File rmName(String input_file){
		File input = new File(input_file);
		File output = new File("interm.txt");
		try{
			Scanner s = new Scanner(input);
			FileWriter fw = new FileWriter(output);
			while(s.hasNextLine()){
				String temp = s.nextLine();
				String temp2[] = temp.split("\\s+");
				for(int i = 0 ; i <temp2.length;i++){
					if(i ==1){
						if(temp2[i].contains(":-")){
							fw.write(temp2[i].substring(2)+" ");
						}
						else{
							fw.write(temp2[i]+" ");
						}
						
						
					}
					else if(i ==0){
						if(temp2[i].contains("@")){
							
						}
						else{
							fw.write(temp2[i]+" ");
						}
					}
					
					else if(i==temp2.length-1){
						if(temp2[i].contains("http")){
							
						}
						else{
							fw.write(temp2[i]+" ");
						}
						
					}
					else{
						fw.write(temp2[i]+" ");
					}
					
					
				}
				fw.write("\n");
			}
			fw.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return output;
		
	}
	
	
	
}


