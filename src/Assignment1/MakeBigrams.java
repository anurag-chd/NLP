package Assignment1;

import java.io.*;
import java.util.*;
public class MakeBigrams {
	public static void main(String args[]){
		File f= new File("C:/Users/Anurag/workspace/NLP/Test.txt");
		File f1= new File("C:/Users/Anurag/Desktop/NLP/review_polarity.tar/review_polarity/txt_sentoken/pos/cv000_29590.txt");
		//File f2= new File("C:/Users/Anurag/Desktop/NLP/review_polarity.tar/review_polarity/txt_sentoken/neg/cv000_29416.txt");
		File f2= new File("C:/Users/Anurag/workspace/NLP/Test.txt");
		HashMap<String,Integer> pos_bigram_map = new HashMap<String,Integer>();
		HashMap<String,Integer> neg_bigram_map = new HashMap<String,Integer>();
		
		getBigram_Map(f1,pos_bigram_map);
		getBigram_Map(f2,neg_bigram_map);
		classify(f,pos_bigram_map,neg_bigram_map);
	}
	
	public static void classify(File f, HashMap<String,Integer> pos_bigram_map, HashMap<String,Integer> neg_bigram_map){
		HashMap<String,Integer> new_bigram_map = new HashMap<String,Integer>();
		getBigram_Map(f,new_bigram_map);
		
		double pos_sent = getSentiment(new_bigram_map,pos_bigram_map);
		double neg_sent = getSentiment(new_bigram_map,neg_bigram_map);
		System.out.println("Positive value " + pos_sent+"\n" +"Negative value " + neg_sent );
		if(pos_sent > neg_sent) {
			System.out.println("Document is positive");
			
		}
		
		else{
			System.out.println("Document is negative");
		}
		
	}
	
	public static double getSentiment(HashMap<String,Integer> new_bigram_map,HashMap<String,Integer> bigram_map){
		double result = 1;
		int total_sum =0;
		Set<String> pos_key_string = bigram_map.keySet();
		for(String str : pos_key_string){
			total_sum = total_sum + bigram_map.get(str);
			
		}
		//System.out.println("Total_sum : " +total_sum);
		Set<String> key_string = new_bigram_map.keySet();
		for(String str : key_string ){
			if(bigram_map.containsKey(str)){
				result = result * new_bigram_map.get(str) * ((double)(bigram_map.get(str) +1)/(total_sum + (bigram_map.get(str)/total_sum) ));
				//System.out.println(new_bigram_map.get(str));
				//System.out.println(bigram_map.get(str));
				System.out.println((double)bigram_map.get(str)/total_sum);
				System.out.println(result);
			}
			/*else{
				
			}*/
			
		}
		
		
		
		
		
		return result;
		
		
		
	}
	
	

	public static void getBigram_Map(File f, HashMap<String,Integer> bigram_map) {
		try{
			Scanner sc=new Scanner(f);
			sc.useDelimiter(" ");
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
			
			//do whatever you want with s
			}
			/*Set<String> key_set = bigram_map.keySet();
			for(String str : key_set){
				System.out.println(str + "  " + bigram_map.get(str));
			}*/
		
			
			
			
		}
		catch(FileNotFoundException e){
			
		}
		 
	}
}
