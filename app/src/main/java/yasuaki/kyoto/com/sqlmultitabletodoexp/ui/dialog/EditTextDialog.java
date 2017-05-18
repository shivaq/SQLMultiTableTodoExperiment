package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;

/**
 * Created by Yasuaki on 2017/05/18.
 */

public class EditTextDialog extends DialogFragment {

  public EditTextDialog() {
  }

  public static EditTextDialog newInstance(String msgStr) {
    EditTextDialog.msgStr = msgStr;
    return new EditTextDialog();
  }

  private static String msgStr;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new Builder(getActivity());
    builder.setMessage(msgStr)
        .setPositiveButton("OK", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
        })
        .setNegativeButton("Cancel", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (dialog != null) {
              dialog.dismiss();
            }
          }
        });
    return builder.create();
  }

  @Override
  public void onPause() {
    super.onPause();
    dismiss();
  }
}
