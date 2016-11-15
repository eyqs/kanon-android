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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String message = getArguments().getString("message", "");
        final String positive = getArguments().getString("positive", "");
        final MainActivity main = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setMessage(message);
        builder.setNegativeButton(getString(R.string.alert_settings),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    main.changeSettings();
                }
            });
        builder.setPositiveButton(positive,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    if (positive.equals(getString(R.string.alert_quit))) {
                        main.quitActivity();
                    }
                }
            });
        return builder.show();
    }
}
