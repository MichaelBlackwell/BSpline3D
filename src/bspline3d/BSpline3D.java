/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bspline3d;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
    double originX;
    double originY;
    Canvas canvas;                              // 2-D drawing surface
    GraphicsContext gc;                         // Drawing tools
    StackPane root;                             // Needed for drawing
    
    int orientation;
    
    TextField nTxtBox;
    TextField pTxtBox;
    
    @Override
    public void start(Stage primaryStage) {
        
        //Begin assigning point values
        points[0] = new Point3D(1,1,1);
        points[1] = new Point3D(0,3,2);
        points[2] = new Point3D(1,5,3);
        points[3] = new Point3D(2,5,4);
        points[4] = new Point3D(3,3,5);
        points[5] = new Point3D(2,1,6);
        points[6] = new Point3D(1,-1,6);
        points[7] = new Point3D(0,-3,5);
        points[8] = new Point3D(1,-5,4);
        points[9] = new Point3D(2,-5,3);
        points[10] = new Point3D(3,-3,2);
        points[11] = new Point3D(2,-1,1);
        
        //Begin assigning knot values
        createKnots();
        
        scale = 40;                         // Assign value for graph scale
        originX = 300;
        originY = 450;
        orientation = 0;
        
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
        
        
        
        //begin animation loop
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.setFill(Color.LIGHTGREY);
                gc.fillRect(0, 0, 1024, 768);
                
                // Draw axes
                gc.setFill(Color.BLACK);
                
                // first axis
                gc.strokeLine(originX,originY,originX + 10 * scale,originY);
                for(int i = 1; i < 10; i++){
                    gc.strokeLine(originX + i * scale,originY - 0.2 * scale,originX + i * scale,originY + 0.2 * scale);
                }
                
                // second axis
                gc.strokeLine(originX,originY,originX,originY - 10 * scale);
                for(int i = 1; i < 10; i++){
                    gc.strokeLine(originX - 0.2 * scale,originY - i * scale,originX + 0.2 * scale,originY - i * scale);
                }

                //X-Y Axes
                if(orientation == 0){
                    //Draw control points
                    for(int i = 0; i < n; i++){
                        gc.setFill(Color.RED);
                        gc.fillOval(points[i].getX() * scale + originX - 4, originY - points[i].getY() * scale - 4, 8, 8);
                    }

                    //Draw and print C(t) values. 100 sample points are used
                    //System.out.println("\nC(t) Values:\n");
                    for(double t = 0.0; t < 1.0; t += 0.01){
                        gc.setFill(Color.BLACK);
                        tempPoint = C(t);
                        //System.out.println("C(" + (float)t + ") = " + (float)tempPoint.getX() + " , " + (float)tempPoint.getY());
                        gc.fillOval(tempPoint.getX() * scale + originX - 2, originY - tempPoint.getY() * scale - 2, 4, 4);
                    }
                }
                
                //Y-Z Axes
                if(orientation == 1){
                    //Draw control points
                    for(int i = 0; i < n; i++){
                        gc.setFill(Color.RED);
                        gc.fillOval(points[i].getY() * scale + originX - 4, originY - points[i].getZ() * scale - 4, 8, 8);
                    }

                    //Draw and print C(t) values. 100 sample points are used
                    //System.out.println("\nC(t) Values:\n");
                    for(double t = 0.0; t < 1.0; t += 0.01){
                        gc.setFill(Color.BLACK);
                        tempPoint = C(t);
                        //System.out.println("C(" + (float)t + ") = " + (float)tempPoint.getY() + " , " + (float)tempPoint.getZ());
                        gc.fillOval(tempPoint.getY() * scale + originX - 2, originY - tempPoint.getZ() * scale - 2, 4, 4);
                    }
                }
                
                //X-Z Axes
                if(orientation == 2){
                    //Draw control points
                    for(int i = 0; i < n; i++){
                        gc.setFill(Color.RED);
                        gc.fillOval(points[i].getX() * scale + originX - 4, originY - points[i].getZ() * scale - 4, 8, 8);
                    }

                    //Draw and print C(t) values. 100 sample points are used
                    //System.out.println("\nC(t) Values:\n");
                    for(double t = 0.0; t < 1.0; t += 0.01){
                        gc.setFill(Color.BLACK);
                        tempPoint = C(t);
                        //System.out.println("C(" + (float)t + ") = " + (float)tempPoint.getX() + " , " + (float)tempPoint.getZ());
                        gc.fillOval(tempPoint.getX() * scale + originX - 2, originY - tempPoint.getZ() * scale - 2, 4, 4);
                    }
                }
            }
        }.start();
        
        //Input dialoge box
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        
        
        
        //input for control points 
        TextField[] pointTxtBox = new TextField[n];
        for(int i = 0; i < n; i++){
            pointTxtBox[i] = new TextField();
            pointTxtBox[i].setPromptText("Enter Point " + i + " \"0,0,0\":");
            pointTxtBox[i].setPrefColumnCount(10);
            pointTxtBox[i].getText();
            GridPane.setConstraints(pointTxtBox[i], 0, i);
            grid.getChildren().add(pointTxtBox[i]);
        }
        
        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
        
        //setup X-Y Axes
        Button xyAxes = new Button("X-Y axes");
        GridPane.setConstraints(xyAxes, 1, 1);
        grid.getChildren().add(xyAxes);
        
        //setup Y-Z Axes
        Button yzAxes = new Button("Y-Z axes");
        GridPane.setConstraints(yzAxes, 1, 2);
        grid.getChildren().add(yzAxes);
        
        //setup X-Z Axes
        Button xzAxes = new Button("X-Z axes");
        GridPane.setConstraints(xzAxes, 1, 3);
        grid.getChildren().add(xzAxes);
        
        //input scale
        TextField scaleTxtBox = new TextField();
        scaleTxtBox.setPromptText("Scale (default=40):");
        scaleTxtBox.setPrefColumnCount(10);
        GridPane.setConstraints(scaleTxtBox, 1, 4);
        grid.getChildren().add(scaleTxtBox);
        
        //input n
        nTxtBox = new TextField();
        nTxtBox.setPromptText("Number of CP's:");
        nTxtBox.setPrefColumnCount(10);
        GridPane.setConstraints(nTxtBox, 1, 5);
        grid.getChildren().add(nTxtBox);
        
        //input n
        TextField pTxtBox = new TextField();
        pTxtBox.setPromptText("Degree:");
        pTxtBox.setPrefColumnCount(10);
        GridPane.setConstraints(pTxtBox, 1, 6);
        grid.getChildren().add(pTxtBox);
        
        
        //Setting an action for the Submit button
        submit.setOnAction(new EventHandler<ActionEvent>() {

        @Override
            public void handle(ActionEvent e) {
                
                
                //parse input points
                String[] input = new String[n];
                String delims = "[,]";
                String[] curInput = new String[3];
                
                for(int i = 0; i < n; i++){
                    input[i] = pointTxtBox[i].getText();
                }
                
                for(int i = 0; i < n; i++){
                    curInput = input[i].split(delims);
                    if(!curInput[0].isEmpty()){
                        points[i] = new Point3D(Double.parseDouble(curInput[0]),
                                                Double.parseDouble(curInput[1]),
                                                Double.parseDouble(curInput[2]));
                    }
                }
                
                //handle scale
                if(!scaleTxtBox.getText().isEmpty()){
                    scale = Double.parseDouble(scaleTxtBox.getText());
                }
                
                if(!nTxtBox.getText().isEmpty()){
                    n = Integer.parseInt(nTxtBox.getText());
                    
                    createKnots();  
                }
                
                if(!pTxtBox.getText().isEmpty()){
                    p = Integer.parseInt(pTxtBox.getText());
                    
                    createKnots();
                }
            }
         });
        
        //steup action for xyAxes button
        xyAxes.setOnAction(new EventHandler<ActionEvent>() {

        @Override
            public void handle(ActionEvent e) {
                
                orientation = 0;
                
            }
         });
        
        //steup action for yzAxes button
        yzAxes.setOnAction(new EventHandler<ActionEvent>() {

        @Override
            public void handle(ActionEvent e) {
                
                orientation = 1;
                
            }
         });
        
        //steup action for xzAxes button
        xzAxes.setOnAction(new EventHandler<ActionEvent>() {

        @Override
            public void handle(ActionEvent e) {
                
                orientation = 2;
                
            }
         });
        
        
        
        
        // Display drawing surface on the screen
        root = new StackPane();
        root.getChildren().add(canvas);
        root.getChildren().add(grid);
        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.LIGHTGREY);
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
    
    public static void createKnots(){
        
        m = n + p + 1;
        knots = new double[m];
        
        //non-uniform knot distribution
        
        for(int i = 0; i < p + 1; i++){
            knots[i] = 0.0;
            //System.out.println("knot " + i + " = " + knots[i]);
        }                   
        
        for(int j = 0; j < n - p - 1; j++){
            knots[j + p + 1] = ((double)j + 1.0) / ((double)n - (double)p);
            //System.out.println("knot " + (j + p + 1) + " = " + knots[j + p + 1]);
        }
        
        for(int i = m - (p + 1); i < m; i++){
            knots[i] = 1.0;
            //System.out.println("knot " + i + " = " + knots[i]);
        }
        
        //uniform knot distribution
        /*
        for(int i = 0; i < m; i++){
            knots[i] = (double)i / (double)(m-1);
        }*/
        
    }
}