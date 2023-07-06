package com.example.mines;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class HelloController {

    public TextField width;
    public TextField hight;
    public AnchorPane currAnc;
    public Button Start;
    public TextField Minestext;
    public ComboBox levelchooser;
    private String level;
    private int size;
    private Button[][] buttons;
    private int wid;
    private int hig;
    private int mine;
    private Stage thisStage;
    private Stage gameStage;
    private GridPane gridPane;
    private Mines minesGame;

    public void LevelChooser(ActionEvent actionEvent) {
        level= (String) this.levelchooser.getValue();
        if (level.equals("Easy")) {
            this.wid=10;
            this.hig=10;
            this.mine=10;
            this.size=80;
        }
        if (level.equals("Medium")) {
            this.wid=15;
            this.hig=15;
            this.mine=40;
            this.size=10;

        }
        if (level.equals("Hard")) {
            this.wid=20;
            this.hig=20;
            this.mine=100;
            this.size=5;

        }
        this.width.setText(Integer.toString(wid));
        this.hight.setText(Integer.toString(hig));
        this.Minestext.setText(Integer.toString(mine));
        this.Start.setDisable(false);

    }

    public class GameListner implements EventHandler<MouseEvent> {
        private Button button;
        private int index ;
        private int i;
        private int j;
        public GameListner(Button button) {
            this.button = button;
        }
        @Override
        public void handle(MouseEvent event) {
           index = gridPane.getChildren().indexOf(event.getSource());
           i=index/wid;
           j=index%hig;
            if (event.getButton() == MouseButton.PRIMARY) {
                if(this.button.getText().equals("."))
                         leftClick(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if(this.button.getText().equals(".")||this.button.getText().equals("F"))
                        rightClick();
            }
        }
        public void leftClick(MouseEvent event) {
            //OPEN BOX
            int index = gridPane.getChildren().indexOf(event.getSource());
            int i=index/wid;
            int j=index%hig;
            boolean flag=minesGame.tryopen(i,j);
            if(!flag){
                //JOptionPane.showMessageDialog(null, "Oppsss you lost", "Try Again", JOptionPane.WARNING_MESSAGE);
                for ( i = 0; i < wid; i++) {
                    for ( j = 0; j < hig; j++) {
                       if(minesGame.getMine(i,j)){
                           Button button = (Button) gridPane.getChildren().get((i*wid)+j);
                           button.setText(minesGame.get(i,j));
                       }
                    }
                }
                //gameStage.close();
                //thisStage.show();
            }
            String val="";
            for ( i = 0; i < wid; i++) {
                for ( j = 0; j < hig; j++) {

                    Button button = (Button) gridPane.getChildren().get((i*wid)+j);

                    val=minesGame.get(i,j);
                    if(!val.equals("."))
                        setBackgroundColor(button,true,1);
                    button.setText(val);
                }
            }
            flag= minesGame.isDone();
            if(flag){
                JOptionPane.showMessageDialog(null, "congratz you won", "Try Again", JOptionPane.WARNING_MESSAGE);
            }
        }
        public void rightClick() {
            //TOGGLE FLAG
            if(this.button.getText().equals(".")){
                this.button.setText("F");
                minesGame.toggleFLag(i,j);
            }
            else if(this.button.getText().equals("F")){
                this.button.setText(".");
                minesGame.toggleFLag(i,j);
            }
        }
    }
    class TextFEildListner implements ChangeListener<String> {
        private final TextField myTextField;
        public TextFEildListner(TextField myTextField) {
            this.myTextField = myTextField;
        }
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            System.out.println();
            if(myTextField.getId().equals("width")){
                try{
                    wid=Integer.parseInt(newValue);
                }catch (NumberFormatException w){
                    System.out.println("Only int numbers");
                }
            }else {
                try{
                    hig=Integer.parseInt(newValue);
                }catch (NumberFormatException w){
                    System.out.println("Only int numbers");
                }
            }
            System.out.println(myTextField.getId() + " changed from " + oldValue + " to " + newValue + "!");
        }
    }
    public void initialize() {
        //width.textProperty().addListener(new TextFEildListner(width));
        //hight.textProperty().addListener(new TextFEildListner(hight));
        //Minestext.textProperty().addListener(new TextFEildListner(Minestext));
        width.setDisable(true);
        hight.setDisable(true);
        Minestext.setDisable(true);
        Start.setDisable(true);
        levelchooser.setItems(FXCollections.observableArrayList("Easy","Medium","Hard"));
    }
    public void reset(ActionEvent actionEvent) throws IOException {
        this.width.setText("");
        this.hight.setText("");
    }
    public void StartGameFunc(ActionEvent actionEvent) throws IOException {
        initialBoard();
    }
    private void initialBoard() throws IOException {
        if(wid>0&&hig>0){
            boolean flag=true;
            minesGame=new Mines(hig,wid,mine);
            buttons = new Button[wid][hig];
            System.out.println(            level);
            for (int i = 0; i < wid; i++) {
                for (int j = 0; j < hig; j++) {
                    buttons[i][j] = new Button(".");
                    buttons[i][j].getStyleClass().add(level);
                    setBackgroundColor( buttons[i][j],flag,0);
                    flag=!flag;
                    buttons[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED,new GameListner(buttons[i][j]));
                }
                flag=!flag;
            }
            gridPane = new GridPane();
            for (int i = 0; i < wid; i++) {
                for (int j = 0; j < hig; j++) {
                    gridPane.add(buttons[i][j], j, i);
                }
            }
            thisStage = (Stage) currAnc.getScene().getWindow();
            thisStage.setResizable(true);
            thisStage.close();
            VBox vbox = new VBox();
            Label label = new Label("Mode:"+this.levelchooser.getValue());
            Button button2 = new Button("Exit");
            button2.setOnAction(eventHandler);
            vbox.getChildren().addAll(label, gridPane, button2);
            vbox.autosize();
            Scene scene = new Scene(vbox);
            URL url = getClass().getResource("styles.css");
            String css = url.toExternalForm();
            scene.getStylesheets().add(css);
            gameStage=new Stage();
            gameStage.setScene(scene);
            gameStage.show();
        }
    }
    protected static void setBackgroundColor(Button button, boolean flag,int num) {
        if(num==0){
            if(flag)
                button.setStyle("-fx-background-color: #38E54D");
            else
                button.setStyle("-fx-background-color: #9CFF2E");
        }
        else {
            if(button.getStyle().equals("-fx-background-color: #38E54D"))
                button.setStyle("-fx-background-color: #C3EDC0");
            else if(button.getStyle().equals("-fx-background-color: #9CFF2E"))
                button.setStyle("-fx-background-color: #E9FFC2");
        }
    }
    EventHandler<ActionEvent> eventHandler = event -> {
        gameStage.close();
        thisStage.show();
    };
}