package fred.freechess;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.SystemClock;
        import android.preference.PreferenceManager;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;

public class Play extends AppCompatActivity {

    SharedPreferences preferences;

    ChessSurface chessSurface;
    LinearLayout linearLayout;
    LinearLayout promotionLayout;

    Button knightButton;
    Button bishopButton;
    Button towerButton;
    Button queenButton;
    Button newGameButton;

    ChessClock blackTime;
    ChessClock whiteTime;
    ChessClock dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        setupButtonListeners();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        promotionLayout = (LinearLayout) findViewById(R.id.promotionLayout);
        promotionLayout.setVisibility(View.INVISIBLE);
        promotionLayout.setEnabled(false);

        blackTime = new ChessClock(this, PieceColor.BLACK);
        whiteTime = new ChessClock(this, PieceColor.WHITE);
        dummy = new ChessClock(this, PieceColor.WHITE);

        linearLayout.addView(whiteTime, 0);
        linearLayout.addView(dummy, 0);  // Dummy view gets removed first new game call
        linearLayout.addView(blackTime, 0);

        newGame();
    }

    void promotionChooser() {
        chessSurface.setEnabled(false);
        promotionLayout.setVisibility(View.VISIBLE);
        promotionLayout.setEnabled(true);
    }

    void pawnPromotionFinished(PieceType type) {
        chessSurface.chessBoard.pawnPromotionFinished(type);

        chessSurface.setEnabled(true);
        promotionLayout.setVisibility(View.INVISIBLE);
        promotionLayout.setEnabled(false);
    }

    void setupButtonListeners() {
        knightButton = (Button) findViewById(R.id.knightButton);
        knightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pawnPromotionFinished(PieceType.N);
            }
        });
        bishopButton = (Button) findViewById(R.id.bishopButton);
        bishopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pawnPromotionFinished(PieceType.B);
            }
        });
        towerButton = (Button) findViewById(R.id.towerButton);
        towerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pawnPromotionFinished(PieceType.R);
            }
        });
        queenButton = (Button) findViewById(R.id.queenButton);
        queenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pawnPromotionFinished(PieceType.Q);
            }
        });
        newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });
    }

    void newGame() {
        System.out.println("New game clicked");

        blackTime.reset(60 * Integer.parseInt(preferences.getString("time_limit", "3")));
        whiteTime.reset(60 * Integer.parseInt(preferences.getString("time_limit", "3")));

        chessSurface = new ChessSurface(this);
        chessSurface.setBackgroundColor(Color.BLACK);
        chessSurface.getHolder().setFixedSize(0, 850);
        linearLayout.removeViewAt(1);
        linearLayout.addView(chessSurface, 1);

        whiteTime.start();
    }

    void switchClocks(PieceColor color) {
        if (color == PieceColor.WHITE) {
            whiteTime.pause();
            blackTime.unPause();
        } else {
            whiteTime.unPause();
            blackTime.pause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClassName(this, "fred.freechess.SettingsActivity");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}