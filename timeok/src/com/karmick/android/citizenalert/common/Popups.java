package com.karmick.android.citizenalert.common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karmick.android.citizenalert.R;

public class Popups {

	public static final String POSITIVE_BTTN_TEXT = "positive";
	public static final String NEGATIVE_BTTN_TEXT = "negative";
	public static final String NEUTRAL_BTTN_TEXT = "neutral";

	public static void showPopup(String message, Context ctx) {

		final Dialog dialog = new Dialog(ctx, R.style.FullHeightDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.alert_text);
		text.setText(message);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public static void showOkPopup(Context ctx, String txt,
			final CustomDialogCallback obj) {
		final Dialog dialog = new Dialog(ctx, R.style.FullHeightDialog);
		dialog.setContentView(R.layout.custom_dialog);
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.alert_text);
		text.setText(txt);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				obj.onOkClick();
			}
		});

		dialog.show();

	}

	public static void showEditextPopup(Context ctx, String txt,
			final CustomInputDialogCallback obj) {
		final Dialog dialog = new Dialog(ctx, R.style.FullHeightDialog);
		dialog.setContentView(R.layout.custom_edittext_dialog);
		final EditText your_email = (EditText) dialog
				.findViewById(R.id.your_email);
		your_email.setText(txt);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean tf = obj
						.onInputOkClick(your_email.getText().toString());

				if (tf) {
					dialog.dismiss();
				}

			}
		});

		dialog.show();

	}

	public static void showYesNoPopup(String message, Context ctx,
			final YesNoClickListener yn) {
		final Dialog dialog = new Dialog(ctx, R.style.FullHeightDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_yes_no);

		TextView text = (TextView) dialog.findViewById(R.id.alert_text);
		text.setText(message);

		Button dialogYesButton = (Button) dialog
				.findViewById(R.id.dialogButtonYes);
		Button dialogNoButton = (Button) dialog
				.findViewById(R.id.dialogButtonNo);
		dialogYesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				yn.onYesButtonClick();
			}
		});

		dialogNoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				yn.onNoButtonClick();

			}
		});

		dialog.show();
	}

	public interface CustomDialogCallback {
		public void onOkClick();
	}

	public interface CustomInputDialogCallback {
		public boolean onInputOkClick(String strr);
	}

	public interface YesNoClickListener {
		public void onYesButtonClick();

		public void onNoButtonClick();
	}

	public static void showToast(String message, Context ctx) {

		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();

	}

}
