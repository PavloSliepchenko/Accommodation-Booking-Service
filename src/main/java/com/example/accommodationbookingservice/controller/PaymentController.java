package com.example.accommodationbookingservice.controller;

import com.example.accommodationbookingservice.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingservice.dto.payment.PaymentResponseFullInfoDto;
import com.example.accommodationbookingservice.model.Payment;
import com.example.accommodationbookingservice.model.User;
import com.example.accommodationbookingservice.service.NotificationService;
import com.example.accommodationbookingservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/payments")
@Tag(name = "Payments management", description = "End points for CRUD operations with payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('CUSTOMER')")
    @Operation(summary = "Get my payments",
            description = "Retrieves payment information for users")
    public List<PaymentResponseDto> getPayments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getPaymentByUserId(user.getId());
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Get payments by user id",
            description = "Retrieves payment information by user id. Available for managers")
    public List<PaymentResponseDto> getPayments(@PathVariable Long userId) {
        return paymentService.getPaymentByUserId(userId);
    }

    @PostMapping(value = "/{bookingId}")
    @PreAuthorize(value = "hasAuthority('CUSTOMER')")
    @Operation(summary = "To pay",
            description = "Initiates payment sessions for booking transactions")
    public PaymentResponseDto setPayment(Authentication authentication,
                                         @PathVariable Long bookingId) {
        User user = (User) authentication.getPrincipal();
        return paymentService.addPayment(bookingId, user.getId());
    }

    @GetMapping(value = "/success")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Success endpoint", description = "Endpoint for redirection in case of "
            + "successful payment operation. Updates payment status to 'PAID' "
            + "and sends a notification to telegram chat")
    public String success(@RequestParam("session_id") String sessionId) {
        PaymentResponseFullInfoDto responseDto =
                paymentService.updatePaymentStatus(sessionId, Payment.PaymentStatus.PAID);
        notificationService.sendNotification(responseDto);
        return "success";
    }

    @GetMapping(value = "/cancel")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Cancel endpoint",
            description = "Endpoint for redirection in case of payment cancellation")
    public String cancel(@RequestParam("session_id") String sessionId) {
        return "canceled";
    }
}
