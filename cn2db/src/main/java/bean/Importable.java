package bean;

public interface Importable {
    String getId();
    default void setParentId(String parentId){

    }
}
