/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bspline3d;

import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Point2D;
import static java.lang.Math.pow;
import java.util.Arrays;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
/**
 * @name CS381 B-Spline Project
 * @author Michael Blackwell
 * @date 4/14/16
 * @class CS 381
 */
public class BSpline3D extends Application {
    
    
    static int n = 12;                          // Number of control points - 1
    static int m = 15;                          // Number of knots - 1
    static Point3D[] points = new Point3D[n];   // 3-Dimensional control points 
    static int p = 2;                           // Degree
    static double[] knots = new double[m];      // knots
    static Point3D tempPoint;                   // Temporary 3D point holder
    double scale;                               // Scale of graph
    Canvas canvas;                              // 2-D drawing surface
    GraphicsContext gc;                         // Drawing tools
    StackPane root;                             // Needed for drawing
    
    @Override
    public void start(Stage primaryStage) {
        
        //Begin assigning point values
        points[0] = new Point3D(1,1,0);
        points[1] = new Point3D(0,3,0);
        points[2] = new Point3D(1,5,0);
        points[3] = new Point3D(2,5,0);
        points[4] = new Point3D(3,3,0);
        points[5] = new Point3D(2,1,0);
        points[6] = new Point3D(1,-1,0);
        points[7] = new Point3D(0,-3,0);
        points[8] = new Point3D(1,-5,0);
        points[9] = new Point3D(2,-5,0);
        points[10] = new Point3D(3,-3,0);
        points[11] = new Point3D(2,-1,0);
        
        //Begin assigning knot values
        knots[0] = 0;
        knots[1] = 0;
        knots[2] = 0;
        knots[3] = 0.1;
        knots[4] = 0.2;
        knots[5] = 0.3;
        knots[6] = 0.4;
        knots[7] = 0.5;
        knots[8] = 0.6;
        knots[9] = 0.7;
        knots[10] = 0.8;
        knots[11] = 0.9;
        knots[12] = 1.0;
        knots[13] = 1.0;
        knots[14] = 1.0;
        
        scale = 40;                         // Assign value for graph scale
        tempPoint = new Point3D(0,0,0);     // Give temporary point an arbitrary initial value
        canvas = new Canvas(1024, 768);     // Create a drawing surface 1024 x 768 pixels
        gc = canvas.getGraphicsContext2D(); // Assign drawing tools to drawing surface
        
        
        //Print initial values for points and knots
        System.out.println("Given Points:\n");
        for(int i = 0; i < n; i++){
            System.out.println("Point " + i + " = " + points[i]);
        }
      
        System.out.println("\nGiven Knots:\n");
        for(int i = 0; i < m; i++){
            System.out.println("Knot " + i + " = " + knots[i]);
        }
        
        //Draw control points
        for(int i = 0; i < points.length; i++){
            gc.setFill(Color.RED);
            gc.fillOval(points[i].getX() * scale + 300, 450 - points[i].getY() * scale, 8, 8);
        }
        
        //Draw and print C(t) values. 100 sample points are used
        System.out.println("\nC(t) Values:\n");
        for(double t = 0.0; t < 1.0; t += 0.01){
            gc.setFill(Color.BLACK);
            tempPoint = C(t);
            System.out.println("C(" + (float)t + ") = " + (float)tempPoint.getX() + " , " + (float)tempPoint.getY());
            gc.fillOval(tempPoint.getX() * scale + 300, 450 - tempPoint.getY() * scale, 5, 5);
        }
        
        // Display drawing surface on the screen
        root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.GREY);
        primaryStage.setTitle("BSpline Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @desc Start of program. Goes immediately to start()
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /*
    @desc computes basis function
    @param int i - current knot
    @param int j - current degree
    @param double t - current t [0,1]
    @return double - coefficient for current spot in recurring basis function
     */
    public static double N(int i, int j, double t){
        
        double a, b; // Temporary values
        
        // Special cases for j == 0
        if (j == 0){
            if ((knots[i] <= t) && (t < knots[i + 1])){
                return 1;
            }
            else {
                return 0;
            }
        }
        
        // Calculate first half of basis equation and assign to a
        a = ((t - knots[i]) / (knots[i + j] - knots[i])) 
                * N(i, j-1, t);
        
        if(Double.isNaN(a)){
            a = 0;
        }
        
        // Calculate second half of basis equation and assign to b
        b = ((knots[i + j + 1] - t) / (knots[i + j + 1] - knots[i + 1]) 
                * N(i + 1, j - 1, t));
        
        if(Double.isNaN(b)){
            b = 0;
        }
        
        // Return the next coefficient
        return a + b;
    }
    
    /*
    @desc calculate point along B-Spline
    @param double t - current t [0,1]
    @return Point3D - B-Spline point for t
    */
    public static Point3D C(double t){
        
        // Temporary 3-D point
        Point3D tempPoint2;
        
        // Assign origin for temporary point
        tempPoint2 = new Point3D(0,0,0);
        
        // Summate B-spline point: P(i) * N(i,p,t), n times
        for(int q = 0; q < n; q++){
            tempPoint2 = tempPoint2.add(points[q].multiply(N(q, p, t)));
            
        }
        
        // Return the B-spline point for t
        return tempPoint2;
    }
}