package com.epam.freelancer.web.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.epam.freelancer.database.model.ObjectHolder;

public class Paginator {
	private static final Integer STEP = 10;
	private final String PAGER_ITEMS_NAME;
	private final String URL_START_NAME;
	private final String URL_FIRST_NAME;
	private final String URL_LAST_NAME;
	private final String CURRENT_POSITION;
	private final String ITEMS_NAME;

	public Paginator(String pAGER_ITEMS_NAME, String uRL_START_NAME,
			String uRL_FIRST_NAME, String uRL_LAST_NAME,
			String cURRENT_POSITION, String iTEMS_NAME)
	{
		PAGER_ITEMS_NAME = pAGER_ITEMS_NAME;
		URL_START_NAME = uRL_START_NAME;
		URL_FIRST_NAME = uRL_FIRST_NAME;
		URL_LAST_NAME = uRL_LAST_NAME;
		CURRENT_POSITION = cURRENT_POSITION;
		ITEMS_NAME = iTEMS_NAME;
	}

	public void next(HttpServletRequest request, List<?> items) {
		if (isFirst(request, items) || isLast(request, items))
			return;

		Integer start = Integer.valueOf(request.getParameter(URL_START_NAME));
		start = start > items.size() ? 0 : start;

		List<?> list = items.subList(start,
				(start + STEP) > items.size() ? items.size() : start + STEP);
		request.setAttribute(PAGER_ITEMS_NAME, list);
		request.setAttribute(ITEMS_NAME, list);
		fillPaginationElement(request, items, start);
	}

	private boolean isLast(HttpServletRequest request, List<?> items) {
		String last = request.getParameter(URL_LAST_NAME);
		if (last == null || !"yes".equals(last))
			return false;

		Integer size = items.size();
		Integer offset = size / STEP * STEP > size ? 0 : size / STEP * STEP;

		List<?> list = items.subList(offset, size);
		request.setAttribute(PAGER_ITEMS_NAME, list);
		request.setAttribute(ITEMS_NAME, list);

		fillPaginationElement(request, items, offset);
		return true;
	}

	private boolean isFirst(HttpServletRequest request, List<?> items) {
		String first = request.getParameter(URL_FIRST_NAME);
		if (first == null || !"yes".equals(first))
			return false;

		List<?> list = items.subList(0, STEP > items.size() ? items.size()
				: STEP);
		request.setAttribute(PAGER_ITEMS_NAME, list);
		request.setAttribute(ITEMS_NAME, list);

		fillPaginationElement(request, items, 0);
		return true;
	}

	private void fillPaginationElement(HttpServletRequest request,
			List<?> iList, Integer currentPosition)
	{
		List<ObjectHolder<String, Integer>> pages = fillPrevBtn(iList,
				currentPosition);
		pages.addAll(fillNextBtn(iList, currentPosition));
		request.setAttribute(CURRENT_POSITION, currentPosition);
		request.setAttribute(PAGER_ITEMS_NAME, pages);
	}

	private List<ObjectHolder<String, Integer>> fillNextBtn(List<?> iList,
			Integer currentPosition)
	{
		Integer size = iList.size();
		Integer limit = size / STEP;
		Integer current = currentPosition / STEP + 1;
		List<ObjectHolder<String, Integer>> pages = new ArrayList<>();
		for (int i = current; i <= limit && i - current < 3; i++) {
			pages.add(new ObjectHolder<String, Integer>("?start=" + (i * STEP),
					i));
		}

		return pages;
	}

	private List<ObjectHolder<String, Integer>> fillPrevBtn(List<?> iList,
			Integer currentPosition)
	{
		Integer current = currentPosition / STEP - 1;
		LinkedList<ObjectHolder<String, Integer>> pages = new LinkedList<>();
		int i = current;
		for (; i >= 0 && current - i < 3; i--) {
			pages.addFirst(new ObjectHolder<String, Integer>("?start="
					+ (i * STEP), i));
		}
		pages.add(new ObjectHolder<String, Integer>("current", current + 1));

		return pages;
	}
}
