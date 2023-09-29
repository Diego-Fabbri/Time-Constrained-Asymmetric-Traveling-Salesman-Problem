
package com.mycompany.time_constrained_asymmetric_traveling_salesman_problem;

import Utility.Data;
import ilog.concert.IloException;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
   public static void main(String[] args) throws IloException, FileNotFoundException{
     System.setOut(new PrintStream("ATSPTW_Problem.log"));
     double [][] costs= Data.costs();
     double speed = Data.speed();
     double [][] travel_times= Data.travel_times(costs,speed);
   
    int problem_size= Data.Problem_size();
    double[] LB=Data.Lower_Bounds();
    double[] UB=Data.Upper_Bounds();
    
    ATSPTW_Model model = new ATSPTW_Model(problem_size,LB,UB,travel_times);
     model.solveModel();
}
}
