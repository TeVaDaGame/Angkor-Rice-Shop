#!/usr/bin/env python3
"""
Rice Ordering Application
A simple console application for ordering different types of rice.
"""

class Rice:
    """Represents a type of rice with name, price, and description."""
    
    def __init__(self, name, price, description):
        self.name = name
        self.price = price
        self.description = description
    
    def __str__(self):
        return f"{self.name} - ${self.price:.2f}/kg - {self.description}"


class OrderItem:
    """Represents an item in the order with rice type and quantity."""
    
    def __init__(self, rice, quantity):
        self.rice = rice
        self.quantity = quantity
    
    def get_total(self):
        return self.rice.price * self.quantity
    
    def __str__(self):
        return f"{self.rice.name} x {self.quantity}kg = ${self.get_total():.2f}"


class Order:
    """Manages the customer's order."""
    
    def __init__(self):
        self.items = []
    
    def add_item(self, rice, quantity):
        """Add an item to the order."""
        if quantity <= 0:
            raise ValueError("Quantity must be greater than 0")
        self.items.append(OrderItem(rice, quantity))
    
    def remove_item(self, index):
        """Remove an item from the order by index."""
        if 0 <= index < len(self.items):
            self.items.pop(index)
            return True
        return False
    
    def get_total(self):
        """Calculate the total price of the order."""
        return sum(item.get_total() for item in self.items)
    
    def is_empty(self):
        """Check if the order is empty."""
        return len(self.items) == 0
    
    def clear(self):
        """Clear all items from the order."""
        self.items = []
    
    def __str__(self):
        if self.is_empty():
            return "Your cart is empty."
        
        result = "Your Order:\n"
        result += "-" * 50 + "\n"
        for i, item in enumerate(self.items, 1):
            result += f"{i}. {item}\n"
        result += "-" * 50 + "\n"
        result += f"Total: ${self.get_total():.2f}"
        return result


class RiceOrderingApp:
    """Main application class for the Rice Ordering System."""
    
    def __init__(self):
        self.rice_types = self._initialize_rice_types()
        self.current_order = Order()
    
    def _initialize_rice_types(self):
        """Initialize the available rice types."""
        return [
            Rice("Jasmine Rice", 3.50, "Aromatic long-grain rice from Thailand"),
            Rice("Basmati Rice", 4.00, "Premium long-grain rice from India"),
            Rice("Brown Rice", 3.00, "Whole grain rice, nutritious and healthy"),
            Rice("Sushi Rice", 4.50, "Short-grain Japanese rice, perfect for sushi"),
            Rice("Wild Rice", 6.00, "Nutty flavored, high in protein"),
            Rice("Arborio Rice", 5.00, "Italian short-grain rice for risotto"),
        ]
    
    def display_menu(self):
        """Display the available rice types."""
        print("\n" + "=" * 60)
        print("Welcome to Rice Ordering Application".center(60))
        print("=" * 60)
        print("\nAvailable Rice Types:")
        print("-" * 60)
        for i, rice in enumerate(self.rice_types, 1):
            print(f"{i}. {rice}")
        print("-" * 60)
    
    def display_options(self):
        """Display the main menu options."""
        print("\nOptions:")
        print("  [A] Add rice to cart")
        print("  [V] View cart")
        print("  [R] Remove item from cart")
        print("  [C] Checkout")
        print("  [Q] Quit")
        print()
    
    def add_to_cart(self):
        """Add a rice type to the cart."""
        try:
            choice = input("Enter rice number (1-{}): ".format(len(self.rice_types)))
            rice_index = int(choice) - 1
            
            if rice_index < 0 or rice_index >= len(self.rice_types):
                print("Invalid rice selection!")
                return
            
            quantity = input("Enter quantity in kg: ")
            quantity = float(quantity)
            
            if quantity <= 0:
                print("Quantity must be greater than 0!")
                return
            
            selected_rice = self.rice_types[rice_index]
            self.current_order.add_item(selected_rice, quantity)
            print(f"\nAdded {quantity}kg of {selected_rice.name} to your cart!")
            
        except ValueError:
            print("Invalid input! Please enter valid numbers.")
        except Exception as e:
            print(f"Error: {e}")
    
    def view_cart(self):
        """Display the current cart."""
        print("\n" + "=" * 60)
        print(self.current_order)
        print("=" * 60)
    
    def remove_from_cart(self):
        """Remove an item from the cart."""
        if self.current_order.is_empty():
            print("\nYour cart is empty!")
            return
        
        self.view_cart()
        try:
            choice = input("\nEnter item number to remove (or 0 to cancel): ")
            item_index = int(choice) - 1
            
            if item_index == -1:
                return
            
            if self.current_order.remove_item(item_index):
                print("Item removed successfully!")
            else:
                print("Invalid item number!")
                
        except ValueError:
            print("Invalid input! Please enter a valid number.")
    
    def checkout(self):
        """Process the checkout."""
        if self.current_order.is_empty():
            print("\nYour cart is empty! Add some rice first.")
            return False
        
        print("\n" + "=" * 60)
        print("CHECKOUT".center(60))
        print("=" * 60)
        print(self.current_order)
        print("=" * 60)
        
        confirm = input("\nConfirm order? (Y/N): ").strip().upper()
        
        if confirm == 'Y':
            print("\n" + "=" * 60)
            print("Order confirmed! Thank you for your purchase!".center(60))
            print(f"Total amount: ${self.current_order.get_total():.2f}".center(60))
            print("=" * 60)
            self.current_order.clear()
            return True
        else:
            print("Order cancelled.")
            return False
    
    def run(self):
        """Run the main application loop."""
        print("\n" + "=" * 60)
        print("RICE ORDERING APPLICATION".center(60))
        print("=" * 60)
        
        while True:
            self.display_menu()
            self.display_options()
            
            choice = input("Enter your choice: ").strip().upper()
            
            if choice == 'A':
                self.add_to_cart()
            elif choice == 'V':
                self.view_cart()
            elif choice == 'R':
                self.remove_from_cart()
            elif choice == 'C':
                if self.checkout():
                    continue_shopping = input("\nWould you like to continue shopping? (Y/N): ").strip().upper()
                    if continue_shopping != 'Y':
                        break
            elif choice == 'Q':
                print("\nThank you for using Rice Ordering Application!")
                break
            else:
                print("\nInvalid option! Please try again.")
        
        print("\nGoodbye!\n")


def main():
    """Main entry point for the application."""
    app = RiceOrderingApp()
    app.run()


if __name__ == "__main__":
    main()
