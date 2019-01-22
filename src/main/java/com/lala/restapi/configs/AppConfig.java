package com.lala.restapi.configs;

import com.lala.restapi.accounts.Account;
import com.lala.restapi.accounts.AccountRole;
import com.lala.restapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public ApplicationRunner applicationRunner() {
//        return new ApplicationRunner() {
//            @Autowired
//            AccountService accountService;
//
//            @Override
//            public void run(ApplicationArguments args) throws Exception {
//                Account account = Account.builder()
//                        .email("ryan@email.com")
//                        .password("ryan")
//                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
//                        .build();
//                accountService.saveAccounts(account);
//            }
//        };
//    }
}
