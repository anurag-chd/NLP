package NLPProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import twitter4j.Status;
import classifier.LanguageModel;

public class Tags_Emoticons_Extract {
	static HashSet<String> pos_emoticon_set = new HashSet<String>();
	static HashSet<String> neg_emoticon_set = new HashSet<String>();
	static HashSet<String> slang_set = new HashSet<String>();
	
	
	static int num_pos_emoticons_inf =0;
	static int num_neg_emoticons_inf =0;
	static int num_pos_emoticons_ninf =0;
	static int num_neg_emoticons_ninf =0;
	static int num_slang_inf =0;
	static int num_slang_ninf =0;
	static int num_excl_inf =0;
	static int num_excl_ninf =0;
	

	static int num_pos_emoticons_test =0;
	static int num_neg_emoticons_test =0;
	static int num_slang_test =0;
	static int num_excl_test =0;
	
	static String posEmotion_file = "C:\\Users\\Anurag\\Desktop\\NLP\\positiveEmoticons.txt";
	static String negEmotion_file = "C:\\Users\\Anurag\\Desktop\\NLP\\negativeEmoticon.txt";
	static String slangs_file = "C:\\Users\\Anurag\\Desktop\\NLP\\InternetSlangs.txt";
	public static String input_tweet_file_non_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\Input_twitter_name_non_inf.txt";
	public static String input_tweet_file_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\Input_twitter_name_inf.txt";
	public static Map<String,Integer> fV_inf_map = new HashMap<String,Integer>();
	public static Map<String,Integer> fV_ninf_map = new HashMap<String,Integer>();
	
	public static String input_folder_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\Influential_tweets"; 
	public static String input_folder_non_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\non_influential_tweets";
	public static String testing_folder_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_inf";
	public static String testing_folder_ninf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_ninf";
	
	public static String output_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\Tags_inf_map.txt";
	public static String output_non_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\Tags_non_inf_map.txt";
	
	public static void main(String args[]) throws IOException{
		populateSet(posEmotion_file,pos_emoticon_set);
		
		populateSet(negEmotion_file,neg_emoticon_set);
		
		populateSlangSet(slangs_file,slang_set);
		
		
		
		
		// count num_emoticons
		num_pos_emoticons_inf = countEmoticons(input_folder_inf,pos_emoticon_set,num_pos_emoticons_inf);
		num_neg_emoticons_inf = countEmoticons(input_folder_inf,neg_emoticon_set,num_neg_emoticons_inf);
		num_pos_emoticons_ninf = countEmoticons(input_folder_non_inf,pos_emoticon_set,num_pos_emoticons_ninf);
		num_neg_emoticons_ninf = countEmoticons(input_folder_non_inf,neg_emoticon_set,num_neg_emoticons_ninf);
		
		
		// count num_emoticons
		num_slang_inf = countSlangs(input_folder_inf,slang_set,num_slang_inf);
		num_slang_ninf = countSlangs(input_folder_non_inf,slang_set,num_slang_ninf);
		
		// count exclamation
		num_excl_inf = countExclamations(input_folder_inf,num_excl_inf);
		num_excl_ninf = countExclamations(input_folder_non_inf,num_excl_ninf);
		
		// print;
		poulateFeatureVector(fV_inf_map,true);
		poulateFeatureVector(fV_ninf_map,false);
		// Writing mpas in file
				//inf map
				File f = new File(output_inf_map);
				FileWriter out = new FileWriter(f);
				for(String s : fV_inf_map.keySet()){
					out.write(s+"~#"+fV_inf_map.get(s)+"\n");
				}
				out.close();
				
				// Non inf file
				
				File f2 = new File(output_non_inf_map);
				FileWriter out2 = new FileWriter(f2);
				for(String s : fV_ninf_map.keySet()){
					out2.write(s+"~#"+fV_ninf_map.get(s)+"\n");
				}
				out2.close();
				
				
				
				/////////////////////////////////////
		//// testing data set
		//createTestingSet(input_tweet_file_non_inf,testing_folder_ninf);
		
		
		//po
		
		
		File folder = new File(testing_folder_inf);
		File[] listOfFiles = folder.listFiles();
		int correct_results =0, totalresults = listOfFiles.length;
		for(File testfile:listOfFiles) {
			
			if(classify_unigram(testfile,fV_inf_map,fV_ninf_map,testing_folder_inf,true))
					correct_results++;		 
			}
		
		System.out.println("Total results: "+ totalresults + " Correct results: " + correct_results);
	
		
		
		
		
		
	}
	
	public static boolean classify_unigram(File file, Map<String,Integer> pos_unigram_map, Map<String,Integer> neg_unigram_map,String Training_set_type,boolean stop_words) throws FileNotFoundException{
        HashMap<String,Integer> test_unigram_map = new HashMap<String,Integer>();
        
        num_pos_emoticons_test = countEmoticons(file.getAbsolutePath(),pos_emoticon_set,num_pos_emoticons_test);
        num_neg_emoticons_test = countEmoticons(file.getAbsolutePath(),neg_emoticon_set,num_neg_emoticons_test);
        num_slang_test = countSlangs(file.getAbsolutePath(), slang_set, num_slang_test);
        num_excl_test = countExclamations(file.getAbsolutePath(), num_excl_test);
        System.out.println(num_pos_emoticons_test+", "+num_neg_emoticons_test+", "+num_slang_test+", "+num_excl_test);
		test_unigram_map.put("Pos_Emoticons",num_pos_emoticons_test);
		test_unigram_map.put("Neg_Emoticons",num_neg_emoticons_test);
		test_unigram_map.put("Slang",num_slang_test);
		test_unigram_map.put("Exclaimation",num_excl_test);
        
        
            
        double total_sum_inf =0;
        for(String str : pos_unigram_map.keySet()){
        	total_sum_inf = total_sum_inf + pos_unigram_map.get(str);
        }
        System.out.println("Total sum influential  "+total_sum_inf);
        
  
        double total_sum_ninf =0;
        for(String str : neg_unigram_map.keySet()){
        	total_sum_ninf = total_sum_ninf + neg_unigram_map.get(str);
        }
        System.out.println("Total sum non influential  "+total_sum_ninf);
        
        
        double inf_sent = Math.log( total_sum_inf/ (total_sum_inf + total_sum_ninf) )  +  getSentiment(test_unigram_map,pos_unigram_map);
        double ninf_sent = Math.log( total_sum_ninf/ (total_sum_inf + total_sum_ninf) ) + getSentiment(test_unigram_map,neg_unigram_map);
        
        System.out.println("Influential sent "+inf_sent);
        System.out.println("Non Influential sent "+ninf_sent);
        
        
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
		
	
	
	public static void poulateFeatureVector(Map<String,Integer> map,boolean infl){
		if(infl){
			map.put("Pos_Emoticons",num_pos_emoticons_inf);
			map.put("Neg_Emoticons",num_neg_emoticons_inf);
			map.put("Slang",num_slang_inf);
			map.put("Exclaimation",num_excl_inf);
		}
		else{
			map.put("Pos_Emoticons",num_pos_emoticons_ninf);
			map.put("Neg_Emoticons",num_neg_emoticons_ninf);
			map.put("Slang",num_slang_ninf);
			map.put("Exclaimation",num_excl_ninf);
		}
		
	}
	
	public static  void run() throws FileNotFoundException{
        populateSet(posEmotion_file,pos_emoticon_set);
		
		populateSet(negEmotion_file,neg_emoticon_set);
		
		populateSlangSet(slangs_file,slang_set);
		
		
		
		
		// count num_emoticons
		countEmoticons(input_folder_inf,pos_emoticon_set,num_pos_emoticons_inf);
		countEmoticons(input_folder_inf,neg_emoticon_set,num_neg_emoticons_inf);
		countEmoticons(input_folder_non_inf,pos_emoticon_set,num_pos_emoticons_ninf);
		countEmoticons(input_folder_non_inf,neg_emoticon_set,num_neg_emoticons_ninf);
		
		
		// count num_emoticons
		countSlangs(input_folder_inf,slang_set,num_slang_inf);
		countSlangs(input_folder_non_inf,slang_set,num_slang_ninf);
		
		// count exclamation
		countExclamations(input_folder_inf,num_excl_inf);
		countExclamations(input_folder_non_inf,num_excl_ninf);
	
		
	}
	
	
	
	public static void populateSet(String inputFile,HashSet<String> set) {
		try{
			
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			String temp;
			while((temp=br.readLine())!=null){
				if(!temp.equals(" ")){
					set.add(temp);
				}
								
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void populateSlangSet(String inputfile,HashSet<String> slang_set) throws FileNotFoundException{
		Scanner s = new Scanner(new File(inputfile));
		while(s.hasNextLine()){
			String temp = s.next();
			String temp2[] = temp.split("\\s+");
			slang_set.add(temp2[0]);
		}
		
	}
	
	public static int countEmoticons(String inputfolder,HashSet<String> set,int num) throws FileNotFoundException{
		File folder = new File(inputfolder);
		if(folder.isDirectory()){
			for(File f : folder.listFiles()){
				Scanner s = new Scanner(f);
				s.useDelimiter("\\s+");
				while(s.hasNext()){
					String temp = s.next();
					
					if(set.contains(temp)){
						num++;
					}
					
				}
			}
			return num;
		}
		else{
			
			
			Scanner s = new Scanner(folder);
			s.useDelimiter("\\s+");
			while(s.hasNext()){
				String temp = s.next();
				
				if(set.contains(temp)){
					num++;
				}
				
			}
			System.out.println(num);
		
		}
		return num;
		
	}
	
	public static void countNegEmoticons(String inputfolder,HashSet<String> set,boolean inf) throws FileNotFoundException{
		File folder = new File(inputfolder);
		if(folder.isDirectory()){
			
		}
		for(File f : folder.listFiles()){
			Scanner s = new Scanner(f);
			s.useDelimiter("\\s+");
			while(s.hasNext()){
				String temp = s.next();
				
				if(set.contains(temp)){
					if(inf == true){
						num_neg_emoticons_inf++;
					}
					else{
						num_neg_emoticons_ninf++;
					}
				}
				
			}
		}
	}
	public static int countExclamations(String inputfolder, int num) throws FileNotFoundException{
		File folder = new File(inputfolder);
		if(folder.isDirectory()){
			for(File f : folder.listFiles()){
				Scanner s = new Scanner(f);
				s.useDelimiter("\\s+");
				while(s.hasNext()){
					String temp = s.next();
					if(temp.contains("!")){
						num++;
					}
					
				}
				
			}
			return num;
		}
		else{
			Scanner s = new Scanner(folder);
			s.useDelimiter("\\s+");
			while(s.hasNext()){
				String temp = s.next();
				if(temp.contains("!")){
					num++;
				}
			}
			return num;
			
		}
		
	}

	
	
	
	
	
	public static int countSlangs(String inputfolder,HashSet<String> slang_set, int num) throws FileNotFoundException{
		File folder = new File(inputfolder);
		if(folder.isDirectory()){
			for(File f : folder.listFiles()){
				Scanner s = new Scanner(f);
				s.useDelimiter("\\s+");
				while(s.hasNext()){
					String temp = s.next();
					if(slang_set.contains(temp)){
						num++;
					}
					
				}
			}
			return num;
		}
		else{
			Scanner s = new Scanner(folder);
			s.useDelimiter("\\s+");
			while(s.hasNext()){
				String temp = s.next();
				if(slang_set.contains(temp)){
					num++;
				}
				
			}
			
		
		}
		return num;
		
	}
	
	public static void createTestingSet(String input_file, String output_folder){
		try{
			File in = new File(input_file);
			
			
			Scanner s = new Scanner(in);
			int test_acc = 0;// 15 accounts
			FileWriter fw = null;
			while(s.hasNextLine()){
				if(test_acc<15){
					String temp = s.nextLine();
					String temp2[] = temp.split("\\s+");
					fw = new FileWriter(new File(output_folder+"\\"+temp2[0]+".txt"));
					ArrayList<Status> tweets_list = new ArrayList<Status>();
					tweets_list = TweetsExtraction.generateTweets(temp2[0]);
					if(tweets_list == null){
						continue;
					}
					else{
						for(Status st : tweets_list){
							if(!st.isRetweet()){
								fw.write("@"+st.getUser().getScreenName()+" :-"+st.getText());
								fw.write("\n");
							}
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

	
	
}
