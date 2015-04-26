package NLPProject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ExtractHashTags {
	public static Map<String,Integer> tag_map= new HashMap<String,Integer>();
	
	public static void main(String args[]) throws IOException{
		String output_file = "C:\\Users\\Anurag\\Desktop\\NLP\\celeb_hashtags.txt";
		String input_file = "C:\\Users\\Anurag\\Desktop\\NLP\\infochimps_dataset_11897_download_15372-tsv\\tokens_by_month\\disk1.tsv";
		String hashcount_file = "C:\\Users\\Anurag\\Desktop\\NLP\\HashTag_count.csv";
		tag_map =populateTagMap(hashcount_file,tag_map);
		
		
		
	}
	
	public static Map<String,Integer> populateTagMap(String input_tag_file, Map<String,Integer> map) throws FileNotFoundException{
		Scanner s= new Scanner(new File(input_tag_file));
		s.nextLine();
		while(s.hasNextLine()){
			String temp = s.nextLine();
			String temp2[] = temp.split(",");
			map.put(temp2[0],Integer.parseInt(temp2[1]));
			System.out.println(temp2[0]+"  "+ temp2[1]);
			
			
		}
		return map;
		
	}
	
	
	
	public static void createTagFile(String input_file, String output_file) throws IOException{
		Scanner s = new Scanner(new File(input_file));
		FileWriter fw = new FileWriter(new File(output_file));
		while(s.hasNextLine()){
			String temp = s.nextLine();
			String temp2[] = temp.split("\\s+");
			if(temp2[0].equals("hashtag")){
				fw.write(temp);
				fw.write("\n");
			}
			else{
				break;
			}
			
		}
	}
	
	
}
