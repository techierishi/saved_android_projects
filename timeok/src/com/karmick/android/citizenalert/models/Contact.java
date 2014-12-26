package com.karmick.android.citizenalert.models;

public class Contact {

	private int _id;
	private String name;
	private String phone;
	private String email;
	private String image;

	public boolean visible;

	boolean selected = false;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String toString() {
		return "[{\" _id\" : \"" + _id + "\"},{\" name\" : \"" + name
				+ "\"},{\" phone\" : \"" + phone + "\"},{\" email\" : \""
				+ email + "\"},{\" image\" : \"" + image + "\"}]";
	}

}
