package raf.si.bolnica.laboratory.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.bolnica.laboratory.entities.*;
import raf.si.bolnica.laboratory.entities.enums.TipVrednosti;
import raf.si.bolnica.laboratory.repositories.*;

import java.sql.Timestamp;
import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    LaboratorijskaAnalizaRepository laboratorijskaAnalizaRepository;

    @Autowired
    ParametarRepository parametarRepository;

    @Autowired
    ParametarAnalizeRepository parametarAnalizeRepository;

    @Autowired
    LaboratorijskiRadniNalogRepository laboratorijskiRadniNalogRepository;

    @Autowired
    RezultatParametraAnalizeRepository rezultatParametraAnalizeRepository;

    @Override
    public void run(String... args) {

        LaboratorijskiRadniNalog laboratorijskiRadniNalog = new LaboratorijskiRadniNalog();
        laboratorijskiRadniNalog.setLaboratorijskiRadniNalogId(1);
        laboratorijskiRadniNalog.setLbp(UUID.fromString("237e9877-e79b-12d4-a765-321741963000"));
        laboratorijskiRadniNalog.setDatumVremeKreiranja(new Timestamp(System.currentTimeMillis()));

        laboratorijskiRadniNalogRepository.save(laboratorijskiRadniNalog);



        String[] analizaNaziv = {"Glukoza","Holesterol","Triglicerid","Urea","Kreatinin","Mokraćna kiselina","Bilirubin","Alanin aminotransferaza","Aspartat aminotransferaza","Kreatinin kinaza","Tireostimulirajući hormon","Slobodni T4","C-reaktivni protein","Leukociti","Eritrociti","Trombociti","Hemoglobin","Kompletna krvna slika","Sedimentacija","SARS CoV-2 antigen","Urin"};
        String[] analizaSkracenica = {"GLU","HOL","TRIG","URE","KREAT","MK","BILIR-uk","ALT","AST","CK","TSH","FT4","CRP","WBC","RBC","PLT","Hb","KKS","SE","SARS CoV-2 antigen","URIN"};
        int brojAnaliza = 21;
        List<LaboratorijskaAnaliza> analize = new ArrayList<>();
        for(int i=0;i<brojAnaliza;i++) {
            LaboratorijskaAnaliza analiza = new LaboratorijskaAnaliza();
            analiza.setNazivAnalize(analizaNaziv[i]);
            analiza.setSkracenica(analizaSkracenica[i]);
            laboratorijskaAnalizaRepository.save(analiza);
            analize.add(analiza);
        }

        String[] parametarNaziv = {"Glukoza","Holesterol","Trigliceridi","Urea","Kreatinin","Mokraćna kiselina","Bilirubin","Alanin aminotransferaza","Aspartat aminotransferaza","Kreatinin kinaza","Tireostimulirajući hormon","Slobotni T4","C-reaktivni protein","Leukociti","Eritrociti","Trombociti","Hemoglobin","Limfociti","Monociti","Granulociti","Limfociti","Monociti","Granulociti","Hematokrit","MCV","MCH","MCHC","RDW","MPV","PDW","PCV","Sedimentacija","SARS CoV-2 antigen","Izgled","Boja","Reakcija, pH","Sepcifična težina","Proteini","Glukoza","Ketoni","Bilirubin","Urobilinogen","Hemoglobin","Nitriti","Askorbinska kiselina","Eritrociti","Leukociti","Pločaste epitelne ćelije", "Male epitelne ćelije","Cilindri","Kristali","Bakterije"};
        String[] jedinicaMere = {"mmol/L","mmol/L","mmol/L","mmol/L","umol/L","umol/L","umol/L","U/L","U/L","U/L","mlU/L","pmol/L","mg/L","10*9/L","10*12/L","10*9/L","g/L","10*9/L","10*9/L","10*9/L","%","%","%","L/L","fL","pg","g/L","%","fL","%","cL/L","mm/1.h","","","","","","","","","","","","","","sveža/hpf","/hpf","/hpf","","","",""};
        TipVrednosti[] tipVrednosti = {TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.NUMERICKA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA,TipVrednosti.TEKSTUALNA};
        Double[] donjaGranica = {3.90,-1.0,-1.0,2.10,53.0,150.0,5.1,10.0,10.0,24.0,0.4,9.9,-1.0,4.0,3.80,140.0,117.0,1.0,0.2,2.0,20.0,4.0,45.0,0.340,81.0,27.0,315.0,10.0,6.5,10.0,0.125,-1.0,-1.0,-1.0,-1.0,4.7,1.003,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0};
        Double[] gornjaGranica = {6.10,5.20,1.70,7.10,97.0,400.0,20.5,40.0,40.0,170.0,4.0,22.7,5.0,10.0,5.60,440.0,155.0,4.0,1.0,7.0,45.0,10.0,70.0,0.500,100.0,34.0,360.0,16.0,11.0,18.0,0.350,20.0,-1.0,-1.0,-1.0,7.8,1.035,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,2.0,5.0,4.0,-1.0,-1.0,-1.0,-1.0};
        int brojParametara = 52;
        List<Parametar> parametri = new ArrayList<>();
        for(int i=0;i<brojParametara;i++) {
            Parametar parametar = new Parametar();
            parametar.setNazivParametra(parametarNaziv[i]);
            parametar.setTipVrednosti(tipVrednosti[i]);
            if(!jedinicaMere[i].equals("")) parametar.setJedinicaMere(jedinicaMere[i]);
            if(!(donjaGranica[i]<0)) parametar.setDonjaGranica(donjaGranica[i]);
            if(!(gornjaGranica[i]<0)) parametar.setGornjaGranica(gornjaGranica[i]);
            parametarRepository.save(parametar);
            parametri.add(parametar);
        }

        HashMap<Integer,Integer> lowerBound = new HashMap<>();
        lowerBound.put(18,14);
        lowerBound.put(19,32);
        lowerBound.put(20,33);
        lowerBound.put(21,34);

        HashMap<Integer,Integer> upperBound = new HashMap<>();
        upperBound.put(18,31);
        upperBound.put(19,32);
        upperBound.put(20,33);
        upperBound.put(21,52);

        for(int i=0;i<brojAnaliza;i++) {
            int l=i,r=i;
            if(lowerBound.keySet().contains(i+1)) {
                l=lowerBound.get(i+1)-1;
            }
            if(upperBound.keySet().contains(i+1)) {
                r=upperBound.get(i+1)-1;
            }
            ParametarAnalize parametarAnalize = new ParametarAnalize();
            for(int j=l;j<=r;j++) {
                parametarAnalize = new ParametarAnalize();
                parametarAnalize.setLaboratorijskaAnaliza(analize.get(i));
                parametarAnalize.setParametar(parametri.get(j));
                parametarAnalizeRepository.save(parametarAnalize);
            }

            RezultatParametraAnalize rezultatParametraAnalize = new RezultatParametraAnalize();
            rezultatParametraAnalize.setLaboratorijskiRadniNalog(laboratorijskiRadniNalog);
            rezultatParametraAnalize.setRezultat("2.3");
            rezultatParametraAnalize.setParametarAnalize(parametarAnalize);
            rezultatParametraAnalize.setDatumVreme(new Timestamp(System.currentTimeMillis()-1000));
            RezultatParametraAnalizeKey key = new RezultatParametraAnalizeKey();
            key.setParametarAnalizeId(parametarAnalize.getParametarAnalizeId());
            key.setLaboratorijskiRadniNalogId(1);
            rezultatParametraAnalize.setId(key);
            rezultatParametraAnalizeRepository.save(rezultatParametraAnalize);
        }







        System.out.println("Laboratory service successfully running..");
    }
}
