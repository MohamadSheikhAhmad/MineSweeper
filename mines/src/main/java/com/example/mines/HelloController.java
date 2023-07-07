package com.example.mines;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
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
    protected int wid;
    protected int hig;
    protected int mine;
    protected Stage thisStage;
    protected Stage gameStage;
    protected Scene Gamescene;
    protected VBox vbox;
    protected Label label;
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
            this.wid= 16;
            this.hig=16;
            this.mine=45;
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
                if(this.button.getText().equals(".")) {
                    try {
                        leftClick(event);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if(this.button.getText().equals(".")||this.button.getText().equals("F"))
                        rightClick();
            }
        }
        private void leftClick(MouseEvent event) throws InterruptedException {
            //OPEN BOX
            int index = gridPane.getChildren().indexOf(event.getSource());
            int i=index/wid;
            int j=index%hig;
            boolean flag=minesGame.tryopen(i,j);
            if(!flag)
                GameLost(i,j);
            String val="";
            for ( i = 0; i < wid; i++) {
                for ( j = 0; j < hig; j++) {
                    Button button = (Button) gridPane.getChildren().get((i*wid)+j);
                    val=minesGame.get(i,j);
                    if(!val.equals(".")&&!val.equals("F"))
                        setBackgroundColor(button,true,1);
                    button.setText(val);
                }
            }
            flag= minesGame.isDone();
            if(flag){
                JOptionPane.showMessageDialog(null, "Congratulations you won", "Try Again", JOptionPane.WARNING_MESSAGE);
                gridPane.setDisable(true);
            }
        }
        private void rightClick() {
            //TOGGLE FLAG
            if(minesGame.toggleFLag(i,j)){
                if(this.button.getText().equals(".")){
                    ImageView imageView = new ImageView();
                    imageView.setImage(new Image(getClass().getResourceAsStream("/icons/flag.png")));
                    imageView.fitWidthProperty().bind(button.widthProperty().divide(1.5));
                    imageView.setPreserveRatio(true);
                    button.setGraphic(imageView);
                    this.button.setText("F");
                }
                else if(this.button.getText().equals("F")){
                    button.setGraphic(null);
                    this.button.setText(".");
                }
            }

        }
        private void GameLost(int x,int y) throws InterruptedException {
            int minesFound = 0;
            int i=0,j=0;
            gridPane.setDisable(true);
            while (minesFound < mine) {
                Button button = (Button) gridPane.getChildren().get((i * wid) + j);
                if (minesGame.getMine(i, j)) {
                    setBackgroundColor(button, true, 2);
                    button.setText("M");
                    minesFound++;
                }
                j++;
                if(j==hig){
                    j=0;i++;
                }
            }
            JOptionPane.showMessageDialog(null, "Oppsss you lost", "Try Again", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void initialize() {

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
            vbox = new VBox();
            label = new Label("Mode:"+this.levelchooser.getValue()+"  Mines:"+this.mine);
            label.setStyle("-fx-font-size: 18pt");
            Button button2 = new Button("Home");
            button2.setStyle( "-fx-font-size: 14pt");
            button2.setOnAction(eventHandler);
            vbox.prefHeight(gridPane.getHeight());
            vbox.prefWidth(gridPane.getWidth());
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(15);
            vbox.getChildren().addAll(label, gridPane, button2);
            //vbox.autosize();
            Gamescene = new Scene(vbox);
            URL url = getClass().getResource("styles.css");
            String css = url.toExternalForm();
            Gamescene.getStylesheets().add(css);
            gameStage=new Stage();
            gameStage.setScene(Gamescene);
            gameStage.setResizable(false);
            gameStage.show();
        }
    }
    protected static void setBackgroundColor(Button button, boolean flag,int num) {
        if(num==0){//initial board
            if(flag)
                button.setStyle("-fx-background-color: #38E54D");
            else
                button.setStyle("-fx-background-color: #9CFF2E");
        }
        else if(num==1) {//open cells
            if(button.getStyle().equals("-fx-background-color: #38E54D"))
                button.setStyle("-fx-background-color: #C3EDC0");
            else if(button.getStyle().equals("-fx-background-color: #9CFF2E"))
                button.setStyle("-fx-background-color: #E9FFC2");
        }else{
            button.setStyle("-fx-background-color: red");

        }
    }
    EventHandler<ActionEvent> eventHandler = event -> {
        gameStage.close();
        thisStage.show();
    };
}