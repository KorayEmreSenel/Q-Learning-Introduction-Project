import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue; 

public class basicQ {

		static Queue<Integer> stepQue = new LinkedList<>();
		static double alpha = 0.1;
		static double gamma = 0.9;
		static double epsilon = 0.3;
		
        public static void main(String[] args) {
        int j = 0 ;
        int columnSize ; int rowSize;
        Scanner input = new Scanner(System.in);
     	System.out.println("Enter alpha value (default 0,1) =");
        alpha = input.nextDouble();
        System.out.println("Enter gamma value (default 0,9) =");
        gamma = input.nextDouble();
        System.out.println("Enter epsilon value (default 0,3) =");
        double epsilon =input.nextDouble();
        System.out.println("Enter column size =");
        columnSize =  input.nextInt();
        System.out.println("Enter row size =");
        rowSize = input.nextInt();
        int Size = rowSize*columnSize;
        double[][] Q = new double[Size][Size];
        int[][] possibleActions = new int[Size][4];
        possibleActions = fillActions(possibleActions,Size,rowSize);
        Random random = new Random();
        int counter = 0; 
        System.out.println("Enter the number of iterations =");
        int iteration = input.nextInt();
        System.out.println("Enter agents start point =");
        int startState = input.nextInt();
        System.out.println("Enter goal point =");
        int goalState = input.nextInt();
		for(int i =0 ;i<iteration;i++) {
		int agentState = startState;
        counter = episodes( iteration,  agentState, startState , goalState , counter , possibleActions, j , Q , random,Size);
        if(i>100) {
        	stepQue.add(counter);
        	j++;
        	if(j == 20) {
        		if(isLearned(j,counter)) {
        			break;
        		}
        		stepQue.remove();
        		j--;
        	}
        }
        if(epsilon > 0.001) {
        epsilon=epsilon-0.00005;
        }
        System.out.println("train "+ i +" solved at " + counter +" steps");
        counter = 0 ;
		}
        input.close();
        
        }
	
	
      static int Qstate(double[][] q,int agentState,int Size) {
    	  double max = 0;
    	  int act = -1;
    	  for(int i = 0 ; i<Size;i++) {
    	  if(max < q[agentState][i]) {
    		  max = q[agentState][i];
    		  act = i ; 
    	  }
    	  }
    	  
    	  return act;
      }
        
      public static int[][] fillActions(int possibleActions[][],int Size,int rowSize){
		  for(int i = 0 ; i<Size ; i++) {
			  if(i%rowSize != 0 && i-1 >= 0)
			  possibleActions[i][0] = i-1;
			  else 
			  possibleActions[i][0] = i;
			 
			  if(i%rowSize != rowSize-1 && i+1 < Size)
			  possibleActions[i][1] = i+1;
			  else
			  possibleActions[i][1]=i;
			  
			  if( i-rowSize >= 0)
			  possibleActions[i][2] = i-rowSize;
			  else 
			  possibleActions[i][2]=i;
			  
			  if( i+rowSize < Size)
			  possibleActions[i][3] = i+rowSize;
			  else 
			  possibleActions[i][3]=i;  
		  }
	        return possibleActions;
	}
	
	static void setQ(double Q[][],double value,int a ,int s){
		Q[a][s]=value;
	}
	
	static double maxQ(double[][] q,int actState,int Size) {
  	  double max = 0;
  	  for(int i = 0 ; i<Size;i++) {
  	  if(max < q[actState][i]) {
  		  max = q[actState][i];
  	  }
  	  }
  	  return max;
    }
	
	static boolean isLearned(int i,int counter) {
		int [] arr = new int [i];
		arr = stepQue.stream().mapToInt(Integer::intValue).toArray();
		double sumOfSample = 0;
		double variance;
		double sampleMean;
		for(int j = 0 ; j<i;j++)
			sumOfSample += arr[j];
		sampleMean = sumOfSample/i;
		double sumVari = 0 ;
		for(int j = 0 ; j<i;j++) {
			sumVari += Math.pow(((double)arr[j]-sampleMean),2);
		}
		variance = sumVari/(double)(i-1);
		if(variance < 0.1) {
			System.out.println("solved at "+ counter+" steps");
		return true;
		}
		else return false;
	}

	static int episodes(int iteration, int agentState,int startState , int goalState , int counter , int possibleActions [][],int j , double Q[][] ,Random random,int Size) {
        while(agentState != goalState) {
        	counter ++;
        	int nextState = agentState;
        	int act = 0;double reward = 0;
        	double rand = random.nextDouble();
        	if(epsilon>rand) {
        	act = random.nextInt(4);
        	nextState = possibleActions[agentState][act];
        	}
        	else 
        		if(Qstate(Q,agentState,Size)<Size && Qstate(Q,agentState,Size)!=-1)
        	nextState = Qstate(Q,agentState,Size);
        		else
        			act =  random.nextInt(4);
        	if(nextState == agentState)
        	 nextState = possibleActions[agentState][act];
        	double q = Q[agentState][nextState];
        	double max =  maxQ(Q,nextState,Size);
        	if(nextState==goalState)
        		reward = 100;
        	double value = q + alpha * (reward + gamma * max - q);
        	if(value > 0)
        	setQ(Q,value,agentState,nextState);
        	agentState = nextState;
        }
        return counter;
       
	}
}