import lombok.Data;

import java.util.List;
import java.util.Map;

// Me.java
@Data
public class Me {
    private Integer age;
    private String name;
    private Map<String, Object> params;
    private List<String> favoriteBooks;
}