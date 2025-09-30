#!/usr/bin/env python3
"""
Unit tests for Rice Ordering Application
"""

import unittest
from rice_ordering_app import Rice, OrderItem, Order, RiceOrderingApp


class TestRice(unittest.TestCase):
    """Test cases for Rice class."""
    
    def test_rice_creation(self):
        rice = Rice("Jasmine Rice", 3.50, "Aromatic rice")
        self.assertEqual(rice.name, "Jasmine Rice")
        self.assertEqual(rice.price, 3.50)
        self.assertEqual(rice.description, "Aromatic rice")
    
    def test_rice_string_representation(self):
        rice = Rice("Jasmine Rice", 3.50, "Aromatic rice")
        expected = "Jasmine Rice - $3.50/kg - Aromatic rice"
        self.assertEqual(str(rice), expected)


class TestOrderItem(unittest.TestCase):
    """Test cases for OrderItem class."""
    
    def setUp(self):
        self.rice = Rice("Jasmine Rice", 3.50, "Aromatic rice")
    
    def test_order_item_creation(self):
        item = OrderItem(self.rice, 2)
        self.assertEqual(item.rice, self.rice)
        self.assertEqual(item.quantity, 2)
    
    def test_order_item_total(self):
        item = OrderItem(self.rice, 2)
        self.assertEqual(item.get_total(), 7.00)
    
    def test_order_item_string_representation(self):
        item = OrderItem(self.rice, 2)
        expected = "Jasmine Rice x 2kg = $7.00"
        self.assertEqual(str(item), expected)


class TestOrder(unittest.TestCase):
    """Test cases for Order class."""
    
    def setUp(self):
        self.order = Order()
        self.rice1 = Rice("Jasmine Rice", 3.50, "Aromatic rice")
        self.rice2 = Rice("Basmati Rice", 4.00, "Premium rice")
    
    def test_empty_order(self):
        self.assertTrue(self.order.is_empty())
    
    def test_add_item(self):
        self.order.add_item(self.rice1, 2)
        self.assertFalse(self.order.is_empty())
        self.assertEqual(len(self.order.items), 1)
    
    def test_add_multiple_items(self):
        self.order.add_item(self.rice1, 2)
        self.order.add_item(self.rice2, 3)
        self.assertEqual(len(self.order.items), 2)
    
    def test_add_invalid_quantity(self):
        with self.assertRaises(ValueError):
            self.order.add_item(self.rice1, 0)
        
        with self.assertRaises(ValueError):
            self.order.add_item(self.rice1, -1)
    
    def test_remove_item(self):
        self.order.add_item(self.rice1, 2)
        self.order.add_item(self.rice2, 3)
        result = self.order.remove_item(0)
        self.assertTrue(result)
        self.assertEqual(len(self.order.items), 1)
    
    def test_remove_invalid_index(self):
        self.order.add_item(self.rice1, 2)
        result = self.order.remove_item(5)
        self.assertFalse(result)
    
    def test_get_total(self):
        self.order.add_item(self.rice1, 2)  # 3.50 * 2 = 7.00
        self.order.add_item(self.rice2, 3)  # 4.00 * 3 = 12.00
        self.assertEqual(self.order.get_total(), 19.00)
    
    def test_clear_order(self):
        self.order.add_item(self.rice1, 2)
        self.order.add_item(self.rice2, 3)
        self.order.clear()
        self.assertTrue(self.order.is_empty())
    
    def test_order_string_empty(self):
        expected = "Your cart is empty."
        self.assertEqual(str(self.order), expected)
    
    def test_order_string_with_items(self):
        self.order.add_item(self.rice1, 2)
        result = str(self.order)
        self.assertIn("Your Order:", result)
        self.assertIn("Jasmine Rice", result)
        self.assertIn("Total:", result)


class TestRiceOrderingApp(unittest.TestCase):
    """Test cases for RiceOrderingApp class."""
    
    def setUp(self):
        self.app = RiceOrderingApp()
    
    def test_app_initialization(self):
        self.assertIsNotNone(self.app.rice_types)
        self.assertGreater(len(self.app.rice_types), 0)
        self.assertIsInstance(self.app.current_order, Order)
    
    def test_rice_types_initialized(self):
        # Check that we have some rice types
        self.assertGreater(len(self.app.rice_types), 0)
        # Check that each is a Rice instance
        for rice in self.app.rice_types:
            self.assertIsInstance(rice, Rice)


if __name__ == "__main__":
    unittest.main()
