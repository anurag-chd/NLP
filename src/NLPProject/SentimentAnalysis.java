package NLPProject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalysis {
public static String input_folder_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\Influential_tweets"; 
public static String input_folder_non_inf ="C:\\Users\\Anurag\\Desktop\\NLP\\non_influential_tweets";
public static String testing_folder_inf = "C:\\Users\\user\\workspace\\dataset_tweets\\testing_inf";


   public static int findSentiment(String line) {

       Properties props = new Properties();
       props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
       //props.setProperty("annotators", "parse, sentiment");
       StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
       int sentiment = 0;
       if (line != null && line.length() > 0) {
           Annotation annotation = pipeline.process(line);
           for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
               Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
               sentiment = RNNCoreAnnotations.getPredictedClass(tree);
           }
       }
       return sentiment;

   }
   
public static void main(String[] args) {
// parsing files
File folder_inf = new File(input_folder_inf);
File folder_non_inf = new File(input_folder_non_inf);
File[] TrainingFiles_inf = folder_inf.listFiles();
File[] TrainingFiles_non_inf = folder_non_inf.listFiles();
SentimentAnalysis sentimentAnalyzer = new SentimentAnalysis();
float sentiment_avg_inf = 0,sentiment_avg_non_inf = 0;
        Scanner s = null;
        int sum =0;
        int count_file =0;
        int count_lines = 0;
for(File f : TrainingFiles_inf){
if(count_file<1){
try{
s = new Scanner(f);
}
catch(Exception e){
e.printStackTrace();
}
while(s.hasNextLine()){
//if(count_lines < 300){
sum = sum + findSentiment(s.nextLine());
count_lines++;
/*}
else{
break;
}*/
}

count_file++;
}
else{
break;
}
}
sentiment_avg_inf = (float) sum/count_lines;
/*Scanner s2 = null;
         sum =0;
for(File f : TrainingFiles_non_inf){
try{
s2 = new Scanner(f);
}
catch(Exception e){
e.printStackTrace();
}
while(s2.hasNextLine()){
sum = sum + findSentiment(s2.nextLine());
}
}
sentiment_avg_non_inf = (float) sum/TrainingFiles_non_inf.length;
*/
System.out.println("Average Sentiment of influential tweets :"+sentiment_avg_inf);
//System.out.println("Average Sentiment of non influential tweets :"+sentiment_avg_non_inf);
         //int sentiment = findSentiment("I am very happy. I am sad and gloomy.");
        //System.out.println("The sentiment is " + sentiment);

}

}