package raf.si.bolnica.user.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.repositories.UserRepository;

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

        Role drSpecOdeljenjaRole = new Role();
        drSpecOdeljenjaRole.setName("ROLE_DR_SPEC_ODELJENJA");

        Role drSpecRole = new Role();
        drSpecRole.setName("ROLE_DR_SPEC");

        Role drSpecPovRole = new Role();
        drSpecPovRole.setName("ROLE_DR_SPEC_POV");

        Role visaMedSestraRole = new Role();
        visaMedSestraRole.setName("ROLE_VISA_MED_SESTRA");

        Role MedSestraRole = new Role();
        MedSestraRole.setName("ROLE_MED_SESTRA");


        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.save(adminRole));
        roles.add(roleRepository.save(drSpecOdeljenjaRole));
        roles.add(roleRepository.save(drSpecRole));
        roles.add(roleRepository.save(drSpecPovRole));
        roles.add(roleRepository.save(visaMedSestraRole));
        roles.add(roleRepository.save(MedSestraRole));


        User user = new User();
        user.setEmail("superadmin");
        user.setPassword(BCrypt.hashpw("superadmin", BCrypt.gensalt()));
        user.setName("Super");
        user.setSurname("Admin");
        user.setRoles(roles);

        userRepository.save(user);
    }
}