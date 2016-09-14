package ca.eyqs.kanon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesView extends View {

    public static final int canvas_width = 160;
    public static final int canvas_height = 202;
    public static final int note_width = 40;
    public static final int note_height = 22;
    public static final int second_offset = 36;
    public static final int acci_padding = 8;
    public static final int acci_width = 32;
    public static final int acci_height = 76;
    public static final int staff_spacing = 10;
    public static final int noteXOffset = acci_width + acci_padding;
    public static final int noteYOffset = (canvas_height - note_height) / 2;
    public static final int acciYOffset = (canvas_height - acci_height) / 2;
    public static List<NotePosition> note_positions = new ArrayList();
    public static Drawable note;
    public static Drawable sharp;
    public static Drawable flat;
    public static Drawable darp;
    public static Drawable dflat;
    public static float density;

    public NotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        note = ContextCompat.getDrawable(this.getContext(), R.drawable.note);
        sharp = ContextCompat.getDrawable(this.getContext(), R.drawable.sharp);
        flat = ContextCompat.getDrawable(this.getContext(), R.drawable.flat);
        darp = ContextCompat.getDrawable(this.getContext(), R.drawable.dsharp);
        dflat = ContextCompat.getDrawable(this.getContext(), R.drawable.dflat);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)
            context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        density = dm.density;
    }

    public void addNote(NoteValue note, boolean isClose, boolean isSecond) {
        int x = acci_width;
        if (isSecond) {
            x = second_offset + acci_width;
        }
        int y = note.getHeight(6) * staff_spacing;
        int a = note.getAccidental();
        int c = 0;
        if (isClose) {
            c = acci_width;
        }
        note_positions.add(new NotePosition(x, y, a, c));
    }

    public void clear() {
        note_positions.clear();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable accidental = sharp;
        // Draw notes from bottom to top, to avoid overlapping white borders
        Collections.sort(note_positions, new Comparator<NotePosition>() {
            public int compare(NotePosition a, NotePosition b) {
                return a.height < b.height ? 1 : -1;
            }
        });
        for (NotePosition np : note_positions) {
            note.setBounds((int) ((np.position + noteXOffset) * density),
                (int) ((np.height + noteYOffset)  * density),
                (int) ((np.position + noteXOffset + note_width) * density),
                (int) ((np.height + noteYOffset + note_height) * density));
            note.draw(canvas);
            if (np.accidental != 0) {
                switch (np.accidental) {
                    case -2:
                        accidental = dflat;
                        break;
                    case -1:
                        accidental = flat;
                        break;
                    case 1:
                        accidental = sharp;
                        break;
                    case 2:
                        accidental = darp;
                        break;
                }
                accidental.setBounds(
                    (int) ((np.position - np.acciXOff) * density),
                    (int) ((np.height + acciYOffset) * density),
                    (int) ((np.position - np.acciXOff + acci_width) * density),
                    (int) ((np.height + acciYOffset + acci_height) * density));
                accidental.draw(canvas);
            }
        }
    }
}
