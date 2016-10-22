package ca.eyqs.kanon;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    private static final String[] NOTE_VALUES = {
        "C", "D", "E", "F", "G", "A", "B"
    };
    private static final String[] ACCI_VALUES = { "bb", "b", "", "#", "x" };
    private static final List<Integer> MAJ_INTERVALS =
        Arrays.asList(2, 3, 6, 7, 9, 10, 13, 14);
    private static final String[] QUALMAJ_VALUES = { "d", "m", "M", "A" };
    private static final String[] QUALPERF_VALUES = { "d", "P", "A" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferences, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            String settings = getArguments().getString("settings");
            if ("clef".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_clef);
            } else if ("possible".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_possible);
                MultiSelectListPreference lp = (MultiSelectListPreference)
                    findPreference("pitch_list");
                List<String> entries = new ArrayList<String>();
                List<String> entryValues = new ArrayList<String>();
                for (String note : NOTE_VALUES) {
                    for (String acci : ACCI_VALUES) {
                        entries.add(note + acci);
                        entryValues.add(note + acci);
                    }
                }
                lp.setEntries(entries.toArray(new String[0]));
                lp.setEntryValues(entryValues.toArray(new String[0]));

                lp = (MultiSelectListPreference)
                    findPreference("interval_list");
                entries = new ArrayList<String>();
                entryValues = new ArrayList<String>();
                for (int i = 1; i <= 15; i++) {
                    if (MAJ_INTERVALS.contains(i)) {
                        for (String qual : QUALMAJ_VALUES) {
                            entries.add(qual + Integer.toString(i));
                            entryValues.add(qual + Integer.toString(i));
                        }
                    } else {
                        for (String qual : QUALPERF_VALUES) {
                            entries.add(qual + Integer.toString(i));
                            entryValues.add(qual + Integer.toString(i));
                        }
                    }
                }
                lp.setEntries(entries.toArray(new String[0]));
                lp.setEntryValues(entryValues.toArray(new String[0]));
            }
        }
    }
}
