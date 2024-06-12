package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingservice.dto.payment.PaymentResponseFullInfoDto;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.PaymentException;
import com.example.accommodationbookingservice.mapper.PaymentMapper;
import com.example.accommodationbookingservice.model.Booking;
import com.example.accommodationbookingservice.model.Payment;
import com.example.accommodationbookingservice.repository.BookingRepository;
import com.example.accommodationbookingservice.repository.PaymentRepository;
import com.example.accommodationbookingservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.cdimascio.dotenv.Dotenv;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String SUCCESS = "Success";
    private static final String FINISH_PAYMENT = "You can finish the payment following the link: ";
    private static final String SUCCESS_URL =
            "http://localhost:8080/api/payments/success?session_id={CHECKOUT_SESSION_ID}";
    private static final String CANCEL_URL =
            "http://localhost:8080/api/payments/cancel?session_id={CHECKOUT_SESSION_ID}";
    private static final String STIPE_SECRET_KEY = Dotenv.load().get("STRIPE_SECRET_KEY");
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentResponseDto> getPaymentByUserId(Long userId) {
        return paymentRepository.findPaymentByUserId(userId).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentResponseDto addPayment(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findByUserIdAndId(userId, bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("""
                        User with id %s doesn't have booking with id %s
                        """, userId, bookingId)));
        Optional<Payment> paymentOptional = paymentRepository.findByBookingId(bookingId);
        if (paymentOptional.isEmpty()) {
            BigDecimal amountToPay =
                    booking.getAccommodation().getDailyRate().multiply(getNumberOfDays(booking));
            Session session = createSession(amountToPay);
            String description = null;
            URI checkoutUri = null;
            try {
                checkoutUri = new URI(session.getUrl());
                description = openUrlInBrowser(checkoutUri);
            } catch (Exception e) {
                description = FINISH_PAYMENT + checkoutUri;
            }

            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmountToPay(amountToPay);
            payment.setSessionId(session.getId());
            payment.setStatus(Payment.PaymentStatus.PENDING);
            try {
                payment.setSessionUrl(new URL(session.getUrl()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            PaymentResponseDto responseDto = paymentMapper.toDto(paymentRepository.save(payment));
            responseDto.setDescription(description);
            return responseDto;
        } else if (paymentOptional.get().getStatus() == Payment.PaymentStatus.PENDING
                && paymentOptional.get().getSessionId() != null) {
            Stripe.apiKey = STIPE_SECRET_KEY;
            try {
                Session session = Session.retrieve(paymentOptional.get().getSessionId());
                String description = openUrlInBrowser(new URI(session.getUrl()));
                PaymentResponseDto responseDto = paymentMapper.toDto(paymentOptional.get());
                responseDto.setDescription(description);
                return responseDto;
            } catch (StripeException | URISyntaxException e) {
                throw new PaymentException("Failed to retrieve session", e);
            }
        }
        throw new PaymentException(String.format("Booking with id %s was paid before", bookingId));
    }

    @Override
    public PaymentResponseFullInfoDto updatePaymentStatus(String sessionId,
                                                          Payment.PaymentStatus status) {
        Optional<Payment> paymentOptional = paymentRepository.findBySessionId(sessionId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setStatus(status);
            if (status == Payment.PaymentStatus.PAID) {
                Booking booking = payment.getBooking();
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
            }
            return paymentMapper.toFullInfoDto(paymentRepository.save(payment));
        }
        throw new PaymentException("No payments were found with session id " + sessionId);
    }

    private Session createSession(BigDecimal amountToPay) {
        Stripe.apiKey = STIPE_SECRET_KEY;
        SessionCreateParams.Builder sessionBuilder = new SessionCreateParams.Builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(SUCCESS_URL)
                .setCancelUrl(CANCEL_URL)
                .addLineItem(
                        SessionCreateParams
                                .LineItem
                                .builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmountDecimal(amountToPay
                                                        .multiply(BigDecimal.valueOf(100)))
                                                .setProductData(SessionCreateParams
                                                        .LineItem
                                                        .PriceData
                                                        .ProductData
                                                        .builder()
                                                        .setName("Accommodation booking")
                                                        .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                );
        SessionCreateParams sessionParams = sessionBuilder.build();
        try {
            return Session.create(sessionParams);
        } catch (StripeException e) {
            throw new PaymentException("Failed to create a session", e);
        }
    }

    private BigDecimal getNumberOfDays(Booking booking) {
        LocalDate checkIn = booking.getCheckIn();
        LocalDate checkOut = booking.getCheckOut();
        if (checkIn.getYear() - checkOut.getYear() == 0) {
            return BigDecimal.valueOf(checkOut.getDayOfYear() - checkIn.getDayOfYear());
        }
        LocalDate dec31 = LocalDate.of(checkIn.getYear(), Month.DECEMBER, 31);
        int daysUsedPreviousYear = dec31.getDayOfYear() - checkIn.getDayOfYear();
        return BigDecimal.valueOf(daysUsedPreviousYear + checkOut.getDayOfYear());
    }

    private String openUrlInBrowser(URI uri) {
        String[] browsers = {"google-chrome", "firefox", "opera", "epiphany",
                "konqueror", "mozilla", "netscape", "xdg-open"};
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();
            if (os.contains("win")) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + uri.toString());
                return SUCCESS;
            } else if (os.contains("mac")) {
                runtime.exec("open " + uri.toString());
                return SUCCESS;
            } else if (os.contains("nix") || os.contains("nux")) {
                for (String browser : browsers) {
                    if (runtime.exec(new String[]{browser, uri.toString()}) == null) {
                        continue;
                    }
                    break;
                }
                return SUCCESS;
            } else {
                return FINISH_PAYMENT + uri;
            }
        } catch (Exception e) {
            return FINISH_PAYMENT + uri;
        }
    }
}
