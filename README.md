# Rice Ordering App

A simple console-based application for ordering different types of rice.

## Features

- Browse various types of rice with prices and descriptions
- Add rice to shopping cart with custom quantities
- View and manage cart items
- Remove items from cart
- Checkout and confirm orders
- Input validation and error handling

## Available Rice Types

1. **Jasmine Rice** - $3.50/kg - Aromatic long-grain rice from Thailand
2. **Basmati Rice** - $4.00/kg - Premium long-grain rice from India
3. **Brown Rice** - $3.00/kg - Whole grain rice, nutritious and healthy
4. **Sushi Rice** - $4.50/kg - Short-grain Japanese rice, perfect for sushi
5. **Wild Rice** - $6.00/kg - Nutty flavored, high in protein
6. **Arborio Rice** - $5.00/kg - Italian short-grain rice for risotto

## Requirements

- Python 3.6 or higher

## Usage

### Running the Application

```bash
python3 rice_ordering_app.py
```

or

```bash
./rice_ordering_app.py
```

### Menu Options

- **[A] Add rice to cart** - Select a rice type and specify quantity in kg
- **[V] View cart** - Display current items in your shopping cart
- **[R] Remove item from cart** - Remove a specific item from your cart
- **[C] Checkout** - Complete your order and see the total
- **[Q] Quit** - Exit the application

### Example Usage

```
Welcome to Rice Ordering Application

Available Rice Types:
------------------------------------------------------------
1. Jasmine Rice - $3.50/kg - Aromatic long-grain rice from Thailand
2. Basmati Rice - $4.00/kg - Premium long-grain rice from India
...

Options:
  [A] Add rice to cart
  [V] View cart
  [R] Remove item from cart
  [C] Checkout
  [Q] Quit

Enter your choice: A
Enter rice number (1-6): 1
Enter quantity in kg: 2

Added 2kg of Jasmine Rice to your cart!
```

## Testing

Run the unit tests with:

```bash
python3 test_rice_ordering_app.py
```

For verbose output:

```bash
python3 test_rice_ordering_app.py -v
```

## Project Structure

```
Rice_Ordering_App/
├── README.md                    # This file
├── rice_ordering_app.py        # Main application
└── test_rice_ordering_app.py   # Unit tests
```

## Classes

### Rice
Represents a type of rice with name, price, and description.

### OrderItem
Represents an item in the order with rice type and quantity.

### Order
Manages the customer's shopping cart and calculates totals.

### RiceOrderingApp
Main application class that handles the user interface and menu system.

## License

This project is provided as-is for educational purposes.