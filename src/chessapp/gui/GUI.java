/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessapp.gui;

import chessapp.controller.*;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 *
 * @author Toothpaste666
 */
public class GUI {
    Controller controller;
    
    StackPane selectedCell;
    
    StackPane[][] guiBoard;
    GridPane boardDisplay;
    HBox topBar;
    
    HBox bottomBar;
    VBox capturedPieceDisplay;
    HBox whitesCapturedPieces;
    HBox blacksCapturedPieces;
    VBox messageDisplay;
    
    Scene lobbyScene;
    Pane lobbyPane;
    Scene gameScene;
    BorderPane gamePane;
    StackPane boardDisplayContainer;
    Stage primaryStage;
    
    ChoiceDialog<String> pawnPromotionDialog;
    Dialog<Pair<String, String>> loginDialog; 
    Alert stalemateAlert;
    Alert checkmateAlert;
    Alert insufficientMaterialAlert;
    Alert drawAgreedAlert;
    Alert disconnectAlert;
    Alert resignAlert;
    Alert winByTimeAlert;
    Alert loseByTimeAlert;
    
    VBox waitingBox;
    
    int enemyGameMins;
    int enemyGameSecs;
    int playerGameMins;
    int playerGameSecs;
    Label playerTimeLabel;
    Label enemyTimeLabel;
    Timeline enemyTimeline;
    Timeline playerTimeline;
    HBox playerClock;
    HBox enemyClock;
    
    public GUI(Stage primaryStage)
    {
        guiBoard = new StackPane[8][8];
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false); 
        this.primaryStage.show();
        this.primaryStage.centerOnScreen();
               
        initGameScene();
        initLobbyScene();
        initLoginDialog();
        initPawnPromotionDialog();
            
        goToLobby();    
    }
    
    public void flipBoardDisplay(){
        boardDisplay.setRotate(180);
    }
    
    public void goToLobby()
    {
        primaryStage.setTitle("Lobby");
        primaryStage.setScene(lobbyScene);
    }
    
    public void setTitle(String title){
        primaryStage.setTitle(title);
    }
    
    public void setController(Controller controller){
        this.controller = controller;
    }
    
    private void initWaitingBox(){
        waitingBox = new VBox();
        waitingBox.setStyle("-fx-background-color: LightGray;\n"
                        + "-fx-border-color: Blue");
        waitingBox.setMaxHeight(100);
        waitingBox.setMaxWidth(200);
        waitingBox.setAlignment(Pos.CENTER);
        waitingBox.getChildren().add(new Label("Waiting For Opponent..."));
        Button cancelButton = new Button("Cancel");
        
        cancelButton.setOnAction(k->{
            controller.writeToServer("CB");
            removeWaitingBox();
            goToLobby();
        });
        waitingBox.getChildren().add(cancelButton);
    }
    
    void initGameScene(){
        initBoardDisplay();
        initWaitingBox();
        initCheckmateAlert();
        initStalemateAlert();
        initInsufficientMaterialAlert(); 
        initDrawAgreedAlert();
        initDisconnectAlert();
        initResignAlert();
        initWinByTimeAlert();
        initLoseByTimeAlert();
    }
    
    public void removeWaitingBox(){
        boardDisplayContainer.getChildren().remove(waitingBox);
    }
    
    public void goToGame(){
        boardDisplayContainer.getChildren().add(waitingBox);
        setTitle("Waiting For Opponent...");
        primaryStage.setScene(gameScene);
        selectedCell = null;
    }
    
    void initLobbyScene(){
        lobbyPane = new Pane();
        lobbyScene = new Scene(lobbyPane, 70 * 8, 70 * 8);
        Button newGameButton = new Button("New Game");
        lobbyPane.getChildren().add(newGameButton);
        
        
        newGameButton.setOnAction(e->
        {            
            controller.writeToServer("NG"); 
            
            goToGame();
        });
    }
    
    public Pair<String, String> getLoginInfo(){
        Optional<Pair<String, String>> result = loginDialog.showAndWait();
        Pair<String, String> resultPair = result.get();
        return resultPair;
    }
    
    public void displayLoginFailed(){
        loginDialog.setHeaderText("Failed to Connect to Server.");
    }
    
    void initLoginDialog(){
        loginDialog = new Dialog<>();
        loginDialog.setTitle("Login");
        loginDialog.setHeaderText("Login");
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        loginDialog.getDialogPane().getButtonTypes().add(loginButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Username");
        TextField ipAddress = new TextField();
        ipAddress.setPromptText("X.X.X.X");
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("IP Address of Server:"), 0, 1);
        grid.add(ipAddress, 1, 1);
        
        Node loginButton = loginDialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        
        username.textProperty().addListener((observable, oldValue, newValue)->{
            if(username.getText().isEmpty() || ipAddress.getText().isEmpty()){
                loginButton.setDisable(true);
            }else{
                loginButton.setDisable(false);
            }
        });
        
        ipAddress.textProperty().addListener((observable, oldValue, newValue)->{
            if(username.getText().isEmpty() || ipAddress.getText().isEmpty()){
                loginButton.setDisable(true);
            }else{
                loginButton.setDisable(false);
            }
        });
        
        loginDialog.getDialogPane().setContent(grid);
        
        loginDialog.setResultConverter(dialogButton -> {
            if(dialogButton == loginButtonType){
                return new Pair<>(username.getText(), ipAddress.getText());
            }
            return null;
        });
        
        Stage stage = (Stage) loginDialog.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });        
    }
    
    public void initPawnPromotionDialog()
    {
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("Queen");
        choices.add("Rook");
        choices.add("Bishop");
        choices.add("Knight");
        pawnPromotionDialog = new ChoiceDialog<>("Queen", choices);
        pawnPromotionDialog.setTitle("Promote Your Pawn");
        pawnPromotionDialog.setHeaderText(null);
        pawnPromotionDialog.setContentText("Promote pawn to: ");
        
        Stage stage = (Stage) pawnPromotionDialog.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest((WindowEvent) -> {
            WindowEvent.consume();
        });
        
        final Button cancelButton = (Button) pawnPromotionDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setVisible(false);
        
    }
    
    public String getPawnPromotionInfo(){
        Optional<String> result = pawnPromotionDialog.showAndWait();
        String choice = "";
        choice = result.get();
        return choice;
    }
      
    public void initBoardDisplay()
    {
        boardDisplay = new GridPane();           
               
        StackPane cell;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col ++) {
                cell = new StackPane();
          
                cell.setMinWidth(70);
                cell.setMaxWidth(70);
                cell.setMinHeight(70);
                cell.setMaxHeight(70);
                
                if ((row + col) % 2 == 0) {
                    cell.setStyle("-fx-background-color: LightGreen;");
                } else {
                    cell.setStyle("-fx-background-color: Green;");
                }
                
                boardDisplay.add(cell, col, row);                        
                guiBoard[row][col] = cell;
                initCellHandler(cell);
            }
        }
        
        boardDisplayContainer = new StackPane();
        boardDisplayContainer.setAlignment(Pos.CENTER);
        boardDisplayContainer.getChildren().add(boardDisplay);
        
        gamePane = new BorderPane();
        gamePane.setCenter(boardDisplayContainer);
        
        //top bar
        topBar = new HBox();
        topBar.setMinHeight(40);
        topBar.setMaxHeight(40);
        topBar.setStyle("-fx-background-color: LightGray;\n"
               + "-fx-border-color: Blue");
        gamePane.setTop(topBar);
        
        //bottom bar
        bottomBar = new HBox();
        bottomBar.setMinHeight(80);
        bottomBar.setMaxHeight(80);
        bottomBar.setStyle("-fx-background-color: LightGray;\n"
               + "-fx-border-color: Blue");
        gamePane.setBottom(bottomBar);
        
        gameScene = new Scene(gamePane, 70 * 8, 70 * 8 + 120);          
    }
    
    public HBox getWhitesCapturedPieces(){
        return whitesCapturedPieces;
    }
    
    public HBox getBlacksCapturedPieces(){
        return blacksCapturedPieces;
    }
    
    public void addCapturedPieceDisplay(){
        capturedPieceDisplay.getChildren().clear();
        whitesCapturedPieces = new HBox();
        blacksCapturedPieces = new HBox();
        whitesCapturedPieces.setMinHeight(20);
        blacksCapturedPieces.setMinHeight(20);
        Label label = new Label("Captured Pieces:");
        label.setStyle("-fx-font-weight: bold;");
        capturedPieceDisplay.getChildren().add(label);
        capturedPieceDisplay.getChildren().add(whitesCapturedPieces);
        capturedPieceDisplay.getChildren().add(blacksCapturedPieces);    
        
        whitesCapturedPieces.getChildren().add(new Label("      White: "));
        blacksCapturedPieces.getChildren().add(new Label("      Black: "));
    }
    
    public void clearMessageDisplay(){
        messageDisplay.getChildren().clear();
    }
    
    public void displayDrawOffer(){
        clearMessageDisplay();
        Label label = new Label(controller.getEnemyUsername());
        messageDisplay.getChildren().add(label);
        label = new Label(" offers a draw. Accept?");
        messageDisplay.getChildren().add(label);
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No ");
        
        buttonBox.getChildren().add(yesButton);
        buttonBox.getChildren().add(noButton);  
        messageDisplay.getChildren().add(buttonBox);
        
        noButton.setOnAction(e->{
            controller.writeToServer("DD");         
            clearMessageDisplay();
        });
        
        yesButton.setOnAction(e->{
            controller.writeToServer("AD");
            clearMessageDisplay();
            
            controller.setInGame(false);
            showDrawAgreedAlert();                                  
            controller.startNewGame();
        });
    }
    
    public void displayDrawOfferSent(){
        clearMessageDisplay();
        Label label = new Label("Draw offer sent.");
        messageDisplay.getChildren().add(label);
        
        Button okButton = new Button("Ok");        
        messageDisplay.getChildren().add(okButton);
        
        okButton.setOnAction(e->{
            clearMessageDisplay();
        });
    }
    
    public void displayDrawDeclined(){
        clearMessageDisplay();
        Label label = new Label(controller.getEnemyUsername());
        messageDisplay.getChildren().add(label);  
        label = new Label("declines your draw offer.");
        messageDisplay.getChildren().add(label);
        
        Button okButton = new Button("Ok");        
        messageDisplay.getChildren().add(okButton);
        
        okButton.setOnAction(e->{
            clearMessageDisplay();
        });
    }
    
    public void displayDrawAccepted(){
        clearMessageDisplay();
        Label label = new Label(controller.getEnemyUsername());
        messageDisplay.getChildren().add(label);  
        label = new Label("accepts your draw offer.");
        messageDisplay.getChildren().add(label);
    }
    
    public void addBottomBar(){
        capturedPieceDisplay = new VBox();
        bottomBar.getChildren().add(capturedPieceDisplay);
        capturedPieceDisplay.setMinWidth(300);
               

        messageDisplay = new VBox();
        bottomBar.getChildren().add(messageDisplay);
    
        messageDisplay.setAlignment(Pos.CENTER);
        messageDisplay.setMinWidth(160);
        
        addPlayerClock();
    }
      
    public void addTopBar(String enemyUsername)
    {    
        HBox buttonBox = new HBox();
        Button resignButton = new Button("  Resign  ");
        Button drawButton = new Button("Offer Draw");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setMinWidth(175);
        buttonBox.setMaxWidth(175);
        buttonBox.getChildren().add(resignButton);
        buttonBox.getChildren().add(drawButton);
        resignButton.setOnAction(e->{
            controller.resignButtonPressed();
        });
        
        drawButton.setOnAction(e->{
            controller.writeToServer("OD");
            displayDrawOfferSent();
        });
        
        HBox nameBox = new HBox();
        Label enemyName = new Label("Playing Against: " + enemyUsername);
        nameBox.setAlignment(Pos.CENTER);
        nameBox.setMinWidth(285);
        nameBox.setMaxWidth(285);
        nameBox.getChildren().add(enemyName);
        topBar.getChildren().add(buttonBox);
        topBar.getChildren().add(nameBox); 
        addEnemyClock();
    }
    
    public void addPlayerClock(){
        playerClock = new HBox();
        playerClock.setMinWidth(100);
        playerClock.setMaxWidth(100);
        playerClock.setAlignment(Pos.CENTER);
        playerTimeLabel = new Label("5:00");
        playerGameMins = 5;
        playerGameSecs = 0;
        playerTimeline = new Timeline();
        playerTimeline.setCycleCount(Timeline.INDEFINITE);
        playerTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e->{
            if(playerGameSecs == 0){
                if(playerGameMins == 0){
                    playerTimeline.stop();
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            controller.setInGame(false);
                            showLoseByTimeAlert();
                            controller.startNewGame();
                        }                
                    }); 
                }
                else{
                    playerGameMins -= 1;
                    playerGameSecs += 59;
                }               
            }else{
                playerGameSecs -= 1;
            }
            playerTimeLabel.setText(String.format("%d:%02d", playerGameMins, playerGameSecs));
        }));
    
        playerClock.getChildren().add(playerTimeLabel); 
        bottomBar.getChildren().add(playerClock);
    }
    
    public void startPlayerClock(){
        playerTimeline.play();
        playerTimeLabel.setStyle("-fx-text-fill: red;");
    }
    
    public void pausePlayerClock(){
        playerTimeline.pause();
        playerTimeLabel.setStyle(null);
    }
    
    public void stopClocks(){
        playerTimeline.stop();
        enemyTimeline.stop();
    }
    
    public void startEnemyClock(){
        enemyTimeline.play();
        enemyTimeLabel.setStyle("-fx-text-fill: red;");
    }
    
    public void pauseEnemyClock(){
        enemyTimeline.pause();
        enemyTimeLabel.setStyle(null);
    }
    
    public void addEnemyClock(){
        enemyClock = new HBox();
        enemyClock.setMinWidth(100);
        enemyClock.setMaxWidth(100);
        enemyClock.setAlignment(Pos.CENTER);
        enemyTimeLabel = new Label("5:00");
        enemyGameMins = 5;
        enemyGameSecs = 0;
        enemyTimeline = new Timeline();
        enemyTimeline.setCycleCount(Timeline.INDEFINITE);
        enemyTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e->{
            if(enemyGameSecs == 0){
                if(enemyGameMins == 0){                  
                    enemyTimeline.stop();
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            controller.setInGame(false);
                            showWinByTimeAlert();
                            controller.startNewGame();
                        }                
                    });                  
                }
                else{
                    enemyGameMins -= 1;
                    enemyGameSecs += 59;
                }               
            }else{
                enemyGameSecs -= 1;
            }
            enemyTimeLabel.setText(String.format("%d:%02d", enemyGameMins, enemyGameSecs));
        }));

        enemyClock.getChildren().add(enemyTimeLabel);
        topBar.getChildren().add(enemyClock);
    }
    
    private void initCheckmateAlert(){
        checkmateAlert = new Alert(Alert.AlertType.INFORMATION, "CHECKMATE!", ButtonType.OK);
        checkmateAlert.setTitle("Checkmate");
        checkmateAlert.setHeaderText(null);      
    }
    
    public void showCheckmateAlert(String color){
        if(controller.getPlayerColorString().equals(color)){
            setTitle("Chess App: You Win!");
            checkmateAlert.setContentText("You win by checkmate." );
        }
        else{
            String nameOfWinner = controller.getEnemyUsername();
            setTitle("Chess App: " + nameOfWinner + " Wins!");
            checkmateAlert.setContentText(nameOfWinner + " wins by checkmate." );
        }
        
        checkmateAlert.showAndWait();
    }
    
    private void initStalemateAlert(){
        stalemateAlert = new Alert(Alert.AlertType.INFORMATION, "DRAW!", ButtonType.OK);
        stalemateAlert.setTitle("Draw");
        stalemateAlert.setHeaderText(null);
        stalemateAlert.setContentText("Draw: Stalemate.");  
    }
    
    public void showStalemateAlert(){
        setTitle("Chess App: Draw!"); 
        stalemateAlert.showAndWait();
    }
    
    private void initDrawAgreedAlert(){
        drawAgreedAlert = new Alert(Alert.AlertType.INFORMATION, "DRAW!", ButtonType.OK);
        drawAgreedAlert.setTitle("Draw!");
        drawAgreedAlert.setHeaderText(null);
        drawAgreedAlert.setContentText("Draw: Players in agreement.");   
    }
    
    public void showDrawAgreedAlert(){
        setTitle("Chess App: Draw!");
        drawAgreedAlert.showAndWait();
    }
    
    private void initDisconnectAlert(){
        disconnectAlert = new Alert(Alert.AlertType.INFORMATION, "YOU WIN!", ButtonType.OK);
        disconnectAlert.setTitle("You Win!");
        disconnectAlert.setHeaderText(null); 
    }
    
    public void showDisconnectAlert(){
        disconnectAlert.setContentText("You Win: " + controller.getEnemyUsername() + " disconnected.");
        setTitle("Chess App: You Win!");
        disconnectAlert.showAndWait();
    }
    
    private void initResignAlert(){
        resignAlert = new Alert(Alert.AlertType.INFORMATION, "YOU WIN!", ButtonType.OK);
        resignAlert.setTitle("You Win!");
        resignAlert.setHeaderText(null);                     
    }
    
    public void showResignAlert(){
        resignAlert.setContentText("You Win: " + controller.getEnemyUsername() + " resigned.");   
        setTitle("Chess App: You Win!");
        resignAlert.showAndWait();
    }
    
    private void initWinByTimeAlert(){
        winByTimeAlert = new Alert(Alert.AlertType.INFORMATION, "YOU WIN!", ButtonType.OK);  
        winByTimeAlert.setTitle("You Win!");
        winByTimeAlert.setHeaderText(null);   
        winByTimeAlert.setContentText("You win on time.");
    }
    
    public void showWinByTimeAlert(){
        setTitle("Chess App: You Win!");
        winByTimeAlert.showAndWait();
    }
    
    private void initLoseByTimeAlert(){
        loseByTimeAlert = new Alert(Alert.AlertType.INFORMATION, "YOU LOSE!", ButtonType.OK);
        loseByTimeAlert.setTitle("You Lose!");
        loseByTimeAlert.setHeaderText(null);
    }
    
    public void showLoseByTimeAlert(){
        String nameOfWinner = controller.getEnemyUsername();        
        loseByTimeAlert.setContentText(nameOfWinner + " wins on time.");
        setTitle("Chess App: " + nameOfWinner + " Wins!");
        loseByTimeAlert.showAndWait();
    }
    
    private void initInsufficientMaterialAlert(){
        insufficientMaterialAlert = new Alert(Alert.AlertType.INFORMATION, "DRAW!", ButtonType.OK);
        insufficientMaterialAlert.setTitle("Draw");
        insufficientMaterialAlert.setHeaderText(null);
        insufficientMaterialAlert.setContentText("Draw: Insufficient material.");  
    }
    
    public void showInsufficientMaterialAlert(){
        setTitle("Chess App: Draw!"); 
        insufficientMaterialAlert.showAndWait();
    }
    
    void initCellHandler(StackPane cell){
        cell.setOnMousePressed(event ->{             
            controller.selectCell(cell);
        });
    } 
    
    public StackPane getCell(int row, int col){
        return guiBoard[row][col];
    }
    
    public void drawCell(int row, int col, String square, boolean highlighted, String fontColor){
        StackPane cell = getCell(row,col);
        
        if(!fontColor.equals(""))
        {
            Image img = new Image("File:./images/default/" + square + ".png");
            ImageView imgv = new ImageView();
            imgv.setFitWidth(60);
            imgv.setFitHeight(60);
            imgv.setImage(img);
                
            if(controller.getPlayerColorString().equals("Black"))
            {
                imgv.setRotate(180);
            }
            cell.getChildren().clear();
            cell.getChildren().add(imgv);
        }
        else
        {
            cell.getChildren().clear();
        }
       
        fontColor = fontColor.toLowerCase();         
        cell.setEffect(null);
        
        if(!highlighted)
            return;
        
        InnerShadow highlight= new InnerShadow();
                    
        if(square.equals("") || cell == selectedCell)
            highlight.setColor(Color.YELLOW);
        else
            highlight.setColor(Color.RED);
                    
        highlight.setWidth(70);
        highlight.setHeight(70);
        
        cell.setEffect(highlight);
           
    }
    
    public StackPane getSelectedCell(){
        return selectedCell;
    }
        
    public void setSelectedCell(StackPane cell){
        selectedCell = cell;
    }    
}
