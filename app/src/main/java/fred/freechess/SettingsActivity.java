package fred.freechess;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by fredrik on 10.12.16.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
