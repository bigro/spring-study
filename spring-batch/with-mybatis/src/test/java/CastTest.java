import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CastTest {

    @Test
    public void name() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("hoge", "hoge");
        map.put("fuga",1);

        int fuga = (int) map.get("fuga");
        String hoge = (String) map.get("hoge");

        System.out.println(hoge);
    }
}
