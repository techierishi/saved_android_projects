package com.rishikesh.cooldialogs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rishikesh.cooldialogs.dialogs.Popups;
import com.rishikesh.cooldialogs.dialogs.Popups.CustomDialogCallback;
import com.rishikesh.cooldialogs.dialogs.Popups.ThreeButtonClickListener;
import com.rishikesh.cooldialogs.dialogs.Popups.YesNoClickListener;
import com.rishikesh.cooldialogs.dialogs.SearchPopup;
import com.rishikesh.cooldialogs.dialogs.SearchPopup.CustomSearchDialogCallback;
import com.rishikesh.cooldialogs.dialogs.SearchPopup.SearchKeyValue;

public class CoolDialogTestActivity extends ActionBarActivity {

	Button button_yes_no;
	Button button_ok;
	Button button_tbttn;
	Button button_search;
	Button button_pop;
	TextView tv_search_result;

	ArrayList<SearchKeyValue> keyValList = new ArrayList<SearchKeyValue>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cool_dialog_test);

		initViews();

		button_yes_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Popups.showYesNoPopup("Do you really want to delete this ?",
						CoolDialogTestActivity.this, new YesNoClickListener() {

							@Override
							public void onYesButtonClick() {

							}

							@Override
							public void onNoButtonClick() {

							}
						});

			}
		});

		button_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Popups.showOkPopup(CoolDialogTestActivity.this,
						"Hello This is a message.", new CustomDialogCallback() {

							@Override
							public void onOkClick() {

							}
						});
			}
		});

		keyValList = getCountriesIdNamePair(CoolDialogTestActivity.this);
		button_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchPopup.showSearchPopup(CoolDialogTestActivity.this,
						keyValList, new CustomSearchDialogCallback() {

							@Override
							public void onItemClick(SearchKeyValue svObj) {
								tv_search_result.setText(svObj.getKey() + " : "
										+ svObj.getValue());
							}
						});

			}
		});

		button_tbttn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(Popups.POSITIVE_BTTN_TEXT, "Edit");
				hm.put(Popups.NEGATIVE_BTTN_TEXT, "Delete");
				hm.put(Popups.NEUTRAL_BTTN_TEXT, "Cancel");

				Popups.showThreeButtonPopup("Please choose option.",
						CoolDialogTestActivity.this, hm,
						new ThreeButtonClickListener() {

							@Override
							public void onPositivieButtonClick() {

							}

							@Override
							public void onNeutraleButtonClick() {

							}

							@Override
							public void onNegativeButtonClick() {

							}
						});
			}
		});

		button_pop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View popupView = layoutInflater.inflate(R.layout.popup, null);
				final PopupWindow popupWindow = new PopupWindow(popupView,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				Button btnDismiss = (Button) popupView
						.findViewById(R.id.dismiss);
				btnDismiss.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});

				popupWindow.showAsDropDown(button_pop, 50, -30);

			}
		});

	}

	public ArrayList<SearchKeyValue> getCountriesIdNamePair(Context ctx) {
		ArrayList<SearchKeyValue> keyValList_local = new ArrayList<SearchKeyValue>();

		SearchPopup sP = new SearchPopup();

		keyValList_local.add(sP.new SearchKeyValue("0", "Afghanistan"));
		keyValList_local.add(sP.new SearchKeyValue("1", "Albania"));
		keyValList_local.add(sP.new SearchKeyValue("2", "Algeria"));
		keyValList_local.add(sP.new SearchKeyValue("3", "Andorra"));
		keyValList_local.add(sP.new SearchKeyValue("4", "Angola"));
		keyValList_local.add(sP.new SearchKeyValue("5", "Antigua and Barbuda"));
		keyValList_local.add(sP.new SearchKeyValue("6", "Argentina"));
		keyValList_local.add(sP.new SearchKeyValue("7", "Armenia"));
		keyValList_local.add(sP.new SearchKeyValue("8", "Aruba"));
		keyValList_local.add(sP.new SearchKeyValue("9", "Australia"));
		keyValList_local.add(sP.new SearchKeyValue("10", "Austria"));
		keyValList_local.add(sP.new SearchKeyValue("11", "Azerbaijan"));
		keyValList_local.add(sP.new SearchKeyValue("12", "Bahamas, The"));
		keyValList_local.add(sP.new SearchKeyValue("13", "Bahrain"));
		keyValList_local.add(sP.new SearchKeyValue("14", "Bangladesh"));
		keyValList_local.add(sP.new SearchKeyValue("15", "Barbados"));
		keyValList_local.add(sP.new SearchKeyValue("16", "Belarus"));
		keyValList_local.add(sP.new SearchKeyValue("17", "Belgium"));
		keyValList_local.add(sP.new SearchKeyValue("18", "Belize"));
		keyValList_local.add(sP.new SearchKeyValue("19", "Benin"));
		keyValList_local.add(sP.new SearchKeyValue("20", "Bhutan"));
		keyValList_local.add(sP.new SearchKeyValue("21", "Bolivia"));
		keyValList_local.add(sP.new SearchKeyValue("22",
				"Bosnia and Herzegovina"));
		keyValList_local.add(sP.new SearchKeyValue("23", "Botswana"));
		keyValList_local.add(sP.new SearchKeyValue("24", "Brazil"));
		keyValList_local.add(sP.new SearchKeyValue("25", "Brunei"));
		keyValList_local.add(sP.new SearchKeyValue("26", "Bulgaria"));
		keyValList_local.add(sP.new SearchKeyValue("27", "Burkina Faso"));
		keyValList_local.add(sP.new SearchKeyValue("28", "Burma"));
		keyValList_local.add(sP.new SearchKeyValue("29", "Burundi"));
		keyValList_local.add(sP.new SearchKeyValue("30", "Cambodia"));
		keyValList_local.add(sP.new SearchKeyValue("31", "Cameroon"));
		keyValList_local.add(sP.new SearchKeyValue("32", "Canada"));
		keyValList_local.add(sP.new SearchKeyValue("33", "Cape Verde"));
		keyValList_local.add(sP.new SearchKeyValue("34",
				"Central African Republic"));
		keyValList_local.add(sP.new SearchKeyValue("35", "Chad"));
		keyValList_local.add(sP.new SearchKeyValue("36", "Chile"));
		keyValList_local.add(sP.new SearchKeyValue("37", "China"));
		keyValList_local.add(sP.new SearchKeyValue("38", "Colombia"));
		keyValList_local.add(sP.new SearchKeyValue("39", "Comoros"));
		keyValList_local.add(sP.new SearchKeyValue("40",
				"Congo, Democratic Republic of the"));
		keyValList_local.add(sP.new SearchKeyValue("41",
				"Congo, Republic of the"));
		keyValList_local.add(sP.new SearchKeyValue("42", "Costa Rica"));
		keyValList_local.add(sP.new SearchKeyValue("43", "Cote d'Ivoire"));
		keyValList_local.add(sP.new SearchKeyValue("44", "Croatia"));
		keyValList_local.add(sP.new SearchKeyValue("45", "Cuba"));
		keyValList_local.add(sP.new SearchKeyValue("46", "Curacao"));
		keyValList_local.add(sP.new SearchKeyValue("47", "Cyprus"));
		keyValList_local.add(sP.new SearchKeyValue("48", "Czech Republic"));
		keyValList_local.add(sP.new SearchKeyValue("49", "Denmark"));
		keyValList_local.add(sP.new SearchKeyValue("50", "Djibouti"));
		keyValList_local.add(sP.new SearchKeyValue("51", "Dominica"));
		keyValList_local.add(sP.new SearchKeyValue("52", "Dominican Republic"));
		keyValList_local.add(sP.new SearchKeyValue("53", "Ecuador"));
		keyValList_local.add(sP.new SearchKeyValue("54", "Egypt"));
		keyValList_local.add(sP.new SearchKeyValue("55", "El Salvador"));
		keyValList_local.add(sP.new SearchKeyValue("56", "Equatorial Guinea"));
		keyValList_local.add(sP.new SearchKeyValue("57", "Eritrea"));
		keyValList_local.add(sP.new SearchKeyValue("58", "Estonia"));
		keyValList_local.add(sP.new SearchKeyValue("59", "Ethiopia"));
		keyValList_local.add(sP.new SearchKeyValue("60", "Fiji"));
		keyValList_local.add(sP.new SearchKeyValue("61", "Finland"));
		keyValList_local.add(sP.new SearchKeyValue("62", "France"));
		keyValList_local.add(sP.new SearchKeyValue("63", "Gabon"));
		keyValList_local.add(sP.new SearchKeyValue("64", "Gambia, The"));
		keyValList_local.add(sP.new SearchKeyValue("65", "Georgia"));
		keyValList_local.add(sP.new SearchKeyValue("66", "Germany"));
		keyValList_local.add(sP.new SearchKeyValue("67", "Ghana"));
		keyValList_local.add(sP.new SearchKeyValue("68", "Greece"));
		keyValList_local.add(sP.new SearchKeyValue("69", "Grenada"));
		keyValList_local.add(sP.new SearchKeyValue("70", "Guatemala"));
		keyValList_local.add(sP.new SearchKeyValue("71", "Guinea"));
		keyValList_local.add(sP.new SearchKeyValue("72", "Guinea-Bissau"));
		keyValList_local.add(sP.new SearchKeyValue("73", "Guyana"));
		keyValList_local.add(sP.new SearchKeyValue("74", "Haiti"));
		keyValList_local.add(sP.new SearchKeyValue("75", "Holy See"));
		keyValList_local.add(sP.new SearchKeyValue("76", "Honduras"));
		keyValList_local.add(sP.new SearchKeyValue("77", "Hong Kong"));
		keyValList_local.add(sP.new SearchKeyValue("78", "Hungary"));
		keyValList_local.add(sP.new SearchKeyValue("79", "Iceland"));
		keyValList_local.add(sP.new SearchKeyValue("80", "India"));
		keyValList_local.add(sP.new SearchKeyValue("81", "Indonesia"));
		keyValList_local.add(sP.new SearchKeyValue("82", "Iran"));
		keyValList_local.add(sP.new SearchKeyValue("83", "Iraq"));
		keyValList_local.add(sP.new SearchKeyValue("84", "Ireland"));
		keyValList_local.add(sP.new SearchKeyValue("85", "Israel"));
		keyValList_local.add(sP.new SearchKeyValue("86", "Italy"));
		keyValList_local.add(sP.new SearchKeyValue("87", "Jamaica"));
		keyValList_local.add(sP.new SearchKeyValue("88", "Japan"));
		keyValList_local.add(sP.new SearchKeyValue("89", "Jordan"));
		keyValList_local.add(sP.new SearchKeyValue("90", "Kazakhstan"));
		keyValList_local.add(sP.new SearchKeyValue("91", "Kenya"));
		keyValList_local.add(sP.new SearchKeyValue("92", "Kiribati"));
		keyValList_local.add(sP.new SearchKeyValue("93", "Korea, North"));
		keyValList_local.add(sP.new SearchKeyValue("94", "Korea, South"));
		keyValList_local.add(sP.new SearchKeyValue("95", "Kosovo"));
		keyValList_local.add(sP.new SearchKeyValue("96", "Kuwait"));
		keyValList_local.add(sP.new SearchKeyValue("97", "Kyrgyzstan"));
		keyValList_local.add(sP.new SearchKeyValue("98", "Laos"));
		keyValList_local.add(sP.new SearchKeyValue("99", "Latvia"));
		keyValList_local.add(sP.new SearchKeyValue("100", "Lebanon"));
		keyValList_local.add(sP.new SearchKeyValue("101", "Lesotho"));
		keyValList_local.add(sP.new SearchKeyValue("102", "Liberia"));
		keyValList_local.add(sP.new SearchKeyValue("103", "Libya"));
		keyValList_local.add(sP.new SearchKeyValue("104", "Liechtenstein"));
		keyValList_local.add(sP.new SearchKeyValue("105", "Lithuania"));
		keyValList_local.add(sP.new SearchKeyValue("106", "Luxembourg"));
		keyValList_local.add(sP.new SearchKeyValue("107", "Macau"));
		keyValList_local.add(sP.new SearchKeyValue("108", "Macedonia"));
		keyValList_local.add(sP.new SearchKeyValue("109", "Madagascar"));
		keyValList_local.add(sP.new SearchKeyValue("110", "Malawi"));
		keyValList_local.add(sP.new SearchKeyValue("111", "Malaysia"));
		keyValList_local.add(sP.new SearchKeyValue("112", "Maldives"));
		keyValList_local.add(sP.new SearchKeyValue("113", "Mali"));
		keyValList_local.add(sP.new SearchKeyValue("114", "Malta"));
		keyValList_local.add(sP.new SearchKeyValue("115", "Marshall Islands"));
		keyValList_local.add(sP.new SearchKeyValue("116", "Mauritania"));
		keyValList_local.add(sP.new SearchKeyValue("117", "Mauritius"));
		keyValList_local.add(sP.new SearchKeyValue("118", "Mexico"));
		keyValList_local.add(sP.new SearchKeyValue("119", "Micronesia"));
		keyValList_local.add(sP.new SearchKeyValue("120", "Moldova"));
		keyValList_local.add(sP.new SearchKeyValue("121", "Monaco"));
		keyValList_local.add(sP.new SearchKeyValue("122", "Mongolia"));
		keyValList_local.add(sP.new SearchKeyValue("123", "Montenegro"));
		keyValList_local.add(sP.new SearchKeyValue("124", "Morocco"));
		keyValList_local.add(sP.new SearchKeyValue("125", "Mozambique"));
		keyValList_local.add(sP.new SearchKeyValue("126", "Namibia"));
		keyValList_local.add(sP.new SearchKeyValue("127", "Nauru"));
		keyValList_local.add(sP.new SearchKeyValue("128", "Nepal"));
		keyValList_local.add(sP.new SearchKeyValue("129", "Netherlands"));
		keyValList_local.add(sP.new SearchKeyValue("130",
				"Netherlands Antilles"));
		keyValList_local.add(sP.new SearchKeyValue("131", "New Zealand"));
		keyValList_local.add(sP.new SearchKeyValue("132", "Nicaragua"));
		keyValList_local.add(sP.new SearchKeyValue("133", "Niger"));
		keyValList_local.add(sP.new SearchKeyValue("134", "Nigeria"));
		keyValList_local.add(sP.new SearchKeyValue("135", "North Korea"));
		keyValList_local.add(sP.new SearchKeyValue("136", "Norway"));
		keyValList_local.add(sP.new SearchKeyValue("137", "Oman"));
		keyValList_local.add(sP.new SearchKeyValue("138", "Pakistan"));
		keyValList_local.add(sP.new SearchKeyValue("139", "Palau"));
		keyValList_local.add(sP.new SearchKeyValue("140",
				"Palestinian Territories"));
		keyValList_local.add(sP.new SearchKeyValue("141", "Panama"));
		keyValList_local.add(sP.new SearchKeyValue("142", "Papua New Guinea"));
		keyValList_local.add(sP.new SearchKeyValue("143", "Paraguay"));
		keyValList_local.add(sP.new SearchKeyValue("144", "Peru"));
		keyValList_local.add(sP.new SearchKeyValue("145", "Philippines"));
		keyValList_local.add(sP.new SearchKeyValue("146", "Poland"));
		keyValList_local.add(sP.new SearchKeyValue("147", "Portugal"));
		keyValList_local.add(sP.new SearchKeyValue("148", "Qatar"));
		keyValList_local.add(sP.new SearchKeyValue("149", "Romania"));
		keyValList_local.add(sP.new SearchKeyValue("150", "Russia"));
		keyValList_local.add(sP.new SearchKeyValue("151", "Rwanda"));
		keyValList_local
				.add(sP.new SearchKeyValue("152", "Sa Kitts and Nevis"));
		keyValList_local.add(sP.new SearchKeyValue("153", "Sa Lucia"));
		keyValList_local.add(sP.new SearchKeyValue("154",
				"Sa Vincent and the Grenadines"));
		keyValList_local.add(sP.new SearchKeyValue("155", "Samoa"));
		keyValList_local.add(sP.new SearchKeyValue("156", "San Marino"));
		keyValList_local.add(sP.new SearchKeyValue("157",
				"Sao Tome and Principe"));
		keyValList_local.add(sP.new SearchKeyValue("158", "Saudi Arabia"));
		keyValList_local.add(sP.new SearchKeyValue("159", "Senegal"));
		keyValList_local.add(sP.new SearchKeyValue("160", "Serbia"));
		keyValList_local.add(sP.new SearchKeyValue("161", "Seychelles"));
		keyValList_local.add(sP.new SearchKeyValue("162", "Sierra Leone"));
		keyValList_local.add(sP.new SearchKeyValue("163", "Singapore"));
		keyValList_local.add(sP.new SearchKeyValue("164", "S Maarten"));
		keyValList_local.add(sP.new SearchKeyValue("165", "Slovakia"));
		keyValList_local.add(sP.new SearchKeyValue("166", "Slovenia"));
		keyValList_local.add(sP.new SearchKeyValue("167", "Solomon Islands"));
		keyValList_local.add(sP.new SearchKeyValue("168", "Somalia"));
		keyValList_local.add(sP.new SearchKeyValue("169", "South Africa"));
		keyValList_local.add(sP.new SearchKeyValue("170", "South Korea"));
		keyValList_local.add(sP.new SearchKeyValue("171", "South Sudan"));
		keyValList_local.add(sP.new SearchKeyValue("172", "Spain"));
		keyValList_local.add(sP.new SearchKeyValue("173", "Sri Lanka"));
		keyValList_local.add(sP.new SearchKeyValue("174", "Sudan"));
		keyValList_local.add(sP.new SearchKeyValue("175", "Suriname"));
		keyValList_local.add(sP.new SearchKeyValue("176", "Swaziland"));
		keyValList_local.add(sP.new SearchKeyValue("177", "Sweden"));
		keyValList_local.add(sP.new SearchKeyValue("178", "Switzerland"));
		keyValList_local.add(sP.new SearchKeyValue("179", "Syria"));
		keyValList_local.add(sP.new SearchKeyValue("180", "Taiwan"));
		keyValList_local.add(sP.new SearchKeyValue("181", "Tajikistan"));
		keyValList_local.add(sP.new SearchKeyValue("182", "Tanzania"));
		keyValList_local.add(sP.new SearchKeyValue("183", "Thailand"));
		keyValList_local.add(sP.new SearchKeyValue("184", "Timor-Leste"));
		keyValList_local.add(sP.new SearchKeyValue("185", "Togo"));
		keyValList_local.add(sP.new SearchKeyValue("186", "Tonga"));
		keyValList_local
				.add(sP.new SearchKeyValue("187", "Trinidad and Tobago"));
		keyValList_local.add(sP.new SearchKeyValue("188", "Tunisia"));
		keyValList_local.add(sP.new SearchKeyValue("189", "Turkey"));
		keyValList_local.add(sP.new SearchKeyValue("190", "Turkmenistan"));
		keyValList_local.add(sP.new SearchKeyValue("191", "Tuvalu"));
		keyValList_local.add(sP.new SearchKeyValue("192", "Uganda"));
		keyValList_local.add(sP.new SearchKeyValue("193", "Ukraine"));
		keyValList_local.add(sP.new SearchKeyValue("194",
				"United Arab Emirates"));
		keyValList_local.add(sP.new SearchKeyValue("195", "United Kingdom"));
		keyValList_local.add(sP.new SearchKeyValue("196", "Uruguay"));
		keyValList_local.add(sP.new SearchKeyValue("197", "Uzbekistan"));
		keyValList_local.add(sP.new SearchKeyValue("198", "Vanuatu"));
		keyValList_local.add(sP.new SearchKeyValue("199", "Venezuela"));
		keyValList_local.add(sP.new SearchKeyValue("200", "Vietnam"));
		keyValList_local.add(sP.new SearchKeyValue("201", "Yemen"));
		keyValList_local.add(sP.new SearchKeyValue("202", "Zambia"));
		keyValList_local.add(sP.new SearchKeyValue("203", "Zimbabwe"));

		return keyValList_local;
	}

	public void initViews() {
		tv_search_result = (TextView) findViewById(R.id.tv_search_result);
		button_tbttn = (Button) findViewById(R.id.button_tbttn);
		button_yes_no = (Button) findViewById(R.id.button_yes_no);
		button_ok = (Button) findViewById(R.id.button_ok);
		button_search = (Button) findViewById(R.id.button_search);
		button_pop = (Button) findViewById(R.id.button_pop);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cool_dialog_test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public String readContacts() {
		StringBuffer sb = new StringBuffer();
		sb.append("......Contact Details.....");
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		String phone = null;
		String emailContact = null;
		String emailType = null;
		String image_uri = "";
		Bitmap bitmap = null;
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				image_uri = cur
						.getString(cur
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					System.out.println("name : " + name + ", ID : " + id);
					sb.append("\n Contact Name:" + name);
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						phone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						sb.append("\n Phone number:" + phone);
						System.out.println("phone" + phone);
					}
					pCur.close();

					Cursor emailCur = cr.query(
							ContactsContract.CommonDataKinds.Email.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (emailCur.moveToNext()) {
						emailContact = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						emailType = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
						sb.append("\nEmail:" + emailContact + "Email type:"
								+ emailType);
						System.out.println("Email " + emailContact
								+ " Email Type : " + emailType);

					}

					emailCur.close();
				}

				if (image_uri != null) {
					System.out.println(Uri.parse(image_uri));
					try {
						bitmap = MediaStore.Images.Media
								.getBitmap(this.getContentResolver(),
										Uri.parse(image_uri));
						sb.append("\n Image in Bitmap:" + bitmap);
						System.out.println(bitmap);

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				sb.append("\n........................................");
			}

			return sb.toString();
		}
		return sb.toString();
	}

}
