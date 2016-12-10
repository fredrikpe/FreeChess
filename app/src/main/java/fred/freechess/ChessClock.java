package fred.freechess;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

/**
 * Created by fredrik on 10.12.16.
 */

public class ChessClock extends Chronometer {

    PieceColor color;
    long timeWhenStopped;
    int secondLimit = 30;

    public ChessClock(Context context, PieceColor color) {
        super(context);
        this.color = color;
        this.setOnChronometerTickListener((new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                //System.out.println(elapsedMillis);
                if (elapsedMillis > 1000 * secondLimit) {
                    chronometer.stop();
                }
            }
        }));
    }

    void reset(int secondLimit) {
        this.secondLimit = secondLimit;
        this.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
    }

    void pause() {
        timeWhenStopped = this.getBase() - SystemClock.elapsedRealtime();
        this.stop();
    }

    void unPause() {
        this.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        this.start();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (color == PieceColor.BLACK && ((Play) getContext()).preferences.getBoolean("flip_black", true)) {
            canvas.save();
            float py = this.getHeight() / 2.0f;
            float px = this.getWidth() / 2.0f;
            canvas.rotate(180, px, py);

            super.onDraw(canvas);

            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }
}
