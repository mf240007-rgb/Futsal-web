package com.tugas.web.controller;

import com.tugas.web.model.Lapang;
import com.tugas.web.service.AppDataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String UPLOAD_DIR = "uploads";
    private final AppDataService appDataService;

    public AdminController(AppDataService appDataService) {
        this.appDataService = appDataService;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    private void populateModel(Model model) {
        long uniqueUserCount = appDataService.getBookings().stream()
                .map(b -> b.getUsername()).distinct().count();
        long todayCount = appDataService.getBookingsToday().size();

        // Hitung jumlah booking per user untuk tab pelanggan
        java.util.Map<String, Long> bookingCountMap = appDataService.getBookings().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        b -> b.getUsername(), java.util.stream.Collectors.counting()));

        model.addAttribute("lapangs", appDataService.getLapangs());
        model.addAttribute("bookings", appDataService.getBookings());
        model.addAttribute("bookingsToday", appDataService.getBookingsToday());
        model.addAttribute("users", appDataService.getUsers());
        model.addAttribute("bookingCountMap", bookingCountMap);
        model.addAttribute("uniqueUserCount", uniqueUserCount);
        model.addAttribute("todayBookingCount", todayCount);
        model.addAttribute("totalUserCount", appDataService.getUsers().size());
    }

    // ── Dashboard ────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        populateModel(model);
        model.addAttribute("lapangForm", new Lapang());
        return "admin-dashboard";
    }

    // ── Lapang CRUD ──────────────────────────────────────────────────────────

    @PostMapping("/lapang/save")
    public String saveLapang(@ModelAttribute Lapang lapang, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        if (lapang.getName() == null || lapang.getName().isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Nama lapang tidak boleh kosong.");
            return "redirect:/admin/dashboard";
        }
        appDataService.saveLapang(lapang);
        redirectAttributes.addFlashAttribute("message", "Data lapang berhasil disimpan.");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/lapang/edit")
    public String editLapang(@RequestParam("id") Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        populateModel(model);
        model.addAttribute("lapangForm", appDataService.findLapangById(id).orElse(new Lapang()));
        return "admin-dashboard";
    }

    @PostMapping("/lapang/delete")
    public String deleteLapang(@RequestParam("id") Long id, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        appDataService.deleteLapang(id);
        redirectAttributes.addFlashAttribute("message", "Lapang berhasil dihapus bersama booking terkait.");
        return "redirect:/admin/dashboard";
    }

    // ── Upload Foto Lapangan ─────────────────────────────────────────────────

    @PostMapping("/lapang/upload-photo")
    public String uploadPhoto(@RequestParam("lapangId") Long lapangId,
                              @RequestParam("photo") MultipartFile photo,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";

        if (photo == null || photo.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Pilih file foto terlebih dahulu.");
            return "redirect:/admin/dashboard";
        }

        String originalFilename = photo.getOriginalFilename();
        if (originalFilename == null) {
            redirectAttributes.addFlashAttribute("message", "Nama file tidak valid.");
            return "redirect:/admin/dashboard";
        }

        String ext = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                : ".jpg";

        // Hanya izinkan ekstensi gambar
        if (!ext.matches("\\.(jpg|jpeg|png|gif|webp)")) {
            redirectAttributes.addFlashAttribute("message", "Format file harus JPG, PNG, GIF, atau WebP.");
            return "redirect:/admin/dashboard";
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = "lapang-" + lapangId + ext;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            appDataService.updateLapangPhoto(lapangId, fileName);
            redirectAttributes.addFlashAttribute("message", "Foto lapangan berhasil diupload.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Gagal upload foto: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    // ── Booking ──────────────────────────────────────────────────────────────

    @PostMapping("/booking/delete")
    public String deleteBooking(@RequestParam("bookingId") Long bookingId,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        boolean deleted = appDataService.deleteBookingAdmin(bookingId);
        redirectAttributes.addFlashAttribute("message", deleted ? "Booking berhasil dihapus." : "Booking tidak ditemukan.");
        return "redirect:/admin/dashboard";
    }

    // ── Data Pelanggan ───────────────────────────────────────────────────────

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("username") String username,
                             HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        appDataService.deleteUser(username);
        redirectAttributes.addFlashAttribute("message", "Akun pelanggan " + username + " berhasil dihapus.");
        return "redirect:/admin/dashboard";
    }
}
