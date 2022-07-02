package raf.si.bolnica.management.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.entities.enums.*;
import raf.si.bolnica.management.repositories.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private VakcinaRepository vakcinaRepository;

    @Autowired
    private VakcinacijaRepository vakcinacijaRepository;

    @Autowired
    private AlergenRepository alergenRepository;

    @Autowired
    private PacijentRepository pacijentRepository;

    @Autowired
    private ZdravstveniKartonRepository zdravstveniKartonRepository;

    @Autowired
    private HospitalizacijaRepository hospitalizacijaRepository;

    @Autowired
    private BolnickaSobaRepository bolnickaSobaRepository;

    @Autowired
    private PosetPacijentuRepository posetPacijentuRepository;


    @Override
    public void run(String... args) throws Exception {
        String[] nazivi = {"PRIORIX", "HIBERIX", "INFLUVAC", "SYNFLORIX", "BCG VAKCINA"};
        String[] opisi = {"Vakcina protiv morbila (malih boginja)", "Kapsulirani antigen hemofilus influence tip B", "Virusne vakcine protiv influence (grip)", "Vakcine protiv pneumokoka", "Vakcine protiv tuberkuloze"};
        String[] tipovi = {"Virusne vakcine", "Bakterijske vakcine", "Virusne vakcine", "Bakterijske vakcine", "Bakterijske vakcine"};
        String[] proizvodjaci = {"GlaxoSmithKline Biologicals S.A., Belgija", "GlaxoSmithKline Biologicals S.A., Belgija", "Abbott Biologicals B.V., Holandija", "GlaxoSmithKline Biologicals S.A., Belgija", "Institut za virusologiju, vakcine i serume \"Torlak\", Republika Srbija"};

        List<Vakcina> vakcine = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Vakcina vakcina = new Vakcina();
            vakcina.setNaziv(nazivi[i]);
            vakcina.setOpis(opisi[i]);
            vakcina.setProizvodjac(proizvodjaci[i]);
            vakcina.setTip(tipovi[i]);
            vakcine.add(vakcina);
            vakcinaRepository.save(vakcina);
        }

        List<Alergen> alergens = new ArrayList<>();
        alergens.add(new Alergen("Mleko"));
        alergens.add(new Alergen("Jaja"));
        alergens.add(new Alergen("Orašasti plodovi"));
        alergens.add(new Alergen("Plodovi mora"));
        alergens.add(new Alergen("Pšenica"));
        alergens.add(new Alergen("Soja"));
        alergens.add(new Alergen("Riba"));
        alergens.add(new Alergen("Penicilin"));
        alergens.add(new Alergen("Cefalosporin"));
        alergens.add(new Alergen("Tetraciklin"));
        alergens.add(new Alergen("Karbamazepin"));
        alergens.add(new Alergen("Ibuprofen"));

        for (Alergen alergen : alergens) {
            alergenRepository.save(alergen);
        }

        Pacijent pacijent = new Pacijent();
        pacijent.setJmbg("1234567898765");
        pacijent.setLbp(UUID.fromString("237e9877-e79b-12d4-a765-321741963000"));
        pacijent.setIme("Pacijent");
        pacijent.setImeRoditelja("Pacijent");
        pacijent.setPrezime("Pacijent");
        pacijent.setPol(Pol.MUSKI);
        pacijent.setDatumRodjenja(Date.valueOf("1990-01-01"));
        pacijent.setMestoRodjenja("Loznica");
        pacijent.setZanimanje("Programer");
        pacijent.setZemljaDrzavljanstva(CountryCode.SRB);
        pacijent.setZemljaStanovanja(CountryCode.SRB);
        pacijent.setEmail("pacijent@gmail.com");
        pacijent.setKontaktTelefon("+381 632309642");
        pacijent.setAdresa("Makedonska");
        pacijent.setBracniStatus(BracniStatus.U_BRAKU);
        pacijent.setBrojDece(2);
        pacijent.setImeStaratelj("Staratelj");
        pacijent.setJmbgStaratelj("1274567898765");
        pacijent.setMestoStanovanja("Beograd");
        pacijent.setPorodicniStatus(PorodicniStatus.OBA_RODITELJA);
        pacijent.setStepenStrucneSpreme(StrucnaSprema.VISOKO);
        pacijent.setZanimanje("Moler");
        pacijentRepository.save(pacijent);

        PosetaPacijentu posetaPacijentu = createPosetaPacijentu(pacijent);
        posetPacijentuRepository.save(posetaPacijentu);

        PosetaPacijentu posetaPacijentu2 = createPosetaPacijentu(pacijent);
        posetPacijentuRepository.save(posetaPacijentu2);

        ZdravstveniKarton zdravstveniKarton = new ZdravstveniKarton();
        zdravstveniKarton.setRhFaktor(RhFaktor.MINUS);
        zdravstveniKarton.setKrvnaGrupa(KrvnaGrupa.AB);
        zdravstveniKarton.setPacijent(pacijent);
        zdravstveniKarton.setDatumRegistracije(Date.valueOf("2010-01-01"));
        zdravstveniKartonRepository.save(zdravstveniKarton);
        Set<AlergenZdravstveniKarton> alergenZdravstveniKartonSet = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            AlergenZdravstveniKarton alergenZdravstveniKarton = new AlergenZdravstveniKarton();
            alergenZdravstveniKarton.setAlergen(alergens.get(i));
            alergenZdravstveniKarton.setZdravstveniKarton(zdravstveniKarton);
            alergenZdravstveniKarton.setObrisan(false);
            alergenZdravstveniKartonSet.add(alergenZdravstveniKarton);
        }
        zdravstveniKarton.setAlergenZdravstveniKarton(alergenZdravstveniKartonSet);
        Set<Vakcinacija> vakcinacije = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            Vakcinacija vakcinacija = new Vakcinacija();
            vakcinacija.setVakcina(vakcine.get(i));
            vakcinacija.setDatumVakcinacije(new Date(System.currentTimeMillis()));
            vakcinacija.setZdravstveniKarton(zdravstveniKarton);
            vakcinacijaRepository.save(vakcinacija);
        }
        zdravstveniKarton.setVakcinacije(vakcinacije);
        zdravstveniKartonRepository.save(zdravstveniKarton);

        BolnickaSoba bolnickaSoba = new BolnickaSoba();
        bolnickaSoba.setOdeljenjeId(1);
        bolnickaSoba.setBrojSobe(1);
        bolnickaSoba.setKapacitet(5);
        bolnickaSoba.setPopunjenost(4);
        bolnickaSoba.setNazivSobe("Soba 1");
        bolnickaSoba.setOpis("Ovo je opis Sobe 1");
        bolnickaSobaRepository.save(bolnickaSoba);

        BolnickaSoba bolnickaSoba2 = new BolnickaSoba();
        bolnickaSoba2.setOdeljenjeId(2);
        bolnickaSoba2.setBrojSobe(2);
        bolnickaSoba2.setKapacitet(10);
        bolnickaSoba2.setPopunjenost(0);
        bolnickaSoba2.setNazivSobe("Soba 2");
        bolnickaSoba2.setOpis("Ovo je opis Sobe 2");
        bolnickaSobaRepository.save(bolnickaSoba2);

    }

    private PosetaPacijentu createPosetaPacijentu(Pacijent pacijent) {
        PosetaPacijentu posetaPacijentu = new PosetaPacijentu();
        posetaPacijentu.setLbpPacijenta(pacijent.getLbp());
        posetaPacijentu.setImePosetioca(pacijent.getIme());
        posetaPacijentu.setJmbgPosetioca(pacijent.getJmbg());
        posetaPacijentu.setNapomena("Ovo je napomena za Pacijenta");
        posetaPacijentu.setPrezimePosetioca(pacijent.getPrezime());
        posetaPacijentu.setLbzRegistratora(pacijent.getLbp());
        posetaPacijentu.setDatumVreme(new Timestamp(System.currentTimeMillis()));
        return posetaPacijentu;
    }
}
