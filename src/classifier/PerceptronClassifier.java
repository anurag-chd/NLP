package classifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import classifier.LanguageModel;
import static classifier.LanguageModel.SET_SIZE;

public class PerceptronClassifier {
	
	public static final int MAX_ITERATION = 300;
	public static final int FEATURE_COUNT_THRESHOLD = 10;
	
	public static Map<String,Float> feature_vector_pos;
	static Map<String,Float> feature_vector_neg;
	static Map<String,Float> feature_vector;
	static Map<String,Float> inverse_vector;
	
	static List<Map<String,Float>> document_feature_vectors_pos;
	static List<Map<String,Float>> document_feature_vectors_neg;
	static Map<String,Float> weight_vector;
	static int bias = 0;
	
	
	public static Map<String,Float> feature_set_extraction(Map<String,Integer> unigram_count){
		Map<String,Float> feature_vector = new HashMap<String,Float>();
		for(String feature:unigram_count.keySet()){
			if(unigram_count.get(feature) > FEATURE_COUNT_THRESHOLD)
				feature_vector.put(feature, 0.0f);
		}
		return feature_vector;
	}
	
	public static Map<String,Float> inverse_vector_extraction(Map<String,Float> feature_vector,
			               Map<String,Integer> unigram_count_pos,Map<String,Integer> unigram_count_neg,int docSize) {
		Map<String,Float> inverse_vector = CopyMap(feature_vector);
		int k1=0,k2=0;
	    for(String key:feature_vector.keySet()) {
	    	k1 = unigram_count_pos.get(key) == null?0:unigram_count_pos.get(key);
	    	k2 = unigram_count_neg.get(key) == null?0:unigram_count_neg.get(key);
	    	inverse_vector.put(key, (float)Math.log((float)docSize/(k1 + k2)));
	    }
	    return  inverse_vector;   
	}
	
	public static Map<String,Float> document_feature_vector(File file,
			           Map<String,Float> feature_vector,
			           Map<String,Float> inverse_vector){
		Map<String,Float> doc_vector =  CopyMap(feature_vector);
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		while(input.hasNext()) {
		    String nextLine = input.nextLine();
		    for(String s: nextLine.split("\\W+")){
		    	if(doc_vector.containsKey(s))
		    		doc_vector.put(s,doc_vector.get(s) + 1);
		    }		    
		}
		
		//Map<String,Float> inverse_doc_vector = MultiplyVectors(doc_vector,inverse_vector);
		
		MultiplyVectorsInline(doc_vector,inverse_vector);
		input.close();
		return doc_vector;
	}
	
	
	
	public static <K> Map<K,Float> UnionVectors(
		    Map<K,Float> map1,Map<K,Float> map2){

		Map<K,Float> result = new HashMap<K,Float>();
		
		    for(K key : map1.keySet()){
		    	result.put(key, map1.get(key));
		    }
		    
		    for(K key : map2.keySet()){
		    	result.put(key, map2.get(key));
		    }
		    return result;
		}
	
	public static <K> Map<K,Float> MultiplyVectors(
		    Map<K,Float> map1,Map<K,Float> map2){

		Map<K,Float> result = new HashMap<K,Float>();
		    for(K key : map1.keySet()){
		    	result.put(key, map1.get(key)*map2.get(key) );
		    }
		    
		    return result;
		}
	
	public static <K> void MultiplyVectorsInline(
		    Map<K,Float> map1,Map<K,Float> map2){

		Map<K,Float> result = new HashMap<K,Float>();
		    for(K key : map1.keySet()){
		    	map1.put(key, map1.get(key)*map2.get(key) );
		    }
		    
		}
	
	public static <K> void AddVectors(
		    Map<K,Float> map1,Map<K,Float> map2){

		    for(K key : map1.keySet()){
		    	map1.put(key, map1.get(key) + map2.get(key) );
		    }

		}
	
	public static <K> void SubtractVectors(
		    Map<K,Float> map1,Map<K,Float> map2){

		    for(K key : map1.keySet()){
		    	map1.put(key, map1.get(key) - map2.get(key) );
		    }

		}
	
	public static <K> Float VectorProduct(
		    Map<K,Float> map1,Map<K,Float> map2){
            
		    Float result=(float) 0.0;
		    for(K key : map1.keySet()){
		    	if(map2.get(key) == null)
		    	    System.out.println("here is the key: " + key);
		    	result+= map1.get(key)*map2.get(key);
		    }
		    
		    return result;
		}
	
	public static <K1,K2> Map<K1,K2> CopyMap(
		    Map<K1,K2> original){

		Map<K1,K2> copy = new HashMap<K1,K2>();
		    for(K1 key : original.keySet()){
		        copy.put(key, original.get(key));
		    }
		    return copy;
		}
	
	public static List<Map<String,Float>> populate_feature_vectors(File[] listOfFiles,boolean isPositive){
		
		List<Map<String,Float>> feature_vectors = new ArrayList<Map<String,Float>>();
		//File[] listOfFiles = folder.listFiles(LanguageModel.file_filter(start,end));
		
		for(File file:listOfFiles){
			//if(isPositive)
				feature_vectors.add(document_feature_vector(file,feature_vector,inverse_vector));
			//else
			//	feature_vectors.add(document_feature_vector(file,feature_vector_neg,inverse_vector_neg));
		}
		
		return feature_vectors;
	}
	
	public static Map<String,Float> populate_weight_vectors(){
		 Map<String,Float> weight_vector = CopyMap(feature_vector);
		 Map<String,Float> document_feature;
		 int no_of_values_changed = 10;
		 boolean isPositive=true;
		 int noOfIterations=0, positive_count= document_feature_vectors_pos.size(), negative_count=document_feature_vectors_neg.size();
		 Random rnd = new Random();
		 
		 while(noOfIterations++ < MAX_ITERATION && no_of_values_changed>0){
			 no_of_values_changed =0;
			 while(positive_count >0 || negative_count>0){
				 isPositive = rnd.nextBoolean();
				 if(isPositive && positive_count >0) {
					 positive_count--;
					 document_feature = document_feature_vectors_pos.get(positive_count);
					 if(VectorProduct(document_feature,weight_vector) + bias <= 0){
						 no_of_values_changed++;
						 AddVectors(weight_vector , document_feature);
						 bias +=1;
					 }
				 }
				 else if (!isPositive && negative_count >0){
					 negative_count--;
					 document_feature = document_feature_vectors_neg.get(negative_count);
					 if(VectorProduct(document_feature,weight_vector) + bias >= 0){
						 no_of_values_changed++;
						 SubtractVectors(weight_vector , document_feature);
						 bias -=1;
					 } 
				 }
			 }
			 
			 //resetting values
			 positive_count= document_feature_vectors_pos.size();
			 negative_count=document_feature_vectors_neg.size();
			 System.out.println(no_of_values_changed);
			 
			 
		 }
		 
		 return weight_vector;
	}
	
	public static boolean classify_perceptron(File file,String Training_set_type){
		Map<String,Float> document_vector = document_feature_vector(file,feature_vector,inverse_vector);
		if(Training_set_type.equals(LanguageModel.POSITIVE_DOC_FOLDER)){
			return ((VectorProduct(document_vector,weight_vector) + bias) >= 0);
		}
		else{
			return ((VectorProduct(document_vector,weight_vector) + bias) < 0);
		}
		

	}
	public static void write_to_file(File file,Map<String,Float> map){
		try {			 
			if (file.exists()) {
				file.delete();
			}
 
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(String key:map.keySet())
				bw.write(key + "  " + map.get(key) + "\n");
			//bw.write(content);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void run(int[] trainingSet,int[] testSet,int[] results,String Training_set_type) {
		
		File logfile = new File(LanguageModel.OUTPUT_FILE + "output.txt");
		//int[] trainingSet = {0,1,2,3},testSet ={4};
		
		File folder_pos = new File(LanguageModel.POSITIVE_DOC_FOLDER);
		Map<String,Integer> unigram_count_pos =  new HashMap<String,Integer>();
		File[] fileTrainingSetpos = LanguageModel.getFiles(folder_pos,trainingSet);
		LanguageModel.populate_unigram_map(folder_pos,fileTrainingSetpos,unigram_count_pos,true);
		feature_vector_pos = feature_set_extraction(unigram_count_pos);
		
		File folder_neg = new File(LanguageModel.NEGATIVE_DOC_FOLDER);
		File[] fileTrainingSetneg = LanguageModel.getFiles(folder_neg,trainingSet);
		Map<String,Integer> unigram_count_neg =  new HashMap<String,Integer>();
		LanguageModel.populate_unigram_map(folder_neg,fileTrainingSetneg,unigram_count_neg,true);
		feature_vector_neg = feature_set_extraction(unigram_count_neg);
		
		feature_vector = UnionVectors(feature_vector_pos,feature_vector_neg);
		
		inverse_vector = inverse_vector_extraction(feature_vector,unigram_count_pos,unigram_count_neg,trainingSet.length*SET_SIZE*2);
		
		document_feature_vectors_pos = populate_feature_vectors( fileTrainingSetpos, true);
		document_feature_vectors_neg = populate_feature_vectors( fileTrainingSetneg, false);
		
		//weight_vector_pos = populate_weight_vectors(true);
		
		//for(String key: weight_vector_pos.keySet())
		//	System.out.println(key + " " + weight_vector_pos.get(key));
		
		//write_to_file(logfilepos,weight_vector_pos);
		

		//inverse_vector_neg = inverse_vector_extraction(feature_vector_neg,unigram_count_neg,end-start);

		
		
		weight_vector = populate_weight_vectors();

		//for(String key: weight_vector_neg.keySet())
		//	System.out.println(key + " " + weight_vector_neg.get(key));
		
		write_to_file(logfile,weight_vector);
		

		File folder = new File(Training_set_type);
		File[] listOfFiles = LanguageModel.getFiles(folder,testSet);
		int correct_results =0, totalresults = testSet.length*200;
		for(File testfile:listOfFiles) {
			if(classify_perceptron(testfile,Training_set_type))
				correct_results++;		 
		}
		
		System.out.println("Total results: "+ totalresults + " Correct results: " + correct_results);
		results[0] = totalresults;
		results[1] = correct_results;
	}

}
