package NLPProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import classifier.LanguageModel;

public class FVCombinationTest {
	public static String input_map_inf_file = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps_inf";
	public static String input_map_non_inf_file = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps_non_inf";
	public static String testing_folder_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_inf";
	public static String testing_folder_non_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_ninf";
	public static String hashtag_file = "C:\\Users\\Anurag\\Desktop\\NLP\\HashTag_count.csv";
	
	public static HashMap<String, Integer> trained_Inf_Map = new HashMap<String, Integer>(); 
	public static HashMap<String, Integer> trained_Non_Inf_Map = new HashMap<String, Integer>();
	
	public static HashMap<String, Integer> testing_Inf_Map = new HashMap<String, Integer>(); 
	public static HashMap<String, Integer> testing_Non_Inf_Map = new HashMap<String, Integer>();
	
	public static void main(String args[]) throws UnsupportedEncodingException, IOException{
		File inf_input_folder = new File(input_map_inf_file);
		File non_inf_input_folder = new File(input_map_non_inf_file);
		
		File [] inf_list_files = inf_input_folder.listFiles();
		for(File f : inf_list_files){
			System.out.println(f.getAbsolutePath());
			Scanner s = new Scanner(f);
			while(s.hasNextLine()){
				String temp = s.nextLine();
				System.out.println(temp);
				String str[] = temp.split("~#");
				System.out.println(str[0]+" "+ str[1]);
				trained_Inf_Map.put(str[0], Integer.parseInt(str[1]));
			}
			
		}
		
		File [] non_inf_list_files = non_inf_input_folder.listFiles();
		for(File f : non_inf_list_files){
			Scanner s2 = new Scanner(f);
			while(s2.hasNextLine()){
				String str[] = s2.nextLine().split("~#");
				trained_Non_Inf_Map.put(str[0], Integer.parseInt(str[1]));
			}
			
		}
		
		File folder = new File(testing_folder_non_inf);
		File[] listOfFiles = folder.listFiles();
		int correct_results =0, totalresults = listOfFiles.length;
		for(File testfile:listOfFiles) {
			
			if(classify_unigram(testfile,trained_Inf_Map,trained_Non_Inf_Map,testing_folder_non_inf,true))
					correct_results++;		 
			}
		
		System.out.println("Total results: "+ totalresults + " Correct results: " + correct_results);
	
		
	}
	
	public static boolean classify_unigram(File file, Map<String,Integer> pos_unigram_map, Map<String,Integer> neg_unigram_map,String Training_set_type,boolean stop_words) throws UnsupportedEncodingException, IOException{
        HashMap<String,Integer> test_unigram_map = new HashMap<String,Integer>();
        HashMap<String,Integer> test_unigram_map1 = new HashMap<String,Integer>();
        HashMap<String,Integer> test_unigram_map2 = new HashMap<String,Integer>();
        HashMap<String,Integer> test_unigram_map3 = new HashMap<String,Integer>();
        HashMap<String,Integer> test_unigram_map4 = new HashMap<String,Integer>();
        HashMap<String,Integer> test_unigram_map5 = new HashMap<String,Integer>();
        // Unigrams//
        LanguageModel.getUnigramMap(file,test_unigram_map1,stop_words);
        
        //
        // Functional Words///
        
        for(String str : pos_unigram_map.keySet() ){
        	test_unigram_map2.put(str, 0);
        }
        test_unigram_map2 = (HashMap<String, Integer>) ExtractFunctionalFeatures.populateMap(file.getAbsolutePath(),test_unigram_map2);
        
        
        // Tags Extraction
        int num_pos_emoticons_test = 0;
        int num_neg_emoticons_test = 0;
        int num_slang_test = 0;
        int num_excl_test = 0;
        
        num_pos_emoticons_test = Tags_Emoticons_Extract.countEmoticons(file.getAbsolutePath(),Tags_Emoticons_Extract.pos_emoticon_set,num_pos_emoticons_test);
        num_neg_emoticons_test = Tags_Emoticons_Extract.countEmoticons(file.getAbsolutePath(),Tags_Emoticons_Extract.neg_emoticon_set,num_neg_emoticons_test);
        num_slang_test = Tags_Emoticons_Extract.countSlangs(file.getAbsolutePath(), Tags_Emoticons_Extract.slang_set, num_slang_test);
        num_excl_test = Tags_Emoticons_Extract.countExclamations(file.getAbsolutePath(), num_excl_test);
        System.out.println(num_pos_emoticons_test+", "+num_neg_emoticons_test+", "+num_slang_test+", "+num_excl_test);
		test_unigram_map3.put("Pos_Emoticons",num_pos_emoticons_test);
		test_unigram_map3.put("Neg_Emoticons",num_neg_emoticons_test);
		test_unigram_map3.put("Slang",num_slang_test);
		test_unigram_map3.put("Exclaimation",num_excl_test);
        
		////
		
		/// Hash Tags
		
		int arr[] = new int[2];
		
		arr = HashtagExtraction.populateTagFeatures(file.getAbsolutePath(), hashtag_file);
		test_unigram_map4.put("TagPerTweet", arr[0]);
		test_unigram_map4.put("PopPerTag", arr[1]);
		
		
		//POS tagging
		POSExtraction parser = new POSExtraction();
		test_unigram_map5 = POSExtraction.populate_postagmap2(parser,file, (HashMap<String, Integer>) test_unigram_map5);
		
		
		
		//combine maps
		test_unigram_map.putAll(test_unigram_map1);
		test_unigram_map.putAll(test_unigram_map2);
		test_unigram_map.putAll(test_unigram_map3);
		test_unigram_map.putAll(test_unigram_map4);
		test_unigram_map.putAll(test_unigram_map5);
		
		//
        
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
		
	
	
	
}
