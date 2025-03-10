package com.demo.enotes_api.enums;

public enum ToDoStatus {
	NOT_STARTED(1, "Not Started"),
	IN_PROGRESS(2, "In Progress"),
	COMPLETED(3, "Completed");

	private Integer id;
	private String name;

	ToDoStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
