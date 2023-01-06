import com.yug.athena.AthenaJdbcUtils;
import org.junit.jupiter.api.Test;

/**
 * @author gy
 * @version 1.0
 * @description 测试 Athena
 * @date 2023/1/6 14:53
 */
public class AthenaTest {

    @Test
    public void query() throws Exception {
        AthenaJdbcUtils.query();
    }

    @Test
    public void createHiveTable() throws Exception {
        AthenaJdbcUtils.createTable();
    }
}
