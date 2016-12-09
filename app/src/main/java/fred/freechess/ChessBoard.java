package fred.freechess;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static fred.freechess.PieceType.B;

/**
 * Created by fred on 12/9/16.
 */

class ChessBoard {

    Vector<Piece> pieces;
    Piece selectedPiece;

    ChessBoard() {
        pieces = new Vector<>();
        List<String> files = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
        for (int i=0; i<8; i++) {
            pieces.add(new Piece(PieceColor.WHITE, new Square(i, 6), PieceType.P));
            pieces.add(new Piece(PieceColor.BLACK, new Square(i, 1), PieceType.P));
        }
        pieces.add(new Piece(PieceColor.WHITE, new Square(0, 7), PieceType.T));
        pieces.add(new Piece(PieceColor.WHITE, new Square(1, 7), PieceType.N));
        pieces.add(new Piece(PieceColor.WHITE, new Square(2, 7), B));
        pieces.add(new Piece(PieceColor.WHITE, new Square(3, 4), PieceType.Q));
        pieces.add(new Piece(PieceColor.WHITE, new Square(4, 7), PieceType.K));
        pieces.add(new Piece(PieceColor.WHITE, new Square(5, 7), B));
        pieces.add(new Piece(PieceColor.WHITE, new Square(6, 7), PieceType.N));
        pieces.add(new Piece(PieceColor.WHITE, new Square(7, 7), PieceType.T));

        pieces.add(new Piece(PieceColor.BLACK, new Square(0, 0), PieceType.T));
        pieces.add(new Piece(PieceColor.BLACK, new Square(1, 0), PieceType.N));
        pieces.add(new Piece(PieceColor.BLACK, new Square(2, 0), B));
        pieces.add(new Piece(PieceColor.BLACK, new Square(3, 0), PieceType.Q));
        pieces.add(new Piece(PieceColor.BLACK, new Square(4, 0), PieceType.K));
        pieces.add(new Piece(PieceColor.BLACK, new Square(5, 0), B));
        pieces.add(new Piece(PieceColor.BLACK, new Square(6, 0), PieceType.N));
        pieces.add(new Piece(PieceColor.BLACK, new Square(7, 0), PieceType.T));

    }

    void move(int file, int rank) {
        for (Piece piece : pieces) {
            if (piece.square.file == file && piece.square.rank == rank) {
                pieces.remove(piece);
                break;
            }
        }
        selectedPiece.square.file = file;
        selectedPiece.square.rank = rank;
    }

    ArrayList<Square> possibleMoves(Piece piece) {
        int file = piece.square.file;
        int rank = piece.square.rank;
        ArrayList<Square> moves = new ArrayList<>();
        switch (piece.type) {
            case K:
                for (int i = file - 1; i < file + 2; i++) {
                    for (int j = rank - 1; j < rank + 2; j++) {
                        if (i != file || j != rank) {
                            addMoveIfValid(i, j, piece.color, moves);
                        }
                    }
                }
                break;
            case Q:
                addHorVerMoves(piece, moves);
                addDiagonalMoves(piece, moves);
                break;
            case B:
                addDiagonalMoves(piece, moves);
                break;
            case N:
                for (int i=file - 2; i<file + 3; i++) {
                    for (int j=rank - 2; j<rank + 3; j++) {
                        if (Math.abs(file - i) == 2 && Math.abs(rank - j) == 1) {
                            addMoveIfValid(i, j, piece.color, moves);
                        } else if (Math.abs(file - i) == 1 && Math.abs(rank - j) == 2) {
                            addMoveIfValid(i, j, piece.color, moves);
                        }
                    }
                }
                break;
            case T:
                addHorVerMoves(piece, moves);
                break;
            case P:
                boolean addOneForward = true;
                boolean addTwoForward = true;
                int rankDirection = piece.color == PieceColor.BLACK ? 1 : -1;
                for (Piece p : pieces) {
                    if (p.square.file == file && p.square.rank == rank + rankDirection) {
                        addOneForward = false;
                    }
                    if (p.square.file == file && p.square.rank == rank + 2*rankDirection) {
                        addTwoForward = false;
                    }
                    if (p.square.file == file + 1 && p.square.rank == rank + rankDirection) {
                        if (p.color != piece.color) {
                            if (onBoard(file + 1, rank + rankDirection))
                                moves.add(new Square(file + 1, rank + rankDirection));
                        }
                    }
                    if (p.square.file == file - 1 && p.square.rank == rank + rankDirection) {
                        if (p.color != piece.color) {
                            if (onBoard(file - 1, rank + rankDirection))
                                moves.add(new Square(file - 1, rank + rankDirection));
                        }
                    }
                }
                if (addOneForward) {
                    if (onBoard(file, rank + rankDirection))
                        moves.add(new Square(file, rank + rankDirection));
                    if (addTwoForward) {
                        if (piece.color == PieceColor.BLACK && rank == 1) {
                            moves.add(new Square(file, rank + 2*rankDirection));
                        }
                        if (piece.color == PieceColor.WHITE && rank == 6) {
                            moves.add(new Square(file, rank + 2*rankDirection));
                        }
                    }
                }
                break;
            default:
        }
        return moves;
    }

    private void addHorVerMoves(Piece piece, ArrayList<Square> moves) {
        for (int i = piece.square.file + 1; i < 8; i++) {
            if (!addMoveIfValid(i, piece.square.rank, piece.color, moves))
                break;
        }
        for (int i = piece.square.file - 1; i >= 0; i--) {
            if (!addMoveIfValid(i, piece.square.rank, piece.color, moves))
                break;
        }
        for (int j = piece.square.rank + 1; j < 8; j++) {
            if (!addMoveIfValid(piece.square.file, j, piece.color, moves))
                break;
        }
        for (int j = piece.square.rank - 1; j >= 0; j--) {
            if (!addMoveIfValid(piece.square.file, j, piece.color, moves))
                break;
        }
    }

    private void addDiagonalMoves(Piece piece, ArrayList<Square> moves) {
        int file = piece.square.file;
        int rank = piece.square.rank;
        while (onBoard(file++, rank++) && addMoveIfValid(file, rank, piece.color, moves)) {}
        file = piece.square.file;
        rank = piece.square.rank;
        while (onBoard(file--, rank++) && addMoveIfValid(file, rank, piece.color, moves)) {}
        file = piece.square.file;
        rank = piece.square.rank;
        while (onBoard(file++, rank--) && addMoveIfValid(file, rank, piece.color, moves)) {}
        file = piece.square.file;
        rank = piece.square.rank;
        while (onBoard(file--, rank--) && addMoveIfValid(file, rank, piece.color, moves)) {}
    }

    private boolean addMoveIfValid(int x, int y, PieceColor color, ArrayList<Square> moves) {
        boolean captured = false;
        if (x >= 0 && x <= 7 && y >=0 && y <= 7) {
            for (Piece piece : pieces) {
                if (piece.square.file == x && piece.square.rank == y) {
                    if (piece.color == color) {
                        return false;
                    } else {
                        captured = true;
                        break;
                    }
                }
            }
        }
        moves.add(new Square(x, y));
        return !captured;
    }

    private boolean onBoard(int x, int y) {
        return x >= 0 && x <= 7 && y >=0 && y <= 7;
    }

    Piece getPiece(int file, int rank) {
        for (Piece p : pieces) {
            if (p.square.file == file && p.square.rank == rank) {
                return p;
            }
        }
        return null;
    }

    static class Piece {
        PieceColor color;
        Square square;
        PieceType type;

        Piece(PieceColor color, Square square, PieceType type) {
            this.color = color;
            this.square = square;
            this.type = type;
        }


    }


    static class Square {
        int file;
        int rank;

        Square(int file, int rank) {
            this.file = file;
            this.rank = rank;
        }
    }
}

