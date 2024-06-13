import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

//Product class implementing Cloneable interface which contains all product functionalities
class Product implements Cloneable {
    private String name;
    private double price;
    private boolean available;
    private int quantity;  
	
	//Constructor to initialize product
    public Product(String name, double price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
        this.quantity = 1;  
    }

    public void display() {
        System.out.println("Product: " + name + ", Price: $" + price + ", Availability: " + available);
    }
    //Override clone method for cloning products
    @Override
    public Product clone() throws CloneNotSupportedException {
        return (Product) super.clone();
    }

    public double getPrice() {
        return price;
    }
	
    public String getName() {
        return name;
    }
	
    public int getQuantity() {
        return quantity;
    }
	
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
	
	public void setPrice(double price) {
        this.price = price;
    }
}
	//ConcreteProduct class extending Product class
    class ConcreteProduct extends Product {
		public ConcreteProduct(String name, double price, boolean available) {
			super(name, price, available);
    }
}

	class SpecialProduct extends Product {
		private double specialDiscount;

		public SpecialProduct(String name, double price, boolean available, double specialDiscount) {
			super(name, price, available);
			this.specialDiscount = specialDiscount;
    }

    @Override
    public SpecialProduct clone() throws CloneNotSupportedException {
        return (SpecialProduct) super.clone();
    }
}
	//implementing Discount Strategies
	interface DiscountStrategy {
		double applyDiscount(double price);
}

	class PercentageOffStrategy implements DiscountStrategy {
		private double percentage;

		public PercentageOffStrategy(double percentage) {
			this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double price) {
        return price * (1 - percentage / 100);
    }
}



//Cart class which has all the shopping cart functionalities
class Cart {
    private List<Product> cartItems;

    public Cart() {
        this.cartItems = new ArrayList<>();
    }
	private DiscountStrategy discountStrategy;
	public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

	//add items into the cart
    public void addToCart(Product product) {
	
		boolean productExists = false;
		for (Product cartProduct : cartItems) {
            if (cartProduct.getName().equalsIgnoreCase(product.getName())) {
                // Product already exists in the cart, increment quantity
				double discountedPrice = discountStrategy.applyDiscount(product.getPrice());
                product.setPrice(discountedPrice);

				cartItems.add(product);
				
				System.out.println(product.getQuantity() + " " + product.getName() +"(s) added to the cart with discounted price: $" + discountedPrice);
                cartProduct.setQuantity(cartProduct.getQuantity() + product.getQuantity());
                System.out.println("Quantity updated for " + cartProduct.getName() + " to " + cartProduct.getQuantity());
                productExists = true;
                break;
            }
        }
		if (!productExists) {
            // Product does not exist in the cart, add it
            cartItems.add(product);
            System.out.println(product.getQuantity() + " " + product.getName() + "(s) added to the cart.");
        }
    }
	//update quantity into the cart
    public void updateQuantity(Product product, int quantity) {
        for (Product cartProduct : cartItems) {
            if (cartProduct.getName().equalsIgnoreCase(product.getName())) {
                cartProduct.setQuantity(quantity);
                System.out.println("Quantity updated for " + cartProduct.getName() + " to " + quantity);
                return;
            }
        }
        System.out.println("Product not found in the cart.");
    }
	//removing items from the cart
    public void removeFromCart(Product product) {
        cartItems.remove(product);
    }

	//calculating the bill amount
    public double calculateTotalBill() {
        double totalBill = 0;
        for (Product item : cartItems) {
           
			totalBill += discountStrategy.applyDiscount(item.getPrice()) * item.getQuantity();
        }
        return totalBill;
    }

    public List<Product> getCartItems() {
        return cartItems;
    }
}

//User class which contains the user functionality
class User {
    private String username;
    private String password;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z_]{3,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&!])[A-Za-z\\d@#$%^&!]{8,}$");

    public User(String username, String password) {
        if (!validateUsername(username)) {
            throw new IllegalArgumentException("Invalid username format.");
        }

        if (!validatePassword(password)) {
            throw new IllegalArgumentException("Invalid password format.");
        }

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    protected static boolean validateUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    protected static boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}

//ECartSystem class 
public class ECartSystem {
    private static List<Product> products = new ArrayList<>();
    private static Cart cart = new Cart();
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    static {
        try {
            users.add(new User("username1", "Password1!"));
            users.add(new User("username2", "Password2!"));
        } catch (IllegalArgumentException ignored) {
           // System.err.println("Error initializing users: " + e.getMessage());
        }
    }
	private static int getUserChoice(Scanner scanner) {
       
		int choice;
		while (true) {
			System.out.print("Enter your choice: ");
			if (scanner.hasNextInt()) {
				choice = scanner.nextInt();
				break; 
		} 	else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Clear the invalid input
        }
    }
    return choice;
    }

   
	//login details
    private static void login(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.next();
        System.out.print("Enter your password: ");
        String password = scanner.next();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful.");
                return;
            }
        }

        System.out.println("Invalid username or password. Please try again.");
    }

	//logout details
    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
	
	//create new user details
    private static void createUser(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.next();

        if (!User.validateUsername(username)) {
            System.out.println("Invalid username format. Please use at least 3 characters, only alphabets, and underscores.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.next();

        if (!User.validatePassword(password)) {
            System.out.println("Invalid password format. Please use at least 8 characters with one uppercase letter, one lowercase letter, one digit, and one special character.");
            return;
        }

        users.add(new User(username, password));
        System.out.println("User created successfully. Please log in.");
    }

    private static void initializeProducts() {
        products.add(new ConcreteProduct("Laptop", 1000, true));
        products.add(new ConcreteProduct("Headphones", 50, true));
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Display products");
        System.out.println("2. Add product to cart");
        System.out.println("3. Update cart quantity");
        System.out.println("4. Remove product from cart");
        System.out.println("5. Display cart");
        System.out.println("6. Display total bill");
        System.out.println("7. Log out");
  
    }
	
	//displaying the products
    private static void displayProducts() {
        System.out.println("Products available:");
        for (Product product : products) {
            product.display();
        }
    }
	//Adding the products into the cart
    private static void addProductToCart(Scanner scanner) {
        if (currentUser != null) {
            String productName = getValidProductName(scanner);
            int quantity = getValidQuantity(scanner);

            for (Product product : products) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    try {
                        Product productClone = product.clone();
                        productClone.setQuantity(quantity);
                        cart.addToCart(productClone);
                      
                        return;
                    } catch (CloneNotSupportedException e) {
                        System.out.println("Error: Unable to clone the product.");
                    }
                }
            }

            System.out.println("Product not found.");
        } else {
            System.out.println("Please log in first.");
        }
    }

    private static String getValidProductName(Scanner scanner) {
        String productName;
        while (true) {
            System.out.print("Enter the product name: ");
            productName = scanner.next();

            boolean productExists = false;
            for (Product product : products) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    productExists = true;
                    break;
                }
            }

            if (productExists)
                break;
            else
                System.out.println("Product not found. Please enter a valid product name.");
        }
        return productName;
    }

    private static int getValidQuantity(Scanner scanner) {
        int quantity;
        while (true) {
            System.out.print("Enter the quantity: ");
            try {
                quantity = scanner.nextInt();
                if (quantity > 0)
                    return quantity;
                else
                    System.out.println("Quantity should be a positive integer.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    }
	
	//updating the cart quantity
    private static void updateCartQuantity(Scanner scanner) {
        if (currentUser != null) {
            String productName = getValidProductName(scanner);
            int newQuantity = getValidQuantity(scanner);
            Product productToUpdate = null;

            for (Product product : cart.getCartItems()) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    productToUpdate = product;
                    break;
                }
            }

            if (productToUpdate != null) {
                cart.updateQuantity(productToUpdate, newQuantity);
            } else {
                System.out.println("Product not found in the cart.");
            }
        } else {
            System.out.println("Please log in first.");
        }
    }
	
	//Remove products from the cart
    private static void removeProductFromCart(Scanner scanner) {
        if (currentUser != null) {
            String productToRemove = getValidProductName(scanner);
            Product productFound = null;

            for (Product product : cart.getCartItems()) {
                if (product.getName().equalsIgnoreCase(productToRemove)) {
                    productFound = product;
                    break;
                }
            }

         
		if (productFound != null) {
            
            int quantityToRemove = getValidQuantityToRemove(scanner, productFound);

            if (quantityToRemove > 0) {
                
                if (quantityToRemove < productFound.getQuantity()) {
                    cart.updateQuantity(productFound, productFound.getQuantity() - quantityToRemove);
                    System.out.println(quantityToRemove + " " + productFound.getName() + "(s) removed from the cart.");
                } else {
                    cart.removeFromCart(productFound);
                    System.out.println(productFound.getName() + " removed from the cart.");
                }
            } else {
                System.out.println("Invalid quantity to remove.");
            }
        } else {
            System.out.println("Product not found in the cart.");
        }
    } else {
        System.out.println("Please log in first.");
    }
	
    }
	
	private static int getValidQuantityToRemove(Scanner scanner, Product product) {
    int quantityToRemove;
    while (true) {
        System.out.print("Enter the quantity to remove (max " + product.getQuantity() + "): ");
        try {
            quantityToRemove = scanner.nextInt();
            if (quantityToRemove >= 0 && quantityToRemove <= product.getQuantity())
                return quantityToRemove;
            else
                System.out.println("Invalid quantity. Please enter a valid quantity.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();
        }
    }
}
	//displaying the  items in the cart
   
	private static void displayCart() {
    if (currentUser != null) {
        List<Product> cartItems = cart.getCartItems();
        System.out.println("Cart Items:");

        List<String> displayedProducts = new ArrayList<>();

        for (Product product : cartItems) {
            String productName = product.getName();
            int quantity = product.getQuantity();

            // If the product is already displayed, update its quantity
            if (displayedProducts.contains(productName)) {
                continue;
            }

            int totalQuantity = quantity;

            
            for (Product otherProduct : cartItems) {
                if (!otherProduct.getName().equals(productName)) {
                    continue;
                }

                // Skip the current product
                if (otherProduct == product) {
                    continue;
                }

                totalQuantity += otherProduct.getQuantity();
            }

            // Display the combined quantity for this product
            System.out.println("Product: " + productName + ", Quantity: " + totalQuantity);

            // Add the product to the displayed list to avoid duplication
            displayedProducts.add(productName);
        }

   
    } else {
        System.out.println("Please log in first.");
    }
}

	//display bill amount
    private static void displayTotalBill() {
        if (currentUser != null) {
            double totalBill = cart.calculateTotalBill();
            System.out.println("Total Bill: Your total bill is $" + totalBill + ".");
        } else {
            System.out.println("Please log in first.");
        }
    }
	
	//Main method
	public static void main(String[] args) {
        initializeProducts();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            if (currentUser == null) {
                System.out.println("\n--- Welcome to the Shopping System ---");
                System.out.println("1. Log in");
                System.out.println("2. Create an account");
                System.out.println("3. Exit");
                choice = getUserChoice(scanner);
                 DiscountStrategy discountStrategy = new PercentageOffStrategy(10);
        cart.setDiscountStrategy(discountStrategy);

                switch (choice) {
                    case 1:
                        login(scanner);
                        break;
                    case 2:
                        createUser(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                displayMenu();
                choice = getUserChoice(scanner);

                switch (choice) {
                    case 1:
                        displayProducts();
                        break;
                    case 2:
                        addProductToCart(scanner);
                        break;
                    case 3:
                        updateCartQuantity(scanner);
                        break;
                    case 4:
                        removeProductFromCart(scanner);
                        break;
                    case 5:
                        displayCart();
                        break;
                    case 6:
                        displayTotalBill();
                        break;
                    case 7:
                        logout();
                        break;
	

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } while (true);
    }
}
