package application;

import model.dao.DAO_Factory;
import model.dao.DepartmentDAO;
import model.entities.Department;

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
		
		if (department.isPresent()){
			Department dep = new Department(department.get().getId(), name);
			departmentDAO.update(dep);
			Menus.queries++;
			Menus.sleep(600);
			Menus.Command("Wanna still updating? [yes] [no]:  ");
			String c = scanner.nextLine();
			switch (c){
				case "yes" ->{
					updateDepartmentMenu();
				}
				case "no" ->{
					return;
				}
				default -> {
					Menus.CommandERR("Option not available.");
				}
			}
		} if (department.isEmpty()){
			Menus.CommandERR("Department with ID " + id + " not found, try again!");
			updateDepartmentMenu();
		}
	}
	
	public static void deleteDepartmentMenu() {
	
	}
	
	public static void findDepartmentByIdMenu() {
	}
	
	public static void findAllDepartments() {
	}
	
	public static void findAllSellersWorkingOnDepartmentById() {
	}
	
}
