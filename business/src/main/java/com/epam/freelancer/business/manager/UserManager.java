package com.epam.freelancer.business.manager;

import com.epam.freelancer.business.encode.Encryption;
import com.epam.freelancer.business.encode.SHA256Util;
import com.epam.freelancer.business.service.AdminService;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.UserEntity;

import java.util.Map;

public class UserManager {
	private DeveloperService developerService;
	private CustomerService customerService;
	private AdminService adminService;

	public UserEntity modifyUser(UserEntity userEntity) {
		if (userEntity instanceof Developer) {
			return developerService.modify((Developer) userEntity);
		}
		if (userEntity instanceof Customer) {
			return customerService.modify((Customer) userEntity);
		}
		if (userEntity instanceof Admin) {
			return adminService.modify((Admin) userEntity);
		}
		return null;
	}

	public UserEntity setIsFirstFalseAndModify(UserEntity userEntity) {
		if (userEntity instanceof Developer) {
			Developer developer = developerService.findById(userEntity.getId());
			developer.setIsFirst(false);
			return developerService.modify(developer);
		}
		if (userEntity instanceof Customer) {
			Customer customer = customerService.findById(userEntity.getId());
			customer.setIsFirst(false);
			return customerService.modify(customer);
		}

		return null;
	}

	public UserEntity createUser(Map<String, String[]> userData, String type) {
		switch (type) {
		case "developer":
			return developerService.create(userData);
		case "customer":
			return customerService.create(userData);
		case "admin":
			return adminService.create(userData);
		default:
			return null;
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

	public Boolean isEmailAvailable(String email) {
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

	public boolean validCredentials(String login, String inputPass,
			UserEntity ue)
	{
		String hashPass = new Encryption(new SHA256Util()).crypt(inputPass,
				ue.getSalt());

		return login.equals(ue.getEmail()) && hashPass.equals(ue.getPassword());

	}
}
