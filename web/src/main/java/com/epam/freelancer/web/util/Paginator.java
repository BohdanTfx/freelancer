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

	public void next(Page page, List<?> items, HttpServletResponse response) {
		Map<String, Integer> map = new HashMap<>();
		map.put("start", page.getStart());
		map.put("step", page.getStep());
		map.put("first", page.getFirst());
		map.put("last", page.getLast());
		next(map, items, response);
	}

	public void next(Map<String, Integer> page, List<?> items,
			HttpServletResponse response)
	{
		responseText.clear();
		if (isFirst(page, response, items) || isLast(page, response, items)) {
			return;
		}

		Integer start = page.get("start");
		start = start > items.size() ? 0 : start;
		Integer step = page.get("step") == null ? 10 : page.get("step");

		List<?> list = items.subList(start,
				(start + step) > items.size() ? items.size() : start + step);

		sendResponse(list, fillPaginationElement(page, list, start, response),
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

	private boolean isLast(Map<String, Integer> page,
			HttpServletResponse response, List<?> items)
	{
		Integer last = page.get("last");
		if (last == null || last != 0)
			return false;
		Integer step = page.get("step") == null ? 10 : page.get("step");

		Integer size = items.size();
		Integer offset = size / step * step > size ? 0 : size / step * step;

		List<?> list = items.subList(offset, size);
		sendResponse(list, fillPaginationElement(page, list, offset, response),
				response);
		return true;
	}

	private boolean isFirst(Map<String, Integer> page,
			HttpServletResponse response, List<?> items)
	{
		Integer last = page.get("first");
		if (last == null || last != 0)
			return false;
		Integer step = page.get("step") == null ? 10 : page.get("step");

		List<?> list = items.subList(0, step > items.size() ? items.size()
				: step);
		sendResponse(list, fillPaginationElement(page, list, 0, response),
				response);
		return true;
	}

	private ObjectHolder<Integer, List<ObjectHolder<String, Integer>>> fillPaginationElement(
			Map<String, Integer> page, List<?> iList, Integer currentPosition,
			HttpServletResponse response)
	{
		List<ObjectHolder<String, Integer>> pages = fillPrevBtn(page,
				currentPosition);
		pages.addAll(fillNextBtn(page, iList.size(), currentPosition));
		return new ObjectHolder<Integer, List<ObjectHolder<String, Integer>>>(
				currentPosition, pages);
	}

	private List<ObjectHolder<String, Integer>> fillNextBtn(
			Map<String, Integer> page, Integer size, Integer currentPosition)
	{
		Integer step = page.get("step") == null ? 10 : page.get("step");
		Integer limit = size / step;
		Integer current = currentPosition / step + 1;
		List<ObjectHolder<String, Integer>> pages = new ArrayList<>();
		for (int i = current; i <= limit && i - current < 3; i++) {
			pages.add(new ObjectHolder<String, Integer>("start", (i * step)));
		}

		return pages;
	}

	private List<ObjectHolder<String, Integer>> fillPrevBtn(
			Map<String, Integer> page, Integer currentPosition)
	{
		Integer step = page.get("step") == null ? 10 : page.get("step");
		Integer current = currentPosition / step - 1;
		LinkedList<ObjectHolder<String, Integer>> pages = new LinkedList<>();
		int i = current;
		for (; i >= 0 && current - i < 3; i--) {
			pages.addFirst(new ObjectHolder<String, Integer>("start",
					(i * step)));
		}
		pages.add(new ObjectHolder<String, Integer>("current", current + 1));

		return pages;
	}

}
