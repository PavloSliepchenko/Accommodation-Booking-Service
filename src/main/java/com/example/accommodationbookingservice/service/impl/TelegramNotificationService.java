package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.model.Booking;
import com.example.accommodationbookingservice.service.NotificationService;
import io.github.cdimascio.dotenv.Dotenv;
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

    private void sendMessage(String message) {
        String requestUrl = String.format("%s?chat_id=%s&text=%s",
                API_URL, TELEGRAM_CHAT_ID, message);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(requestUrl, null, String.class);
    }
}
