# Library Management System

A comprehensive REST API built with Spring Boot for managing a library's operations, including books, members, users, publishers, categories, authors, and borrowing transactions.

## 🚀 Features

### Core Functionality
- **Book Management**: Complete CRUD operations with advanced search and filtering
- **Member Management**: Member registration, updates, and status management
- **User Management**: Role-based access control (Admin, Librarian, Staff)
- **Borrowing System**: Book checkout, return, and fine calculation
- **Authentication & Authorization**: JWT-based security with role-based permissions

### Advanced Features
- **Multi-Author Support**: Books can have multiple authors
- **Hierarchical Categories**: Nested category structure with full path navigation
- **Publisher Management**: Publisher information and book relationships
- **Activity Logging**: Comprehensive user activity tracking
- **File Upload**: Book cover image management
- **Search & Filtering**: Advanced search across multiple fields
- **Pagination**: Efficient data loading for large datasets

## 🏗️ Architecture

### Technology Stack
- **Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **Database**: MySQL with Hibernate ORM
- **Security**: JWT Authentication with BCrypt password encoding
- **Validation**: Bean Validation (JSR-303)
- **Build Tool**: Maven

### Project Structure
```
src/main/java/com/library/management/
├── config/           # Configuration classes (Security, CORS)
├── controller/       # REST Controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA Entities
├── repository/      # Data Access Layer
├── security/        # Security components (JWT, UserDetails)
├── service/         # Business Logic Layer
└── exception/       # Custom Exception Handlers
```

## 🔐 Security & Roles

### Role Hierarchy
- **ADMIN**: Full system access, user management, system configuration
- **LIBRARIAN**: Book and member management, borrowing operations, reports
- **STAFF**: Read-only access to books, basic member operations

### Authentication Flow
1. User logs in with username/password
2. System validates credentials and returns JWT token
3. Token must be included in Authorization header for protected endpoints
4. Role-based access control enforces permissions

## 📊 Database Schema

### Key Entities
- **Users**: System users with role-based access
- **Members**: Library members who can borrow books
- **Books**: Core book information with metadata
- **Authors**: Author information with many-to-many book relationship
- **Categories**: Hierarchical category structure
- **Publishers**: Publisher information
- **BorrowRecords**: Borrowing transactions and history
- **UserActivities**: Audit log for user actions

### Relationships
- Books ↔ Authors (Many-to-Many)
- Books ↔ Categories (Many-to-Many)
- Books → Publisher (Many-to-One)
- Categories → Parent Category (Self-referencing)
- BorrowRecords → Book, Member, User (Many-to-One)

## 🔧 API Endpoints

### Authentication
```
POST /api/auth/login          # User authentication
```

### Book Management
```
GET    /api/books                    # List all books (paginated)
GET    /api/books/{id}               # Get book by ID
POST   /api/books                    # Create new book
PUT    /api/books/{id}               # Update book
DELETE /api/books/{id}               # Delete book (Admin only)
GET    /api/books/search             # Search books
GET    /api/books/available          # List available books
GET    /api/books/category/{id}      # Books by category
GET    /api/books/author/{id}        # Books by author
POST   /api/books/{id}/upload-cover  # Upload book cover
```

### Member Management
```
GET    /api/members           # List all members
GET    /api/members/{id}      # Get member by ID
POST   /api/members           # Create member
PUT    /api/members/{id}      # Update member
DELETE /api/members/{id}      # Delete member
PUT    /api/members/{id}/deactivate  # Deactivate member
```

### User Management (Admin Only)
```
GET    /api/users             # List all users
GET    /api/users/{id}        # Get user by ID
POST   /api/users             # Create user
PUT    /api/users/{id}        # Update user
DELETE /api/users/{id}        # Delete user
```

### Borrowing Operations
```
GET    /api/borrow                    # List borrow records
POST   /api/borrow/book              # Borrow a book
PUT    /api/borrow/{id}/return       # Return a book
GET    /api/borrow/overdue           # Get overdue records
GET    /api/borrow/member/{id}       # Member's borrow history
```

### Categories & Authors
```
GET    /api/categories        # List categories
GET    /api/categories/tree   # Hierarchical category tree
POST   /api/categories        # Create category
PUT    /api/categories/{id}   # Update category
DELETE /api/categories/{id}   # Delete category

GET    /api/authors           # List authors
POST   /api/authors           # Create author
PUT    /api/authors/{id}      # Update author
DELETE /api/authors/{id}      # Delete author
GET    /api/authors/search    # Search authors
```

## ⚙️ Configuration

### Database Setup
1. Create MySQL database: `library_management`
2. Update `application.properties` with your database credentials
3. Application will auto-create tables on startup

### JWT Configuration
- Token expiration: 24 hours (configurable)
- Refresh token: 7 days (configurable)
- Secret key: Configurable via environment variable

### File Upload
- Book covers stored in: `${user.home}/library-management/uploads/book-covers`
- Max file size: 5MB
- Supported formats: Images only

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- MySQL 8.0+
- Maven 3.6+

### Running the Application
1. Clone the repository
2. Configure database connection in `application.properties`
3. Run: `mvn spring-boot: run`
4. Application starts on port 8081

### Default Admin User
Create an admin user through direct database insertion or API:
```json
{
  "username": "admin",
  "password": "admin123",
  "email": "admin@library.com",
  "role": "ADMIN"
}
```

## 📋 Business Rules

### Borrowing Rules
- Maximum 5 books per member
- Default borrowing period: 14 days (configurable)
- Daily fine rate: $1.00 for overdue books
- Members must be active to borrow books
- Books must be available for borrowing

### Security Rules
- Passwords encrypted with BCrypt (strength 12)
- JWT tokens required for all protected endpoints
- Role-based access control enforced
- User activities logged for audit purposes

## 🔍 Key Design Decisions

### Why JWT?
- Stateless authentication suitable for REST APIs
- Scalable for distributed systems
- Contains user roles for client-side decisions

### Why Role-Based Access?
- Clear separation of responsibilities
- Easy to extend with new roles
- Granular permission control

### Why Hierarchical Categories?
- Flexible organization structure
- Support for library taxonomy standards
- Easy navigation and filtering

### Why Activity Logging?
- Audit trail for compliance
- User behavior analysis
- Security monitoring

## 🧪 Testing

The API can be tested using:
- Postman collections (included)
- Unit tests with MockMvc
- Integration tests with TestContainers

## 📈 Performance Considerations

- Database indexes on frequently queried fields
- Pagination for large datasets
- Lazy loading for entity relationships
- Connection pooling with HikariCP
- Caching with Caffeine

## 🔒 Security Features

- Password encryption (BCrypt)
- JWT token validation
- CORS configuration
- Input validation
- SQL injection prevention
- XSS protection headers

## 📝 Future Enhancements

- Email notifications for overdue books
- Book reservation system
- Digital book support
- Advanced reporting dashboard
- Mobile app integration
- Barcode scanning support

---

**Author**: Abdulrahman  
**Contact**: abdulrahmangamaall@gmail.com

