package com.lala.restapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountsRepository accountsRepository;

    @Test
    public void findByUsername() {
        // Given
        String password = "ryan";
        String username = "ryan@email.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .rolse(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountsRepository.save(account);

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getUsername()).isEqualTo(username);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsernameFail() {
        String username = "ryan@email.com";
        accountService.loadUserByUsername(username);
    }
}