package com.skillbox.hotel_reservations.statistics;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVExportService {
    private final StatisticsRepository statisticsRepository;

    public void exportStatisticsToCSV(HttpServletResponse response) throws IOException,
            CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"statistics.csv\"");

        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);

        StatefulBeanToCsv<StatisticsEntry> beanToCsv = new StatefulBeanToCsvBuilder<StatisticsEntry>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        
        List<StatisticsEntry> entries = statisticsRepository.findAll();

        beanToCsv.write(entries);
    }
}
