package com.garageisep.wschess.systems;

import com.garageisep.wschess.components.PieceColor;
import com.garageisep.wschess.components.Position;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class RenderSystem implements Initializable {

    private final GameSystem gameSystem = new GameSystem(this);
    private final Pane[][] board = new Pane[8][8];
    private final RessourceSystem ressourceSystem = new RessourceSystem();

    @FXML
    public GridPane boardPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("RenderController initialized");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean isBlack = (i + j) % 2 == 0;
                ImageView imageView = new ImageView();
                Pane pane = new Pane();
                pane.getChildren().add(imageView);
                Label label = new Label(i + "," + j);
                label.setTextFill(isBlack ? PieceColor.WHITE.toColor() : PieceColor.BLACK.toColor());
                pane.getChildren().add(label);
                pane.setBackground(new Background(new BackgroundFill(isBlack ? PieceColor.BLACK.toColor() : PieceColor.WHITE.toColor(), null, null)));
                int finalI = i;
                int finalJ = j;
                pane.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> gameSystem.onBoardClick(finalI, finalJ));
                board[i][j] = pane;
                boardPane.add(pane, i, j);
            }
        }
        gameSystem.initalize();
    }

    public void setPiece(int x, int y, String imageRef) {
        ImageView imageView = (ImageView) board[x][y].getChildren().get(0);
        imageView.setImage(ressourceSystem.getImage(imageRef));
    }

    public void clearPiece(int x, int y) {
        ImageView imageView = (ImageView) board[x][y].getChildren().get(0);
        imageView.setImage(null);
    }

    /**
     * @param pos The xy position of the piece on the board
     * @param hasPiece If there is a piece at the position
     */
    public void setPossibleMove(Position pos, boolean hasPiece) {
        Pane pane = board[pos.getX()][pos.getY()];
        Color color = hasPiece ? new Color(.96, .431, .431, 1) : new Color(.431, .764, .96, 1);
        pane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    public void clearPossibleMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pane pane = board[i][j];
                boolean isBlack = (i + j) % 2 == 0;
                pane.setBackground(new Background(new BackgroundFill(isBlack ? new Color(.2, .2, .2, 1) : new Color(.8, .8, .8, 1), null, null)));
            }
        }
    }
}
