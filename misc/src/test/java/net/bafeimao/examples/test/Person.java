package net.bafeimao.examples.test;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class Person implements Comparable<Person> {
	private int id;
	private String name;
	private int age;

	public Person(int id) {
		this.id = id;
	}

	public Person(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			Person that = (Person) obj;
			return Objects.equal(id, that.id) && Objects.equal(name, that.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, name);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("name", name).add("age", age).toString();
	}

	@Override
	public int compareTo(Person that) {
		return ComparisonChain.start()
				.compare(age, that.age)
				.compare(name, that.name)
				.compare(id, that.id)
				.result();
	}
}
