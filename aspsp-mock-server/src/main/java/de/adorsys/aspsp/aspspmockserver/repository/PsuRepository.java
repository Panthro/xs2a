package de.adorsys.aspsp.aspspmockserver.repository;

import de.adorsys.aspsp.xs2a.spi.domain.Psu;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile({"mongo", "fongo"})
public interface PsuRepository extends MongoRepository<Psu, String> {

    List<Psu> findPsuByAccountDetailsList_Iban(String iban);

    Optional<Psu> findPsuByAccountDetailsList_Id(String accountId);
}
