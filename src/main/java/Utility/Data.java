package Utility;

public class Data {
// V={0,1, 2, 3, 4,5,6,7}= 

    public static int Problem_size() {

        return 8; // depot + nodes to visit =|V|=n
    }
   public static double speed() {
// we assume that costs are distances, and time travels on arcs (i,j)in A are proportional due to a costant speed
// if costs are distances just set speed = 1
        return 1;
    }
    public static double[][] costs() {// matrix size 8x8
        // In this matrix triangle inequalities hold
        double[][] costs = {
            //0    1    2     3    4    5   6     7
            {Double.MAX_VALUE, 5.5, 4.2, 2.6, 2.4, 1.3, 2.5, 4.3,},// Node 0
            {4.7, Double.MAX_VALUE, 3.7, 2.1, 5.1, 6, 7.2, 9,},// Node 1
            {4.2, 4.5, Double.MAX_VALUE, 1.6, 3.2, 5.5, 6.7, 8.5,}, // Node 2
            {2.6, 2.9, 1.6, Double.MAX_VALUE, 3, 3.9, 5.1, 6.9,},// Node 3
            {3.8, 4.1, 2.8, 1.2, Double.MAX_VALUE, 5.1, 6.3, 8.1,},// Node 4
            {3.9, 7.4, 6.1, 4.5, 3.3, Double.MAX_VALUE, 1.2, 3,},// Node 5 
            {3.5, 7, 5.7, 4.1, 2.9, 1.2, Double.MAX_VALUE, 2.3,},// Node 6
            {5.8, 9.3, 8, 6.4, 5.2, 3, 2.3, Double.MAX_VALUE,},// Node 7
        };

        return costs;
    }

    public static double[][] travel_times(double[][] costs, double speed) {
        
        double[][] travel_times = new double[costs.length][costs[0].length];
        for (int i = 0; i < travel_times.length; i++) {
            for (int j = 0; j < travel_times[0].length; j++) {

                travel_times[i][j] = costs[i][j] / speed;
            }

        }

        return travel_times;
    }

    public static double[] Lower_Bounds() {
        double[] LB = {0, 0, 0, 0, 0, 0, 0, 0};
        return LB;
    }

    public static double[] Upper_Bounds() {
        double[] UB = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
        return UB;
    }

}
