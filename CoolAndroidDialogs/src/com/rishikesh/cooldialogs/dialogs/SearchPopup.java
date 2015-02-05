package com.rishikesh.cooldialogs.dialogs;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rishikesh.cooldialogs.R;
import com.rishikesh.cooldialogs.dialogs.SearchPopup.SearchKeyValue;



public class SearchPopup {
	private static SearchListAdpater dataAdapter = null;

	public static void showSearchPopup(final Context ctx,
			final ArrayList<SearchKeyValue> keyValList,
			final CustomSearchDialogCallback obj) {
		final Dialog dialog = new Dialog(ctx, R.style.FullHeightDialog);
		dialog.setContentView(R.layout.custom_search_dialog);
		
		dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation4;
		
		ListView listView = (ListView) dialog.findViewById(R.id.listView1);
		
		dataAdapter = new SearchListAdpater(ctx, keyValList);
		listView.setAdapter(dataAdapter);
		listView.setDivider(null);
		listView.setDividerHeight(0);

		listView.setTextFilterEnabled(true);

		final Button cancel_bttn = (Button) dialog
				.findViewById(R.id.button_cancel_dlg);

		cancel_bttn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		final EditText myFilter = (EditText) dialog.findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				dataAdapter.filter(s.toString());

			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				dialog.dismiss();
				obj.onItemClick(keyValList.get(position));
			}
		});

		dialog.show();

	}

	public interface CustomSearchDialogCallback {
		public void onItemClick(SearchKeyValue svObj);
	}

	public class SearchKeyValue {
		private String key;
		private String value;

		public SearchKeyValue(String k, String v) {
			this.key = k;
			this.value = v;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "[{\" key\" : \""+ key+"\"},{\" value\" : \""+ value+"\"}]";
		}
	}
}

class SearchListAdpater extends BaseAdapter {

	private Activity activity;
	private ArrayList<SearchKeyValue> data;
	private ArrayList<SearchKeyValue> arraylist;

	private static LayoutInflater inflater = null;
	SearchKeyValue tempValues = null;
	int i = 0;

	public SearchListAdpater(Context a, ArrayList<SearchKeyValue> d) {

		activity = (Activity) a;

		Log.d(ConstantClass.LOGTAG, "" + d.toString());
		this.data = d;

		this.arraylist = new ArrayList<SearchKeyValue>();
		this.arraylist.addAll(d);

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	// Filter Class
	public void filter(String charText) {

		Log.d(ConstantClass.LOGTAG, "Function calles");

		charText = charText.toLowerCase(Locale.getDefault());
		data.clear();
		if (charText.length() == 0) {
			data.addAll(arraylist);
		} else {
			for (SearchKeyValue kv : arraylist) {
				if (kv.getValue().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					data.add(kv);
				}
			}
		}
		notifyDataSetChanged();
	}

	public int getCount() {

		if (data != null) {
			if (data.size() <= 0)
				return 1;
			else
				return data.size();
		}
		return 1;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView key;
		public TextView value;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			vi = inflater.inflate(R.layout.dialog_searchlist_row, null);

			holder = new ViewHolder();
			holder.key = (TextView) vi.findViewById(R.id.key);
			holder.value = (TextView) vi.findViewById(R.id.value);

			vi.setTag(holder);

		} else
			holder = (ViewHolder) vi.getTag();

		if (data != null) {
			if (data.size() <= 0) {

				holder.key.setText("No Data");
				holder.value.setText("");

			} else {
				tempValues = null;
				tempValues = (SearchKeyValue) data.get(position);

				holder.key.setText("" + tempValues.getKey());
				holder.value.setText(tempValues.getValue());

			}
		} else {
			holder.key.setText("No Data");
			holder.value.setText("");

		}

		return vi;
	}

}
