package edu.sdccd.cisc191.template;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.text.html.ImageView;
import java.awt.event.MouseEvent;

public class OptionButton extends Button {
    private String text;
    private double width;
    private double height;
    private boolean isClicked;
    private Paint textColor;

    public OptionButton (String text, double width, double height) {
        setText(text);
        setPrefWidth(width);
        setPrefHeight(height);
    }
    public void changeTextColor(Paint color){
        setTextFill(color);
    }
    public void setTextColor(int i){

    }
    public void changeTextColor(int i){
        switch (i) {
            case 0:
                setTextFill(Color.web("#FF0000"));
                break;
            case 1:
                setTextFill(Color.web("#0000FF"));
                break;
            case 2:
                setTextFill(Color.web("#FFFF00"));
                break;
            case 3:
                setTextFill(Color.web("#00FF00"));
                break;
            case 4:
                setTextFill(Color.web("#FFA500"));
                break;
            case 5:
                setTextFill(Color.web("#A020F0"));
            default:
        }
    }
    public void changeBackGroundColor(){
        setStyle("-fx-background-color: #98DBC5");
    }
    public void addImage(ImageView image){
    }
    public void handleClick() {
        isClicked = true;
    }
}