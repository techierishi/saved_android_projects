package com.karmick.android.citizenalert.fileexplorer;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.karmick.android.citizenalert.R;

public class FileChooser extends ListActivity {

	private List<String> myList = new ArrayList<String>();
	private ArrayList<File> fileList = new ArrayList<File>();

	private File currentDir;
	private FileArrayAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = new File("/storage");
		fill(currentDir);

		/**
		 * To search files uncomment below codes
		 */
		/*
		 * File root = new File(Environment.getExternalStorageDirectory()
		 * .getAbsolutePath()); fileList= getfile(root); fill2(fileList);
		 */

	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Current Dir: " + f.getName());
		List<Item> dir = new ArrayList<Item>();
		List<Item> fls = new ArrayList<Item>();
		try {
			for (File ff : dirs) {
				Date lastModDate = new Date(ff.lastModified());
				DateFormat formater = DateFormat.getDateTimeInstance();
				String date_modify = formater.format(lastModDate);
				if (ff.isDirectory()) {

					File[] fbuf = ff.listFiles();
					int buf = 0;
					if (fbuf != null) {
						buf = fbuf.length;
					} else
						buf = 0;
					String num_item = String.valueOf(buf);
					if (buf == 0)
						num_item = num_item + " item";
					else
						num_item = num_item + " items";

					String checkHidden = ff.getName();
					if (!checkHidden.startsWith(".")) {
						dir.add(new Item(ff.getName(), num_item, date_modify,
								ff.getAbsolutePath(), "directory_icon"));
					}

				} else {

					// Only few file extensions will be shown in the list

					if (isValidFile(ff)) {
						fls.add(new Item(ff.getName(), ff.length() + " Byte",
								date_modify, ff.getAbsolutePath(), "file_icon"));
					}
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Item("..", "Parent Directory", "", f.getParent(),
					"directory_up"));
		adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view,
				dir);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Item o = adapter.getItem(position);
		if (o.getImage().equalsIgnoreCase("directory_icon")
				|| o.getImage().equalsIgnoreCase("directory_up")) {
			currentDir = new File(o.getPath());
			fill(currentDir);
		} else {
			onFileClick(o);
		}
	}

	private void onFileClick(Item o) {
		// Toast.makeText(this, "Folder Clicked: "+ currentDir,
		// Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra("GetPath", currentDir.toString());
		intent.putExtra("GetFileName", o.getName());
		setResult(RESULT_OK, intent);
		finish();
	}

	private void fill2(ArrayList<File> f) {
		ArrayList<File> dirs = f;
		this.setTitle("Choose File");
		List<Item> dir = new ArrayList<Item>();
		List<Item> fls = new ArrayList<Item>();
		try {
			for (File ff : dirs) {
				Date lastModDate = new Date(ff.lastModified());
				DateFormat formater = DateFormat.getDateTimeInstance();
				String date_modify = formater.format(lastModDate);
				if (ff.isDirectory()) {

					File[] fbuf = ff.listFiles();
					int buf = 0;
					if (fbuf != null) {
						buf = fbuf.length;
					} else
						buf = 0;
					String num_item = String.valueOf(buf);
					if (buf == 0)
						num_item = num_item + " item";
					else
						num_item = num_item + " items";

					// String formated = lastModDate.toString();
					dir.add(new Item(ff.getName(), num_item, date_modify, ff
							.getAbsolutePath(), "directory_icon"));
				} else {

					fls.add(new Item(ff.getName(), ff.length() + " Byte",
							date_modify, ff.getAbsolutePath(), "file_icon"));
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);

		adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view,
				dir);
		this.setListAdapter(adapter);
	}

	public ArrayList<File> getfile(File dir) {
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					// fileList.add(listFile[i]);
					getfile(listFile[i]);

				} else {
					if (listFile[i].getName().endsWith(".png")
							|| listFile[i].getName().endsWith(".jpg")
							|| listFile[i].getName().endsWith(".jpeg")
							|| listFile[i].getName().endsWith(".gif")
							|| listFile[i].getName().endsWith(".txt")
							|| listFile[i].getName().endsWith(".pdf")
							|| listFile[i].getName().endsWith(".rtf")
							|| listFile[i].getName().endsWith(".mp3"))

					{
						fileList.add(listFile[i]);
						myList.add(fileList.get(i).getAbsolutePath());

					}
				}

			}
		}
		return fileList;
	}

	public boolean isValidFile(File vF) {
		if (vF.getName().endsWith(".png") || vF.getName().endsWith(".jpg")
				|| vF.getName().endsWith(".jpeg")
				|| vF.getName().endsWith(".gif")
				|| vF.getName().endsWith(".txt")
				|| vF.getName().endsWith(".pdf")
				|| vF.getName().endsWith(".rtf")
				|| vF.getName().endsWith(".mp3")
				|| vF.getName().endsWith(".doc")) {
			return true;
		}
		return false;
	}
}
