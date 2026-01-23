# Angkor Rice Shop

A modern, feature-rich Android e-commerce application for browsing and purchasing premium Cambodian rice products with admin management capabilities.

## 📱 Overview

Angkor Rice Shop is a fully-functional Android e-commerce app that showcases premium rice products with user authentication, shopping cart functionality, delivery location selection via Google Maps, and an admin dashboard for product management. The app features a golden-themed UI, intuitive navigation, and Firebase backend integration.

## ✨ Features

### User Authentication
- **Firebase Authentication**: Secure user registration and login
- **Email/Password Login**: Standard authentication flow
- **User Profiles**: Create and manage user accounts
- **Session Persistence**: Automatic login for returning users

### Product Management (Admin)
- **6 Rice Varieties**: Jasmine, White, Brown, Sticky, Red, and Fragrant rice
- **Admin Dashboard**: View all products with full details
- **Add Products**: Create new rice products with name, price, weight, and description
- **Edit Products**: Modify existing product details (name, price, origin)
- **Delete Products**: Remove products with confirmation dialog
- **Product Display**: Cards showing name, origin, price in Firestore
- **Auto-Initialization**: Default 6 products load automatically if database is empty

### Shopping Experience
- **Browse Products**: View all available rice products
- **Product Details**: Full descriptions, origins, quality information, and pricing
- **Shopping Cart**: 
  - Add items with quantity control
  - Real-time cart updates
  - Remove items from cart
  - Persistent cart data via SharedPreferences
- **Checkout Screen**: 
  - Review order summary
  - Enter customer name, phone, and address
  - Calculate shipping costs
  - Display subtotal, shipping, and total

### Location Services
- **Google Maps Integration**: Interactive map for delivery location selection
- **GPS Location Detection**: 
  - Dual-provider support (GPS + Network)
  - Automatic centering on user's actual location
  - Last known location fallback
- **Map Interactions**:
  - Click to select location
  - Drag marker for precision adjustment
  - Search by address functionality
  - "My Location" button to center on current GPS
- **Reverse Geocoding**: Convert coordinates to detailed addresses

### User Profile Management
- **View Profile**: Display user information
- **Edit Profile**: 
  - Enable edit mode with one tap
  - Modify user name with real-time feedback
  - Save/Cancel functionality
  - Hide order history during editing
  - Firestore persistence
- **Order History**: View past orders and details
- **Logout**: Secure sign-out functionality

### User Interface
- **Golden Theme**: Consistent gold (#C9A961) and cream (#F5E6D3) color scheme
- **Modern Design**: CardView-based layouts with elevation and shadows
- **Responsive Layout**: Optimized for various screen sizes
- **Status Bar Integration**: Proper system bar spacing (40dp padding)
- **Themed Buttons**: Gold text on white buttons matching app theme
- **Professional Cards**: Material Design CardView components throughout

## 🎨 Design Features

- **Consistent Color Scheme**: 
  - Primary Gold: #C9A961 (headers, accents)
  - Cream Background: #F5E6D3 (cards, panels)
  - White Buttons: Clean, modern appearance
- **Rounded Cards**: Modern card design with 8dp corner radius
- **Proper Spacing**: Consistent margins and padding throughout
- **Status Bar Integration**: 40dp padding for status bar alignment
- **Elevation & Shadows**: Material Design depth

## 🏗️ Architecture

### Activities
- **WelcomeScreenActivity**: User authentication (Register/Login)
- **MainActivity**: Product listing and shopping
- **ProductDetailActivity**: Detailed product view with add-to-cart
- **CartActivity**: Shopping cart management
- **CheckoutActivity**: Order checkout and customer information
- **MapPickerActivity**: Location selection with Google Maps
- **ProfileActivity**: User profile, editing, order history
- **OrderHistoryActivity**: View past orders
- **AdminProductListActivity**: Admin product management dashboard
- **AdminAddProductActivity**: Add new products form
- **AdminEditProductActivity**: Edit product details form

### Data Management
- **Firebase Authentication**: User login and registration
- **Firebase Firestore**: 
  - User profiles (name, email)
  - Product catalog (name, price, weight, origin, description)
  - Order history
- **SharedPreferences**: Local cart data storage
- **LocationManager**: GPS and Network location providers

### Key Classes
- **CartManager**: Singleton pattern for app-wide cart access
- **CartItem**: Data model for cart items with product info and quantity
- **AdminProductAdapter**: RecyclerView adapter for product management
- **LocationListener**: Implements location updates from LocationManager

### Drawables
- `quantity_button_background.xml`: White buttons with rounded corners
- `rounded_input_field.xml`: Input field backgrounds
- `search_bar_background.xml`: Search field styling

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
- **Backend**: Firebase (Authentication, Firestore)
- **Maps**: Google Maps API
- **Location Services**: LocationManager with GPS and Network providers
- **Architecture**: Activity-based with Singleton and MVC patterns

## 📲 Installation & Setup

### Prerequisites
- Android Studio (Arctic Fox or newer)
- Android SDK 21+
- Java 8 or higher
- Firebase project setup
- Google Maps API key

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

### Firebase Setup
1. Create Firebase project at https://console.firebase.google.com/
2. Add Android app to project
3. Download `google-services.json` and place in `app/` directory
4. Enable Authentication (Email/Password)
5. Create Firestore database
6. Set up security rules for data access

### Google Maps Setup
1. Get API key from Google Cloud Console
2. Add to `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY_HERE" />
   ```

## 🎯 How to Use

### As a Customer
1. **Register/Login**: Create account or sign in with email
2. **Browse Products**: Scroll through available rice products
3. **View Details**: Tap product card to see full details
4. **Add to Cart**: Select quantity and add to shopping cart
5. **Manage Cart**: Adjust quantities or remove items
6. **Checkout**: Proceed to order summary and delivery information
7. **Select Location**: Use map picker to choose delivery address
8. **Place Order**: Confirm and complete the order
9. **View Profile**: Check personal info and order history
10. **Edit Profile**: Update user name with Firestore persistence

### As an Admin
1. **Login**: Use admin account
2. **Access Dashboard**: Navigate to admin product management
3. **View Products**: See all products with details
4. **Add Product**: Create new rice product
5. **Edit Product**: Modify product information
6. **Delete Product**: Remove products with confirmation
7. **View Details**: Product origin and pricing information

## 🎨 Color Scheme

- **Primary Gold**: #C9A961 (Headers, accents, text)
- **Cream Background**: #F5E6D3 (Cards, panels)
- **White**: #FFFFFF (Button backgrounds)
- **Black**: #000000 (Primary text)
- **Gray**: #666666 (Secondary text, hints)

## 📋 Project Structure

```
AngkorRiceApp/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/angkorriceapp/
│           │   ├── WelcomeScreenActivity.java
│           │   ├── MainActivity.java
│           │   ├── CartActivity.java
│           │   ├── ProductDetailActivity.java
│           │   ├── CheckoutActivity.java
│           │   ├── MapPickerActivity.java
│           │   ├── ProfileActivity.java
│           │   ├── OrderHistoryActivity.java
│           │   ├── AdminProductListActivity.java
│           │   ├── AdminAddProductActivity.java
│           │   ├── AdminEditProductActivity.java
│           │   ├── AdminProductAdapter.java
│           │   ├── CartManager.java
│           │   └── CartItem.java
│           └── res/
│               ├── layout/
│               │   ├── activity_welcome_screen.xml
│               │   ├── activity_main.xml
│               │   ├── activity_cart.xml
│               │   ├── activity_product_detail.xml
│               │   ├── activity_checkout.xml
│               │   ├── activity_map_picker.xml
│               │   ├── activity_profile.xml
│               │   ├── activity_order_history.xml
│               │   ├── activity_admin_product_list.xml
│               │   ├── activity_admin_add_product.xml
│               │   ├── activity_admin_edit_product.xml
│               │   └── item_admin_product.xml
│               ├── drawable/
│               │   ├── quantity_button_background.xml
│               │   ├── rounded_input_field.xml
│               │   ├── search_bar_background.xml
│               │   └── ic_person.xml
│               └── values/
│                   └── colors.xml
├── build.gradle.kts
├── settings.gradle.kts
├── google-services.json
└── README.md
```

## ✅ Implemented Features

- ✅ User Authentication (Firebase)
- ✅ Product Browsing and Details
- ✅ Shopping Cart with Persistence
- ✅ Checkout Process
- ✅ Google Maps Location Picker
- ✅ GPS-based Location Detection (Dual Provider)
- ✅ User Profiles with Edit Functionality
- ✅ Order History Tracking
- ✅ Admin Product Management (CRUD Operations)
- ✅ Firestore Database Integration
- ✅ Professional UI with Consistent Theming
- ✅ Real-time Data Updates

## 🚀 Future Enhancements

- [ ] Payment Gateway Integration (Stripe/PayPal)
- [ ] Order Status Tracking with Real-time Updates
- [ ] Product Search and Filter Functionality
- [ ] User Reviews and Ratings System
- [ ] Admin Analytics Dashboard
- [ ] Favorites/Wishlist Feature
- [ ] Push Notifications
- [ ] Inventory Management System
- [ ] Multi-language Support
- [ ] Advanced Admin Analytics

## 🐛 Known Issues & Fixes

### Recent Fixes
- Fixed map location picker not centering on actual current GPS location
- Implemented dual provider location detection (GPS + Network)
- Added proper button styling with gold theme throughout
- Implemented profile edit feature with Firestore persistence

## 📝 License

This project is open source and available under the MIT License.

## 👤 Author

**TeVaDaGame**

- GitHub: [@TeVaDaGame](https://github.com/TeVaDaGame)
- Repository: [Angkor-Rice-Shop](https://github.com/TeVaDaGame/Angkor-Rice-Shop)

## 📞 Support

For issues, feature requests, or questions, please open an issue on the GitHub repository.

---

**Last Updated**: January 23, 2026

Made with ❤️ for premium Cambodian rice lovers