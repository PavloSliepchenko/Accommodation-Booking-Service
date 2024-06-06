package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.model.Accommodation;
import com.example.accommodationbookingservice.model.Booking;
import com.example.accommodationbookingservice.model.User;
import com.example.accommodationbookingservice.service.NotificationService;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramNotificationService implements NotificationService {
    private static final Dotenv DOTENV = Dotenv.configure().load();
    private static final String TELEGRAM_CHAT_ID = DOTENV.get("TELEGRAM_CHAT_ID");
    private static final String TELEGRAM_BOT_TOKEN = DOTENV.get("TELEGRAM_BOT_TOKEN");
    private static final String API_URL = "https://api.telegram.org/bot" + TELEGRAM_BOT_TOKEN
            + "/sendMessage";

    @Override
    public void sendNotification(BookingResponseDto responseDto) {
        sendMessage(String.format("""
                        %s
                        Check in: %s
                        Check out: %s
                        Accommodation id: %s
                        Number of available accommodations: %s
                        User id: %s
                        """, responseDto.getStatus().equals(Booking.BookingStatus.PENDING.name())
                        ? "New booking!" : "Cancelled booking",
                responseDto.getCheckIn(), responseDto.getCheckOut(),
                responseDto.getAccommodation().getId(),
                responseDto.getAccommodation().getAvailability(), responseDto.getUser().getId()));
    }

    @Override
    public void sendNotification(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            sendMessage("No expired bookings today!");
        }

        for (Booking booking : bookings) {
            User user = booking.getUser();
            Accommodation accommodation = booking.getAccommodation();
            sendMessage(String.format("""
                    Expired booking!
                    Booking id: %s
                    User's id: %s
                    User's name: %s
                    Accommodation info:
                    Accommodation id: %s
                    Accommodation type: %s
                    Accommodation location: %s
                    Accommodation availability: %s
                    """, booking.getId(), user.getId(), user.getFirstName(),
                    accommodation.getId(), accommodation.getType(), accommodation.getLocation(),
                    accommodation.getAvailability()));
        }
    }

    private void sendMessage(String message) {
        String requestUrl = String.format("%s?chat_id=%s&text=%s",
                API_URL, TELEGRAM_CHAT_ID, message);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(requestUrl, null, String.class);
    }
}
