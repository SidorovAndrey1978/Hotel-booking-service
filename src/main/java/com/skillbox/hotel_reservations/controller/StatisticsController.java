package com.skillbox.hotel_reservations.controller;

import com.skillbox.hotel_reservations.statistics.CSVExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final CSVExportService csvExportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download-csv")
    public ResponseEntity<Void> downloadStatisticsAsCSV(HttpServletResponse response) throws IOException {
        try {
            csvExportService.exportStatisticsToCSV(response);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
