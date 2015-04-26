package NLPProject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

public class POSExtraction {
	public static String testing_folder_inf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_inf";
	public static String testing_folder_ninf = "C:\\Users\\Anurag\\Desktop\\NLP\\testing_ninf";
	public static String input_map_inf_file = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps_inf\\Pos_inf_map.txt";
	public static String input_map_non_inf_file = "C:\\Users\\Anurag\\Desktop\\NLP\\Maps_non_inf\\Pos_non_inf_map.txt";
    int count=0;
    static Map<String,Integer> postagcount_map_inf;
    static Map<String,Integer> postagcount_map_non_inf;
	private void getTokenPOS(int type,int sentNum, CoreMap sentence) {
		// traversing the words in the current sentence
		// a CoreLabel is a CoreMap with additional token-specific methods
		// System.out.println("Sentence "+sentNum+":");
		System.out.println("The parser came here" + count++);
		for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
			// this is the text of the token
			String word = token.get(TextAnnotation.class);
			// this is the POS tag of the token
			String pos = token.get(PartOfSpeechAnnotation.class);
			System.out.println(word + "--" + pos);
			// this is the index of the token
			int index = token.index();
			/*if(index == -1)
				continue;
				*/
			if(type == 1){
				//System.out.println("it came here");
			if (postagcount_map_inf.containsKey(pos)) {
				postagcount_map_inf.put(pos, postagcount_map_inf.get(pos) + 1);
				//System.out.println("second  time");
			} else {
				postagcount_map_inf.put(pos, 1);
				//System.out.println("first time");
			}
			}
			else {
				if (postagcount_map_non_inf.containsKey(pos)) {
					postagcount_map_non_inf.put(pos, postagcount_map_non_inf.get(pos) + 1);
			} else {
				postagcount_map_non_inf.put(pos, 1);
			}
			}
		}
	}

	
	private void getTokenPOS2(HashMap<String, Integer> map,int sentNum, CoreMap sentence) {
		// traversing the words in the current sentence
		// a CoreLabel is a CoreMap with additional token-specific methods
		// System.out.println("Sentence "+sentNum+":");
		System.out.println("The parser came here" + count++);
		for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
			// this is the text of the token
			String word = token.get(TextAnnotation.class);
			// this is the POS tag of the token
			String pos = token.get(PartOfSpeechAnnotation.class);
			System.out.println(word + "--" + pos);
			// this is the index of the token
			int index = token.index();
			/*if(index == -1)
				continue;
				*/
			
				//System.out.println("it came here");
			if (map.containsKey(pos)) {
				map.put(pos, map.get(pos) + 1);
				//System.out.println("second  time");
			} else {
				map.put(pos, 1);
				//System.out.println("first time");
			}
			
			
		}
	}
	
	
	
	public void parseSentence(int type, String text) {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.put("annotators",	"tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);
		// run all Annotators on this text
		pipeline.annotate(document);
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		// get pos tag of each sentence for each word
		
		// assign vars
		for (int i = 0; i < sentences.size(); i++) {
			getTokenPOS(type,i + 1, sentences.get(i));
		}
				
	}
	
	public void parseSentence2(HashMap<String, Integer> map, String text) {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.put("annotators",	"tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);
		// run all Annotators on this text
		pipeline.annotate(document);
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		// get pos tag of each sentence for each word
		
		// assign vars
		for (int i = 0; i < sentences.size(); i++) {
			getTokenPOS2(map,i + 1, sentences.get(i));
		}
				
	}
	
	
public static void populate_postagmap(POSExtraction parser,File[] TrainingFiles,
		 String file_str,int type) throws FileNotFoundException, UnsupportedEncodingException{

		 
		for(File file:TrainingFiles){
			File mod_file = FVCreation.rmName(file.getAbsolutePath());
			StringBuilder doc_string = new StringBuilder();
			Scanner input = null;
			try {
				input = new Scanner(mod_file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	
			while(input.hasNextLine()) {
			    String nextLine = input.nextLine();
			    System.out.println(nextLine);
			    nextLine.replaceAll("#\\w*", "");
			    System.out.println(nextLine);
			    
			    parser.parseSentence(type,nextLine);
			    
			}
						
			
		}
		
		
		return;
		
	}

public static HashMap<String,Integer> populate_postagmap2(POSExtraction parser,File testingFile,HashMap<String,Integer> map)
		  throws FileNotFoundException, UnsupportedEncodingException{

			
		
			File mod_file = FVCreation.rmName(testingFile.getAbsolutePath());
			StringBuilder doc_string = new StringBuilder();
			Scanner input = null;
			try {
				input = new Scanner(mod_file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	
			while(input.hasNextLine()) {
			    String nextLine = input.nextLine();
			    System.out.println(nextLine);
			    nextLine.replaceAll("#\\w*", "");
			    System.out.println(nextLine);
			    
			    parser.parseSentence2(map,nextLine);
			    
			}
						
			
		
		
		
		return map;
		
	}






	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		POSExtraction parser = new POSExtraction();
		
		postagcount_map_inf = new HashMap<String,Integer>();		
		postagcount_map_non_inf = new HashMap<String,Integer>();		

		File f_inf = new File(input_map_inf_file);
		File f_non_inf = new File(input_map_non_inf_file);
		
		Scanner s1 = new Scanner(f_inf);
		while(s1.hasNextLine()){
			String temp[] = s1.nextLine().split("~#");
			System.out.println(temp[0]+"  "+temp[1] );
			postagcount_map_inf.put(temp[0], Integer.parseInt(temp[1]));
			
		}
		
		Scanner s2 = new Scanner(f_non_inf);
		while(s2.hasNextLine()){
			String temp[] = s2.nextLine().split("~#");
			postagcount_map_non_inf.put(temp[0], Integer.parseInt(temp[1]));
			
		}
		
		File folder = new File(testing_folder_inf);
		File[] listOfFiles = folder.listFiles();
		int correct_results =0, totalresults = listOfFiles.length;
		for(File testfile:listOfFiles) {
			
			if(classify_unigram(testfile,postagcount_map_inf,postagcount_map_non_inf,testing_folder_inf,true))
					correct_results++;		 
			}
		
		System.out.println("Influential Total results: "+ totalresults + " Correct results: " + correct_results);
		
		
		// Non Influential test
		
		File folder2 = new File(testing_folder_ninf);
		File[] listOfFiles2 = folder2.listFiles();
		int correct_results2 =0, totalresults2 = listOfFiles2.length;
		for(File testfile:listOfFiles2) {
			
			if(classify_unigram(testfile,postagcount_map_inf,postagcount_map_non_inf,testing_folder_inf,true))
					correct_results2++;		 
			}
		
		System.out.println("Non Influential Total results: "+ totalresults2 + " Correct results: " + correct_results2);
	

	}
	
	public static boolean classify_unigram(File file, Map<String,Integer> pos_unigram_map, Map<String,Integer> neg_unigram_map,String Training_set_type,boolean stop_words) throws FileNotFoundException, UnsupportedEncodingException{
        Map<String,Integer> test_unigram_map = new HashMap<String,Integer>();
        for(String str : pos_unigram_map.keySet() ){
        	test_unigram_map.put(str, 0);
        }
        POSExtraction parser = new POSExtraction();
        test_unigram_map = populate_postagmap2(parser,file, (HashMap<String, Integer>) test_unigram_map);
        
            
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
	
}

