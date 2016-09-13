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

    public static final int canvas_width = 80;
    public static final int canvas_height = 202;
    public static final int note_width = 40;
    public static final int note_height = 22;
    public static final int acci_width = 32;
    public static final int acci_height = 76;
    public static final int second_offset = 36;
    public static final int staff_spacing = 10;
    public static final int noteXOffset = acci_width + 8;
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

    public void addNote(NoteValue note, boolean second) {
        int x;
        if (second) {
            x = second_offset;
        } else {
            x = 0;
        }
        int y = note.getHeight(6) * staff_spacing;
        int accidental = note.getAccidental();
        note_positions.add(new NotePosition(x, y, accidental));
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
                return a.getY() < b.getY() ? 1 : -1;
            }
        });
        for (NotePosition np : note_positions) {
            note.setBounds((int) ((np.getX() + noteXOffset) * density),
                (int) ((np.getY() + noteYOffset)  * density),
                (int) ((np.getX() + note_width + noteXOffset) * density),
                (int) ((np.getY() + note_height + noteYOffset) * density));
            note.draw(canvas);
            if (np.getA() != 0) {
                switch (np.getA()) {
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
                accidental.setBounds((int) (np.getX() * density),
                    (int) ((np.getY() + acciYOffset) * density),
                    (int) ((np.getX() + acci_width) * density),
                    (int) ((np.getY() + acci_height + acciYOffset) * density));
                accidental.draw(canvas);
            }
        }
    }
}
