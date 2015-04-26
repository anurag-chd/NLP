package classifier;
import NLPProject.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import NLPProject.FVCreation;

public class LanguageModel {
	
	public static final String POSITIVE_DOC_FOLDER = "C:\\Users\\Anurag\\workspace\\NLP\\src\\txt_sentoken\\pos";
	public static final String NEGATIVE_DOC_FOLDER = "C:\\Users\\Anurag\\workspace\\NLP\\src\\txt_sentoken\\neg";
	public static final String STOP_WORDS = "C:\\Users\\Anurag\\workspace\\NLP\\src\\resource\\StopWords.txt";
	public static final String OUTPUT_FILE = "C:\\Users\\Anurag\\workspace\\NLP\\src\\output\\";
	public static final int SET_SIZE = 200;
	public static Set<String> stopWords;
	
	static{
		
		set_stopwords();
	}
	
	public static void set_stopwords() {
		stopWords = new LinkedHashSet<String>();
        BufferedReader bur= null;
		try {
			bur = new BufferedReader(new FileReader(STOP_WORDS));
			for(String line;(line = bur.readLine()) != null;)
			   stopWords.add(line.trim());		
			bur.close();
        }
        catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getUnigramCount(File file, Map<String,Integer> total_unigrams){
		Map<String,Integer> curr_unigrams = new HashMap<String,Integer> ();
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// storing the unigrams		
		while(input.hasNext()) {
		    String nextLine = input.nextLine();
		    //System.out.println( nextLine );
		    for(String s: nextLine.split("\\W+")){
		    	if(!stopWords.contains(s))
		    		curr_unigrams.put(s,1);
		    }		    
		}
		
		for(String key:curr_unigrams.keySet()){
			if(total_unigrams.containsKey(key))
				total_unigrams.put(key,total_unigrams.get(key) + 1);
			else 
				total_unigrams.put(key,1);
		}
		


		input.close();
	}
	
	
	public static void getUnigramMap(File file, Map<String,Integer> list_unigrams,boolean stop_words){
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// storing the unigrams		
		while(input.hasNext()) {
		    String nextLine = input.nextLine();
		    //System.out.println( nextLine );
		    for(String s: nextLine.split("\\W+")){
		    	if(list_unigrams.containsKey(s))
		    		list_unigrams.put(s,list_unigrams.get(s) + 1);
		    	else
		    		if(stop_words && !stopWords.contains(s))
		    			list_unigrams.put(s,1);
		    		else if(stop_words && stopWords.contains(s))
		    		{
		    			//do nothing 
		    		}
		    		else
		    			list_unigrams.put(s,1);
		    }		    
		}
		
		
		/*
		for(String key:list_unigrams.keySet())
			if(list_unigrams.get(key) > 2)
			 System.out.print(key + "   ");
		
        */
		
		input.close();
	}
	
	
	public static void getBigramMap(File file, Map<String, Integer> bigram_map) {
        try{
            Scanner sc=new Scanner(file);
            sc.useDelimiter("\\W+");
            String s;
            String temp = sc.next();
            while(sc.hasNext())
            {
                if(temp == ""){
                    temp = sc.next();
                    continue;
                }
                s=sc.next();
                if(s.charAt(s.length()-1)== '.'){
                    s =s.substring(0, s.length()-1);
                    String s2 = temp+" "+s ;
                    temp ="";
                    if(bigram_map.containsKey(s2)){
                        int i = bigram_map.get(s2);
                        bigram_map.put(s2, ++i);
                    }
                    else{
                        bigram_map.put(s2, 1);
                    }
                    //System.out.println(s2);
                }
                else{
                    String s2 = temp+" "+s ;
                    temp = s;
                    if(bigram_map.containsKey(s2)){
                        int i = bigram_map.get(s2);
                        bigram_map.put(s2, ++i);
                    }
                    else{
                        bigram_map.put(s2, 1);
                    }
                   
                    //System.out.println(s2);
               
                }
            } 
            
            sc.close();
        }
        catch(FileNotFoundException e){
           
        }
         
    }
	
	
	public static void populate_unigram_count(File folder,File[] listOfFiles,
			Map<String,Integer> unigram_map){
		
		
		//File[] listOfFiles = folder.listFiles(file_filter(start,end));
		
		for(File file:listOfFiles){
			getUnigramCount(file,unigram_map);
		}
		
		/*		
		for(String key:unigram_map.keySet()) {
			if(unigram_map.get(key) != 1)
			System.out.println(key + "  " + unigram_map.get(key));
		}
		
		*/
		
	}
	
	public static void populate_unigram_map(File folder,File[] listOfFiles,
			Map<String,Integer> unigram_map,boolean stop_words){
		
		
		//File[] listOfFiles = folder.listFiles(file_filter(start,end));
		
		for(File file:listOfFiles){
			///Anurag Tweets Cleaning
			File interim = FVCreation.rmName(file.getAbsolutePath());
			////
			
			getUnigramMap(interim,unigram_map,stop_words);
		}
		
	}
	
	
	public static void populate_bigram_map(File folder,File[] listOfFiles,
			Map<String,Integer> bigram_map){


		//File[] listOfFiles = folder.listFiles(file_filter(start,end));

		for(File file:listOfFiles){
			getBigramMap(file,bigram_map);
		}

	}
	
	public static boolean classify_bigram(File file, Map<String,Integer> pos_bigram_map, Map<String,Integer> neg_bigram_map,
			      String Training_set_type){
        HashMap<String,Integer> test_bigram_map = new HashMap<String,Integer>();
        getBigramMap(file,test_bigram_map);
       
        double total_sum_pos =0;
        for(String str : pos_bigram_map.keySet()){
        	total_sum_pos = total_sum_pos + pos_bigram_map.get(str);
        }
  
        double total_sum_neg =0;
        for(String str : neg_bigram_map.keySet()){
        	total_sum_neg = total_sum_neg + neg_bigram_map.get(str);
        }
        
        double pos_sent = Math.log( total_sum_pos/ (total_sum_pos + total_sum_neg) )  +  getSentiment(test_bigram_map,pos_bigram_map);
        double neg_sent = Math.log( total_sum_neg/ (total_sum_pos + total_sum_neg) ) +  getSentiment(test_bigram_map,neg_bigram_map);
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
        
        System.out.println("Positive sentiment: " + pos_sent + "Negative sentiment: " + neg_sent);
        
        //if(Training_set_type.equals(POSITIVE_DOC_FOLDER)){
        //	return (pos_sent > neg_sent);
        //}
        //else{
        	return (pos_sent < neg_sent);
        //}
        
       
    }
	
	
	public static boolean classify_unigram(File file, Map<String,Integer> pos_unigram_map, Map<String,Integer> neg_unigram_map,String Training_set_type,boolean stop_words){
        HashMap<String,Integer> test_unigram_map = new HashMap<String,Integer>();
        getUnigramMap(file,test_unigram_map,stop_words);
        
        double total_sum_pos =0;
        for(String str : pos_unigram_map.keySet()){
        	total_sum_pos = total_sum_pos + pos_unigram_map.get(str);
        }
  
        double total_sum_neg =0;
        for(String str : neg_unigram_map.keySet()){
        	total_sum_neg = total_sum_neg + neg_unigram_map.get(str);
        }
        
        double pos_sent = Math.log( total_sum_pos/ (total_sum_pos + total_sum_neg) )  +  getSentiment(test_unigram_map,pos_unigram_map);
        double neg_sent = Math.log( total_sum_neg/ (total_sum_pos + total_sum_neg) ) +  getSentiment(test_unigram_map,neg_unigram_map);
        
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
        
        if(Training_set_type.equals(POSITIVE_DOC_FOLDER)){
        	return (pos_sent > neg_sent);
        }
        else{
        	return (pos_sent < neg_sent);
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
    
    
	public static FileFilter file_filter(final int num1, final int num2){
		
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
            	
            	int endIndex1 = pathname.toString().lastIndexOf("\\cv");
            	int endIndex2 = pathname.toString().lastIndexOf("_");
                String filenumber = pathname.toString().substring(endIndex1 + 3,endIndex2);
               return  Integer.parseInt(filenumber) >= num1 && Integer.parseInt(filenumber) < num2;
            }
         };
         
         return filter;
		
	}

	public static File[] getFiles(File folder,int[] dataSet){
		List<File> listOfFiles = new ArrayList<File>();
		for(int i:dataSet){
			File[] listSet = folder.listFiles(file_filter(i*SET_SIZE,(i+1)*SET_SIZE));
			listOfFiles.addAll(Arrays.asList(listSet));
		}
		
		return listOfFiles.toArray(new File[listOfFiles.size()]);
		
	}
	
	
	public static void run(int[] trainingSet,int[] testSet,int[] results, String Training_set_type, String classifier,
			       boolean stop_words) {
		//read all files in folder

		//int[] trainingSet = {0,1,2,3},testSet ={4};
		set_stopwords();

		Map<String,Integer> unigram_map_pos = null;
		Map<String,Integer> bigram_map_pos = null;
		Map<String,Integer> unigram_map_neg = null;
		Map<String,Integer> bigram_map_neg = null;
		
		File folder_pos = new File(POSITIVE_DOC_FOLDER);
		File[] fileTrainingSetpos = getFiles(folder_pos,trainingSet);
		if(classifier.equals("unigram")){
			unigram_map_pos =  new HashMap<String,Integer>();
			populate_unigram_map(folder_pos,fileTrainingSetpos,unigram_map_pos,stop_words);
		}
		if(classifier.equals("bigram")){
			bigram_map_pos =  new HashMap<String,Integer>();
			populate_bigram_map(folder_pos,fileTrainingSetpos,bigram_map_pos);
		}
		
		File folder_neg = new File(NEGATIVE_DOC_FOLDER);
		File[] fileTrainingSetneg = getFiles(folder_neg,trainingSet);
		if(classifier.equals("unigram")){
			unigram_map_neg =  new HashMap<String,Integer>();
			populate_unigram_map(folder_neg,fileTrainingSetneg,unigram_map_neg,stop_words);
		}
		if(classifier.equals("bigram")){
			bigram_map_neg =  new HashMap<String,Integer>();		
			populate_bigram_map(folder_neg,fileTrainingSetneg,bigram_map_neg);
		}
		//File testfile = new File("C:\\Users\\user\\workspace\\NLP\\src\\txt_sentoken\\pos\\cv958_12162.txt");
		//classify_unigram(testfile,unigram_map_pos,unigram_map_neg);
		//classify_bigram(testfile,bigram_map_pos,bigram_map_neg);
		

		File folder = new File(Training_set_type);
		File[] listOfFiles = getFiles(folder,testSet);
		int correct_results =0, totalresults = testSet.length*SET_SIZE;
		if(classifier.equals("unigram")){
			for(File testfile:listOfFiles) {
				if(classify_unigram(testfile,unigram_map_pos,unigram_map_neg,Training_set_type,stop_words))
					correct_results++;		 
			}
		}
		else if(classifier.equals("bigram")){
			for(File testfile:listOfFiles) {
				if(classify_bigram(testfile,bigram_map_pos,bigram_map_neg,Training_set_type))
					correct_results++;		 
			}
		}
		else{
			System.out.println("Wrong classifier argument");
			return;
		}
		
		
		System.out.println("Total results: "+ totalresults + " Correct results: " + correct_results);
		results[0] = totalresults;
		results[1] = correct_results;
	 
	}

}
