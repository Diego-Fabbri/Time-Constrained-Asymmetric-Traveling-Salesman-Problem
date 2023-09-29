package com.mycompany.time_constrained_asymmetric_traveling_salesman_problem;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloObjectiveSense;
import ilog.cplex.IloCplex;

public class ATSPTW_Model {

    protected IloCplex model;

    protected int n;
    protected double[][] travel_time;
    protected double[] LB;
    protected double[] UB;
    protected IloNumVar[] t;
    protected IloIntVar[][] y;

    ATSPTW_Model(int problem_size, double[] LB, double[] UB, double[][] travel_time) throws IloException {
        this.n = problem_size;

        this.travel_time = travel_time;
        this.LB = LB;
        this.UB = UB;
        this.model = new IloCplex();
        this.t = new IloNumVar[n + 1];// t_i= 0....n+1
        this.y = new IloIntVar[n][n];

    }
// create variables

    protected void addVariables() throws IloException {
        for (int i = 0; i <= n; i++) {

            t[i] = (IloNumVar) model.numVar(0, Float.MAX_VALUE, IloNumVarType.Float, "t[" + i + "]");

        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(i!=j)
                y[i][j] = (IloIntVar) model.numVar(0, 1, IloNumVarType.Int, "y[" + i + "][" + j + "]");

            }

        }
    }

    //The following code creates the objective function for the problem
    protected void addObjective() throws IloException {
        IloLinearNumExpr objective = model.linearNumExpr();
        objective.addTerm(t[n], 1);// t_(n+1)
        IloObjective Obj = model.addObjective(IloObjectiveSense.Minimize, objective);
    }

    //The following code creates the constraints for the problem.
    protected void addConstraints() throws IloException {
        // Set initial value of t_0
        model.addEq(t[0], 0);

        // Constrain (2)
        for (int i = 1; i <= n - 1; i++) {
            IloLinearNumExpr expr_2 = model.linearNumExpr();
            expr_2.addTerm(t[i], 1);
            expr_2.addTerm(-travel_time[0][i], y[0][i]);
            model.addGe(expr_2, 0);
        }
        // Constrain (3)

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                if (i != j) {
                    double M = (UB[i] - LB[j] + travel_time[i][j]);
                    IloLinearNumExpr expr_3 = model.linearNumExpr();
                    expr_3.addTerm(t[i], 1);
                    expr_3.addTerm(t[j], -1);
                    expr_3.addTerm(y[i][j], M);
                    model.addLe(expr_3, UB[i] - LB[j]);
               }

            }
        }

        // Constrain (4)
        for (int j = 1; j <= n - 1; j++) {
            IloLinearNumExpr expr_4 = model.linearNumExpr();
            for (int i = 0; i < n; i++) {
                if(i!=j)
                expr_4.addTerm(y[i][j], 1);

            }
            model.addEq(expr_4, 1);
        }

        // Constrain (5)
        for (int i = 1; i <= n - 1; i++) {
            IloLinearNumExpr expr_5 = model.linearNumExpr();
            for (int j = 0; j < n; j++) {
                if(i!=j)
                expr_5.addTerm(y[i][j], 1);

            }
            model.addEq(expr_5, 1);

        }

        // Constrain (6)
        for (int i = 1; i <= n - 1; i++) {
            IloLinearNumExpr expr_6 = model.linearNumExpr();
            expr_6.addTerm(t[i], 1);
            expr_6.addTerm(t[n], -1);
            model.addLe(expr_6, -travel_time[i][0]);
        }
        // Constrain (7 lower bounds)
        for (int i = 1; i <= n - 1; i++) {
            model.addGe(t[i], LB[i]);
        }
        // Constain (7 upper bounds)
        for (int i = 1; i <= n - 1; i++) {
            model.addLe(t[i], UB[i]);
        }
        
        // Constrain (8)
        IloLinearNumExpr expr_8 = model.linearNumExpr();
        for (int i = 1; i <= n - 1; i++) {
            expr_8.addTerm(y[i][0], 1);
        }
            model.addEq(expr_8, 1);
            
        // Constrain (9)
        IloLinearNumExpr expr_9 = model.linearNumExpr();
        for (int j = 1; j <= n - 1; j++) {
            expr_9.addTerm(y[0][j], 1);
        }
            model.addEq(expr_9, 1);

        }

    

    public void solveModel() throws IloException {
        addVariables();
        addObjective();
        addConstraints();
        model.exportModel("ATSPTW_Problem.lp");

        model.solve();
        System.out.println("Matrix of time distances is");
        for (int i = 0; i < n; i++) {
            System.out.println("Node: " + i);
            for (int j = 0; j < n; j++) {
                System.out.print(" " + travel_time[i][j] + " ");
            }
            System.out.println();
        }

        if (model.getStatus() == IloCplex.Status.Feasible
                | model.getStatus() == IloCplex.Status.Optimal) {
            System.out.println();
            System.out.println("Solution status = " + model.getStatus());
            System.out.println();
            System.out.println("Problem size: " + n);

            System.out.println();
            System.out.println("Makespan " + model.getObjValue());
            System.out.println();

            System.out.println("The variables t_{i} ");

            for (int i = 0; i <= n - 1; i++) {
                if (i == 0) {
                    System.out.println(" Tour from Node " + i + " begins at time " + model.getValue(t[i]) + " <--- " + t[i].getName());
                } else {
                    System.out.println("----> Node: " + i + " is visited at time " + model.getValue(t[i]) + " <--- " + t[i].getName());
                }
            }
            System.out.println();
            System.out.println("The variables y_{ij} ");            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i!=j && model.getValue(y[i][j]) != 0 ){
                System.out.println(y[i][j].getName()+" = " +  model.getValue(y[i][j]));
                    }
            }  
            }
            }
    else {
            System.out.println();
            System.out.println("The problem status is: " + model.getStatus());
        }

    }
}
