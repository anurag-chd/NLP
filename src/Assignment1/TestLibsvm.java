package Assignment1;

import libsvm.*;

import java.io.*;
import java.util.*;
public class TestLibsvm {
	
	public static void main(String args[]){
	
		 
		File f = new File("C:/Users/Anurag/Desktop/NLP/review_polarity.tar/review_polarity/txt_sentoken/neg/cv000_29416.txt");
		File f2 = new File("C:/Users/Anurag/Desktop/NLP/review_polarity.tar/review_polarity/txt_sentoken/neg/cv001_19502.txt");
		HashMap<String,Integer> pos_unigram_map = new HashMap<String,Integer>();
		HashSet<String> training_set = new HashSet<String>();
		try{
			BufferedReader reader=new BufferedReader(new FileReader(f));
	        String line=null;
	        int lineNum=0;
	        while((line=reader.readLine())!=null){
	        	String[] tokens=line.split("\\s+");
	        	for(String s : tokens){
	        		
	        		training_set.add(s);
	        	}
	        }
	        
	        reader.close();

		}
		catch(Exception e){
			
		}
		//training_set = pos_unigram_map.keySet();		
		ArrayList<String> str_arr = new ArrayList<String>();
		for(String s : training_set){
			str_arr.add(s);
		}
		
		
		/**/
		
		try{
			BufferedReader reader=new BufferedReader(new FileReader(f2));
	        String line=null;
	        int lineNum=0;
	        while((line=reader.readLine())!=null){
	        	String[] tokens=line.split("\\s+");
	        	for(String s : tokens){
	        		if(pos_unigram_map.containsKey(s)){
	        			int i = pos_unigram_map.get(s);
	        			pos_unigram_map.put(s,++i);
	        		}
	        		else{
	        			pos_unigram_map.put(s,1);
	        		}
	        		
	        	}
	        }
	        
	        reader.close();

			
			
		}
		catch(Exception e){
			
		}
		Set<String> key_set = pos_unigram_map.keySet();
		ArrayList <Integer> str_count = new ArrayList<Integer>(str_arr.size());
		
		for(int j =0; j<str_arr.size();j++){
			str_count.add(0);
		}
		System.out.println(str_count.size());
		for(String s: key_set){
			int count = str_arr.indexOf(s);
			System.out.println(count);
			if(count>=0){
				str_count.add(count,pos_unigram_map.get(s));
			}
			
		}
		
		for(int i = 0 ; i< str_count.size();i++){
			if(str_count.get(i)>0){
				System.out.print(i+":"+str_count.get(i)+" ");
			}
			
		}
		
       /* svm s = new svm();
        svm_model sm = new svm_model();*/
      //  svm_scale ssc = new svm_scale();
        //svm.buildClassifier(data);
	}
}
