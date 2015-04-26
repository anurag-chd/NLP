package classifier;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import classifier.LanguageModel;
import classifier.PerceptronClassifier;

public class DriverProgram {

	public static void main(String[] args) {
		try{
			File f = new File("Result_stats2.txt");
			//FileWriter fw = new FileWriter(f);
			FileWriter fw = new FileWriter(f);
			//LanguageModel lm = new LanguageModel();
			//PerceptronClassifier pc = new PerceptronClassifier();
			//creating the test sets one training data and one test data
			int [] testSet = {4};
			
			/*
			fw.write("Language model"+"\n");
			fw.write("Unigram Classifier"+"\n");
			fw.write("[With Stop Words]"+"\n");
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				int [] trainingSet = {i};
				LanguageModel.run(trainingSet, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER,"unigram",true );
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: "+trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				int [] trainingSet = {i};
				LanguageModel.run(trainingSet, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER,"unigram",true );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: "+trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			ArrayList<Integer> trainingSet = new ArrayList<Integer>();
			///////////////////
			
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : trainingSet){
					trainingset_arr[a] = a;
				}
				LanguageModel.run(trainingset_arr, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER ,"unigram", true);
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				for(int j=0;j<trainingset_arr.length;j++){
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			
			trainingSet.removeAll(trainingSet);
			
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : trainingSet){
					trainingset_arr[a] = a;
				}
				LanguageModel.run(trainingset_arr, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER,"unigram",true );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				for(int j=0;j<trainingset_arr.length;j++){
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			
			fw.write("\n");
			///////////////// unigrams without stop words/////////////////////
			fw.write("[WithOut Stop Words]"+"\n");
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				int [] stop_trainingSet = {i};
				LanguageModel.run(stop_trainingSet, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER,"unigram",false );
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: "+stop_trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				int [] stop_trainingSet = {i};
				LanguageModel.run(stop_trainingSet, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER,"unigram",false );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: "+stop_trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			//ArrayList<Integer> trainingSet = new ArrayList<Integer>();
			///////////////////
			trainingSet.removeAll(trainingSet);
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : trainingSet){
					trainingset_arr[a] = a;
				}
				LanguageModel.run(trainingset_arr, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER ,"unigram", false);
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				for(int j=0;j<trainingset_arr.length;j++){
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			
			trainingSet.removeAll(trainingSet);
			
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : trainingSet){
					trainingset_arr[a] = a;
				}
				LanguageModel.run(trainingset_arr, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER,"unigram",false );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				for(int j=0;j<trainingset_arr.length;j++){
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			
			fw.write("\n");
			
			
			
			*/
			
			
			
			///////////bi grams//////////////
			fw.write("Bigram Classifier"+"\n");
			for(int i=1;i<4;i++){
				int [] results = new int[2];
				int [] bi_trainingSet = {i};
				LanguageModel.run(bi_trainingSet, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER,"bigram",true );
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: "+bi_trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				int [] bi_trainingSet = {i};
				LanguageModel.run(bi_trainingSet, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER,"bigram",true );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: "+bi_trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			
			fw.write("\n");
			//ArrayList<Integer> trainingSet = new ArrayList<Integer>();
			///////////////////
			/*
			trainingSet.removeAll(trainingSet);
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : trainingSet){
					trainingset_arr[a] = a;
				}
				LanguageModel.run(trainingset_arr, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER ,"bigram",true);
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				for(int j=0;j<trainingset_arr.length;j++){
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			
			trainingSet.removeAll(trainingSet);
			
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : trainingSet){
					trainingset_arr[a] = a;
				}
				LanguageModel.run(trainingset_arr, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER,"bigram",true );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				for(int j=0;j<trainingset_arr.length;j++){
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			
			fw.write("\n");
			
	////////////////////////Stop words ////////////////////////////////////////////
			LanguageModel.set_stopwords();
			
			
			
			
			fw.write("PerceptronClassifier"+"\n");
			
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				int [] percept_trainingSet = {i};
				PerceptronClassifier.run(percept_trainingSet, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER );
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: "+percept_trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				int [] percept_trainingSet = {i};
				PerceptronClassifier.run(percept_trainingSet, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: "+percept_trainingSet[0]+" "+"Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			
			ArrayList<Integer> percept_trainingSet = new ArrayList<Integer>();
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				percept_trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : percept_trainingSet){
					trainingset_arr[a] = a;
				}
				System.out.println(Arrays.toString(trainingset_arr));
				System.out.println(Arrays.toString(testSet));
				PerceptronClassifier.run(trainingset_arr, testSet, results,LanguageModel.POSITIVE_DOC_FOLDER );
				fw.write("{POSITIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				for(int j=0;j<trainingset_arr.length;j++){
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			fw.write("\n");
			
			trainingSet.removeAll(trainingSet);
			
			for(int i=0;i<4;i++){
				int [] results = new int[2];
				trainingSet.add(i);
				int []trainingset_arr=new int[i+1];
				for(Integer a : trainingSet){
					trainingset_arr[a] = a;
				}
				PerceptronClassifier.run(trainingset_arr, testSet, results,LanguageModel.NEGATIVE_DOC_FOLDER );
				fw.write("{NEGATIVE TRAINING SET} "+"Training Set: {");//+trainingset_arr+" Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				
				for(int j=0;j<trainingset_arr.length;j++){
					
					int a = trainingset_arr[j];
					fw.write(""+a+',');
				}
				fw.write("} Test Set: "+testSet[0]+ " Total results : "+results[0]+" Correct_Results : "+results[1]+" Precision: "+((float)(results[1]*100)/results[0]));
				fw.write("\n");
			}
			
			*/
			fw.close();
		}
		catch(Exception e){
			
		}

		
		
		

	}

}
