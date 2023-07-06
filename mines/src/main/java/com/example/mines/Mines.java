package com.example.mines;

import java.util.Random;

public class Mines {
    private int height;
    private int width;
    private int minesNum;
    private boolean[][] mines;
    private String[][] board;
    //add mines manualy
    public void addMineMan(int i,int j){
        if(inBound(i,j)){
            if( !this.mines[i][j])
                this.mines[i][j]=true;
        }
    }
    public Mines(int height,int width,int nummines){
        this.height=height;
        this.width=width;
        this.minesNum=nummines;
        this.mines=new boolean[height][width];
        this.board=new String[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                this.board[i][j]=".";
                this.mines[i][j]=false;
            }
        }
        addMine();
    }
    //add mines in random positions
    private boolean addMine(){
        int count=0;
        Random random = new Random();
        while(count<this.minesNum){
            int row = random.nextInt(width);
            int column = random.nextInt(height);
            if(!this.mines[row][column]) {
                this.mines[row][column] = true;
                count++;
            }
        }
        return true;
    }
    //toggle a flag
    public void toggleFLag(int x,int y){
        if(board[x][y]=="F")
            board[x][y]=".";
        else
            board[x][y]="F";
    }
    public boolean isDone(){
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if(board[i][j]=="."&&mines[i][j]==false)
                    return false;
            }
        }
        return true;
    }
    private boolean inBound(int x,int y){
        if(!(x<0||y<0||x>=height||y>=width))
            return true;
        return false;
    }
    private int CheckNighborForMine(int i, int j){
        int count=0;
        for(int x=i-1;x<i+2;x++){
            for(int y=j-1;y<j+2;y++){
                if(inBound(x,y)) {
                    if(this.mines[x][y]==true)
                        count++;
                }
            }
        }
        return count;
    }
    public String get(int i ,int j){
        return this.board[i][j];
    }
    public boolean getMine(int i,int j){return this.mines[i][j];}
    public String toString(){
        String str="";
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                str+=get(i,j);
                str+=" ";
            }
            str+="\n";
        }
        return str;
    }
    public boolean tryopen(int i,int j){
        if(!inBound(i,j))
            return false;
        if(this.mines[i][j]==true)
            return false;
        if(this.board[i][j]==" "||this.board[i][j]=="F")
            return true;
        int count =CheckNighborForMine(i,j);
        if(count==0){
            this.board[i][j]=" ";
        }
        else{
            this.board[i][j]=Integer.toString(count);
        }
        if(this.board[i][j]==" "){
            tryopen(i-1,j-1);
            tryopen(i-1,j);
            tryopen(i-1,j+1);
            tryopen(i,j-1);
            tryopen(i,j+1);
            tryopen(i+1,j-1);
            tryopen(i+1,j);
            tryopen(i+1,j+1);
        }
        return true;
    }
}
