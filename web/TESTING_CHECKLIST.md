# 🧪 MySQL Integration - Testing Checklist

## Pre-Testing Setup

- [ ] **XAMPP installed** dan path accessible
- [ ] **MySQL service running** di XAMPP (port 3306)
- [ ] **Java 21** installed dan di PATH
- [ ] **Maven** atau Maven Wrapper available

---

## 🔧 Step 1: Environment Verification

### Check MySQL Status
- [ ] Buka XAMPP Control Panel
- [ ] MySQL status = **Running** (hijau)
- [ ] Port 3306 tidak dipakai aplikasi lain

### Check phpMyAdmin Access
- [ ] Buka browser: http://localhost/phpmyadmin
- [ ] Bisa login (default: no password)
- [ ] Sidebar menampilkan list databases

---

## 🚀 Step 2: Application Startup

### Build Project (Optional)
```cmd
cd c:\TUGAS\TUGAS ALGO\tugas besar\web
mvnw.cmd clean package
```

- [ ] Build **SUCCESS** (no errors)
- [ ] MySQL connector JAR downloaded

### Run Application
```cmd
cd c:\TUGAS\TUGAS ALGO\tugas besar\web
mvnw.cmd spring-boot:run
```

- [ ] Application starts without errors
- [ ] Port 8080 available

### Verify Console Logs
Look for these in console output:

- [ ] `Hibernate: create table users ...` (jika first time)
- [ ] `Hibernate: create table lapang ...`
- [ ] `Hibernate: create table bookings ...`
- [ ] `Started WebApplication in X seconds`
- [ ] **NO** error messages tentang database connection

---

## 💾 Step 3: Database Verification

### Check Database Created
- [ ] Buka phpMyAdmin: http://localhost/phpmyadmin
- [ ] Database **futsalbook_db** muncul di sidebar
- [ ] Klik database tersebut

### Check Tables Created
Dalam database `futsalbook_db`:
- [ ] Table **users** exists
- [ ] Table **lapang** exists
- [ ] Table **bookings** exists

### Check Seed Data
- [ ] Table `lapang` contains **4 rows** (Lapang 1-4)
- [ ] Table `users` contains **1 row** (user1)
- [ ] Table `bookings` is **empty** (belum ada booking)

### Verify Table Structure

**Table: users**
- [ ] Column: `id` (BIGINT, PK, Auto Increment)
- [ ] Column: `username` (VARCHAR, UNIQUE)
- [ ] Column: `password` (VARCHAR)
- [ ] Column: `role` (VARCHAR)
- [ ] Column: `email` (VARCHAR, NULL)
- [ ] Column: `phone` (VARCHAR, NULL)
- [ ] Column: `registered_at` (DATETIME)

**Table: lapang**
- [ ] Column: `id` (BIGINT, PK, Auto Increment)
- [ ] Column: `name` (VARCHAR)
- [ ] Column: `photo_path` (VARCHAR, NULL)

**Table: bookings**
- [ ] Column: `id` (BIGINT, PK, Auto Increment)
- [ ] Column: `lapang_id` (BIGINT)
- [ ] Column: `lapang_name` (VARCHAR)
- [ ] Column: `username` (VARCHAR)
- [ ] Column: `date` (DATE)
- [ ] Column: `start_time` (TIME)
- [ ] Column: `end_time` (TIME)
- [ ] Column: `created_at` (DATETIME)

---

## 🧑‍💻 Step 4: User Registration Test

### Test Registration Flow
- [ ] Buka: http://localhost:8080
- [ ] Klik "Buat Akun" atau langsung ke: http://localhost:8080/register
- [ ] Isi form:
  - Username: `testuser`
  - Password: `test123`
- [ ] Klik "Daftar"
- [ ] Redirect ke **/dashboard**
- [ ] Dashboard menampilkan welcome message dengan username `testuser`

### Verify in Database
- [ ] Buka phpMyAdmin → `futsalbook_db` → table `users`
- [ ] **2 rows** sekarang (user1 + testuser)
- [ ] User `testuser` ada dengan:
  - password: `test123`
  - role: `USER`
  - registered_at: timestamp saat ini

---

## 🔐 Step 5: Login Test

### Test Existing User Login
- [ ] Logout atau buka incognito window
- [ ] Buka: http://localhost:8080/login
- [ ] Login dengan:
  - Username: `user1`
  - Password: `user123`
- [ ] Redirect ke **/dashboard**
- [ ] Dashboard menampilkan "Halo, user1!"

### Test New User Login
- [ ] Logout
- [ ] Login dengan:
  - Username: `testuser`
  - Password: `test123`
- [ ] Redirect ke **/dashboard**
- [ ] Dashboard menampilkan "Halo, testuser!"

### Test Admin Login
- [ ] Logout
- [ ] Login dengan:
  - Username: `admin`
  - Password: `admin123`
- [ ] Redirect ke **/admin**
- [ ] Admin dashboard displayed

---

## 📅 Step 6: Booking Test

### Create New Booking
- [ ] Login sebagai user (testuser)
- [ ] Di dashboard, lihat "Lapang Tersedia"
- [ ] Pilih salah satu lapang (misal: Lapang 1)
- [ ] Klik "Book"
- [ ] Isi form booking:
  - Date: **Besok** (tomorrow)
  - Start Time: `10:00`
  - End Time: `12:00`
- [ ] Submit booking
- [ ] Success message muncul
- [ ] Booking muncul di "Riwayat Booking Saya"

### Verify Booking in Database
- [ ] Buka phpMyAdmin → table `bookings`
- [ ] **1 row** baru dengan:
  - lapang_id: (ID Lapang yang dipilih)
  - lapang_name: "Lapang 1" (atau yang dipilih)
  - username: `testuser`
  - date: besok
  - start_time: 10:00:00
  - end_time: 12:00:00
  - created_at: timestamp sekarang

### Test Booking Visibility
- [ ] Booking muncul di user dashboard (Riwayat Booking)
- [ ] Logout dan login sebagai `admin`
- [ ] Booking muncul di admin dashboard (Semua Booking)

---

## 📸 Step 7: Upload Photo Test

### Upload Lapang Photo
- [ ] Login sebagai **admin**
- [ ] Di admin dashboard, scroll ke "Daftar Lapang"
- [ ] Pilih foto untuk Lapang 1 (upload file)
- [ ] Submit upload
- [ ] Success message muncul

### Verify Photo in Database
- [ ] Buka phpMyAdmin → table `lapang`
- [ ] Lapang 1 row sekarang punya `photo_path` yang terisi (misal: `lapang-1.jpg`)

### Verify Photo Display
- [ ] Logout, login sebagai user
- [ ] Di dashboard "Lapang Tersedia"
- [ ] Lapang 1 menampilkan **foto yang diupload** (bukan placeholder)
- [ ] Foto terlihat jelas dan besar

---

## 🗑️ Step 8: Delete Operations Test

### Delete Booking (User)
- [ ] Login sebagai user (testuser)
- [ ] Di "Riwayat Booking", klik "Batalkan" pada booking
- [ ] Konfirmasi cancel
- [ ] Booking hilang dari list
- [ ] Verify di database → row dihapus dari table `bookings`

### Delete Booking (Admin)
- [ ] Create booking baru sebagai user
- [ ] Login sebagai admin
- [ ] Di "Semua Booking", klik "Hapus"
- [ ] Booking hilang dari list
- [ ] Verify di database → row dihapus

### Delete User (Admin)
- [ ] Login sebagai admin
- [ ] Di tab "Data Pelanggan", klik "Hapus" pada user `testuser`
- [ ] Konfirmasi delete
- [ ] User hilang dari list
- [ ] Verify di database → user dihapus dari table `users`
- [ ] ⚠️ Bookings milik user tersebut **TIDAK** otomatis terhapus (by design)

---

## 🔄 Step 9: Persistence Test (CRITICAL)

### Restart Application
- [ ] Stop aplikasi Spring Boot (Ctrl+C di terminal)
- [ ] Wait for shutdown complete
- [ ] Start aplikasi lagi: `mvnw.cmd spring-boot:run`
- [ ] Aplikasi start successfully

### Verify Data Persistence
- [ ] Buka phpMyAdmin
- [ ] Database `futsalbook_db` **masih ada**
- [ ] Table `users` **masih ada** dengan data yang sama
- [ ] Table `lapang` **masih ada** dengan foto paths
- [ ] Table `bookings` **masih ada** (jika ada booking yang belum dihapus)

### Test Application After Restart
- [ ] Buka http://localhost:8080
- [ ] Login dengan user yang dibuat sebelumnya
- [ ] User **bisa login** (data tidak hilang)
- [ ] Riwayat booking **masih ada** (jika belum dihapus)
- [ ] Foto lapang **masih ditampilkan**

### ✅ **PERSISTENCE TEST PASSED** = MySQL integration berhasil!

---

## 🧹 Step 10: Data Consistency Test

### Test Concurrent Booking Conflict
- [ ] Login sebagai user1 di browser A
- [ ] Login sebagai testuser di browser B (incognito)
- [ ] User1: Book Lapang 1, date: besok, time: 14:00-16:00
- [ ] User2: Book **Lapang 1 yang sama**, date: besok, time: 15:00-17:00 (overlap)
- [ ] **Expected:** User2 booking ditolak (conflict)
- [ ] Verify di database: hanya 1 booking tersimpan (user1)

### Test Delete Lapang Cascade
- [ ] Login sebagai admin
- [ ] Hapus salah satu lapang (misal: Lapang 4)
- [ ] Verify di database:
  - [ ] Lapang 4 dihapus dari table `lapang`
  - [ ] Bookings untuk Lapang 4 **ikut terhapus** dari table `bookings`

---

## 📊 Step 11: Query Performance Test

### Test Large Dataset
Optional - untuk test performance:

1. Insert banyak users (via phpMyAdmin atau script):
   - [ ] 100 users inserted
   - [ ] Application masih responsive

2. Insert banyak bookings:
   - [ ] 500 bookings inserted
   - [ ] Dashboard load time < 2 detik

3. Check console logs:
   - [ ] SQL queries di-print (karena `show-sql=true`)
   - [ ] No N+1 query problems

---

## ⚠️ Troubleshooting Checklist

### If Database Not Created
- [ ] Check console logs untuk error messages
- [ ] Verify MySQL running di XAMPP
- [ ] Try manual create via phpMyAdmin
- [ ] Try run `database_setup.sql`

### If Connection Refused
- [ ] MySQL service running?
- [ ] Port 3306 open? (`netstat -an | findstr 3306`)
- [ ] Firewall blocking connection?
- [ ] Try `mysql -u root -p` di command line

### If Tables Not Created
- [ ] Check `spring.jpa.hibernate.ddl-auto=update` di application.properties
- [ ] Check console untuk DDL SQL statements
- [ ] Try change ke `ddl-auto=create` (WARNING: drops existing tables)

### If Data Not Persisting
- [ ] Transaction successful? Check console
- [ ] Database file permissions OK?
- [ ] Disk space available?

---

## 📋 Final Verification

### Complete Success Criteria
- [x] ✅ MySQL running
- [x] ✅ Database `futsalbook_db` created
- [x] ✅ 3 tables created (users, lapang, bookings)
- [x] ✅ Seed data loaded (4 lapang, 1 user)
- [x] ✅ User registration works → data saved to DB
- [x] ✅ User login works → data fetched from DB
- [x] ✅ Booking creation works → data saved to DB
- [x] ✅ Photo upload works → path saved to DB
- [x] ✅ Delete operations work → data removed from DB
- [x] ✅ **CRITICAL:** Restart app → data persists (not lost)
- [x] ✅ Admin dapat melihat semua data pelanggan
- [x] ✅ Conflict detection works (double booking prevented)

---

## 🎉 Success!

Jika semua checklist di atas **PASSED**, maka MySQL integration berhasil 100%!

**Aplikasi Anda sekarang:**
- ✅ Terhubung dengan MySQL database
- ✅ Data persistent (tidak hilang restart)
- ✅ Full CRUD operations
- ✅ Transaction support
- ✅ Production-ready architecture (dengan catatan security enhancements)

---

## 📞 Next Steps

Setelah testing berhasil, pertimbangkan:
1. Implement password hashing (BCrypt)
2. Add foreign key constraints
3. Add database indexes untuk performance
4. Setup database backup strategy
5. Add audit trail (created_by, updated_at, etc.)
6. Implement soft delete (deleted_at column)

Baca **MYSQL_SETUP_GUIDE.md** untuk detail lebih lanjut!
