package guru.springframework.creditcard.config;

import guru.springframework.creditcard.interceptors.EncryptionInterceptor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

//@Configuration
public class InterceptorRegistration implements HibernatePropertiesCustomizer {
    private final EncryptionInterceptor encryptionInterceptor;

    public InterceptorRegistration(EncryptionInterceptor encryptionInterceptor) {
        this.encryptionInterceptor = encryptionInterceptor;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.INTERCEPTOR, encryptionInterceptor);
    }
}
