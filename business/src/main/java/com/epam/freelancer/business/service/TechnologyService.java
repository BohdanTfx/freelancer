package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.GenericManyToManyDao;
import com.epam.freelancer.database.dao.TechnologyDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 23.01.2016.
 */
public class TechnologyService extends GenericService<Technology, Integer> {
	private GenericManyToManyDao<Ordering, Technology, Worker, Integer> orderingTechnoloyManyToManyDao;

	public TechnologyService() {
		super(DAOManager.getInstance().getDAO(
				TechnologyDao.class.getSimpleName()));
		DAOManager daoManager = DAOManager.getInstance();
		genericDao.setConnectionPool(daoManager.getConnectionPool());
	}

	@Override
	public Technology create(Map<String, String[]> data) {
		if (!isDataValid(prepareData(data)))
			throw new RuntimeException("Validation exception");

		Technology technology = new Technology();
		String[] value = data.get("name");
		technology.setName(value != null ? value[0] : null);
		technology = genericDao.save(technology);
		return technology;
	}

	private Map<ValidationParametersBuilder.Parameters, String> prepareData(
			Map<String, String[]> data)
	{
		Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
		map.put(ValidationParametersBuilder.createParameters(false)
						.minLength(1).maxLength(50),
				data.get("name") == null ? null : data.get("name")[0]);
		return map;
	}

	public void setOrderingTechnoloyManyToManyDao(
			GenericManyToManyDao<Ordering, Technology, Worker, Integer> orderingTechnoloyManyToManyDao)
	{
		this.orderingTechnoloyManyToManyDao = orderingTechnoloyManyToManyDao;
		this.orderingTechnoloyManyToManyDao.setConnectionPool(DAOManager
				.getInstance().getConnectionPool());
	}

	public List<Ordering> findTechnologyOrderings(Integer techId) {
		return orderingTechnoloyManyToManyDao.getBasedOnSecond(techId);
	}

	public List<Technology> findTechnolodyByOrderingId(Integer orderId) {
		return orderingTechnoloyManyToManyDao.getBasedOnFirst(orderId);
	}
}
