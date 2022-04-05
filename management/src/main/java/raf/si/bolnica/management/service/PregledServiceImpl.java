package raf.si.bolnica.management.service;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;
import raf.si.bolnica.management.repositories.IstorijaBolestiRepository;
import raf.si.bolnica.management.repositories.PacijentRepository;
import raf.si.bolnica.management.repositories.PregledRepository;
import raf.si.bolnica.management.repositories.ZdravstveniKartonRepository;
import raf.si.bolnica.management.requests.CreatePregledRequestDTO;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class PregledServiceImpl implements PregledService{

    @Autowired
    private PregledRepository pregledRepository;

    @Autowired
    private IstorijaBolestiRepository istorijaBolestiRepository;

    @Autowired
    private ZdravstveniKartonRepository zdravstveniKartonRepository;

    @Autowired
    private PacijentRepository pacijentRepository;

    @Override
    public Pregled getPregledById(Long id) {
        return pregledRepository.findById(id).orElse(null);

    }

    @Override
    public Pregled createPregled(CreatePregledRequestDTO requestDTO) {

        Pacijent pacijent = pacijentRepository.getPacijentByLbp(requestDTO.getLbp());
        ZdravstveniKarton zdravstveniKarton = zdravstveniKartonRepository.findZdravstveniKartonByPacijent(pacijent);

        Pregled pregled = new Pregled();

        pregled.setZdravstveniKarton(zdravstveniKarton);
        pregled.setZaposleniId(requestDTO.getZaposleniId());
        pregled.setDatumPregleda(Date.valueOf(LocalDate.now()));
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
            istorijaBolesti.setOpisTekucegStanja(requestDTO.getOpisTekucgStanja());
            istorijaBolesti.setPodatakValidanOd(Date.valueOf(LocalDate.now()));
            istorijaBolesti.setPodatakValidanDo(Date.valueOf("31-12-9999"));
            istorijaBolesti.setPodaciValidni(true);

            if (pregled.getSadasnjaBolest() != null) {

                IstorijaBolesti istorijaBolestiAktuelna = istorijaBolestiRepository.
                        getIstorijaBolestiByZdravstveniKartonAndPodaciValidni(pregled.getZdravstveniKarton(), true);
                istorijaBolestiAktuelna.setPodatakValidanDo(Date.valueOf(LocalDate.now()));
                istorijaBolestiAktuelna.setPodaciValidni(false);
                istorijaBolestiRepository.save(istorijaBolestiAktuelna);

                istorijaBolesti.setIndikatorPoverljivosti(istorijaBolestiAktuelna.getIndikatorPoverljivosti());
                istorijaBolesti.setDatumPocetkaZdravstvenogProblema(istorijaBolestiAktuelna.getDatumPocetkaZdravstvenogProblema());
                if (requestDTO.getRezultatLecenja() != RezultatLecenja.U_TOKU
                        && requestDTO.getRezultatLecenja() != null) {
                    istorijaBolesti.setDatumZavrsetkaZdravstvenogProblema(Date.valueOf(LocalDate.now()));
                }

            } else {
                istorijaBolesti.setIndikatorPoverljivosti(pregled.getIndikatorPoverljivosti());
                istorijaBolesti.setDatumPocetkaZdravstvenogProblema(Date.valueOf(LocalDate.now()));
            }

            istorijaBolestiRepository.save(istorijaBolesti);
        }

        return pregledRepository.save(pregled);
    }
}
