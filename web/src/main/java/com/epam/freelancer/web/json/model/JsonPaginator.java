package com.epam.freelancer.web.json.model;

import java.util.Map;

public class JsonPaginator {
	private Map<String, Object> content;
	private Page page;

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
