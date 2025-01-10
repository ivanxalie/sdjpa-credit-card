package guru.springframework.creditcard.domain;

import guru.springframework.creditcard.service.EncryptionService;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class CreditCardJpaCallback {
    private final EncryptionService encryptionService;

    public CreditCardJpaCallback(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PrePersist
    @PreUpdate
    public void beforeInsertOrUpdate(CreditCard card) {
        card.setCreditCardNumber(encryptionService.encrypt(card.getCreditCardNumber()));
    }

    @PostPersist
    @PostLoad
    @PostUpdate
    public void postLoad(CreditCard card) {
        card.setCreditCardNumber(encryptionService.decrypt(card.getCreditCardNumber()));
    }
}
