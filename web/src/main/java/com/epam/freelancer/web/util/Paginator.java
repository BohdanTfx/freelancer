package com.epam.freelancer.web.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.epam.freelancer.database.model.ObjectHolder;

public class Paginator {
	private static final Integer STEP = 1;
	private final String ITEMS_NAME;
	private final String URL_START_NAME;
	private final String URL_FIRST_NAME;
	private final String URL_LAST_NAME;
	private final String CURRENT_POSITION;

	public Paginator(String iTEMS_NAME, String uRL_START_NAME,
			String uRL_FIRST_NAME, String uRL_LAST_NAME, String cURRENT_POSITION)
	{
		ITEMS_NAME = iTEMS_NAME;
		URL_START_NAME = uRL_START_NAME;
		URL_FIRST_NAME = uRL_FIRST_NAME;
		URL_LAST_NAME = uRL_LAST_NAME;
		CURRENT_POSITION = cURRENT_POSITION;
	}

	public void next(HttpServletRequest request, List<?> items) {
		if (isFirst(request, items) || isLast(request, items))
			return;

		Integer start = Integer.valueOf(request.getParameter(URL_START_NAME));
		start = start > items.size() ? 0 : start;
		request.setAttribute(
				ITEMS_NAME,
				items.subList(start,
						(start + STEP) > items.size() ? items.size() : start
								+ STEP));
		fillPaginationElement(request, items, start % STEP);
	}

	private boolean isLast(HttpServletRequest request, List<?> items) {
		String last = request.getParameter(URL_LAST_NAME);
		if (last == null || !"yes".equals(last))
			return false;

		fillPaginationElement(request, items, items.size() % STEP);
		Integer size = items.size();
		Integer offset = STEP > size ? 0 : size % STEP;
		request.setAttribute(ITEMS_NAME, items.subList(offset, size));

		return true;
	}

	private boolean isFirst(HttpServletRequest request, List<?> items) {
		String first = request.getParameter(URL_FIRST_NAME);
		if (first == null || !"yes".equals(first))
			return false;

		fillPaginationElement(request, items, 0);
		request.setAttribute(ITEMS_NAME,
				items.subList(0, STEP > items.size() ? items.size() : STEP));

		return true;
	}

	private void fillPaginationElement(HttpServletRequest request,
			List<?> iList, Integer currentPosition)
	{
		List<ObjectHolder<String, Integer>> pages = fillPrevBtn(iList,
				currentPosition);
		pages.addAll(fillNextBtn(iList, currentPosition));
		request.setAttribute(CURRENT_POSITION, currentPosition % STEP);
		request.setAttribute(ITEMS_NAME, pages);
	}

	private List<ObjectHolder<String, Integer>> fillNextBtn(List<?> iList,
			Integer currentPosition)
	{
		Integer size = iList.size();
		Integer limit = size / STEP;
		Integer current = currentPosition / STEP + 1;
		List<ObjectHolder<String, Integer>> pages = new ArrayList<>();
		for (int i = current; i < limit && i - current < 3; i++) {
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
		for (int i = current; i >= 0 && current - i < 3; i--) {
			pages.addFirst(new ObjectHolder<String, Integer>("?start="
					+ (i * STEP), i));
		}

		return pages;
	}
}
