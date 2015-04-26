package NLPProject;
import java.io.*;
import java.util.Scanner;

public class ScreenName_TrustRank_Mapping {
	static String uname_file = "C:\\Users\\Anurag\\Desktop\\NLP\\celebrities_profiles_comp.txt";
	static String uname_uid_file = "C:\\Users\\Anurag\\Desktop\\NLP\\infochimps_dataset_11898_download_15373-tsv\\twitter_user_id_mapping\\twitter_user_id_mapping.tsv";
	static String uid_trstrank_file ="C:\\Users\\Anurag\\Desktop\\NLP\\trstrank\\trstrank.tsv";
	static String test_file = "C:\\Users\\Anurag\\Desktop\\NLP\\trstrank\\splitfiles\\disk2.csv";
	
	public static void main(String args[]) throws IOException{
		//BufferedReader br = new BufferedReader(new FileReader(new File(uid_trstrank_file)));
		Scanner s = new Scanner(new File(uid_trstrank_file));
		//FileWriter fw = new FileWriter(new File("C:\\Users\\Anurag\\Desktop\\NLP\\trstrank\\splitfiles\\new_disk2.csv"));
		FileWriter fw = new FileWriter(new File("C:\\Users\\Anurag\\Desktop\\NLP\\trstrank\\trstrank_update.csv"));
		String temp;
		int count = 0;
		//s.useDelimiter("\\s+");
		int next_count = 0;
		while(s.hasNextLine()){
		
				temp = s.nextLine();
				String arr[] = temp.split("\\s+");
				if(arr.length == 3){
					//System.err.println("first loop");
					String st = "garbage";
					fw.write(st+",");
					fw.write(arr[0]+",");
					fw.write(arr[1]+",");
					fw.write(arr[2]+"\n");
				}
				if(arr.length == 4){
					//System.err.println("second loop");
					//System.out.println(arr[0]);
					if(arr[0].length() == 0){
						//System.out.println("Zero length");
						fw.write("null,");
					}
					else{
						fw.write(arr[0]+",");
					}
					
					fw.write(arr[1]+",");
					fw.write(arr[2]+",");
					fw.write(arr[3]+"\n");
				}
				
				/*String temp2[]=temp.split("\\s+");
				for(int i = 0; i< temp2.length;i++){
					
					fw.write(temp2[i]);
					if(!(i==temp2.length-1)){
						fw.write(',');
					}
				}
				fw.write("\n");*/
		
			/*}
			else{
				break;
		
			}
			count++;
		*/			
		}
		fw.close();
		s.close();
		//fw2.close();
		//br.close();
	}
}
