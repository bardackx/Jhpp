package com.bardackx.jhpp.tests.collections;

import java.util.List;
import java.util.Set;

public class TodoList {

	private String title;
	private List<TodoListItem> items;
	private Set<String> tags;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<TodoListItem> getItems() {
		return items;
	}

	public void setItems(List<TodoListItem> items) {
		this.items = items;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TodoList other = (TodoList) obj;
		if (items == null) {
			if (other.items != null) return false;
		} else if (!items.equals(other.items)) return false;
		if (tags == null) {
			if (other.tags != null) return false;
		} else if (!tags.equals(other.tags)) return false;
		if (title == null) {
			if (other.title != null) return false;
		} else if (!title.equals(other.title)) return false;
		return true;
	}

}
