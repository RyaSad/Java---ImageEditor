package tasks;

import java.util.Random;

public class Dog {
	
	private String name;
	private String gender;
	private int age;
	
	
	public Dog(String name) {
		this.name = name;
		
		Random rand = new Random();
		
		int r = rand.nextInt(2);
		
		if(r == 0) {
			this.gender = "Male";
		}else {
			this.gender = "Female";
		}
		
		this.age = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public String getGender() {
		return gender;
	}
	
	public int getAge() {
		return age;
	}
	
	/*
	public void setAge(int age) {
		this.age = age;
	}
	*/
	
	public void birthday() {
		this.age++;
	}


}
