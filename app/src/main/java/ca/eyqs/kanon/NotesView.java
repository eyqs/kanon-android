package ca.eyqs.kanon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class NotesView extends View {

    public static final int canvas_width = 80;
    public static final int canvas_height = 202;
    public static final int note_width = 40;
    public static final int note_height = 22;
    public static double density;

    public NotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)
            context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        density = dm.density;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable note = ContextCompat.getDrawable(
            this.getContext(), R.drawable.note);
        int x = 0;
        int y = (canvas_height - note_height) * (int) (density / 2);
        note.setBounds(x, y, x + (int) (note_width * density),
            y + (int) (note_height * density));
        note.draw(canvas);
    }
}
