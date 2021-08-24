package se.nackademin.java20.lab1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.nackademin.java20.lab1.application.PersonalFinanceService;
import se.nackademin.java20.lab1.domain.AccountRepository;
import se.nackademin.java20.lab1.persistance.AccountRepositoryHibernate;

import javax.persistence.EntityManager;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public AccountRepository accountRepository(EntityManager em) {
        return new AccountRepositoryHibernate(em);
    }

    @Bean
    public PersonalFinanceService personalFinanceService(AccountRepository accountRepository) {
        return new PersonalFinanceService(accountRepository);
    }
}
