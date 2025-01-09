package guru.springframework.creditcard.repositories;

import guru.springframework.creditcard.domain.CreditCard;
import guru.springframework.creditcard.service.EncryptionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditCardRepositoryTest {
    private static final String CREDIT_CARD = "12345678900000";
    @Autowired
    private CreditCardRepository repository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JdbcTemplate template;

    @AfterEach
    void setUp() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    void testSaveAndStoreCreditCard() {
        CreditCard card = new CreditCard();
        card.setCreditCardNumber(CREDIT_CARD);
        card.setCvv("123");
        card.setExpirationDate("12/2028");

        CreditCard saved = repository.saveAndFlush(card);

        System.out.printf("Getting CC from database: %s%n", saved.getCreditCardNumber());

        System.out.println("CC At Rest");
        System.out.printf("CC Encrypted: %s%n", encryptionService.encrypt(CREDIT_CARD));

        Map<String, Object> rows = template.queryForMap("select * from credit_card where id = " + saved.getId());

        String cardValue = (String) rows.get("credit_card_number");

        assertThat(saved.getCreditCardNumber()).isNotEqualTo(cardValue);
        assertThat(cardValue).isEqualTo(encryptionService.encrypt(CREDIT_CARD));

        CreditCard fetched = repository.findById(saved.getId()).orElseThrow();

        assertThat(saved.getCreditCardNumber()).isEqualTo(fetched.getCreditCardNumber());
    }
}