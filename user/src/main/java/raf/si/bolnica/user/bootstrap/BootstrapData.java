package raf.si.bolnica.user.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.repositories.OdeljenjeRepository;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.repositories.UserRepository;
import raf.si.bolnica.user.repositories.ZdravstvenaUstanovaRepository;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OdeljenjeRepository odeljenjeRepository;

    @Autowired
    private ZdravstvenaUstanovaRepository zdravstvenaUstanovaRepository;

    @Override
    public void run(String... args) throws Exception {
        ZdravstvenaUstanova zdravstvenaUstanova = new ZdravstvenaUstanova();
        zdravstvenaUstanova.setAdresa("Heroja Milana Tepića 1, Beograd");
        zdravstvenaUstanova.setDatumOsnivanja(new Date(Calendar.getInstance().getTime().getTime()));
        zdravstvenaUstanova.setDelatnost("Ginekologija i akušerstvo");
        zdravstvenaUstanova.setMesto("Beograd");
        zdravstvenaUstanova.setNaziv("Kliničko-bolnički centar \"Dragiša Mišović\"");
        zdravstvenaUstanova.setPoslovniBrojBolnice(1234);
        zdravstvenaUstanova.setSkracenNaziv("KBC Dragiša Mišović");

        zdravstvenaUstanova = zdravstvenaUstanovaRepository.save(zdravstvenaUstanova);

        Odeljenje odeljenje = new Odeljenje();
        odeljenje.setNaziv("Hirurgija");
        odeljenje.setBolnica(zdravstvenaUstanova);
        odeljenje.setPoslovniBrojOdeljenja(12345);

        Odeljenje odeljenjeLaboratorija = new Odeljenje();
        odeljenjeLaboratorija.setNaziv("Laboratorija");
        odeljenjeLaboratorija.setBolnica(zdravstvenaUstanova);
        odeljenjeLaboratorija.setPoslovniBrojOdeljenja(12111);

        Odeljenje odeljenjeDijagnostika = new Odeljenje();
        odeljenjeDijagnostika.setNaziv("Dijagnostika");
        odeljenjeDijagnostika.setBolnica(zdravstvenaUstanova);
        odeljenjeDijagnostika.setPoslovniBrojOdeljenja(12315);

        Odeljenje odeljenjePedijatrija = new Odeljenje();
        odeljenjePedijatrija.setNaziv("Pedijatrija");
        odeljenjePedijatrija.setBolnica(zdravstvenaUstanova);
        odeljenjePedijatrija.setPoslovniBrojOdeljenja(11111);

        Odeljenje odeljenjeToksikologija = new Odeljenje();
        odeljenjeToksikologija.setNaziv("Toksikologija");
        odeljenjeToksikologija.setBolnica(zdravstvenaUstanova);
        odeljenjeToksikologija.setPoslovniBrojOdeljenja(22222);

        Odeljenje odeljenjeGinekologija = new Odeljenje();
        odeljenjeGinekologija.setNaziv("Ginekologija");
        odeljenjeGinekologija.setBolnica(zdravstvenaUstanova);
        odeljenjeGinekologija.setPoslovniBrojOdeljenja(33333);

        Odeljenje odeljenjeNeurologija = new Odeljenje();
        odeljenjeNeurologija.setNaziv("Neurologija");
        odeljenjeNeurologija.setBolnica(zdravstvenaUstanova);
        odeljenjeNeurologija.setPoslovniBrojOdeljenja(44444);

        Odeljenje odeljenjeNeurohirurgija = new Odeljenje();
        odeljenjeNeurohirurgija.setNaziv("Neurohirurgija");
        odeljenjeNeurohirurgija.setBolnica(zdravstvenaUstanova);
        odeljenjeNeurohirurgija.setPoslovniBrojOdeljenja(55555);

        Odeljenje odeljenjeKardiologija = new Odeljenje();
        odeljenjeKardiologija.setNaziv("Kardiologija");
        odeljenjeKardiologija.setBolnica(zdravstvenaUstanova);
        odeljenjeKardiologija.setPoslovniBrojOdeljenja(66666);

        odeljenje = odeljenjeRepository.save(odeljenje);
        odeljenjeRepository.save(odeljenjeLaboratorija);
        odeljenjeRepository.save(odeljenjeDijagnostika);
        odeljenjeRepository.save(odeljenjePedijatrija);
        odeljenjeRepository.save(odeljenjeToksikologija);
        odeljenjeRepository.save(odeljenjeGinekologija);
        odeljenjeRepository.save(odeljenjeNeurologija);
        odeljenjeRepository.save(odeljenjeNeurohirurgija);
        odeljenjeRepository.save(odeljenjeKardiologija);

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        Role drSpecOdeljenjaRole = new Role();
        drSpecOdeljenjaRole.setName("ROLE_DR_SPEC_ODELJENJA");

        Role drSpecRole = new Role();
        drSpecRole.setName("ROLE_DR_SPEC");

        Role drSpecPovRole = new Role();
        drSpecPovRole.setName("ROLE_DR_SPEC_POV");

        Role visaMedSestraRole = new Role();
        visaMedSestraRole.setName("ROLE_VISA_MED_SESTRA");

        Role medSestraRole = new Role();
        medSestraRole.setName("ROLE_MED_SESTRA");

        Role visiLabaratorijskiTehnicarRole = new Role();
        visiLabaratorijskiTehnicarRole.setName("ROLE_VISI_LABORATORIJSKI_TEHNICAR");

        Role labaratorijskiTehnicarRole = new Role();
        labaratorijskiTehnicarRole.setName("ROLE_LABORATORIJSKI_TEHNICAR");

        Role medicinskiBiohemicarRole = new Role();
        medicinskiBiohemicarRole.setName("ROLE_MEDICINSKI_BIOHEMICAR");

        Role specijalistaMedicinskeBiohemijeRole = new Role();
        specijalistaMedicinskeBiohemijeRole.setName("ROLE_SPECIJALISTA_MEDICINSKE_BIOHEMIJE");

        Role recepcioner = new Role();
        recepcioner.setName("ROLE_RECEPCIONER");


        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.save(adminRole));
        roles.add(roleRepository.save(drSpecOdeljenjaRole));
        roles.add(roleRepository.save(drSpecRole));
        roles.add(roleRepository.save(drSpecPovRole));
        roles.add(roleRepository.save(visaMedSestraRole));
        roles.add(roleRepository.save(medSestraRole));
        roles.add(roleRepository.save(visiLabaratorijskiTehnicarRole));
        roles.add(roleRepository.save(labaratorijskiTehnicarRole));
        roles.add(roleRepository.save(medicinskiBiohemicarRole));
        roles.add(roleRepository.save(specijalistaMedicinskeBiohemijeRole));
        roles.add(roleRepository.save(recepcioner));


        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(BCrypt.hashpw("superadmin", BCrypt.gensalt()));
        user.setName("Super");
        user.setSurname("Admin");
        user.setRoles(roles);
        user.setOdeljenje(odeljenje);

        //admin user fields
        user.setLbz(UUID.fromString("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"));
        user.setName("admin");
        user.setSurname("adminic");
        user.setDatumRodjenja(new Date(System.currentTimeMillis()));
        user.setPol("Muski");
        user.setJmbg("123456789");
        user.setAdresaStanovanja("adresa 1");
        user.setMestoStanovanja("SRBIJA");
        user.setTitula("Dr. sci. med");
        user.setKorisnickoIme("superadmin");
        user.setZanimanje("Spec. endrokrinolog");
        user.setKontaktTelefon("+381 69312321");


        userRepository.save(user);
    }
}