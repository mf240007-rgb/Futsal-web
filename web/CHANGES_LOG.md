# MySQL Integration - Changes Log

## 📅 Date: June 22, 2026

## 🎯 Objective
Mengintegrasikan aplikasi FutsalBook dengan MySQL database (XAMPP) untuk persistensi data, menggantikan in-memory ArrayList dengan Spring Data JPA.

---

## 📝 Files Modified

### 1. **pom.xml**
**Changes:**
- Added dependency: `spring-boot-starter-data-jpa`
- Added dependency: `mysql-connector-j` (runtime scope)

**Impact:** Enable JPA/Hibernate dan MySQL driver

---

### 2. **application.properties**
**Changes:**
```properties
# MySQL Database Configuration (XAMPP)
spring.datasource.url=jdbc:mysql://localhost:3306/futsalbook_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

**Impact:** Configure database connection ke XAMPP MySQL

---

### 3. **User.java**
**Changes:**
- Added: `@Entity` annotation
- Added: `@Table(name = "users")` annotation
- Added: `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)` untuk field `id`
- Added: `private Long id` field + getter/setter
- Added: `@Column` annotations untuk constraints (unique, nullable, column names)

**Before:** Plain POJO class
**After:** JPA Entity dengan auto-increment ID

---

### 4. **Lapang.java**
**Changes:**
- Added: `@Entity` annotation
- Added: `@Table(name = "lapang")` annotation
- Added: `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)` untuk field `id`
- Changed constructor: Removed `Long id` parameter (auto-generated sekarang)
- Added: `@Column` annotations

**Before:** Plain POJO class dengan manual ID
**After:** JPA Entity dengan auto-increment ID

---

### 5. **Booking.java**
**Changes:**
- Added: `@Entity` annotation
- Added: `@Table(name = "bookings")` annotation
- Added: `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)` untuk field `id`
- Changed constructor: Removed `Long id` parameter (auto-generated sekarang)
- Added: `@Column` annotations dengan snake_case column names

**Before:** Plain POJO class dengan manual ID
**After:** JPA Entity dengan auto-increment ID

---

### 6. **AppDataService.java**
**Major Refactoring:**

**BEFORE:**
```java
private final List<Lapang> lapangs = new ArrayList<>();
private final List<User> users = new ArrayList<>();
private final List<Booking> bookings = new ArrayList<>();
private final AtomicLong lapangCounter = new AtomicLong(1);
private final AtomicLong bookingCounter = new AtomicLong(1);
```

**AFTER:**
```java
@Autowired
private LapangRepository lapangRepository;

@Autowired
private UserRepository userRepository;

@Autowired
private BookingRepository bookingRepository;
```

**Changes:**
- Removed: All ArrayList dan AtomicLong counters
- Added: Autowired repositories
- Modified: All methods untuk gunakan repository calls
- Added: `@Transactional` annotations untuk write operations
- Modified: `init()` method - seed data hanya jika database kosong
- Changed: All CRUD operations delegate ke repositories

**Impact:** Data sekarang persistent di MySQL, tidak hilang saat restart

---

## 📁 Files Created (New)

### 7. **UserRepository.java**
**Location:** `src/main/java/com/tugas/web/repository/UserRepository.java`

**Content:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    List<User> findByRole(String role);
}
```

**Purpose:** CRUD operations untuk User entity

---

### 8. **LapangRepository.java**
**Location:** `src/main/java/com/tugas/web/repository/LapangRepository.java`

**Content:**
```java
public interface LapangRepository extends JpaRepository<Lapang, Long> {
}
```

**Purpose:** CRUD operations untuk Lapang entity

---

### 9. **BookingRepository.java**
**Location:** `src/main/java/com/tugas/web/repository/BookingRepository.java`

**Content:**
```java
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUsernameOrderByCreatedAtDesc(String username);
    List<Booking> findByDateOrderByStartTimeAsc(LocalDate date);
    List<Booking> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN false ELSE true END FROM Booking b ...")
    boolean isAvailable(...);
}
```

**Purpose:** CRUD operations untuk Booking entity + custom availability check query

---

### 10. **database_setup.sql**
**Location:** `web/database_setup.sql`

**Purpose:** Optional manual database creation script

**Usage:** 
- Run di phpMyAdmin jika database tidak auto-create
- Contains CREATE DATABASE and CREATE TABLE statements
- Includes sample data inserts

---

### 11. **MYSQL_SETUP_GUIDE.md**
**Location:** `web/MYSQL_SETUP_GUIDE.md`

**Purpose:** Comprehensive setup dan troubleshooting guide untuk MySQL integration

**Sections:**
- What has been implemented
- How to run
- How to verify connection
- Important changes
- Troubleshooting
- Database schema
- Next steps

---

### 12. **QUICK_START.txt**
**Location:** `web/QUICK_START.txt`

**Purpose:** Quick reference card untuk start aplikasi dengan MySQL

---

### 13. **CHANGES_LOG.md** (this file)
**Location:** `web/CHANGES_LOG.md`

**Purpose:** Complete log of all changes made untuk MySQL integration

---

## 🔄 Migration Path

### Data Flow Before (In-Memory)
```
Controller → AppDataService → ArrayList (in-memory) → ❌ Data lost on restart
```

### Data Flow After (MySQL)
```
Controller → AppDataService → Repository → JPA/Hibernate → MySQL Database → ✅ Data persisted
```

---

## ⚠️ Breaking Changes

### 1. Constructor Changes
**Lapang:**
- Before: `new Lapang(Long id, String name)`
- After: `new Lapang(String name)` (ID auto-generated)

**Booking:**
- Before: `new Booking(Long id, Long lapangId, ...)`
- After: `new Booking(Long lapangId, ...)` (ID auto-generated)

**Impact:** AppDataService updated, controllers tidak terpengaruh (tetap pakai service methods)

### 2. User Entity Now Has ID
- Before: Username sebagai identifier
- After: Long ID sebagai primary key, username unique constraint

**Impact:** Repository methods return User dengan ID field populated

---

## ✅ Backward Compatibility

### Controllers
✅ **NO CHANGES NEEDED** - All controllers tetap kompatibel karena hanya interact dengan AppDataService, bukan langsung dengan data structure.

### Templates
✅ **NO CHANGES NEEDED** - Templates tetap sama karena model methods (getPhotoUrl(), getCreatedAtFormatted(), etc.) tidak berubah.

### Data Seeding
✅ **STILL WORKS** - Init method tetap seed 4 lapang dan 1 user demo, tapi sekarang disimpan di database.

---

## 🧪 Testing Requirements

### Prerequisites
1. ✅ XAMPP installed
2. ✅ MySQL service running di XAMPP
3. ✅ Port 3306 available
4. ✅ Java 21 installed
5. ✅ Maven atau Maven Wrapper available

### Test Cases
1. ✅ Start aplikasi → database auto-created
2. ✅ Tables auto-created (users, lapang, bookings)
3. ✅ Data seeding berhasil (4 lapang, 1 user)
4. ✅ Register user baru → saved to database
5. ✅ Login → fetch from database
6. ✅ Create booking → saved to database
7. ✅ Upload foto lapang → photo_path updated in database
8. ✅ Restart aplikasi → data tetap ada (persistent)
9. ✅ Delete booking → removed from database
10. ✅ Delete user → removed from database

---

## 📊 Database Schema

### Database: `futsalbook_db`

#### Table: `users`
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    registered_at DATETIME
);
```

#### Table: `lapang`
```sql
CREATE TABLE lapang (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    photo_path VARCHAR(500)
);
```

#### Table: `bookings`
```sql
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lapang_id BIGINT NOT NULL,
    lapang_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at DATETIME
);
```

---

## 🚀 Deployment Notes

### Development Environment
- Database: futsalbook_db
- Host: localhost:3306
- Username: root
- Password: (empty)
- DDL: update (auto-create tables)

### Production Recommendations
1. Change `spring.jpa.hibernate.ddl-auto` dari `update` ke `validate`
2. Set proper MySQL username/password
3. Use connection pooling (HikariCP already included in Spring Boot)
4. Add database backup strategy
5. Encrypt passwords (BCrypt)
6. Add foreign key constraints
7. Add indexes untuk performance

---

## 📈 Performance Considerations

### Before (ArrayList)
- ✅ Fast in-memory access
- ❌ No persistence
- ❌ Data lost on restart
- ❌ No concurrent access safety
- ❌ No transaction support

### After (MySQL)
- ✅ Data persistence
- ✅ Transaction support
- ✅ Concurrent access safety (database locks)
- ✅ Query optimization via indexes
- ⚠️ Slightly slower than in-memory (network + disk I/O)
- ✅ Scalable (can handle large datasets)

---

## 🔒 Security Notes

### Current Implementation
⚠️ **INSECURE for production:**
- Passwords stored as plain text
- No SQL injection protection for raw queries (OK, using JPA)
- No authentication token/session management
- Admin credentials hardcoded

### Recommendations
1. Use BCryptPasswordEncoder untuk hash passwords
2. Implement Spring Security
3. Add CSRF protection
4. Use JWT or session tokens
5. Move admin credentials ke database
6. Add input validation
7. Add rate limiting untuk login attempts

---

## 📞 Support

Jika ada masalah:
1. Check MYSQL_SETUP_GUIDE.md (troubleshooting section)
2. Check application logs di console
3. Check MySQL logs di XAMPP
4. Verify database di phpMyAdmin

---

**Status: ✅ IMPLEMENTATION COMPLETE**
**Tested: ⏳ Waiting for user testing**
**Ready for Production: ❌ Requires security enhancements**
