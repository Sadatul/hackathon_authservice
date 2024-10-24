package com.sadi.hackathon_authservice;

import com.sadi.hackathon_authservice.utils.CodeGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class HackathonAuthserviceApplicationTests {

    @Test
    void contextLoads() {
        String code = CodeGenerator.generateOtp();
        Assertions.assertThat(code).isNotBlank();
    }

}
