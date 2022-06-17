package com.clickup.service;

import com.clickup.entity.User;
import com.clickup.entity.enums.SystemRoleName;
import com.clickup.payload.ApiResponse;
import com.clickup.payload.RegisterDto;
import com.clickup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new NotFoundException(email));
    }

    public ApiResponse registerUser(RegisterDto registerDto) {

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail){
            return new ApiResponse("bunday foydalanivchi mavjud",false);
        }
        User user = new User(
                registerDto.getFullName(),
                registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()),
                SystemRoleName.SYSTEM_USER
        );
        int code = new Random().nextInt(999999);
        user.setEmailCode(String.valueOf(code).substring(0,4));
        userRepository.save(user);
        sendEmail(registerDto.getEmail(),user.getEmailCode());
        return new ApiResponse("user saqlandi ", true);

    }


    public Boolean sendEmail(String sendingEmail,String emailCode){
        try {
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom("from@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Accountni tasdiqlang");
            mailMessage.setText(emailCode);
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (user.getEmailCode().equals(emailCode)){
                user.setEnabled(true);
                userRepository.save(user);
                return new ApiResponse("Account tasdiqlandi", true);
            }
            return new ApiResponse("kod xato",false);
        }
        return new ApiResponse("bunday user mavjud emas",false);
    }
}
