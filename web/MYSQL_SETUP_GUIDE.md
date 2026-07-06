# MySQL Integration Setup Guide - FutsalBook

## ✅ What Has Been Implemented

Aplikasi Anda sekarang sudah terintegrasi penuh dengan MySQL database menggunakan Spring Data JPA. Berikut yang sudah dilakukan:

### 1. **Dependencies Added** (pom.xml)
   - `spring-boot-starter-data-jpa` - untuk JPA/Hibernate
   - `mysql-connector-j` - MySQL driver

### 2. **Database Configuration** (application.properties)
   - Database: `futsalbook_db` (auto-create jika belum ada)
   - Host: `localhost:3306` (XAMPP default)
   - Username: `root`
   - Password: (kosong - XAMPP default)
   - Auto DDL: `update` (tabel dibuat otomatis)

### 3. **JPA Entities**
   - `User.java` - @Entity dengan auto-increment ID
   - `Lapang.java` - @Entity dengan auto-increment ID
   - `Booking.java` - @Entity dengan auto-increment ID

### 4. **JPA Repositories**
   - `UserRepository.java` - CRUD operations untuk User
   - `LapangRepository.java` - CRUD operations untuk Lapang
   - `BookingRepository.java` - CRUD operations untuk Booking + custom query

### 5. **Service Layer Refactored**
   - `AppDataService.java` - semua ArrayList diganti dengan repository calls
   - @Transactional untuk consistency
   - Data seeding tetap ada (4 lapang + 1 demo user)

---

## 🚀 Cara Menjalankan

### Step 1: Start XAMPP MySQL
1. Buka **XAMPP Control Panel**
2. Klik **Start** pada **MySQL**
3. Pastikan statusnya jadi hijau (running)

### Step 2: Build Project (Optional - Download Dependencies)
```cmd
cd c:\TUGAS\TUGAS ALGO\tugas besar\web
mvnw.cmd clean package
```

### Step 3: Run Application
```cmd
cd c:\TUGAS\TUGAS ALGO\tugas besar\web
mvnw.cmd spring-boot:run
```

Atau jalankan dari IDE Anda (Run `WebApplication.java`)

### Step 4: Verify Database Creation
1. Buka browser: http://localhost/phpmyadmin
2. Anda akan melihat database baru: **futsalbook_db**
3. Di dalamnya ada 3 tabel:
   - `users`
   - `lapang`
   - `bookings`

---

## 🔍 Cara Mengecek Koneksi Berhasil

### 1. Check Console Logs
Saat aplikasi start, Anda akan melihat:
```
Hibernate: create table users ...
Hibernate: create table lapang ...
Hibernate: create table bookings ...
```

### 2. Check phpMyAdmin
- Buka: http://localhost/phpmyadmin
- Pilih database: `futsalbook_db`
- Anda akan melihat:
  - 4 rows di tabel `lapang` (Lapang 1-4)
  - 1 row di tabel `users` (user1)

### 3. Test Application
1. Buka: http://localhost:8080
2. Login sebagai:
   - Username: `user1`
   - Password: `user123`
3. Buat booking baru
4. Restart aplikasi
5. **Data tetap ada!** (tidak hilang lagi)

---

## 📝 Perubahan Penting

### ❌ SEBELUM (In-Memory)
- Data disimpan di ArrayList
- Data HILANG setiap restart
- Tidak ada persistence

### ✅ SEKARANG (MySQL)
- Data disimpan di MySQL database
- Data PERSISTEN (tidak hilang restart)
- Full CRUD operations
- Transaction support

---

## 🛠️ Troubleshooting

### Problem 1: "Communications link failure"
**Penyebab:** MySQL di XAMPP belum running

**Solusi:**
1. Buka XAMPP Control Panel
2. Start MySQL service
3. Restart aplikasi Spring Boot

### Problem 2: "Access denied for user 'root'"
**Penyebab:** Password MySQL tidak sesuai

**Solusi:** Edit `application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Problem 3: Database tidak ter-create otomatis
**Penyebab:** Parameter createDatabaseIfNotExist tidak bekerja

**Solusi:** Create manual via phpMyAdmin:
1. Buka phpMyAdmin
2. Klik "New" di sidebar
3. Database name: `futsalbook_db`
4. Collation: `utf8mb4_unicode_ci`
5. Create

Atau jalankan `database_setup.sql` di phpMyAdmin

---

## 📊 Database Schema

### Table: `users`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK, Auto) | User ID |
| username | VARCHAR(255) | Unique username |
| password | VARCHAR(255) | Plain password |
| role | VARCHAR(50) | USER/ADMIN |
| email | VARCHAR(255) | Email (optional) |
| phone | VARCHAR(50) | Phone (optional) |
| registered_at | DATETIME | Registration timestamp |

### Table: `lapang`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK, Auto) | Lapang ID |
| name | VARCHAR(255) | Nama lapang |
| photo_path | VARCHAR(500) | Relative path foto |

### Table: `bookings`
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK, Auto) | Booking ID |
| lapang_id | BIGINT | ID lapang yang dibooking |
| lapang_name | VARCHAR(255) | Nama lapang (denormalized) |
| username | VARCHAR(255) | Username yang booking |
| date | DATE | Tanggal booking |
| start_time | TIME | Jam mulai |
| end_time | TIME | Jam selesai |
| created_at | DATETIME | Waktu booking dibuat |

---

## 🎯 Next Steps (Optional Improvements)

1. **Password Hashing** - Encrypt password dengan BCrypt
2. **Foreign Keys** - Add FK constraints antara tables
3. **Indexes** - Add index untuk query performance
4. **Validation** - Add @Valid annotations di entities
5. **Soft Delete** - Add deleted_at column instead of hard delete
6. **Audit Trail** - Track who created/updated records

---

## 📞 Admin Hardcoded (Tidak di Database)

Admin login tetap hardcoded di `AuthController.java`:
- Username: `admin`
- Password: `admin123`

Ini **TIDAK** disimpan di database. Hanya user dengan role "USER" yang disimpan.

---

## ✅ Testing Checklist

- [ ] XAMPP MySQL running
- [ ] Aplikasi Spring Boot start tanpa error
- [ ] Database `futsalbook_db` muncul di phpMyAdmin
- [ ] 3 tables created: users, lapang, bookings
- [ ] Data seed berhasil (4 lapang, 1 user)
- [ ] Register user baru → data masuk ke tabel users
- [ ] Buat booking → data masuk ke tabel bookings
- [ ] Upload foto lapang → photo_path terupdate di tabel lapang
- [ ] Restart aplikasi → data tetap ada (tidak hilang)

---

**Selamat! Aplikasi Anda sekarang sudah terhubung dengan MySQL! 🎉**
