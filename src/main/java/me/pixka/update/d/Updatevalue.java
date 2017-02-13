package me.pixka.update.d;

import javax.persistence.Entity;

import me.pixka.data.Data;

@Entity
public class Updatevalue extends Data {

	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
