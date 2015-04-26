package Assignment1;

import java.io.*;
import java.util.*;

import 	de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;
import libsvm.*;

public class LIbvsm_ex{
    public static void main(String[] args) throws Exception{

        // Preparing the SVM param
        svm_parameter param=new svm_parameter();
        param.svm_type=svm_parameter.C_SVC;
        param.kernel_type=svm_parameter.RBF;
        param.gamma=0.5;
        param.nu=0.5;
        param.cache_size=20000;
        param.C=1;
        param.eps=0.001;
        param.p=0.1;

        HashMap<Integer, HashMap<Integer, Double>> featuresTraining=new HashMap<Integer, HashMap<Integer, Double>>();
        HashMap<Integer, Integer> labelTraining=new HashMap<Integer, Integer>();
        HashMap<Integer, HashMap<Integer, Double>> featuresTesting=new HashMap<Integer, HashMap<Integer, Double>>();

        HashSet<Integer> features=new HashSet<Integer>();
        int no_of_features=0;

        //Read in training data
        BufferedReader reader=null;
        try{
            reader=new BufferedReader(new FileReader("Training.txt"));
        	//reader=new BufferedReader(new FileReader("a1a.train"));
            String line=null;
            int lineNum=0;
            while((line=reader.readLine())!=null){
                featuresTraining.put(lineNum, new HashMap<Integer,Double>());
                String[] tokens=line.split("\\s+");
                int label=Integer.parseInt(tokens[0]);
                labelTraining.put(lineNum, label);
                for(int i=1;i<tokens.length;i++){
                    String[] fields=tokens[i].split(":");
                    int featureId=Integer.parseInt(fields[0]);
                    if(featureId>no_of_features)
                    	no_of_features=featureId;
                    double featureValue=Double.parseDouble(fields[1]);
                    features.add(featureId);
                    featuresTraining.get(lineNum).put(featureId, featureValue);
                }
            lineNum++;
            }

            reader.close();
        }catch (Exception e){

        }

        //Read in test data
        try{
            reader=new BufferedReader(new FileReader("Test.txt"));
        	//reader=new BufferedReader(new FileReader("a9a.test"));
            String line=null;
            int lineNum=0;
            while((line=reader.readLine())!=null){

                featuresTesting.put(lineNum, new HashMap<Integer,Double>());
                String[] tokens=line.split("\\s+");
                for(int i=1; i<tokens.length;i++){
                    String[] fields=tokens[i].split(":");
                    int featureId=Integer.parseInt(fields[0]);
                    double featureValue=Double.parseDouble(fields[1]);
                    featuresTesting.get(lineNum).put(featureId, featureValue);
                }
            lineNum++;
            }
            reader.close();
        }catch (Exception e){

        }
        /// liblinear/////////////
        Problem problem = new Problem();
        int numTrainingInstances=featuresTraining.keySet().size();
        problem.l = numTrainingInstances;  // number of training examples
        problem.n = no_of_features+1; // number of features
        problem.x = new FeatureNode[problem.l][]; // feature nodes
        problem.y = new double[problem.l]; // target values

        for(int i=0;i<numTrainingInstances;i++){
            HashMap<Integer,Double> tmp=featuresTraining.get(i);
            problem.x[i]=new FeatureNode[tmp.keySet().size()];
           TreeSet<Integer> t_set = new TreeSet<Integer>(tmp.keySet());
            // Collections.sort(tmp.keySet());
            int indx=0;
            for(Integer id:t_set){
            	FeatureNode node=new FeatureNode(id+1,tmp.get(id));
                problem.x[i][indx]=node;
                indx++;
            }
            problem.y[i]=labelTraining.get(i);
        }
        
            
            
            
        
        SolverType solver = SolverType.L2R_LR; // -s 0
        double C = 1.0;    // cost of constraints violation
        double eps = 0.01; // stopping criteria

        Parameter parameter = new Parameter(solver, C, eps);
        Model model = Linear.train(problem, parameter);
        File modelFile = new File("model");
        model.save(modelFile);
        // load model or use it directly
        model = Model.load(modelFile);

        for(Integer testInstance:featuresTesting.keySet()){
            HashMap<Integer, Double> tmp=featuresTesting.get(testInstance);
            int numFeatures=tmp.keySet().size();
            FeatureNode[] x=new FeatureNode[numFeatures];
            int featureIndx=0;
            for(Integer feature:tmp.keySet()){
                x[featureIndx]=new FeatureNode(feature,tmp.get(feature));
                //x[featureIndx].index=feature;
                //x[featureIndx].value=tmp.get(feature);
                featureIndx++;
            }
        
        
        
        
        //Feature[] instance = { new FeatureNode(1, 4), new FeatureNode(2, 2) };
        double prediction = Linear.predict(model, x);
        System.out.println(testInstance+"\t"+prediction);
       }
        
        
        
        
        
        
        
        
        
        //////////////////////////////////////////////////
        
        
        
        
        
        
        
        

        //Train the SVM model
        /*svm_problem prob=new svm_problem();
        //int numTrainingInstances=featuresTraining.keySet().size();
        prob.l=numTrainingInstances;
        prob.y=new double[prob.l];
        prob.x=new svm_node[prob.l][];

        for(int i=0;i<numTrainingInstances;i++){
            HashMap<Integer,Double> tmp=featuresTraining.get(i);
            prob.x[i]=new svm_node[tmp.keySet().size()];
            int indx=0;
            for(Integer id:tmp.keySet()){
                svm_node node=new svm_node();
                node.index=id;
                node.value=tmp.get(id);
                prob.x[i][indx]=node;
                indx++;
            }

            prob.y[i]=labelTraining.get(i);
        }
        
        svm_model model=svm.svm_train(prob,param);

        for(Integer testInstance:featuresTesting.keySet()){
            HashMap<Integer, Double> tmp=new HashMap<Integer, Double>();
            int numFeatures=tmp.keySet().size();
            svm_node[] x=new svm_node[numFeatures];
            int featureIndx=0;
            for(Integer feature:tmp.keySet()){
                x[featureIndx]=new svm_node();
                x[featureIndx].index=feature;
                x[featureIndx].value=tmp.get(feature);
                featureIndx++;
            }

            double d=svm.svm_predict(model, x);
            

            System.out.println(testInstance+"\t"+d);
        }

*/    
      }
}