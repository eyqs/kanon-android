/* Kanon v1.2
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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import android.preference.PreferenceScreen;

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
    private static String[] CLEF_STRINGS;
    private static final String[] NOTE_VALUES = {
        "C", "D", "E", "F", "G", "A", "B"
    };
    private static final String[] ACCI_VALUES = { "bb", "b", "", "#", "x" };
    private static final String[] QUALMAJ_VALUES = { "d", "m", "M", "A" };
    private static final String[] QUALPERF_VALUES = { "d", "P", "A" };
    private static final String[] CLEF_VALUES = {
        "Treble", "Alto", "Tenor", "Bass", "French",
        "Soprano", "Mezzo", "Baritone", "Varbaritone", "Subbass"
    };
    private static final String[] CLEFRANGE_KEYS = {
        "range_treble", "range_alto", "range_tenor", "range_bass",
        "range_french", "range_soprano", "range_mezzo",
        "range_baritone", "range_varbaritone", "range_subbass"
    };

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferences, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);
        if (header.id == R.id.feedback_header) {
            sendEmailFeedback();
        }
    }

    private void sendEmailFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", "kanon@eyqs.ca", null));
        intent.putExtra(Intent.EXTRA_SUBJECT,
            getString(R.string.feedback_subject));
        try {
            startActivity(Intent.createChooser(intent,
                getString(R.string.feedback)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,
                getString(R.string.feedback_error), Toast.LENGTH_SHORT).show();
        }
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
            CLEF_STRINGS = new String[]{
                getString(R.string.clef_treble),
                getString(R.string.clef_alto),
                getString(R.string.clef_tenor),
                getString(R.string.clef_bass),
                getString(R.string.clef_french),
                getString(R.string.clef_soprano),
                getString(R.string.clef_mezzo),
                getString(R.string.clef_baritone),
                getString(R.string.clef_varbaritone),
                getString(R.string.clef_subbass),
            };

            String settings = getArguments().getString("settings");
            if ("clef".equals(settings)) {
                addPreferencesFromResource(R.xml.preferences_empty);
                PreferenceScreen ps = this.getPreferenceScreen();
                ListPreference clef_lp = new ListPreference(ps.getContext());
                clef_lp.setKey("clef_list");
                clef_lp.setTitle(getString(R.string.clef));
                clef_lp.setSummary(getString(R.string.settings_clef));
                List<String> clef_entries = new ArrayList<>(10);
                List<String> clef_values = new ArrayList<>(10);
                for (String clef : CLEF_STRINGS) {
                    clef_entries.add(clef);
                }
                for (String clef : CLEF_VALUES) {
                    clef_values.add(clef);
                }
                clef_lp.setEntries(clef_entries.toArray(emptyArray));
                clef_lp.setEntryValues(clef_values.toArray(emptyArray));
                ps.addPreference(clef_lp);

                PreferenceCategory pc =
                    new PreferenceCategory(ps.getContext());
                pc.setTitle(getString(R.string.settings_range));
                ps.addPreference(pc);

                for (int i = 0; i < CLEF_STRINGS.length; ++i) {
                    EditTextPreference ep =
                        new EditTextPreference(ps.getContext());
                    ep.setKey(CLEFRANGE_KEYS[i]);
                    ep.setTitle(CLEF_STRINGS[i]);
                    ps.addPreference(ep);
                }
            } else if ("possible".equals(settings)) {
                addPreferencesFromResource(R.xml.preferences_empty);
                PreferenceScreen ps = this.getPreferenceScreen();
                MultiSelectListPreference pitch_lp =
                    new MultiSelectListPreference(ps.getContext());
                pitch_lp.setKey("pitch_list");
                pitch_lp.setTitle(getString(R.string.pitch));
                pitch_lp.setSummary(getString(R.string.settings_pitch));
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
                pitch_lp.setEntries(pitch_entries.toArray(emptyArray));
                pitch_lp.setEntryValues(pitch_values.toArray(emptyArray));
                ps.addPreference(pitch_lp);

                MultiSelectListPreference ival_lp =
                    new MultiSelectListPreference(ps.getContext());
                ival_lp.setKey("interval_list");
                ival_lp.setTitle(getString(R.string.interval));
                ival_lp.setSummary(getString(R.string.settings_interval));
                List<String> interval_entries = new ArrayList<>(52);
                List<String> interval_values = new ArrayList<>(52);
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
                interval_entries.remove(getString(R.string.interval_diminished)
                    + SIZE_STRINGS[1]);
                interval_values.remove("d1");
                ival_lp.setEntries(interval_entries.toArray(emptyArray));
                ival_lp.setEntryValues(interval_values.toArray(emptyArray));
                ps.addPreference(ival_lp);
            }
        }
    }
}
