package fred.freechess;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fred on 12/9/16.
 */


public class ChessSurface extends SurfaceView {

    Paint paint = new Paint();

    Paint canvasPaint = new Paint();
    Bitmap canvasBitmap;
    Canvas drawCanvas;

    ChessBoard chessBoard;
    ArrayList<ChessBoard.Square> possibleMoves = new ArrayList<>();

    int squareWidth;
    int squareHeight;

    public ChessSurface(Context context) {
        super(context);
        chessBoard = new ChessBoard(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawBoard(canvas);
        this.drawPieces(canvas);

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isEnabled()) {
            return true;
        }
        float touched_x = event.getX();
        float touched_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int file = (int)touched_x / squareWidth;
            int rank = (int)touched_y / squareHeight;

            boolean movedPiece = false;
            for (ChessBoard.Square square : possibleMoves) {
                if (square.file == file && square.rank == rank) {
                    chessBoard.move(file, rank);
                    movedPiece = true;
                    break;
                }
            }

            possibleMoves.clear();
            chessBoard.selectedPiece = null;
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

            if (!movedPiece) {
                ChessBoard.Piece piece = chessBoard.getPiece(file, rank);
                if (piece != null && piece.color == chessBoard.colorToMove) {
                    possibleMoves.clear();
                    chessBoard.selectedPiece = piece;
                    drawPossibleMoves(piece);
                }
            }
        }
        invalidate();
        return true; //processed
    }

    private void drawPossibleMoves(ChessBoard.Piece piece) {
        possibleMoves = chessBoard.possibleMoves(piece);
        canvasPaint.setColor(Color.BLUE);
        canvasPaint.setStyle(Paint.Style.STROKE);
        canvasPaint.setStrokeWidth(6);
        outerLoop:
        for (ChessBoard.Square square : possibleMoves) {
            int x = square.file * squareWidth;
            int y = square.rank * squareHeight;
            for (ChessBoard.Piece p : chessBoard.pieces) {
                if (p.square.file == square.file && p.square.rank == square.rank) {
                    drawCanvas.drawRect(x, y, x + squareWidth, y + squareHeight, canvasPaint);
                    continue outerLoop;
                }
            }
            drawCanvas.drawCircle(x + squareWidth/2, y + squareHeight/2, 10, canvasPaint);
        }
    }

    private void drawPieces(Canvas canvas) {
        paint.setTextSize(120);
        paint.setColor(Color.BLACK);
        for (ChessBoard.Piece piece : this.chessBoard.pieces) {
            int x = piece.square.file * squareWidth;
            int y = piece.square.rank * squareHeight;

            if (piece.color == PieceColor.BLACK && ((Play) getContext()).preferences.getBoolean("flip_black", true)) {
                x += squareWidth * 7/8;
                y += squareHeight / 10;

                canvas.save();
                canvas.rotate(-180, x, y);
                canvas.drawText(piece.type.toString(piece.color), x, y, paint);
                canvas.restore();
            } else {
                x += squareWidth /6;
                y += squareHeight * 0.95;
                canvas.drawText(piece.type.toString(piece.color), x, y, paint);
            }
        }
    }

    private void drawBoard(Canvas canvas) {
        squareWidth = canvas.getWidth() / 8;
        squareHeight = canvas.getHeight() / 8;

        paint.setStyle(Paint.Style.FILL);
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if ((i + j & 1) != 0) {
                    paint.setColor(Color.parseColor("#7fa87f")); //rgb(85,107,47)); //rgb(107, 142, 35));

                } else {
                    paint.setColor(Color.rgb(255,240,245));
                }
                int left = i * squareWidth;
                int top = j * squareHeight;
                canvas.drawRect(left, top, left + squareWidth, top + squareHeight, paint);
            }
        }
    }


    private int squareFromCoor(float x, float y) {
        return 8;
    }

}