package me.pixka.update.d;

import javax.persistence.Entity;

import me.pixka.data.Data;
/**
 * ใช้สำหรับเก็บข้อมูล ขอ device แต่ละตัว  update Version
 * @author kykub
 *
 */
@Entity
public class Updatedata extends Data {


	private String currentversion;

	public String getCurrentversion() {
		return currentversion;
	}

	public void setCurrentversion(String currentversion) {
		this.currentversion = currentversion;
	}
	
}
