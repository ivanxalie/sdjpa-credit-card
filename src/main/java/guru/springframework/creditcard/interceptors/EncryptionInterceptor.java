package guru.springframework.creditcard.interceptors;

import guru.springframework.creditcard.service.EncryptionService;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class EncryptionInterceptor extends EmptyInterceptor {
    private final EncryptionService encryptionService;

    public EncryptionInterceptor(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onSave(entity, id, processFields(entity, state, propertyNames, "onSave"), propertyNames, types);
    }

    private Object[] processFields(Object entity, Object[] state, String[] propertyNames, String type) {
        List<String> fields = getAnnotationFields(entity);

        for (String field : fields) {
            for (int i = 0; i < propertyNames.length; i++) {
                if (propertyNames[i].equals(field)) {
                    if ("onSave".equals(type) || "onFlushDirty".equals(type))
                        state[i] = encryptionService.encrypt(state[i].toString());
                    else if ("onLoad".equals(type))
                        state[i] = encryptionService.decrypt(state[i].toString());
                }
            }
        }


        return state;
    }

    private List<String> getAnnotationFields(Object entity) {
        List<String> fields = new ArrayList<>();

        for (Field field : entity.getClass().getDeclaredFields())
            if (field.isAnnotationPresent(EncryptedString.class))
                fields.add(field.getName());

        return fields;
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onLoad(entity, id, processFields(entity, state, propertyNames, "onLoad"), propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return super.onFlushDirty(entity, id, processFields(entity, currentState, propertyNames, "onFlushDirty"),
                previousState, propertyNames, types);
    }
}
