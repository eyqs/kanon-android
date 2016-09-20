package ca.eyqs.kanon;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final char[] NOTES = { 'C', 'D', 'E', 'F', 'G', 'A', 'B' };
    private static final String[] ACCIDENTALS = { "bb", "b", "", "#", "x" };
    private static final String[] QUALITIES = { "d", "m", "P", "M", "A" };
    private static RadioGroup qualGroup;
    private static RadioGroup size1;
    private static RadioGroup size2;
    private static RadioGroup size3;
    private static boolean isChecking = true;
    private static char qualityGuess = '0';
    private static int sizeGuess = 0;
    private static char quality = '0';
    private static int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
            0, RadioGroup.LayoutParams.MATCH_PARENT, 1);
        /** Made by ishitcno1 at https://gist.github.com/ishitcno1/9544243 */
        qualGroup = (RadioGroup) findViewById(R.id.interval_quality);
        for (int i = 0; i < 5; ++i) {
            RadioButton button = new RadioButton(this);
            button.setAllCaps(false);
            button.setBackgroundResource(R.drawable.push_button);
            button.setButtonDrawable(null);
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
                        qualityGuess = ((String) button.getTag()).charAt(0);
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
            button.setButtonDrawable(null);
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
        generateInterval();
    }

    public void changeSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
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

    private NoteValue generateNote() {
        Random rand = new Random();
        int index = rand.nextInt(NOTES.length);
        char pitch = NOTES[index];
        index = rand.nextInt(ACCIDENTALS.length);
        String accidental = ACCIDENTALS[index];
        int octave = 4;
        return new NoteValue(Character.toString(pitch)
            + accidental + Integer.toString(octave));
    }

    private void generateInterval() {
        NotesView notes = (NotesView) findViewById(R.id.notes);
        notes.clear();
        NoteValue a = generateNote();
        NoteValue b = generateNote();
        size = Math.abs(a.getHeight(6) - b.getHeight(6)) + 1;
        quality = 'M';
        if (size <= 6) {
            boolean aIsClose = false;
            boolean bIsClose = false;
            boolean aIsSecond = false;
            boolean bIsSecond = false;
            if (a.getAccidental() != 0 && b.getAccidental() != 0) {
                if (size <= 2) {
                    aIsClose = true;
                    bIsClose = true;
                } else if (a.getHeight(6) > b.getHeight(6)) {
                    aIsClose = true;
                } else {
                    bIsClose = true;
                }
            }
            if (size <= 2) {
                if (a.getHeight(6) < b.getHeight(6)) {
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
            notes.addNote(a, aIsClose, aIsSecond);
            notes.addNote(b, bIsClose, bIsSecond);
        } else {
            notes.addNote(a, false, false);
            notes.addNote(b, false, false);
        }
        notes.invalidate();
    }

    /** Made by slightfoot at https://gist.github.com/slightfoot/6330866 */
    private AudioTrack generateTone(double freqHz, int durationMs) {
        int count = (int) (44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        for (int i = 0; i < count; i += 2) {
            short sample = (short) (0x7FFF *
                Math.sin(2 * Math.PI * i / (44100.0 / freqHz)));
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
            AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
            count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }
}
