package guru.springframework.creditcard.config;

import guru.springframework.creditcard.listeners.PostLoadListener;
import guru.springframework.creditcard.listeners.PreInsertListener;
import guru.springframework.creditcard.listeners.PreUpdateListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
public class ListenersRegistration {

    @Autowired
    public void configureListeners(
            LocalContainerEntityManagerFactoryBean factoryBean,
            PostLoadListener postLoadListener,
            PreInsertListener preInsertListener,
            PreUpdateListener preUpdateListener) {
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) factoryBean.getNativeEntityManagerFactory();
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.appendListeners(EventType.PRE_INSERT, preInsertListener);
        registry.appendListeners(EventType.PRE_UPDATE, preUpdateListener);
        registry.appendListeners(EventType.POST_LOAD, postLoadListener);
    }
}
