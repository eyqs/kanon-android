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
    private static final List<NotePosition> note_positions =
        new ArrayList<>(20);
    private static Drawable note;
    private static Drawable sharp;
    private static Drawable flat;
    private static Drawable dsharp;
    private static Drawable dflat;
    private static final Comparator<NotePosition> comp =
        new Comparator<NotePosition>() {
            @Override
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
            getContext(), R.drawable.note);
        sharp = ContextCompat.getDrawable(
            getContext(), R.drawable.sharp);
        flat = ContextCompat.getDrawable(
            getContext(), R.drawable.flat);
        dsharp = ContextCompat.getDrawable(
            getContext(), R.drawable.dsharp);
        dflat = ContextCompat.getDrawable(
            getContext(), R.drawable.dflat);
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

    @Override
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
                if (np.accidental == -2) {
                    accidental = dflat;
                } else if (np.accidental == -1) {
                    accidental = flat;
                } else if (np.accidental == 1) {
                    accidental = sharp;
                } else if (np.accidental == 2) {
                    accidental = dsharp;
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
