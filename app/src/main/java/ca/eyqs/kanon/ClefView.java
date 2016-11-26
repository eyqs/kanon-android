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
    private final int clef_width;
    private final int clef_height;
    private final int staff_spacing;
    private final int base_height;
    private final int ottava_width;
    private final int ottava_height;
    private final int ottava_middle;
    private final int gclef_top;
    private final int cclef_top;
    private final int fclef_top;
    private final int gclef_bottom;
    private final int cclef_bottom;
    private final int fclef_bottom;
    private static Drawable ottava;
    private static Drawable gclef;
    private static Drawable cclef;
    private static Drawable fclef;
    private static Drawable clef;
    private static int ottava_type;
    private static int ottava_position;
    private static int clef_position;

    public ClefView(Context context, AttributeSet attrs) {
        super(context, attrs);
        canvas_height = (int) context.getResources().getDimension(
            R.dimen.clef_height);
        clef_width = (int) context.getResources().getDimension(
            R.dimen.clef_width);
        clef_height = (int) context.getResources().getDimension(
            R.dimen.clef_image_height);
        staff_spacing = (int) context.getResources().getDimension(
            R.dimen.staff_spacing);
        base_height = (canvas_height - clef_height) / 2;
        ottava_width = (int) context.getResources().getDimension(
            R.dimen.ottava_width);
        ottava_height = (int) context.getResources().getDimension(
            R.dimen.ottava_height);
        ottava_middle = (clef_width - ottava_width) / 2;
        gclef_top = (int) context.getResources().getDimension(
            R.dimen.gclef_top) + base_height;
        cclef_top = (int) context.getResources().getDimension(
            R.dimen.cclef_top) + base_height;
        fclef_top = (int) context.getResources().getDimension(
            R.dimen.fclef_top) + base_height;
        gclef_bottom = (int) context.getResources().getDimension(
            R.dimen.gclef_bottom) + base_height;
        cclef_bottom = (int) context.getResources().getDimension(
            R.dimen.cclef_bottom) + base_height;
        fclef_bottom = (int) context.getResources().getDimension(
            R.dimen.fclef_bottom) + base_height;
        ottava = ContextCompat.getDrawable(
            getContext(), R.drawable.ottava);
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
            ottava_type = 0;
            clef_position = 0;
        } else if (new_clef.equals("Alto")) {
            clef = cclef;
            ottava_type = 0;
            clef_position = 0;
        } else if (new_clef.equals("Tenor")) {
            clef = cclef;
            ottava_type = 0;
            clef_position = -2;
        } else if (new_clef.equals("Bass")) {
            clef = fclef;
            ottava_type = 0;
            clef_position = 0;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        clef.setBounds(0, base_height + clef_position * staff_spacing,
            clef_width,
            base_height + clef_position * staff_spacing + clef_height);
        clef.draw(canvas);
        switch (ottava_type) {
        case 0:
            break;
        case 8:
            ottava.setBounds(ottava_middle, ottava_position,
                ottava_middle + ottava_width,
                ottava_position + ottava_height);
            ottava.draw(canvas);
            break;
        }
    }
}
