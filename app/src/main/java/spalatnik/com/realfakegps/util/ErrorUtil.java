package spalatnik.com.realfakegps.util;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class ErrorUtil {

    public static void showError(Context context, String error) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(error);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
