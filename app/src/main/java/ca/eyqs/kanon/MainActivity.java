package ca.eyqs.kanon;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RadioGroup qualGroup;
    private RadioGroup size1;
    private RadioGroup size2;
    private RadioGroup size3;
    private boolean isChecking = true;
    private char qualityGuess = '0';
    private int sizeGuess = 0;
    private char quality = '0';
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Made by ishitcno1 at https://gist.github.com/ishitcno1/9544243 */
        qualGroup = (RadioGroup) findViewById(R.id.interval_quality);
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

    /** Called when the user pushes any button */
    public void pushButton(View view) {
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

    private void generateInterval() {
        NotesView notes = (NotesView) findViewById(R.id.notes);
        notes.clear();
        NoteValue a = new NoteValue("C5");
        NoteValue b = new NoteValue("B4");
        size = Math.abs(a.getHeight() - b.getHeight()) + 1;
        quality = 'M';
        if (size <= 2) {
            if (a.getHeight() < b.getHeight()) {
                notes.addNote(a, false);
                notes.addNote(b, true);
            } else {
                notes.addNote(a, true);
                notes.addNote(b, false);
            }
        } else {
            notes.addNote(a, false);
            notes.addNote(b, false);
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
