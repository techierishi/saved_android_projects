package com.rishikesh.cooldialogs.dialogs;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rishikesh.cooldialogs.R;

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

		dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

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

	public interface CustomDialogCallback {
		public void onOkClick();
	}

	public static void showYesNoPopup(String message, Context ctx,
			final YesNoClickListener yn) {
		final Dialog dialog = new Dialog(ctx, R.style.FullHeightDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_yes_no);
		
		dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation2;

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

	public static void showThreeButtonPopup(String message, Context ctx,
			HashMap<String, String> buttonText,
			final ThreeButtonClickListener yn) {
		final Dialog dialog = new Dialog(ctx, R.style.FullHeightDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_three_button);
		
		dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation3;

		TextView text = (TextView) dialog.findViewById(R.id.alert_text);
		text.setText(message);

		Button dialogPosButton = (Button) dialog
				.findViewById(R.id.dialogButtonPos);
		dialogPosButton.setText(buttonText.get(Popups.POSITIVE_BTTN_TEXT));

		Button dialogNegButton = (Button) dialog
				.findViewById(R.id.dialogButtonNeg);
		dialogNegButton.setText(buttonText.get(Popups.NEGATIVE_BTTN_TEXT));

		Button dialogCancelButton = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);
		dialogCancelButton.setText(buttonText.get(Popups.NEUTRAL_BTTN_TEXT));

		dialogPosButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				yn.onPositivieButtonClick();
			}
		});

		dialogNegButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				yn.onNegativeButtonClick();

			}
		});

		dialogCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				yn.onNeutraleButtonClick();
			}
		});

		dialog.show();
	}

	public static void showToast(String message, Context ctx) {

		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();

	}

	public interface YesNoClickListener {
		public void onYesButtonClick();

		public void onNoButtonClick();
	}

	public interface ThreeButtonClickListener {

		public void onPositivieButtonClick();

		public void onNegativeButtonClick();

		public void onNeutraleButtonClick();
	}
}
