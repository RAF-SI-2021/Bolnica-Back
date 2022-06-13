package raf.si.bolnica.laboratory.services;

import net.sf.jasperreports.engine.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;
import raf.si.bolnica.laboratory.repositories.LaboratorijskiRadniNalogRepository;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.util.Map;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService{

    @Autowired
    LaboratorijskiRadniNalogRepository repository;

    @Override
    public ResponseEntity<?> reportPrint(Map<String, Object> request) {
        try {
            String laboratorijskiRadniNalogId = request.get("labRadniNalogId").toString();

            LaboratorijskiRadniNalog labRadniNalog =  repository.findByLaboratorijskiRadniNalogId(Long.valueOf(laboratorijskiRadniNalogId));

            if(labRadniNalog == null){
                return ResponseEntity.notFound().build();
            }
            request.put("lbd" , labRadniNalog.getLbp().toString());
            request.remove("laboratorijskiRadniNalogId");

            JRDataSource jrDataSource = new JREmptyDataSource();

            InputStream stream = this.getClass().getResourceAsStream("/LabReport.jrxml");

            JasperReport labReport = JasperCompileManager.compileReport(stream);
            JasperPrint labPrint = JasperFillManager.fillReport(labReport, request, jrDataSource);

            byte[] reportByteArray = JasperExportManager.exportReportToPdf(labPrint);

            labRadniNalog.setLabReport(DatatypeConverter.printBase64Binary(reportByteArray));

            repository.save(labRadniNalog);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
