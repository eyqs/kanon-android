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
    public static final int second_offset = 36;
    public static final int staff_spacing = 10;
    public static List<NotePosition> note_positions = new ArrayList();
    public static Drawable note;
    public static float density;

    public NotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        note = ContextCompat.getDrawable(this.getContext(), R.drawable.note);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)
            context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        density = dm.density;
    }

    public void addNote(int position, int height) {
        int x = position * second_offset;
        int y = height * staff_spacing + (canvas_height - note_height) / 2;
        note_positions.add(new NotePosition(x, y));
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw notes from bottom to top, to avoid overlapping white borders
        Collections.sort(note_positions, new Comparator<NotePosition>() {
            public int compare(NotePosition a, NotePosition b) {
                return a.getY() < b.getY() ? 1 : -1;
            }
        });
        for (NotePosition np : note_positions) {
            note.setBounds((int) (np.getX() * density),
                (int) (np.getY() * density),
                (int) ((np.getX() + note_width) * density),
                (int) ((np.getY() + note_height) * density));
            note.draw(canvas);
        }
    }
}
