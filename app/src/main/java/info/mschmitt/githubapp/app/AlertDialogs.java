package info.mschmitt.githubapp.app;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * @author Matthias Schmitt
 */
public class AlertDialogs {
    public static void showErrorDialog(Context context, Throwable throwable,
                                       Runnable retryRunnable) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(throwable.getLocalizedMessage());
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Retry", (dialog, which) -> {
            dialog.dismiss();
            retryRunnable.run();
        });
        alertDialog.show();
    }
}
