package raf.si.bolnica.management.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.bolnica.management.entities.Vakcina;
import raf.si.bolnica.management.repositories.AlergenZdravstveniKartonRepository;
import raf.si.bolnica.management.repositories.PacijentRepository;
import raf.si.bolnica.management.repositories.VakcinaRepository;
import raf.si.bolnica.management.entities.Alergen;
import raf.si.bolnica.management.repositories.AlergenRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private VakcinaRepository vakcinaRepository;

    @Autowired
    private AlergenRepository alergenRepository;

    @Autowired
    private AlergenZdravstveniKartonRepository alergenZdravstveniKartonRepository;

    @Autowired
    private PacijentRepository pacijentRepository;


    @Override
    public void run(String... args) throws Exception {
        String[] nazivi = {"PRIORIX", "HIBERIX", "INFLUVAC", "SYNFLORIX", "BCG VAKCINA"};
        String[] opisi = {"Vakcina protiv morbila (malih boginja)", "Kapsulirani antigen hemofilus influence tip B", "Virusne vakcine protiv influence (grip)", "Vakcine protiv pneumokoka", "Vakcine protiv tuberkuloze"};
        String[] tipovi = {"Virusne vakcine", "Bakterijske vakcine", "Virusne vakcine", "Bakterijske vakcine", "Bakterijske vakcine"};
        String[] proizvodjaci = {"GlaxoSmithKline Biologicals S.A., Belgija", "GlaxoSmithKline Biologicals S.A., Belgija", "Abbott Biologicals B.V., Holandija","GlaxoSmithKline Biologicals S.A., Belgija", "Institut za virusologiju, vakcine i serume \"Torlak\", Republika Srbija"};

        for (int i = 0; i < 5; i++) {
            Vakcina vakcina = new Vakcina();
            vakcina.setNaziv(nazivi[i]);
            vakcina.setOpis(opisi[i]);
            vakcina.setProizvodjac(proizvodjaci[i]);
            vakcina.setTip(tipovi[i]);
            vakcinaRepository.save(vakcina);
        }

        List<Alergen> alergens = new ArrayList<Alergen>();
        alergens.add(new Alergen("Mleko"));
        alergens.add(new Alergen("Jaja"));
        alergens.add(new Alergen("Orašasti plodovi"));
        alergens.add(new Alergen("Plodovi mora"));
        alergens.add( new Alergen("Pšenica"));
        alergens.add(new Alergen("Soja"));
        alergens.add(new Alergen("Riba"));
        alergens.add(new Alergen("Penicilin"));
        alergens.add(new Alergen("Cefalosporin"));
        alergens.add(new Alergen("tetraciklin"));
        alergens.add(new Alergen("Karbamazepin"));
        alergens.add(new Alergen("Ibuprofen"));

        for (Alergen alergen:alergens) {
            alergenRepository.save(alergen);
        }

    }
}
