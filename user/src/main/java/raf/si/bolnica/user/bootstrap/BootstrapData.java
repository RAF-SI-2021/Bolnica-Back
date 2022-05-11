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

        odeljenje = odeljenjeRepository.save(odeljenje);

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


        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(BCrypt.hashpw("superadmin", BCrypt.gensalt()));
        user.setName("Super");
        user.setSurname("Admin");
        user.setRoles(roles);
        user.setOdeljenje(odeljenje);

        //admin user fields
        user.setLbz(UUID.randomUUID());
        user.setName("admin");
        user.setSurname("adminic");
        user.setDatumRodjenja(new Date(System.currentTimeMillis()));
        user.setPol("Muski");
        user.setJmbg("123456789");
        user.setAdresaStanovanja("adresa 1");
        user.setMestoStanovanja("SRBIJA");
        user.setTitula("titula");
        user.setKorisnickoIme("superadmin");
        user.setZanimanje("zanimanje");
        user.setKontaktTelefon("+381 69312321");



        userRepository.save(user);
    }
}