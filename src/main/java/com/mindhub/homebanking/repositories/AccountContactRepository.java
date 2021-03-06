package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.AccountContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountContactRepository extends JpaRepository<AccountContact, Long> {
}
