package raf.si.bolnica.laboratory.services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PdfGeneratorService {

    public ResponseEntity<?> reportPrint(Map<String, Object> request);
}
