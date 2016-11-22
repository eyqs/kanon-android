/* Kanon v1.2
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

public class ClefView extends View {

    private final int canvas_height;
    private final int clef_height;
    private final int clef_width;
    private final int base_height;
    private static Drawable gclef;
    private static Drawable cclef;
    private static Drawable fclef;
    private static Drawable clef;

    public ClefView(Context context, AttributeSet attrs) {
        super(context, attrs);
        canvas_height = (int) context.getResources().getDimension(
            R.dimen.clef_height);
        clef_height = (int) context.getResources().getDimension(
            R.dimen.clef_image_height);
        clef_width = (int) context.getResources().getDimension(
            R.dimen.clef_width);
        base_height = (canvas_height - clef_height) / 2;
        gclef = ContextCompat.getDrawable(
            getContext(), R.drawable.gclef);
        cclef = ContextCompat.getDrawable(
            getContext(), R.drawable.cclef);
        fclef = ContextCompat.getDrawable(
            getContext(), R.drawable.fclef);
    }

    public void setClef(String new_clef) {
        if (new_clef.equals("Treble")) {
            clef = gclef;
        } else if (new_clef.equals("Alto")) {
            clef = cclef;
        } else if (new_clef.equals("Bass")) {
            clef = fclef;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        clef.setBounds(0, base_height, clef_width, base_height + clef_height);
        clef.draw(canvas);
    }
}
