package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Worker;

import java.util.List;

public interface DevTechManyToManyDao extends
		GenericManyToManyDao<Developer, Technology, Worker, Integer>
{
	List<Technology> getTechnologiesByDevId(Integer id);
	void saveTechnologies(Integer firstId, Integer secondId);
}
