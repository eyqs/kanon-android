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
import java.util.List;

public class NotesView extends View {

    public static final int canvas_width = 80;
    public static final int canvas_height = 202;
    public static final int note_width = 40;
    public static final int note_height = 22;
    public static final int second_offset = 36;
    public static final int staff_spacing = 10;
    public static List<NotePosition> notes = new ArrayList<NotePosition>();
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
        float x = position * second_offset;
        float y = height * staff_spacing + (canvas_height - note_height) / 2;
        notes.add(new NotePosition(x, y));
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (NotePosition np : notes) {
            note.setBounds((int) (np.getX() * density),
                (int) (np.getY() * density),
                (int) ((np.getX() + note_width) * density),
                (int) ((np.getY() + note_height) * density));
            note.draw(canvas);
        }
    }
}
