package business;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.database.dao.DeveloperDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Developer;

public class DataTransformerUpdateTest {
	private static DeveloperService developerService;
	private static GenericDao<Developer, Integer> developerDao;

	@BeforeClass
	public static void init() {
		developerService = (DeveloperService) ApplicationContext.getInstance()
				.getBean("developerService");
		developerDao = DAOManager.getInstance().getDAO(
				DeveloperDao.class.getSimpleName());
	}

	@Test
	@Ignore
	public final void test() {
		Developer developer = developerService.findById(30);
		assertNotNull(developer);
		
		developer.setEmail(UUID.randomUUID().toString());
		developer.setRegUrl(UUID.randomUUID().toString());
		developer.setUuid(UUID.randomUUID().toString());
		
		developerDao.update(developer);
	}

}
