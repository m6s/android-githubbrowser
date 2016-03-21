package info.mschmitt.githubbrowser.ui;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * @author Matthias Schmitt
 */
public class AlertDialogs {
    public static void showErrorDialog(Context context, Throwable throwable, Runnable retryRunnable,
                                       Runnable cancelHandler) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Error");
        String message = throwable.getLocalizedMessage();
        message = message == null ? throwable.toString() : message;
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss();
            cancelHandler.run();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Retry", (dialog, which) -> {
            dialog.dismiss();
            retryRunnable.run();
        });
        alertDialog.show();
    }
}
