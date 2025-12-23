# Angkor Rice Shop

A modern, feature-rich Android e-commerce application for brow.
sing and purchasing premium Cambodian rice products.

## 📱 Overview

Angkor Rice Shop is an Android app that showcases 6 different types of premium rice with an intuitive shopping experience. The app features a golden-themed UI, dynamic product showcase, and a fully functional cart system.

## ✨ Features

### Product Management
- **6 Rice Varieties**: Jasmine, White, Brown, Sticky, Red, and Fragrant rice
- **Product Details**: Full descriptions, origins, quality information, and pricing
- **Multiple Sizes**: Each product available in 10kg, 25kg, and 50kg options
- **Dynamic Pricing**: Prices automatically adjust based on selected size

### User Interface
- **Golden Theme**: Consistent gold (#C9A961) and cream (#F5E6D3) color scheme
- **Product Showcase**: Horizontally scrollable showcase with all 6 rice types
- **Rounded Cards**: Modern card design with rounded corners (20dp radius)
- **Responsive Layout**: Optimized for various screen sizes

### Shopping Cart
- **Dynamic Cart System**: Add different rice types to cart
- **Cart Management**: 
  - View up to 6 items simultaneously
  - Adjust quantities with +/- buttons
  - Remove items from cart
  - Real-time price calculations
- **Persistent Data**: CartManager singleton pattern for app-wide access

### Product Details
- **Detailed View**: Click any product to see full details
- **Size Selection**: Choose preferred package size
- **Quantity Control**: Adjust quantity before adding to cart
- **Quick Add**: Add items directly to cart with one tap

### Navigation
- **Bottom Navigation**: Easy access to Home, Cart, and Profile (Profile placeholder)
- **Product Showcase Navigation**: Click showcase items to view full details
- **Back Navigation**: Seamless navigation between screens

## 🎨 Design Features

- **Consistent Styling**: All buttons use same dark gray background (#3E3E3E) with 20dp rounded corners
- **Proper Spacing**: 24dp margins between product images and text in cart
- **Status Bar Integration**: Proper system bar spacing with fitsSystemWindows
- **Price Formatting**: All prices display with 2 decimal places (e.g., $25.00)

## 🏗️ Architecture

### Activities
- **MainActivity**: Main product listing and showcase
- **CartActivity**: Shopping cart management
- **ProductDetailActivity**: Detailed product view

### Classes
- **CartManager**: Singleton pattern for global cart management
- **CartItem**: Data model for cart items
- **MainActivity**: Product data and UI management
- **ProductDetailActivity**: Product detail handling

### Drawables
- `quantity_button_background.xml`: Dark gray buttons (20dp radius)
- `rounded_cart_item_background.xml`: Cream cards (15dp radius)
- `rounded_product_detail_background.xml`: Detail screen cards (20dp radius)
- `quantity_display_background.xml`: Quantity display background

## 📦 Product Details

| Product | Base Price | Origin | Quality |
|---------|-----------|--------|---------|
| Jasmine Rice | $25.00 | Battambang Province | Premium |
| White Rice | $22.00 | Battambang Province | Standard |
| Brown Rice | $28.00 | Siem Reap Province | Organic |
| Sticky Rice | $20.00 | Pursat Province | Premium |
| Red Rice | $24.00 | Battambang Province | Organic |
| Fragrant Rice | $26.00 | Kampong Thom Province | Premium |

## 🛠️ Technologies Used

- **Language**: Java
- **Build System**: Gradle
- **Min SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 13 (API 33)
- **Architecture Pattern**: Activity-based with Singleton pattern

## 📲 Installation

### Prerequisites
- Android Studio (Arctic Fox or newer)
- Android SDK 21+
- Java 8 or higher

### Build & Run
```bash
# Clone the repository
git clone https://github.com/TeVaDaGame/Angkor-Rice-Shop.git
cd Angkor-Rice-Shop

# Build the project
./gradlew build

# Install on connected device
./gradlew installDebug

# Or open in Android Studio and run directly
```

## 🎯 How to Use

1. **Browse Products**: Scroll through the product showcase at the top
2. **View Details**: Tap any product to see full details
3. **Select Size**: Choose between 10kg, 25kg, or 50kg
4. **Adjust Quantity**: Use +/- buttons to set quantity
5. **Add to Cart**: Tap "Add to Cart" button
6. **View Cart**: Navigate to cart screen to review items
7. **Manage Cart**: Adjust quantities or remove items as needed

## 🎨 Color Scheme

- **Primary Gold**: #C9A961 (Header, backgrounds)
- **Cream**: #F5E6D3 (Cards, content areas)
- **Dark Gray**: #3E3E3E (Buttons)
- **Text**: #000000 (Black)
- **Secondary Text**: #555555 (Gray)

## 📋 Project Structure

```
AngkorRiceApp/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/angkorriceapp/
│           │   ├── MainActivity.java
│           │   ├── CartActivity.java
│           │   ├── ProductDetailActivity.java
│           │   ├── CartManager.java
│           │   └── CartItem.java
│           └── res/
│               ├── layout/
│               │   ├── activity_main.xml
│               │   ├── activity_cart.xml
│               │   ├── activity_product_detail.xml
│               │   ├── layout_products_list.xml
│               │   └── layout_product_showcase.xml
│               ├── drawable/
│               │   ├── quantity_button_background.xml
│               │   ├── rounded_cart_item_background.xml
│               │   └── rounded_product_detail_background.xml
│               └── values/
│                   └── colors.xml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🚀 Future Enhancements

- [ ] User authentication and profiles
- [ ] Order history and tracking
- [ ] Payment gateway integration
- [ ] Product reviews and ratings
- [ ] Search and filter functionality
- [ ] Wishlist feature
- [ ] Push notifications
- [ ] Inventory management

## 📝 License

This project is open source and available under the MIT License.

## 👤 Author

**TeVaDaGame**

- GitHub: [@TeVaDaGame](https://github.com/TeVaDaGame)
- Repository: [Angkor-Rice-Shop](https://github.com/TeVaDaGame/Angkor-Rice-Shop)

## 📞 Support

For issues, feature requests, or questions, please open an issue on the GitHub repository.

---

**Last Updated**: December 18, 2025

Made with ❤️ for premium Cambodian rice lovers