import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Product class to represent items in the inventory
class Product implements Serializable {
    private String id;
    private String name;
    private int quantity;
    private double price;

    public Product(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return id + ": " + name + " - Quantity: " + quantity + ", Price: $" + price;
    }
}

// Inventory class to manage a collection of products
class Inventory implements Serializable {
    private Map<String, Product> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void removeProduct(String id) {
        products.remove(id);
    }

    public void updateProduct(String id, int newQuantity, double newPrice) {
        Product product = products.get(id);
        if (product != null) {
            product.setQuantity(newQuantity);
            product.setPrice(newPrice);
        }
    }

    public Product searchProduct(String idOrName) {
        for (Product product : products.values()) {
            if (product.getId().equals(idOrName) || product.getName().equalsIgnoreCase(idOrName)) {
                return product;
            }
        }
        return null;
    }

    public void viewInventory() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Product product : products.values()) {
                System.out.println(product);
            }
        }
    }
}

// FileManager class to handle saving and loading the inventory to/from a file
class FileManager {
    private static final String FILE_NAME = "inventory.ser";

    public static void saveInventory(Inventory inventory) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(inventory);
            System.out.println("Inventory saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static Inventory loadInventory() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Inventory) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
            return new Inventory();  // Return an empty inventory if file not found or corrupted
        }
    }
}

// Main class to interact with the user
public class Main {
    private static Inventory inventory = FileManager.loadInventory();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Update Product");
            System.out.println("4. View Inventory");
            System.out.println("5. Search Product");
            System.out.println("6. Save and Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    removeProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    inventory.viewInventory();
                    break;
                case 5:
                    searchProduct();
                    break;
                case 6:
                    FileManager.saveInventory(inventory);
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct() {
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        Product product = new Product(id, name, quantity, price);
        inventory.addProduct(product);
        System.out.println("Product added successfully.");
    }

    private static void removeProduct() {
        System.out.print("Enter Product ID to remove: ");
        String id = scanner.nextLine();
        inventory.removeProduct(id);
        System.out.println("Product removed successfully.");
    }

    private static void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        inventory.updateProduct(id, quantity, price);
        System.out.println("Product updated successfully.");
    }

    private static void searchProduct() {
        System.out.print("Enter Product ID or Name to search: ");
        String idOrName = scanner.nextLine();
        Product product = inventory.searchProduct(idOrName);
        if (product != null) {
            System.out.println("Product found: " + product);
        } else {
            System.out.println("Product not found.");
        }
    }
}
