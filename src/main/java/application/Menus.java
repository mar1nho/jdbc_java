package application;

import javax.sound.sampled.LineListener;
import java.io.Console;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Menus {
	
	public static int queries = 0;
	
	public static void mainMenu(){
		if (queries == 0){
			introdution();
			queries++;
		}
		
		Scanner sc = new Scanner(System.in);
		
		Map<Integer, String> firstMenu = new HashMap<>();
		firstMenu.put(1, "Department");
		firstMenu.put(2, "Sellers");
		firstMenu.put(3, "Queries Requested");
		
		sleep(600);
		
		CommandERR("-> CHOOSE AN OPTION: [1] Department  [2] Seller  [3] Queries Requested");
		int c = sc.nextInt();
		String choice = firstMenu.get(c);
			try {
				switch (choice){
					case "Department" -> {
						initDepMenu();
						break;
					}
					case "Sellers" -> {
						initSellerMenu();
						break;
					}
					case "Queries Requested" -> {
						int querie = queries - 1;
						CommandERR("Queries: " + querie);
						mainMenu();
						break;
					}
					default -> {
						sleep(500);
						CommandERR("No option available!");
					}
				}
			} catch (NullPointerException e) {
				sleep(500);
				CommandERR("No choice found with this numerate, try again with one that actually works :D!");
				return;
			}
		
		
	}
	
	private static void initSellerMenu() {
	}
	
	private static void initDepMenu() {
		sleep(500);
		CommandERR("============MENU============\n[1] - Register Department\n[2] - Update Department\n[3] - Delete Department\n[4] - Find Department By ID\n" +
					   "[5] - Find All Departments\n[6] - Find All Sellers on Department By ID\n[7] - MENU");
		int c = 0;
		try{
			Scanner sc = new Scanner(System.in);
			c = sc.nextInt();
		} catch (InputMismatchException e) {
			sleep(500);
			CommandERR("String is not suitable for this application menu sections.");
			return;
		}
		
		switch (c) {
			case 1 -> {
				DepartmentCommand.registerDepartmentMenu();
				initDepMenu();
			}
			case 2 -> {
				DepartmentCommand.updateDepartmentMenu();
				initDepMenu();
			}
			case 3 -> {
				DepartmentCommand.deleteDepartmentMenu();
				initDepMenu();
			}
			case 4 -> {
				DepartmentCommand.findDepartmentByIdMenu();
				initDepMenu();
			}
			case 5 -> {
				DepartmentCommand.findAllDepartments();
				initDepMenu();
			}
			case 6 -> {
				DepartmentCommand.findAllSellersWorkingOnDepartmentById();
				initDepMenu();
			}
			case 7 -> {
				mainMenu();
			}
			default -> {
				CommandERR("No option available.");
				initDepMenu();
			}
		}
	}
	
	public static void introdution(){
		try {
			Thread.sleep(450);
			CommandNL("Hello! Welcome to JDBC in Java.");
			Thread.sleep(450);
			CommandNL("We have a few implementations with PostgresSQL");
			Thread.sleep(450);
			CommandNL("Soon, a full documentation for guiding new people is coming.");
			Thread.sleep(450);
			CommandNL("For now, the documentation is on the application itself! :D");
			Thread.sleep(450);
			CommandNL("So enjoy, and any help you can find me on twitter @m4rinhu or github /mar1nho");
			Thread.sleep(450);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void CommandNL(String txt){
		System.out.println(txt);
		System.out.flush();
	}
	
	public static void Command(String txt){
		System.out.print(txt);
		System.out.flush();
	}
	
	public static void CommandERR(String txt){
		System.err.println(txt);
		System.out.flush();
	}
	
	public static void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
