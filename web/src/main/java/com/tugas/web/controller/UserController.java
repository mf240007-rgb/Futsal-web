package com.tugas.web.controller;

import com.tugas.web.model.Booking;
import com.tugas.web.model.User;
import com.tugas.web.service.AppDataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping
public class UserController {

    private final AppDataService appDataService;

    public UserController(AppDataService appDataService) {
        this.appDataService = appDataService;
    }

    // ── Register ─────────────────────────────────────────────────────────────

    @GetMapping("/register")
    public String registerForm(Model model, HttpSession session) {
        // Kalau sudah login, langsung redirect — tidak perlu register ulang
        if (session.getAttribute("username") != null) {
            if ("ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/admin/dashboard";
            return "redirect:/dashboard";
        }
        model.addAttribute("userForm", new User());
        return "register";
    }

    @PostMapping("/proses-register")
    public String prosesRegister(@ModelAttribute User user,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (user.getUsername() == null || user.getUsername().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            model.addAttribute("errorMessage", "Username dan password wajib diisi.");
            return "register";
        }
        if (appDataService.existsUser(user.getUsername())) {
            model.addAttribute("errorMessage", "Username sudah terdaftar, silakan gunakan username lain.");
            return "register";
        }
        user.setRole("USER");
        appDataService.addUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registrasi berhasil! Silakan login.");
        return "redirect:/login";
    }

    // ── Dashboard User ────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) return "redirect:/login";

        String username = (String) session.getAttribute("username");
        List<Booking> myBookings = appDataService.getBookingsByUser(username);
        long todayCount = appDataService.getBookings().stream()
                .filter(b -> b.getDate().equals(LocalDate.now()))
                .count();

        model.addAttribute("username", username);
        model.addAttribute("usernameInitial", username.substring(0, 1).toUpperCase());
        model.addAttribute("myBookings", myBookings);
        model.addAttribute("myBookingCount", myBookings.size());
        model.addAttribute("lapangs", appDataService.getLapangs());
        model.addAttribute("lapangCount", appDataService.getLapangs().size());
        model.addAttribute("todayBookingCount", todayCount);
        return "dashboard";
    }

    // ── Booking ───────────────────────────────────────────────────────────────

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) return "redirect:/login";

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("usernameInitial", username.substring(0, 1).toUpperCase());
        model.addAttribute("lapangs", appDataService.getLapangs());
        model.addAttribute("bookings", appDataService.getBookingsByUser(username));
        model.addAttribute("bookingForm", new Booking());
        return "home";
    }

    @PostMapping("/booking")
    public String createBooking(@RequestParam("lapangId") Long lapangId,
                                @RequestParam("date") String date,
                                @RequestParam("startTime") String startTime,
                                @RequestParam("endTime") String endTime,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) return "redirect:/login";

        String username = (String) session.getAttribute("username");
        LocalDate bookingDate = LocalDate.parse(date);
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        if (!end.isAfter(start)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Waktu selesai harus setelah waktu mulai.");
            return "redirect:/home";
        }
        if (!appDataService.isLapangAvailable(lapangId, bookingDate, start, end)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lapang sudah dibooking pada rentang waktu tersebut.");
            return "redirect:/home";
        }
        appDataService.addBooking(lapangId, username, bookingDate, start, end);
        redirectAttributes.addFlashAttribute("successMessage", "Booking berhasil dikonfirmasi!");
        return "redirect:/home";
    }

    @PostMapping("/booking/cancel")
    public String cancelBooking(@RequestParam("bookingId") Long bookingId,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) return "redirect:/login";

        String username = (String) session.getAttribute("username");
        boolean deleted = appDataService.deleteBooking(bookingId, username);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking berhasil dibatalkan.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking tidak ditemukan atau bukan milik Anda.");
        }
        return "redirect:/home";
    }
}
