package com.epam.freelancer.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.epam.freelancer.database.model.ObjectHolder;
import com.epam.freelancer.web.json.model.Page;

public class Paginator {
	private ObjectMapper objectMapper;
	private Map<String, Object> responseText;

	public Paginator() {
		objectMapper = new ObjectMapper();
		responseText = new HashMap<>();
	}

	public void next(Page page, HttpServletResponse response,
			Integer totalSize, List<?> list)
	{
		Map<String, Integer> map = new HashMap<>();
		map.put("start", page.getStart());
		map.put("step", page.getStep());
		map.put("first", page.getFirst());
		map.put("last", page.getLast());
		next(map, response, totalSize, list);
	}

	public void next(Map<String, Integer> page, HttpServletResponse response,
			Integer totalSize, List<?> list)
	{
		responseText.clear();
		Integer step = page.get("step") == null ? 10 : page.get("step");

		if (isLast(page)) {

			Integer offset = totalSize / step * step > totalSize ? 0
					: totalSize / step * step;

			sendResponse(list,
					fillPaginationElement(totalSize, page, offset, response),
					response);
			return;
		}

		Integer start = page.get("start");
		start = start > totalSize ? 0 : start;

		sendResponse(list,
				fillPaginationElement(totalSize, page, start, response),
				response);
	}

	private void sendResponse(List<?> list,
			ObjectHolder<Integer, List<ObjectHolder<String, Integer>>> result,
			HttpServletResponse response)
	{
		responseText.put("currentPosition", result.getFirst());
		responseText.put("pages", result.getSecond());
		responseText.put("items", list);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.print(objectMapper.writeValueAsString(responseText));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isLast(Map<String, Integer> page) {
		Integer last = page.get("last");
		if (last == null || last == 0)
			return false;
		return true;
	}

	private ObjectHolder<Integer, List<ObjectHolder<String, Integer>>> fillPaginationElement(
			Integer totalSize, Map<String, Integer> page,
			Integer currentPosition, HttpServletResponse response)
	{
		List<ObjectHolder<String, Integer>> pages = fillPrevBtn(page,
				currentPosition);
		pages.addAll(fillNextBtn(page, totalSize, currentPosition));
		return new ObjectHolder<Integer, List<ObjectHolder<String, Integer>>>(
				currentPosition, pages);
	}

	private List<ObjectHolder<String, Integer>> fillNextBtn(
			Map<String, Integer> page, Integer size, Integer currentPosition)
	{
		Integer step = page.get("step") == null ? 10 : page.get("step");
		Integer limit = size / step;
		Integer current = currentPosition / step;
		List<ObjectHolder<String, Integer>> pages = new ArrayList<>();
		for (int i = current; i <= limit && i - current < 3; i++) {
			pages.add(new ObjectHolder<String, Integer>("start", i));
		}

		return pages;
	}

	private List<ObjectHolder<String, Integer>> fillPrevBtn(
			Map<String, Integer> page, Integer currentPosition)
	{
		Integer step = page.get("step") == null ? 10 : page.get("step");
		Integer current = currentPosition / step;
		LinkedList<ObjectHolder<String, Integer>> pages = new LinkedList<>();
		int i = current;
		for (; i >= 0 && current - i < 3; i--) {
			pages.addFirst(new ObjectHolder<String, Integer>("start", i));
		}
		pages.add(new ObjectHolder<String, Integer>("current", current));

		return pages;
	}

}
