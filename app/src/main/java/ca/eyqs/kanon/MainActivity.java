package ca.eyqs.kanon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.StateListDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int SETTINGS_REQUEST = 1;
    private static final double SAMPLE_FREQ = 44100.0;
    private static final double MS_TO_S = 0.001;
    private static final int SAMPLE_NUM = 0x7FFF;
    private static final int INSUFFICIENT_LIMIT = 10;
    private static final Character[] WHITENOTES_ARRAY = {
        'C', 'D', 'E', 'F', 'G', 'A', 'B'
    };
    private static final List<Character> WHITENOTES
        = Arrays.asList(WHITENOTES_ARRAY);
    private static final String[] QUALITIES = { "d", "m", "P", "M", "A" };
    private static final String DEFAULT_CLEF = "Treble";
    private static final String DEFAULT_TREBLE = "D4-G5";
    private static final String DEFAULT_ALTO = "E3-A4";
    private static final String DEFAULT_BASS = "F2-B3";
    private static final String[] DEFAULT_PITCH_ARRAY = {
        "C", "D", "E", "F", "G", "A", "B"
    };
    private static final Set<String> DEFAULT_PITCHES =
        new HashSet<>(Arrays.asList(DEFAULT_PITCH_ARRAY));
    private static final String[] DEFAULT_INTERVAL_ARRAY = {
        "P1", "m2", "M2", "m3", "M3", "P4", "A4",
        "d5", "P5", "m6", "M6", "m7", "M7", "P8"
    };
    private static final Set<String> DEFAULT_INTERVALS =
        new HashSet<>(Arrays.asList(DEFAULT_INTERVAL_ARRAY));
    private static final Map<String, Integer> CLEFRANGES = makeClefMap();
    private static Map<String, Integer> makeClefMap() {
        Map<String, Integer> res = new HashMap<>(4);
        res.put("Treble", 6);
        res.put("Alto", 0);
        res.put("Bass", -6);
        return Collections.unmodifiableMap(res);
    }
    private static final Map<Integer, Map<Integer, Character>> INTERVALS =
        makeIntervalMap();
    private static Map<Integer, Map<Integer, Character>> makeIntervalMap() {
        Map<Integer, Map<Integer, Character>> res = new HashMap<>(7);
        for (int i = 1; i < 8; i++) {
            res.put(i, new HashMap<Integer, Character>(4));
        }
        res.get(1).put(11, 'd');
        res.get(1).put(0, 'P');
        res.get(1).put(1, 'A');
        res.get(2).put(0, 'd');
        res.get(2).put(1, 'm');
        res.get(2).put(2, 'M');
        res.get(2).put(3, 'A');
        res.get(3).put(2, 'd');
        res.get(3).put(3, 'm');
        res.get(3).put(4, 'M');
        res.get(3).put(5, 'A');
        res.get(4).put(4, 'd');
        res.get(4).put(5, 'P');
        res.get(4).put(6, 'A');
        res.get(5).put(6, 'd');
        res.get(5).put(7, 'P');
        res.get(5).put(8, 'A');
        res.get(6).put(7, 'd');
        res.get(6).put(8, 'm');
        res.get(6).put(9, 'M');
        res.get(6).put(10, 'A');
        res.get(7).put(9, 'd');
        res.get(7).put(10, 'm');
        res.get(7).put(11, 'M');
        res.get(7).put(0, 'A');
        return Collections.unmodifiableMap(res);
    }
    private RadioGroup qualGroup;
    private RadioGroup size1;
    private RadioGroup size2;
    private RadioGroup size3;
    private static boolean badChordsConfirmed = false;
    private static boolean isChecking = true;
    private static char qualityGuess = '0';
    private static int sizeGuess = 0;
    private static char quality = '0';
    private static int size = 0;
    private static String clef;
    private static String[] trueRange;
    private static Map<String, String> ranges;
    private static Set<String> pitches;
    private static Set<String> intervals;
    private static List<List<NoteValue>> possible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = PreferenceManager
            .getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sp.edit();
        if (sp.getString("clef_list", null) == null) {
            ed.putString("clef_list", DEFAULT_CLEF);
        } if (sp.getStringSet("pitch_list", null) == null) {
            ed.putStringSet("pitch_list", DEFAULT_PITCHES);
        } if (sp.getStringSet("interval_list", null) == null) {
            ed.putStringSet("interval_list", DEFAULT_INTERVALS);
        } if (sp.getString("range_treble", null) == null) {
            ed.putString("range_treble", DEFAULT_TREBLE);
        } if (sp.getString("range_alto", null) == null) {
            ed.putString("range_alto", DEFAULT_ALTO);
        } if (sp.getString("range_bass", null) == null) {
            ed.putString("range_bass", DEFAULT_BASS);
        }
        ed.apply();

        clef = sp.getString("clef_list", DEFAULT_CLEF);
        pitches = sp.getStringSet("pitch_list", DEFAULT_PITCHES);
        intervals = sp.getStringSet("interval_list", DEFAULT_INTERVALS);
        ranges = new HashMap<>(3);
        ranges.put("treble", sp.getString("range_treble", DEFAULT_TREBLE));
        ranges.put("alto", sp.getString("range_alto", DEFAULT_ALTO));
        ranges.put("bass", sp.getString("range_bass", DEFAULT_BASS));
        setClef();
        setRanges();
        generatePossibilities();

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
            0, RadioGroup.LayoutParams.MATCH_PARENT, 1);
        if (getResources().getConfiguration().orientation ==
            Configuration.ORIENTATION_LANDSCAPE) {
            params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT, 0, 1);
        }

        /** Made by ishitcno1 at https://gist.github.com/ishitcno1/9544243 */
        qualGroup = (RadioGroup) findViewById(R.id.interval_quality);
        for (int i = 0; i < 5; ++i) {
            RadioButton button = new RadioButton(this);
            button.setAllCaps(false);
            button.setBackgroundResource(R.drawable.push_button);
            button.setButtonDrawable(new StateListDrawable());
            button.setGravity(Gravity.CENTER);
            button.setLayoutParams(params);
            button.setOnClickListener(clickListener);
            button.setTag(QUALITIES[i]);
            button.setText(QUALITIES[i]);
            qualGroup.addView(button);
        }
        qualGroup.setOnCheckedChangeListener(
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int id) {
                    if (id != -1) {
                        RadioButton button = (RadioButton) findViewById(id);
                        qualityGuess = ((CharSequence) button.getTag())
                            .charAt(0);
                    }
                }
            }
        );
        size1 = (RadioGroup) findViewById(R.id.interval_size_one);
        size2 = (RadioGroup) findViewById(R.id.interval_size_two);
        size3 = (RadioGroup) findViewById(R.id.interval_size_three);
        for (int i = 1; i <= 15; ++i) {
            RadioButton button = new RadioButton(this);
            button.setBackgroundResource(R.drawable.push_button);
            button.setButtonDrawable(new StateListDrawable());
            button.setGravity(Gravity.CENTER);
            button.setLayoutParams(params);
            button.setOnClickListener(clickListener);
            button.setTag(String.valueOf(i));
            button.setText(String.valueOf(i));
            if (i <= 5) {
                size1.addView(button);
            } else if (i <= 10) {
                size2.addView(button);
            } else {
                size3.addView(button);
            }
        }
        size1.setOnCheckedChangeListener(
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int id) {
                    if (id != -1 && isChecking) {
                        isChecking = false;
                        size2.clearCheck();
                        size3.clearCheck();
                        RadioButton button = (RadioButton) findViewById(id);
                        sizeGuess = Integer.parseInt((String) button.getTag());
                        isChecking = true;
                    }
                }
            }
        );
        size2.setOnCheckedChangeListener(
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int id) {
                    if (id != -1 && isChecking) {
                        isChecking = false;
                        size3.clearCheck();
                        size1.clearCheck();
                        RadioButton button = (RadioButton) findViewById(id);
                        sizeGuess = Integer.parseInt((String) button.getTag());
                        isChecking = true;
                    }
                }
            }
        );
        size3.setOnCheckedChangeListener(
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int id) {
                    if (id != -1 && isChecking) {
                        isChecking = false;
                        size1.clearCheck();
                        size2.clearCheck();
                        RadioButton button = (RadioButton) findViewById(id);
                        sizeGuess = Integer.parseInt((String) button.getTag());
                        isChecking = true;
                    }
                }
            }
        );
        runChangedApp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runChangedApp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SETTINGS_REQUEST) {
            boolean changed = false;
            SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
            Map<String, String> newRanges = new HashMap<>(3);
            newRanges.put("treble",
                sp.getString("range_treble", DEFAULT_TREBLE));
            newRanges.put("alto",
                sp.getString("range_alto", DEFAULT_ALTO));
            newRanges.put("bass",
                sp.getString("range_bass", DEFAULT_BASS));
            if (!clef.equals(sp.getString("clef_list", DEFAULT_CLEF))) {
                changed = true;
                clef = sp.getString("clef_list", DEFAULT_CLEF);
                setClef();
                setRanges();
            } if (!ranges.equals(newRanges)) {
                changed = true;
                ranges = newRanges;
                setRanges();
            } if (!pitches.equals(
                sp.getStringSet("pitch_list", DEFAULT_PITCHES))) {
                changed = true;
                pitches = sp.getStringSet("pitch_list", DEFAULT_PITCHES);
            } if (!intervals.equals(
                sp.getStringSet("interval_list", DEFAULT_INTERVALS))) {
                changed = true;
                intervals =
                    sp.getStringSet("interval_list", DEFAULT_INTERVALS);
            }

            if (changed) {
                badChordsConfirmed = false;
                generatePossibilities();
            }
        }
    }

    private void runChangedApp() {
        if (possible.isEmpty()) {
            alertNoChords();
        } else if (!badChordsConfirmed) {
            badChordsConfirmed = true;
            if (possible.size() < INSUFFICIENT_LIMIT) {
                alertInsufficientChords();
            }
            generateInterval();
        }
    }

    public void changeSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_REQUEST);
    }

    private void setClef() {
        View v = findViewById(R.id.clef_image);
        if (clef.equals("Treble")) {
            v.setBackgroundResource(R.drawable.treble);
        } else if (clef.equals("Alto")) {
            v.setBackgroundResource(R.drawable.alto);
        } else if (clef.equals("Bass")) {
            v.setBackgroundResource(R.drawable.bass);
        }
    }

    private void setRanges() {
        if (clef.equals("Treble")) {
            trueRange = ranges.get("treble").split("-");
        } else if (clef.equals("Alto")) {
            trueRange = ranges.get("alto").split("-");
        } else if (clef.equals("Bass")) {
            trueRange = ranges.get("bass").split("-");
        }
    }

    private final View.OnClickListener clickListener =
        new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (qualityGuess != '0' && sizeGuess != 0) {
                qualGroup.clearCheck();
                size1.clearCheck();
                size2.clearCheck();
                size3.clearCheck();
                if (qualityGuess == quality && sizeGuess == size) {
                    generateInterval();
                }
                qualityGuess = '0';
                sizeGuess = 0;
            }
        }
    };

    private void generatePossibilities() {
        Set<String> possibleNotes = new HashSet<>();
        int lowPitchIndex = WHITENOTES.indexOf(trueRange[0].charAt(0));
        int highPitchIndex = WHITENOTES.indexOf(trueRange[1].charAt(0));
        int lowOctave = Integer.parseInt(trueRange[0].substring(1));
        int highOctave = Integer.parseInt(trueRange[1].substring(1));
        if (lowOctave == highOctave) {
            for (String pitch : pitches) {
                if (WHITENOTES.indexOf(pitch.charAt(0)) >= lowPitchIndex &&
                    WHITENOTES.indexOf(pitch.charAt(0)) <= highPitchIndex) {
                    possibleNotes.add(pitch + Integer.toString(lowOctave));
                }
            }
        } else if (lowOctave < highOctave) {
            for (String pitch : pitches) {
                if (WHITENOTES.indexOf(pitch.charAt(0)) >= lowPitchIndex) {
                    possibleNotes.add(pitch + Integer.toString(lowOctave));
                }
                for (int octv = lowOctave + 1; octv < highOctave; octv++) {
                    possibleNotes.add(pitch + Integer.toString(octv));
                }
                if (WHITENOTES.indexOf(pitch.charAt(0)) <= highPitchIndex) {
                    possibleNotes.add(pitch + Integer.toString(highOctave));
                }
            }
        }

        possible = new ArrayList<>();
        for (String root : possibleNotes) {
            for (String ival : intervals) {
                List<NoteValue> interval = new ArrayList<>();
                NoteValue bass = new NoteValue(root);
                NoteValue treble = bass.transposeUp(ival);
                if (possibleNotes.contains(treble.toString())) {
                    interval.add(bass);
                    interval.add(treble);
                    possible.add(interval);
                }
            }
        }
    }

    private void generateInterval() {
        int middle = CLEFRANGES.get(clef);
        NotesView notes = (NotesView) findViewById(R.id.notes);
        notes.clear();

        Random rand = new Random();
        NoteValue a;
        NoteValue b;
        List<NoteValue> interval = possible.get(
            rand.nextInt(possible.size()));
        a = interval.get(0);
        b = interval.get(1);

        int semitones;
        if (a.getHeight(middle) < b.getHeight(middle)) {
            size = b.getHeight(middle) - a.getHeight(middle);
            semitones = a.getMidi() - b.getMidi();
        } else {
            size = a.getHeight(middle) - b.getHeight(middle);
            semitones = b.getMidi() - a.getMidi();
        }
        quality = INTERVALS.get(size % 7 + 1).get(semitones % 12);
        size += 1;

        if (size <= 6) {
            boolean aIsClose = false;
            boolean bIsClose = false;
            boolean aIsSecond = false;
            boolean bIsSecond = false;
            if (a.getAccidental() != 0 && b.getAccidental() != 0) {
                if (size <= 2) {
                    aIsClose = true;
                    bIsClose = true;
                } else if (a.getHeight(middle) > b.getHeight(middle)) {
                    aIsClose = true;
                } else {
                    bIsClose = true;
                }
            }
            if (size <= 2) {
                if (a.getHeight(middle) < b.getHeight(middle)) {
                    if (a.getAccidental() != 0) {
                        aIsClose = true;
                    }
                    aIsSecond = true;
                } else {
                    bIsSecond = true;
                    if (b.getAccidental() != 0) {
                        bIsClose = true;
                    }
                }
            }
            notes.addNote(a, aIsClose, aIsSecond, middle);
            notes.addNote(b, bIsClose, bIsSecond, middle);
        } else {
            notes.addNote(a, false, false, middle);
            notes.addNote(b, false, false, middle);
        }
        notes.invalidate();
    }

    private void alertNoChords() {
        NotesView notes = (NotesView) findViewById(R.id.notes);
        notes.clear();
        notes.invalidate();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No intervals match your pitch, range, and " +
            "interval settings.\n\nPlease adjust the settings and try again.");
        builder.setCancelable(false);
        builder.setNegativeButton("Back to settings",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Button btn = (Button) findViewById(R.id.settings_btn);
                    changeSettings(btn);
                    dialog.cancel();
                }
            });
        builder.setPositiveButton("Quit",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory( Intent.CATEGORY_HOME );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    dialog.cancel();
                }
            });
        builder.show();
    }

    private void alertInsufficientChords() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Only " + Integer.toString(possible.size()) +
            " distinct intervals match your pitch, range, and interval " +
            "settings.\n\nAre you sure you would like to continue?");
        builder.setNegativeButton("Back to settings",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Button btn = (Button) findViewById(R.id.settings_btn);
                    changeSettings(btn);
                    dialog.cancel();
                }
            });
        builder.setPositiveButton("Continue",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        builder.show();
    }

    /** Made by slightfoot at https://gist.github.com/slightfoot/6330866 */
    private static AudioTrack generateTone(double freqHz, int durationMs) {
        int count = (int) (SAMPLE_FREQ * 2 * (durationMs * MS_TO_S)) & ~1;
        short[] samples = new short[count];
        for (int i = 0; i < count; i += 2) {
            short sample = (short) (SAMPLE_NUM *
                Math.sin(2 * Math.PI * i / (SAMPLE_FREQ / freqHz)));
            samples[i] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(
            AudioManager.STREAM_MUSIC, (int) SAMPLE_FREQ,
            AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
            count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }
}
