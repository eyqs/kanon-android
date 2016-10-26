/* Kanon v1.1
 * Copyright (c) 2016 Eugene Y. Q. Shen.
 *
 * Kanon is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version
 * 3 of the License, or (at your option) any later version.
 *
 * Kanon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package ca.eyqs.kanon;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    private static final String[] emptyArray = new String[0];
    private static final List<Integer> MAJ_INTERVALS =
        Arrays.asList(2, 3, 6, 7, 9, 10, 13, 14);
    private static String[] NOTE_STRINGS;
    private static String[] ACCI_STRINGS;
    private static String[] SIZE_STRINGS;
    private static String[] QUALMAJ_STRINGS;
    private static String[] QUALPERF_STRINGS;
    private static final String[] NOTE_VALUES = {
        "C", "D", "E", "F", "G", "A", "B"
    };
    private static final String[] ACCI_VALUES = { "bb", "b", "", "#", "x" };
    private static final String[] QUALMAJ_VALUES = { "d", "m", "M", "A" };
    private static final String[] QUALPERF_VALUES = { "d", "P", "A" };

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
            NOTE_STRINGS = new String[]{
                getString(R.string.note_c), getString(R.string.note_d),
                getString(R.string.note_e), getString(R.string.note_f),
                getString(R.string.note_g), getString(R.string.note_a),
                getString(R.string.note_b)
            };
            ACCI_STRINGS = new String[]{
                getString(R.string.note_dflat), getString(R.string.note_flat),
                getString(R.string.note_none), getString(R.string.note_sharp),
                getString(R.string.note_dsharp)
            };
            SIZE_STRINGS = new String[]{
                getString(R.string.interval_0), getString(R.string.interval_1),
                getString(R.string.interval_2), getString(R.string.interval_3),
                getString(R.string.interval_4), getString(R.string.interval_5),
                getString(R.string.interval_6), getString(R.string.interval_7),
                getString(R.string.interval_8), getString(R.string.interval_9),
                getString(R.string.interval_10),
                getString(R.string.interval_11),
                getString(R.string.interval_12),
                getString(R.string.interval_13),
                getString(R.string.interval_14),
                getString(R.string.interval_15)
            };
            QUALMAJ_STRINGS = new String[]{
                getString(R.string.interval_diminished),
                getString(R.string.interval_minor),
                getString(R.string.interval_major),
                getString(R.string.interval_augmented)
            };
            QUALPERF_STRINGS = new String[]{
                getString(R.string.interval_diminished),
                getString(R.string.interval_perfect),
                getString(R.string.interval_augmented)
            };
            String settings = getArguments().getString("settings");
            if ("clef".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_clef);
            } else if ("possible".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_possible);
                List<String> pitch_entries = new ArrayList<>(35);
                List<String> pitch_values = new ArrayList<>(35);
                for (String note : NOTE_STRINGS) {
                    for (String acci : ACCI_STRINGS) {
                        pitch_entries.add(note + acci);
                    }
                }
                for (String note : NOTE_VALUES) {
                    for (String acci : ACCI_VALUES) {
                        pitch_values.add(note + acci);
                    }
                }
                MultiSelectListPreference pitch_lp =
                    (MultiSelectListPreference) findPreference("pitch_list");
                pitch_lp.setEntries(pitch_entries.toArray(emptyArray));
                pitch_lp.setEntryValues(pitch_values.toArray(emptyArray));

                List<String> interval_entries = new ArrayList<>(53);
                List<String> interval_values = new ArrayList<>(53);
                for (int i = 1; i <= 15; i++) {
                    if (MAJ_INTERVALS.contains(i)) {
                        for (String qual : QUALMAJ_STRINGS) {
                            interval_entries.add(qual + SIZE_STRINGS[i]);
                        }
                        for (String qual : QUALMAJ_VALUES) {
                            interval_values.add(qual + Integer.toString(i));
                        }
                    } else {
                        for (String qual : QUALPERF_STRINGS) {
                            interval_entries.add(qual + SIZE_STRINGS[i]);
                        }
                        for (String qual : QUALPERF_VALUES) {
                            interval_values.add(qual + Integer.toString(i));
                        }
                    }
                }
                MultiSelectListPreference ival_lp = (MultiSelectListPreference)
                    findPreference("interval_list");
                ival_lp.setEntries(interval_entries.toArray(emptyArray));
                ival_lp.setEntryValues(interval_values.toArray(emptyArray));
            }
        }
    }
}
