package kma.topic8.springrestsample;

import org.springframework.stereotype.Service;

import kma.topic8.springrestsample.dto.LoginDto;
import kma.topic8.springrestsample.dto.LoginResponseDto;

@Service
public class UserService {

    public LoginResponseDto doLogin(final LoginDto loginDto) {
        System.out.println("inside service");
        return LoginResponseDto.of(loginDto.getLogin(), "success");
    }

}
