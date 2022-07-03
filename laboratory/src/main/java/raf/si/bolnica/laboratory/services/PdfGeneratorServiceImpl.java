package raf.si.bolnica.laboratory.services;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;
import raf.si.bolnica.laboratory.entities.LaboratoriskiNalazParametar;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;
import raf.si.bolnica.laboratory.repositories.LaboratorijskiRadniNalogRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "transactionManager")
public class PdfGeneratorServiceImpl implements PdfGeneratorService{

    @Autowired
    LaboratorijskiRadniNalogRepository repository;
    @Autowired
    RezultatParametraAnalizeService rezultatParametraAnalizeService;

    @Override
    @Transactional()
    public ResponseEntity<?> reportPrint(Map<String, Object> request) {
        try {
            String laboratorijskiRadniNalogId = request.get("labRadniNalogId").toString();

            LaboratorijskiRadniNalog labRadniNalog =  repository.findByLaboratorijskiRadniNalogId(Long.valueOf(laboratorijskiRadniNalogId));

            if(labRadniNalog == null){
                return ResponseEntity.notFound().build();
            }

            request.put("lbp", labRadniNalog.getLbp().toString());
            request.put("datumivreme", labRadniNalog.getDatumVremeKreiranja());
            request.put("imeiprezime", "Pacijent Pacijent");
            //request.put("", labRadniNalog.getLbzBiohemicar());
            List<RezultatParametraAnalize> rezultati = rezultatParametraAnalizeService.getRezultateParametaraAnalizeByRadniNalog(labRadniNalog);

            List<LaboratoriskiNalazParametar> list = new ArrayList<LaboratoriskiNalazParametar>();

            for (RezultatParametraAnalize parametar: rezultati){
                LaboratoriskiNalazParametar labNalazparam = new LaboratoriskiNalazParametar();
                labNalazparam.setIdParametra(parametar.getParametarAnalize().getParametarAnalizeId());
                labNalazparam.setOcitana_vrednost(parametar.getRezultat());
                labNalazparam.setParametar(parametar.getParametarAnalize().getParametar().getNazivParametra());
                labNalazparam.setJedinica_mere(parametar.getParametarAnalize().getParametar().getJedinicaMere());
                labNalazparam.setMin_ref_vrednost(parametar.getParametarAnalize().getParametar().getDonjaGranica());
                labNalazparam.setMax_ref_vrednost(parametar.getParametarAnalize().getParametar().getGornjaGranica());

                list.add(labNalazparam);

            }
            request.remove("laboratorijskiRadniNalogId");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(list);

            request.put("CollectionBeanParam", itemsJRBean);

            JRDataSource jrDataSource = new JREmptyDataSource();

            InputStream stream = this.getClass().getResourceAsStream("/LabReport.jrxml");

            JasperReport labReport = JasperCompileManager.compileReport(stream);
            JasperPrint labPrint = JasperFillManager.fillReport(labReport, request, jrDataSource);

            //byte[] reportByteArray = JasperExportManager.exportReportToPdf(labPrint);
            byte[] reportByteArray = Base64.getEncoder().encode(JasperExportManager.exportReportToPdf(labPrint));

            labRadniNalog.setLabReport(new SerialBlob(reportByteArray));

            repository.save(labRadniNalog);
            return new ResponseEntity<>(
                    Base64.getEncoder().encode(JasperExportManager.exportReportToPdf(labPrint)),
                    HttpStatus.OK
            );
            //return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
