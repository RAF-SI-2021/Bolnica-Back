package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;
import raf.si.bolnica.management.repositories.IstorijaBolestiRepository;
import raf.si.bolnica.management.repositories.PacijentRepository;
import raf.si.bolnica.management.repositories.PregledRepository;
import raf.si.bolnica.management.repositories.ZdravstveniKartonRepository;
import raf.si.bolnica.management.requests.CreatePregledReportRequestDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.UUID;

@Service
@Transactional("transactionManager")
public class PregledServiceImpl implements PregledService {

    @Autowired
    PregledRepository pregledRepository;

    @Autowired
    private IstorijaBolestiRepository istorijaBolestiRepository;

    @Autowired
    private ZdravstveniKartonRepository zdravstveniKartonRepository;

    @Autowired
    private PacijentRepository pacijentRepository;


    @Override
    public Pregled savePregled(Pregled pregled) {
        return pregledRepository.save(pregled);
    }

    @Override
    public Pregled createPregledReport(CreatePregledReportRequestDTO requestDTO) {

        Pacijent pacijent = pacijentRepository.findByLbp(UUID.fromString(requestDTO.getLbp()));
        ZdravstveniKarton zdravstveniKarton = zdravstveniKartonRepository.findZdravstveniKartonByPacijent(pacijent);

        Pregled pregled = new Pregled();

        pregled.setZdravstveniKarton(zdravstveniKarton);
        pregled.setZaposleniId(UUID.fromString(requestDTO.getZaposleniId()));
        pregled.setDatumPregleda(new Date(Calendar.getInstance().getTime().getTime()));
        pregled.setDijagnoza(requestDTO.getDijagnoza());
        pregled.setGlavneTegobe(requestDTO.getGlavneTegobe());
        pregled.setLicnaAnamneza(requestDTO.getLicnaAnamneza());
        pregled.setMisljenjePacijenta(requestDTO.getMisljenjePacijenta());
        pregled.setObjektivniNalaz(requestDTO.getObjektivniNalaz());
        pregled.setSavet(requestDTO.getSavet());
        pregled.setPorodicnaAnamneza(requestDTO.getPorodicnaAnamneza());
        pregled.setSadasnjaBolest(requestDTO.getSadasnjaBolest());
        pregled.setPredlozenaTerapija(requestDTO.getPredlozenaTerapija());


        if (pregled.getDijagnoza() != null) {
            IstorijaBolesti istorijaBolesti = new IstorijaBolesti();

            istorijaBolesti.setDijagnoza(pregled.getDijagnoza());
            istorijaBolesti.setRezultatLecenja(requestDTO.getRezultatLecenja());
            istorijaBolesti.setOpisTekucegStanja(requestDTO.getOpisTekucegStanja());
            istorijaBolesti.setPodatakValidanOd(new Date(Calendar.getInstance().getTime().getTime()));
            istorijaBolesti.setPodatakValidanDo(Date.valueOf("9999-12-31"));
            istorijaBolesti.setPodaciValidni(true);

            if (pregled.getSadasnjaBolest() != null) {

                IstorijaBolesti istorijaBolestiAktuelna = istorijaBolestiRepository.
                        getIstorijaBolestiByZdravstveniKartonAndPodaciValidni(pregled.getZdravstveniKarton(), true);
                istorijaBolestiAktuelna.setPodatakValidanDo(new Date(Calendar.getInstance().getTime().getTime()));
                istorijaBolestiAktuelna.setPodaciValidni(false);
                istorijaBolestiRepository.save(istorijaBolestiAktuelna);

                istorijaBolesti.setIndikatorPoverljivosti(istorijaBolestiAktuelna.getIndikatorPoverljivosti());
                istorijaBolesti.setDatumPocetkaZdravstvenogProblema(istorijaBolestiAktuelna.getDatumPocetkaZdravstvenogProblema());
                if (requestDTO.getRezultatLecenja() != RezultatLecenja.U_TOKU
                        && requestDTO.getRezultatLecenja() != null) {
                    istorijaBolesti.setDatumZavrsetkaZdravstvenogProblema(new Date(Calendar.getInstance().getTime().getTime()));
                }

            } else {
                istorijaBolesti.setIndikatorPoverljivosti(pregled.getIndikatorPoverljivosti());
                istorijaBolesti.setDatumPocetkaZdravstvenogProblema(Date.valueOf(LocalDate.now()));
            }

            istorijaBolestiRepository.save(istorijaBolesti);
        }

        return pregled;
    }
}
