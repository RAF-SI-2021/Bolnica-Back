package raf.si.bolnica.management.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;
import raf.si.bolnica.management.service.ScheduledAppointmentService;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class ManagementControllerTest {

    @Mock
    LoggedInUser loggedInUser;

    @InjectMocks
    ManagementController managementController;
    @Mock
    ScheduledAppointmentService appointmentService;

    CreateScheduledAppointmentRequestDTO getCreateRequest() {
        CreateScheduledAppointmentRequestDTO request = new CreateScheduledAppointmentRequestDTO();

        request.setNote("hey drugari");
        request.setAppointmentEmployeeId(1);
        request.setExaminationEmployeeId(2);
        request.setDateAndTimeOfAppointment(new Timestamp(45552053));

        return request;
    }

    UpdateAppointmentStatusDTO getUpdateAppointmentRequest() {
        UpdateAppointmentStatusDTO request = new UpdateAppointmentStatusDTO();

        request.setAppointmentId(2);
        request.setAppointmentStatus("OTKAZANO");
        return request;
    }

    UpdateArrivalStatusDTO getUpdateArrivalRequest() {
        UpdateArrivalStatusDTO request = new UpdateArrivalStatusDTO();

        request.setAppointmentId(1);
        request.setArrivalStatus("OTKAZAO");
        return request;
    }
    @Test
    public void creatingAppointment200() {

        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_VISA_MED_SESTRA");
        CreateScheduledAppointmentRequestDTO request = getCreateRequest();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.setAppointment(request);

        assert(response.getStatusCodeValue() == 200);
    }
    @Test
    public void creatingAppointmentUnauthorized() {

        Set<String> roles = new TreeSet<>();


        CreateScheduledAppointmentRequestDTO request = getCreateRequest();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.setAppointment(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() != 200);
    }

    @Test
    public void creatingAppointmentWithNullValue() {

        Set<String> roles = new TreeSet<>();

        roles.add("");
        CreateScheduledAppointmentRequestDTO request = getCreateRequest();
        request.setDateAndTimeOfAppointment(null);
        lenient().when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.setAppointment(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() != 200);
    }

    @Test
    public void updateAppointment() {

        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_DR_SPEC_ODELJENJA");
        UpdateAppointmentStatusDTO request = getUpdateAppointmentRequest();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.updateAppointmentStatus(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() == 200);
    }
    @Test
    public void updateAppointmentUnauthorized() {

        Set<String> roles = new TreeSet<>();

        UpdateAppointmentStatusDTO request = getUpdateAppointmentRequest();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.updateAppointmentStatus(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() != 200);
    }

    @Test
    public void updateAppointmentWithNullValues() {

        Set<String> roles = new TreeSet<>();

        UpdateAppointmentStatusDTO request = getUpdateAppointmentRequest();
        lenient().when(loggedInUser.getRoles()).thenReturn(roles);
        request.setAppointmentStatus(null);
        ResponseEntity<?> response = managementController.updateAppointmentStatus(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() != 200);
    }

    @Test
    public void updateArrival() {

        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_VISA_MED_SESTRA");
        UpdateArrivalStatusDTO request = getUpdateArrivalRequest();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.updateArrivalStatus(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() == 200);
    }

    @Test
    public void updateArrivalRequestUnauthorized() {

        Set<String> roles = new TreeSet<>();

        roles.add("");
        UpdateArrivalStatusDTO request = getUpdateArrivalRequest();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.updateArrivalStatus(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() != 200);
    }
    @Test
    public void updateArrivalWithNullValues() {

        Set<String> roles = new TreeSet<>();

        UpdateArrivalStatusDTO request = getUpdateArrivalRequest();
        lenient().when(loggedInUser.getRoles()).thenReturn(roles);
        request.setArrivalStatus(null);
        ResponseEntity<?> response = managementController.updateArrivalStatus(request);
        System.out.println(response);
        assert(response.getStatusCodeValue() != 200);
    }
}