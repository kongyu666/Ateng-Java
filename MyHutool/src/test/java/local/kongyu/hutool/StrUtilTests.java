package local.kongyu.hutool;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class StrUtilTests {

    /**
     * 格式化字符串，类似于slf4j的使用方法
     */
    @Test
    public void test1() {
        String format = StrUtil.format("{}+{}={}", 1, 1, 2);
        System.out.println(format);
    }

    /**
     * 将列表按照指定分隔符为字符串
     */
    @Test
    public void test2() {
        String join = StrUtil.join(",", Arrays.asList(1, 2, 3, 4, 5));
        System.out.println(join);
    }

}
