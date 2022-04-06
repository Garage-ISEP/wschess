package com.garageisep.wschess.systems;

import com.garageisep.wschess.entities.Bishop;
import com.garageisep.wschess.entities.King;
import com.garageisep.wschess.entities.Knight;
import com.garageisep.wschess.entities.Pawn;
import com.garageisep.wschess.entities.Piece;
import com.garageisep.wschess.components.PieceColor;
import com.garageisep.wschess.entities.Queen;
import com.garageisep.wschess.entities.Rook;
import com.garageisep.wschess.components.Position;

import java.util.List;

public class GameSystem {
    private final RenderSystem renderSystem;
    private final Piece[][] board = new Piece[8][8];
    private Position selectedPiece;
    private List<Position> possibleMoves;
    private PieceColor turn = PieceColor.WHITE;

    public GameSystem(RenderSystem renderSystem) {
        this.renderSystem = renderSystem;
    }


    public void initalize() {
        // Create the pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0 || j == 7) {
                    PieceColor color = j == 0 ? PieceColor.BLACK : PieceColor.WHITE;
                    if (i == 0 || i == 7)
                        board[i][j] = new Rook(color);
                    else if (i == 1 || i == 6)
                        board[i][j] = new Knight(color);
                    else if (i == 2 || i == 5)
                        board[i][j] = new Bishop(color);
                    else if (i == 3)
                        board[i][j] = new Queen(color);
                    else board[i][j] = new King(color);
                } else if (j == 1 || j == 6) {
                    PieceColor color = j == 1 ? PieceColor.BLACK : PieceColor.WHITE;
                    board[i][j] = new Pawn(color);
                }
            }
        }
        update();
    }

    public void onBoardClick(int i, int j) {
        if (hasFriendlyPieceAt(i, j)) {
            selectedPiece = new Position(i, j);
            possibleMoves = board[i][j].getPossibleMoves(new Position(i, j), this);
            update();
        } else if (selectedPiece != null && possibleMoves.contains(new Position(i, j))) {
            movePiece(selectedPiece, new Position(i, j));
            selectedPiece = null;
        } else if (!hasPieceAt(i, j))
            selectedPiece = null;
    }

    public boolean hasPieceAt(int x, int y) {
        if (isInBoard(x, y))
            return board[x][y] != null;
        else return false;
    }
    public boolean hasPieceAt(Position position) {
        return hasPieceAt(position.getX(), position.getY());
    }

    public boolean hasEnnemyPieceAt(int x, int y) {
        return hasPieceAt(x, y) && board[x][y].getColor() != turn;
    }

    public boolean hasFriendlyPieceAt(int x, int y) {
        return hasPieceAt(x, y) && board[x][y].getColor() == turn;
    }
    public Piece getPieceAt(int i, int j) {
        if (hasPieceAt(i, j))
            return board[i][j];
        else return null;
    }

    public boolean isInBoard(Position position) {
        return position.getX() >= 0 && position.getX() < 8 && position.getY() >= 0 && position.getY() < 8;
    }

    public boolean isInBoard(int x, int y) {
        return isInBoard(new Position(x, y));
    }

    private void movePiece(Position from, Position to) {
        board[to.getX()][to.getY()] = board[from.getX()][from.getY()];
        board[from.getX()][from.getY()] = null;
        board[to.getX()][to.getY()].moved();
        update();
        renderSystem.clearPossibleMoves();
        turn = turn == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private void update() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null)
                    renderSystem.setPiece(i, j, board[i][j].getImageSignature());
                else renderSystem.clearPiece(i, j);
            }
        }
        if (possibleMoves != null) {
            renderSystem.clearPossibleMoves();
            for (Position p : possibleMoves) {
                renderSystem.setPossibleMove(p, hasPieceAt(p));
            }
        }
    }
}
