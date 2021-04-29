package view;

import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Ship;
import model.SmallInfoLabel;
import view.ShipModel.CellValue;

public class GameView extends Group {
    private static AnchorPane gamePane;
    private static Scene gameScene;
    private static Parent root;

    private FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StarShooter.fxml"));
    private static Controller controller;

    @FXML private int gvRowCount;
    @FXML private int gvColumnCount;
    private ImageView[][] cellViews;

    private final Image blockImage = new Image(getClass().getResourceAsStream("/res/spaceBuilding_018.png"));
    private final Image coinImage = new Image(getClass().getResourceAsStream("/res/smalldot.png"));
    private static Image shipImage;

    private final Image enemyImages[] = {
            new Image(getClass().getResourceAsStream("/res/spaceShips_004.png")),
            new Image(getClass().getResourceAsStream("/res/spaceShips_009.png"))
    };

    private final Image bulletImage = new Image(getClass().getResourceAsStream("/res/laserRed15.png"));

    public static ViewManager viewManager = new ViewManager();

    public final static double CELL_WIDTH = 34.0;

    private SmallInfoLabel pointsLabel;
    private ImageView[] playerLifes;
    private int playerLife;
    private int points;

    public GameView() {
        initializeStage();
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
    }

    // make new empty grid of cells
    private void initializeGameViewGrid() {
        if (gvRowCount > 0 && gvColumnCount > 0) {
            this.cellViews = new ImageView[gvRowCount][gvColumnCount];
            for (int row = 0; row < gvRowCount; row++) {
                for (int column = 0; column < gvColumnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double) row * CELL_WIDTH);
                    imageView.setY((double) column * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    public void createNewGame(Ship chosenShip) throws Exception {
        //this.menuStage = menuStage;
        //this.menuStage.hide();

        createGameElements(chosenShip);

    	/*
    	this.shipImage = new Image(getClass().getResourceAsStream(chosenShip.getUrl()));
    	this.gamePane = new AnchorPane();
    	Image backgroundImage = new Image("view/resources/space.png", 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		gamePane.setBackground(new Background(background));
		*/

        root = loader.load();
        controller = loader.getController();
        root.setOnKeyPressed(controller);

        double sceneWidth = controller.getBoardWidth();
        double sceneHeight = controller.getBoardHeight();
        gameScene = new Scene(root, sceneWidth, sceneHeight);
        viewManager.getMainStage().setScene(gameScene);
        root.requestFocus();
    }

    private void createGameElements(Ship chosenShip) {
        playerLife = 2;
        pointsLabel = new SmallInfoLabel("POINTS : 00");
        pointsLabel.setLayoutX(460);
        pointsLabel.setLayoutY(20);
        gamePane.getChildren().add(pointsLabel);
        playerLifes = new ImageView[3];

        for (int i = 0; i < playerLifes.length; i++) {
            playerLifes[i] = new ImageView(chosenShip.getLifeUrl());
            playerLifes[i].setLayoutX(455 + (i*50));
            playerLifes[i].setLayoutY(80);
            gamePane.getChildren().add(playerLifes[i]);
        }

    }

    // update based off of model of grid
    public void update(PlayerModel player, List<EnemyAIModel> enemies) {
    	/*
    	assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        if (model.getRowCount() != this.rowCount || model.getColumnCount() != this.columnCount) {
            initializeGrid();
        }
        set the image to correspond with the value of that cell
        */
        for (int column = 0; column < gvColumnCount; column++) {
            for (int row = 0; row < gvRowCount; row++) {
                CellValue value = player.getCellValue(row, column);
                if (value == CellValue.COIN) {
                    this.cellViews[row][column].setImage(this.coinImage);
                } else if (value == CellValue.BLOCK) {
                    this.cellViews[row][column].setImage(this.blockImage);
                } else {
                    this.cellViews[row][column].setImage(null);
                }


                if (row == player.getLocation().getX() && column == player.getLocation().getY()){
                    this.cellViews[row][column].setImage(shipImage);
                    if (player.getLastDirection() == ShipModel.Direction.RIGHT) {
                        this.cellViews[row][column].setRotate(90);
                    } else if (player.getLastDirection() == ShipModel.Direction.LEFT) {
                        this.cellViews[row][column].setRotate(-90);
                    } else if (player.getLastDirection() == ShipModel.Direction.UP || player.getLastDirection() == ShipModel.Direction.NONE) {
                        this.cellViews[row][column].setRotate(0);
                    } else if (player.getLastDirection() == ShipModel.Direction.DOWN) {
                        this.cellViews[row][column].setRotate(180);
                    }
                }

                for (int i = 0; i < enemies.size(); i++) {
                    if (row == enemies.get(i).getLocation().getX() && column == enemies.get(i).getLocation().getY()) {
                        this.cellViews[row][column].setImage(this.enemyImages[i]);
                        if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.RIGHT) {
                            this.cellViews[row][column].setRotate(90);
                        } else if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.LEFT) {
                            this.cellViews[row][column].setRotate(-90);
                        } else if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.UP || enemies.get(i).getCurrentDirection() == ShipModel.Direction.NONE) {
                            this.cellViews[row][column].setRotate(0);
                        } else if (enemies.get(i).getCurrentDirection() == ShipModel.Direction.DOWN) {
                            this.cellViews[row][column].setRotate(180);
                        }
                    }
                }
            }
        }
    }

    public int getGvRowCount() {
        return this.gvRowCount;
    }


    public void setGvRowCount(int rowCount) {
        this.gvRowCount = rowCount;
        this.initializeGameViewGrid();
    }

    public int getGvColumnCount() {
        return this.gvColumnCount;
    }

    public void setGvColumnCount(int columnCount) {
        this.gvColumnCount = columnCount;
        this.initializeGameViewGrid();
    }

    public void setShipImage(Ship chosenShip) {
        shipImage = new Image(getClass().getResourceAsStream(chosenShip.getShipUrl()));
    }

}
