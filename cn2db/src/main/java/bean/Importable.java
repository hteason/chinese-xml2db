package bean;

import java.util.UUID;

public interface Importable {
    String getId();
    default String uuid(){
        return UUID.randomUUID().toString();
    }
    default void setParentId(String parentId){

    }
}
