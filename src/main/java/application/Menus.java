package application;

import java.util.*;

public class Menus {
	
	public static int queries = 0;
	
	public static void mainMenu() {
		Scanner sc = new Scanner(System.in);
		
		if (queries == 0) {
			introdution();
			queries++;
		}
		
		Map<Integer, String> firstMenu = new HashMap<>();
		firstMenu.put(1, "Department");
		firstMenu.put(2, "Sellers");
		firstMenu.put(3, "Queries Requested");
		firstMenu.put(4, " END APPLICATION");
		
		List<String> option = new ArrayList<>();
		
		for (Map.Entry<Integer, String> entry : firstMenu.entrySet()) {
			int key = entry.getKey();
			String value = entry.getValue();
			option.add("  [" + key + "] " + value);
		}
		
		StringBuilder menu = new StringBuilder("-> CHOOSE AN OPTION:");
		for (String s : option) {
			menu.append(s);
		}
		
		sleep(600);
		
		CommandERR(menu.toString());
		
		int c = inputMismatchException(sc);
		if (c == 0) {
			mainMenu();
		}
		
		String choice = firstMenu.get(c);
		try {
			switch (choice) {
				case "Department" -> {
					initDepMenu();
				}
				case "Sellers" -> {
					initSellerMenu();
				}
				case "Queries Requested" -> {
					int querie = queries - 1;
					CommandERR("Queries: " + querie);
					mainMenu();
				}
				case " END APPLICATION" -> {
					System.exit(0);
				}
				default -> {
					sleep(500);
					CommandERR("No option available!");
				}
			}
		} catch (NullPointerException e) {
			mainMenu();
		}
	}
	
	private static void initSellerMenu() {
	}
	
	public static void initDepMenu() {
		Scanner scanner = new Scanner(System.in);
		sleep(500);
		CommandERR("============MENU============\n[1] - Register Department\n[2] - Update Department\n[3] - Delete Department\n[4] - Find Department By ID\n" +
				"[5] - Find All Departments\n[6] - Find All Sellers on Department By ID\n[7] - MENU");
		int c = inputMismatchException(scanner);
		if (c == 0) {
			initDepMenu();
		}
		switch (c) {
			case 1 -> DepartmentCommand.registerDepartmentMenu();
			case 2 -> DepartmentCommand.updateDepartmentMenu();
			case 3 -> DepartmentCommand.deleteDepartmentMenu();
			case 4 -> DepartmentCommand.findDepartmentByIdMenu();
			case 5 -> {
				DepartmentCommand.findAllDepartments();
				Menus.initDepMenu();
			}
			case 6 -> DepartmentCommand.findAllSellersWorkingOnDepartmentById();
			case 7 -> mainMenu();
			default -> CommandERR("No option available.");
		}
		initDepMenu();
	}
	
	public static void introdution() {
		String[] introductory = {"Hello! Welcome to JDBC in Java.",
				"We have a few implementations with PostgresSQL",
				"Soon, a full documentation for guiding new people is coming.",
				"For now, the documentation is on the application itself! :D",
				"So enjoy, and any help you can find me on twitter @m4rinhu or github /mar1nho"};
		for (String s : introductory) {
			sleep(450);
			CommandNL(s);
		}
	}
	
	public static void CommandNL(String txt) {
		System.out.println(txt);
		System.out.flush();
	}
	
	public static void Command(String txt) {
		System.out.print(txt);
		System.out.flush();
	}
	
	public static void CommandF(String txt) {
		System.out.printf(txt);
		System.out.flush();
	}
	
	public static void CommandERR(String txt) {
		System.err.println(txt);
		System.out.flush();
	}
	
	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static int inputMismatchException(Scanner scanner) {
		int number;
		try {
			number = scanner.nextInt();
			return number;
		} catch (InputMismatchException e) {
			CommandERR("Please, enter an INTEGER on the console.");
		}
		return 0;
	}
}
