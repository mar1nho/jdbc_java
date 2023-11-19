package application;

import db.DBException;
import model.dao.DAO_Factory;
import model.dao.DepartmentDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DepartmentCommand {
	
	private static final DepartmentDAO departmentDAO = DAO_Factory.createDepartmentDAO();
	
	public static void registerDepartmentMenu() {
		Scanner scanner = new Scanner(System.in);
		Menus.sleep(500);
		Menus.CommandERR("==== DEPARTMENT REGISTRATION ====");
		Menus.sleep(500);
		Menus.Command("Enter a Department name to register: ");
		String name = scanner.nextLine();
		Department department = new Department(null, name);
		Menus.sleep(500);
		Menus.queries++;
		departmentDAO.insert(department);
		Menus.initDepMenu();
	}
	
	public static void updateDepartmentMenu() {
		Scanner scanner = new Scanner(System.in);
		
		Menus.sleep(500);
		Menus.CommandERR("==== DEPARTMENT UPDATE ====");
		Menus.sleep(500);
		Menus.Command("Enter a Department ID to update: ");
		
		int id = scanner.nextInt();
		scanner.nextLine();
		
		Menus.Command("Enter the new Department name: ");
		String name = scanner.nextLine();
		
		Optional<Department> department = Optional.ofNullable(departmentDAO.findById(id));
		
		if (department.isPresent()) {
			Department dep = new Department(department.get().getId(), name);
			departmentDAO.update(dep);
			
			Menus.queries++;
			Menus.sleep(600);
			Menus.Command("Wanna still updating? [yes] [no]:  ");
			String c = scanner.nextLine();
			
			switch (c) {
				case "yes" -> {
					updateDepartmentMenu();
				}
				case "no" -> {
					Menus.initDepMenu();
				}
				default -> {
					Menus.CommandERR("Option not available.");
				}
			}
			Menus.initDepMenu();
		}
		if (department.isEmpty()) {
			Menus.CommandERR("Department with ID " + id + " not found, try again!");
			updateDepartmentMenu();
		}
	}
	
	public static void deleteDepartmentMenu() {
		Scanner scanner = new Scanner(System.in);
		Menus.Command("Type and ID to delete or type 'all' to see all Departments: ");
		String c = scanner.next();
		
		if (c.equals("all")) {
			findAllDepartments();
			Menus.sleep(450);
			deleteDepartmentMenu();
		} else {
			int id = Integer.parseInt(c);
			departmentDAO.deleteById(id);
			Menus.initDepMenu();
		}
	}
	
	public static void findDepartmentByIdMenu() {
		Scanner sc = new Scanner(System.in);
		
		Menus.Command("Enter an ID to find a Department: ");
		int id = Menus.inputMismatchException(sc);
		if (id == 0) {
			findDepartmentByIdMenu();
		}
		
		Optional<Department> data = Optional.ofNullable(departmentDAO.findById(id));
		
		if (data.isPresent()) {
			Department department = data.get();
			String msg = String.format("-> One result found:  [ { ID: %d  |  Name: %s } ] \n", department.getId(), department.getName());
			Menus.Command(msg);
			Menus.queries++;
			Menus.sleep(150);
			Menus.initDepMenu();
		}
		if (data.isEmpty()) {
			Menus.CommandERR("Check above and try again!");
			Menus.sleep(3000);
			findAllDepartments();
			Menus.sleep(150);
			findDepartmentByIdMenu();
		}
	}
	
	public static void findAllDepartments() {
		List<Department> list = departmentDAO.findAll();
		for (Department department : list) {
			Menus.sleep(50);
			String msg = String.format("[ { ID: %d  |  Name: %s } ]", department.getId(), department.getName());
			Menus.CommandERR(msg);
			Menus.queries++;
		}
	}
	
	public static void findAllSellersWorkingOnDepartmentById() {
		Scanner sc = new Scanner(System.in);
		Menus.Command("Enter an ID to find all Sellers on that Department: ");
		int id = Menus.inputMismatchException(sc);
		
		List<Seller> data = departmentDAO.findAllSellersOnDepartmentById(id);
		
		if (data.isEmpty()) {
			Menus.CommandERR("Result on sellers with ID " + id + " has return 0.");
		}
		if (!data.isEmpty()) {
			for (Seller seller : data) {
				Menus.sleep(450);
				String sell = String.format("Name: %s\nDepartment: %s \nBase Salary: %.2f \nE-Mail: %s\n", seller.getName(), seller.getDepartment().getName(), seller.getBaseSalary(), seller.getEmail());
				Menus.CommandF(sell + "\n");
			}
		} else {
			throw new DBException("Unexpected error, please, open an Issue Request on Github");
		}
	}
	
}
