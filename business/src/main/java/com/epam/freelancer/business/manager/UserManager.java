package com.epam.freelancer.business.manager;

import com.epam.freelancer.business.service.AdminService;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.UserEntity;

public class UserManager {
	private DeveloperService developerService;
	private CustomerService customerService;
	private AdminService adminService;

	public void modifyUser(UserEntity userEntity) {
		if (userEntity instanceof Developer) {
			developerService.modify((Developer) userEntity);
		}
		if (userEntity instanceof Customer) {
			customerService.modify((Customer) userEntity);
		}
		if (userEntity instanceof Admin) {
			adminService.modify((Admin) userEntity);
		}
	}

	public UserEntity findUserByEmail(String email) {
		Admin admin = adminService.findByEmail(email);
		Customer customer = customerService.findByEmail(email);
		Developer developer = developerService.findByEmail(email);

		if (admin != null)
			return admin;
		if (customer != null)
			return customer;
		if (developer != null)
			return developer;

		return null;
	}

	public UserEntity findUserByUUID(String uuid) {
		Admin admin = adminService.findByUUID(uuid);
		Customer customer = customerService.findByUUID(uuid);
		Developer developer = developerService.findByUUID(uuid);

		if (admin != null)
			return admin;
		if (customer != null)
			return customer;
		if (developer != null)
			return developer;

		return null;
	}

	public boolean isEmailAvailable(String email) {
		return adminService.emailAvailable(email)
				&& customerService.emailAvailable(email)
				&& developerService.emailAvailable(email);
	}

	public boolean isUUIDAvailable(String uuid) {
		return adminService.uuidAvailable(uuid)
				&& customerService.uuidAvailable(uuid)
				&& developerService.uuidAvailable(uuid);
	}

	public void setDeveloperService(DeveloperService developerService) {
		this.developerService = developerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
}
