package raf.si.bolnica.management.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.bolnica.management.entities.Alergen;
import raf.si.bolnica.management.repositories.AlergenRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private AlergenRepository alergenRepository;

    @Override
    public void run(String... args) throws Exception {

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

        for(Alergen alergen:alergens){
            alergenRepository.save(alergen);
        }
        alergenRepository.save(new Alergen("Mleko"));


    }
}
