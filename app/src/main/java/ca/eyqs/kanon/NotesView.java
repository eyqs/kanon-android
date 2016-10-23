package ca.eyqs.kanon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesView extends View {
    private final int canvas_height;
    private final int note_width;
    private final int note_height;
    private final int second_offset;
    private final int acci_padding;
    private final int acci_width;
    private final int acci_height;
    private final int staff_spacing;
    private final int noteXOffset;
    private final int noteYOffset;
    private final int acciYOffset;
    private static final List<NotePosition> note_positions = new ArrayList<>();
    private static Drawable note;
    private static Drawable sharp;
    private static Drawable flat;
    private static Drawable dsharp;
    private static Drawable dflat;
    private static final Comparator<NotePosition> comp =
        new Comparator<NotePosition>() {
            public int compare(NotePosition a, NotePosition b) {
                return a.height < b.height ? 1 : -1;
            }
        };

    public NotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        canvas_height = (int) context.getResources().getDimension(
            R.dimen.notes_height);
        note_width = (int) context.getResources().getDimension(
            R.dimen.note_width);
        note_height = (int) context.getResources().getDimension(
            R.dimen.note_height);
        second_offset = (int) context.getResources().getDimension(
            R.dimen.second_offset);
        acci_padding = (int) context.getResources().getDimension(
            R.dimen.acci_padding);
        acci_width = (int) context.getResources().getDimension(
            R.dimen.acci_width);
        acci_height = (int) context.getResources().getDimension(
            R.dimen.acci_height);
        staff_spacing = (int) context.getResources().getDimension(
            R.dimen.staff_spacing);
        noteXOffset = acci_width + acci_padding;
        noteYOffset = (canvas_height - note_height) / 2;
        acciYOffset = (canvas_height - acci_height) / 2;
        note = ContextCompat.getDrawable(
            this.getContext(), R.drawable.note);
        sharp = ContextCompat.getDrawable(
            this.getContext(), R.drawable.sharp);
        flat = ContextCompat.getDrawable(
            this.getContext(), R.drawable.flat);
        dsharp = ContextCompat.getDrawable(
            this.getContext(), R.drawable.dsharp);
        dflat = ContextCompat.getDrawable(
            this.getContext(), R.drawable.dflat);
    }

    public void addNote(NoteValue note, boolean isClose, boolean isSecond,
                        int staff_middle) {
        int x = acci_width;
        if (isSecond) {
            x = second_offset + acci_width;
        }
        int y = note.getHeight(staff_middle) * staff_spacing;
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
        Collections.sort(note_positions, comp);
        for (NotePosition np : note_positions) {
            note.setBounds(np.position + noteXOffset, np.height + noteYOffset,
                np.position + noteXOffset + note_width,
                np.height + noteYOffset + note_height);
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
                        accidental = dsharp;
                        break;
                }
                accidental.setBounds(np.position - np.acciXOff,
                    np.height + acciYOffset,
                    np.position - np.acciXOff + acci_width,
                    np.height + acciYOffset + acci_height);
                accidental.draw(canvas);
            }
        }
    }
}
