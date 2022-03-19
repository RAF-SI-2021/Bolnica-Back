package raf.si.bolnica.user.service;

import raf.si.bolnica.user.requests.LoginRequestDTO;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    String login(LoginRequestDTO requestDTO, HttpServletRequest request);
}
