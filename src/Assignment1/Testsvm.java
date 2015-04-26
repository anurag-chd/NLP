package Assignment1;

import java.io.*;
import java.util.*;

import libsvm.*;
public class Testsvm {
	
	public static final String POSITIVE_DOC_FOLDER = "C:\\Users\\Anurag\\Desktop\\NLP\\review_polarity.tar\\review_polarity\\txt_sentoken\\pos";
	public static final String NEGATIVE_DOC_FOLDER = "C:\\Users\\Anurag\\Desktop\\NLP\\review_polarity.tar\\review_polarity\\txt_sentoken\\neg";

	public static void main(String args[]){
		File folder_pos = new File(POSITIVE_DOC_FOLDER);
		File folder_neg = new File(NEGATIVE_DOC_FOLDER);
		Map<String,Integer> unigram_map =  new HashMap<String,Integer>();
		populate_unigram_map(folder_pos,2,999,unigram_map);
		populate_unigram_map(folder_neg,0,999,unigram_map);
		Set<String> key_string = unigram_map.keySet();
		
		ArrayList<String> str_arr = new ArrayList<String>();
		for(String s : key_string){
			str_arr.add(s);
		}
		
		File out_file = new File("Training.txt");
		try{
			FileWriter fw = new FileWriter(out_file);
			File[] listOfFiles = folder_pos.listFiles(file_filter(1,999));
			Map<String,Integer> unigram_map_file;    
	        for(File file:listOfFiles){
	        	unigram_map_file = new HashMap<String,Integer>();
	            getUnigramMap(file,unigram_map_file);
	            
	            Set<String> key_set = unigram_map_file.keySet();
	    		ArrayList <Integer> str_count = new ArrayList<Integer>(str_arr.size());
	    		
	    		for(int j =0; j<str_arr.size();j++){
	    			str_count.add(0);
	    		}
	    		
	    		for(String s: key_set){
	    			int count = str_arr.indexOf(s);
	    			
	    			if(count>=0){
	    				str_count.add(count,unigram_map_file.get(s));
	    			}
	    			
	    		}
	    		fw.write("+1 ");
	    		for(int i = 0 ; i< str_count.size();i++){
	    			if(str_count.get(i)>0){
	    				//System.out.print(i+":"+str_count.get(i)+" ");
	    				//fw.write(i+":"+str_count.get(i)+" ");
	    				fw.write(i+":"+"1 ");
	    			}
	    			
	    		}
	    		fw.write("\n");
	    	}
	        listOfFiles = folder_neg.listFiles(file_filter(0,999));
	        for(File file:listOfFiles){
	        	
	        	unigram_map_file = new HashMap<String,Integer>();
	            getUnigramMap(file,unigram_map_file);
	            
	            Set<String> key_set = unigram_map_file.keySet();
	    		ArrayList <Integer> str_count = new ArrayList<Integer>(str_arr.size());
	    		
	    		for(int j =0; j<str_arr.size();j++){
	    			str_count.add(0);
	    		}
	    		
	    		for(String s: key_set){
	    			int count = str_arr.indexOf(s);
	    			
	    			if(count>=0){
	    				str_count.add(count,unigram_map_file.get(s));
	    			}
	    			
	    		}
	    		fw.write("-1 ");
	    		for(int i = 0 ; i< str_count.size();i++){
	    			if(str_count.get(i)>0){
	    				//System.out.print(i+":"+str_count.get(i)+" ");
	    				fw.write(i+":"+str_count.get(i)+" ");
	    			}
	    			
	    		}
	    		fw.write("\n");
	    	}
	        fw.close();
			
			
		}
		catch(Exception e){
			
		}
		try{
			FileWriter fw2 = new FileWriter(new File("Test.txt"));
			File f2 = new File("C:/Users/Anurag/Desktop/NLP/review_polarity.tar/review_polarity/txt_sentoken/pos/cv000_29590.txt");
			
			Map<String,Integer> unigram_map_file;    
	        //for(File file:listOfFiles){
	        	unigram_map_file = new HashMap<String,Integer>();
	            getUnigramMap(f2,unigram_map_file);
	            
	            Set<String> key_set = unigram_map_file.keySet();
	    		ArrayList <Integer> str_count = new ArrayList<Integer>(str_arr.size());
	    		
	    		for(int j =0; j<str_arr.size();j++){
	    			str_count.add(0);
	    		}
	    		
	    		for(String s: key_set){
	    			int count = str_arr.indexOf(s);
	    			
	    			if(count>=0){
	    				str_count.add(count,unigram_map_file.get(s));
	    			}
	    			
	    		}
	    		//fw.write("+1 ");
	    		for(int i = 0 ; i< str_count.size();i++){
	    			if(str_count.get(i)>0){
	    				//System.out.print(i+":"+str_count.get(i)+" ");
	    				//fw2.write(i+":"+str_count.get(i)+" ");
	    				fw2.write(i+":"+"1 ");
	    			}
	    			
	    		}
	    		fw2.write("\n");
	    		fw2.close();
	    	//}
		}
		catch(Exception e){
			
		}
		
		
		
		
		
		
		
	}
	
	
	public static void populate_unigram_map(File folder,int start, int end,
            Map<String,Integer> unigram_map){
       
       
        File[] listOfFiles = folder.listFiles(file_filter(start,end));
       
        for(File file:listOfFiles){
            getUnigramMap(file,unigram_map);
        }
        
       
        /*       
        for(String key:unigram_map.keySet()) {
            if(unigram_map.get(key) != 1)
            System.out.println(key + "  " + unigram_map.get(key));
        }
       
        */
       
    }
	
	 public static FileFilter file_filter(final int num1, final int num2){
	       
	        FileFilter filter = new FileFilter() {
	            @Override
	            public boolean accept(File pathname) {
	               
	                int endIndex1 = pathname.toString().lastIndexOf("\\cv");
	                int endIndex2 = pathname.toString().lastIndexOf("_");
	                String filenumber = pathname.toString().substring(endIndex1 + 3,endIndex2);
	               return  Integer.parseInt(filenumber) >= num1 && Integer.parseInt(filenumber) <= num2;
	            }
	         };
	        
	         return filter;
	       
	    }
	
	public static void getUnigramMap(File file, Map<String,Integer> list_unigrams){
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
  ////////////////////////////////////////////////////////////////////////////          
            for(String s: nextLine.split("\\s+")){
 /////////////////////////////////////////////////////////////////////           	
                if(list_unigrams.containsKey(s))
                    list_unigrams.put(s,list_unigrams.get(s) + 1);
                else
                    list_unigrams.put(s,1);
            }           
        }
       

        input.close();
    }
   
	
	
}
