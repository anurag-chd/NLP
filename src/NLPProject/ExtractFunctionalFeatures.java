package NLPProject;

import java.io.*;
import java.util.*;

public class ExtractFunctionalFeatures {
	public static String input_folder_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\Influential_tweets"; 
	public static String input_folder_non_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\non_influential_tweets";
	public static String testing_folder_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_inf";
	public static String functional_file = "C:\\Users\\Anurag\\Desktop\\NLP\\Functional_words.txt";
	public static String testing_folder_ninf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_ninf";
	
		public static String output_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\Functional_words_inf_map.txt";
		public static String output_non_inf_map = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps\\Functional_words_non_inf_map.txt";
	
	public static void main(String args[]) throws IOException{
		
		Map<String,Integer> inf_functional_map = new HashMap<String,Integer>();
		inf_functional_map = createMap(functional_file,inf_functional_map);
		Map<String,Integer> ninf_functional_map = new HashMap<String,Integer>();
		Map<String,Integer> test_inf_functional_map = new HashMap<String,Integer>();
		for(String str : inf_functional_map.keySet()){
			ninf_functional_map.put(str,0);
			test_inf_functional_map.put(str,0);
		}
			
		inf_functional_map =populateMap(input_folder_inf,inf_functional_map);
		ninf_functional_map = populateMap(input_folder_non_inf, ninf_functional_map);
		//test_inf_functional_map = populateMap(testing_folder_inf, test_inf_functional_map);
		for(String str : inf_functional_map.keySet()){
			System.out.println(str+" :   Influential count "+ inf_functional_map.get(str)+" Non Influential Count  "+ ninf_functional_map.get(str));
		}
		
		// Writing maps in file
		//inf map
		File f = new File(output_inf_map);
		FileWriter out = new FileWriter(f);
		for(String s : inf_functional_map.keySet()){
			out.write(s+"~#"+inf_functional_map.get(s)+"\n");
		}
		out.close();
		
		// Non inf file
		
		File f2 = new File(output_non_inf_map);
		FileWriter out2 = new FileWriter(f2);
		for(String s : ninf_functional_map.keySet()){
			out2.write(s+"~#"+ninf_functional_map.get(s)+"\n");
		}
		out2.close();
		
		
		
		/////////////////////////////////////
		
		// Influential test
		
		
		File folder = new File(testing_folder_inf);
		File[] listOfFiles = folder.listFiles();
		int correct_results =0, totalresults = listOfFiles.length;
		for(File testfile:listOfFiles) {
			
			if(classify_unigram(testfile,inf_functional_map,ninf_functional_map,testing_folder_inf,true))
					correct_results++;		 
			}
		
		System.out.println("Influential Total results: "+ totalresults + " Correct results: " + correct_results);
		
		
		// Non Influential test
		
		File folder2 = new File(testing_folder_ninf);
		File[] listOfFiles2 = folder2.listFiles();
		int correct_results2 =0, totalresults2 = listOfFiles2.length;
		for(File testfile:listOfFiles2) {
			
			if(classify_unigram(testfile,inf_functional_map,ninf_functional_map,testing_folder_inf,true))
					correct_results2++;		 
			}
		
		System.out.println("Non Influential Total results: "+ totalresults2 + " Correct results: " + correct_results2);
	
		
	}
	
	public static boolean classify_unigram(File file, Map<String,Integer> pos_unigram_map, Map<String,Integer> neg_unigram_map,String Training_set_type,boolean stop_words) throws FileNotFoundException{
        Map<String,Integer> test_unigram_map = new HashMap<String,Integer>();
        for(String str : pos_unigram_map.keySet() ){
        	test_unigram_map.put(str, 0);
        }
        test_unigram_map = populateMap(file.getAbsolutePath(),test_unigram_map);
        
            
        double total_sum_inf =0;
        for(String str : pos_unigram_map.keySet()){
        	total_sum_inf = total_sum_inf + pos_unigram_map.get(str);
        }
       // System.out.println("Total sum influential  "+total_sum_inf);
        
  
        double total_sum_ninf =0;
        for(String str : neg_unigram_map.keySet()){
        	total_sum_ninf = total_sum_ninf + neg_unigram_map.get(str);
        }
        //System.out.println("Total sum non influential  "+total_sum_ninf);
        
        
        double inf_sent = Math.log( total_sum_inf/ (total_sum_inf + total_sum_ninf) )  +  getSentiment(test_unigram_map,pos_unigram_map);
        double ninf_sent = Math.log( total_sum_ninf/ (total_sum_inf + total_sum_ninf) ) + getSentiment(test_unigram_map,neg_unigram_map);
        
       // System.out.println("Influential sent "+inf_sent);
        //System.out.println("Non Influential sent "+ninf_sent);
        
        
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
	
	public static Map<String,Integer> populateMap(String input_folder,Map<String,Integer> map) throws FileNotFoundException{
		File folder = new File(input_folder);
		if(folder.isDirectory()){
			File file_list[] = folder.listFiles();
			for(File f : file_list){
				Scanner s = new Scanner(f);
				s.useDelimiter("\\s+");
				while(s.hasNext()){
					String temp = s.next();
					if(map.containsKey(temp)){
						map.put(temp, map.get(temp)+1);
					}
				}
				
				
			}
		
		}
		else{
			Scanner s = new Scanner(folder);
			s.useDelimiter("\\s+");
			while(s.hasNext()){
				String temp = s.next();
				if(map.containsKey(temp)){
					map.put(temp, map.get(temp)+1);
				}
			}
		}
			
		return map;
	}
	
	public static Map<String,Integer> createMap(String input_file,Map<String,Integer> map ) throws FileNotFoundException{
		File f = new File(input_file);
		Scanner s = new Scanner(f);
		while(s.hasNextLine()){
			map.put(s.nextLine(),0);
		}
		return map;
	}
	
}
