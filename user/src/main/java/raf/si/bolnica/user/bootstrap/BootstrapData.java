package raf.si.bolnica.user.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.repositories.UserRepository;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.save(adminRole));

        User user = new User();
        user.setEmail("superadmin");
        user.setPassword(BCrypt.hashpw("superadmin", BCrypt.gensalt()));
        user.setName("Super");
        user.setSurname("Admin");
        user.setRoles(roles);

        //admin user fields
        user.setLicniBrojZaposlenog(123);
        user.setName("admin");
        user.setSurname("adminic");
        user.setDatumRodjenja(new Date(System.currentTimeMillis()));
        user.setPol("Muski");
        user.setJmbg("123456789");
        user.setAdresaStanovanja("adresa 1");
        user.setMestoStanovanja("SRBIJA");
        user.setTitula("titula");
        user.setKorisnickoIme("superadmin");
        



        userRepository.save(user);
    }
}
