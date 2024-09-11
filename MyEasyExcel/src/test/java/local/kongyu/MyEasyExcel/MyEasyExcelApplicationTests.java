package local.kongyu.MyEasyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import local.kongyu.MyEasyExcel.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
@Slf4j
class MyEasyExcelApplicationTests {

    /**
     * 读取Excel每一行的数据
     */
    @Test
    void easyExcelRead01() {
        String fileName = "data\\user.xlsx";
        ArrayList<UserInfoVo> list = new ArrayList<>();
        // 设置listener
        PageReadListener<UserInfoVo> listener = new PageReadListener<>(dataList -> {
            for (UserInfoVo user : dataList) {
                log.info("读取到一条数据{}", JSON.toJSONString(user));
                list.add(user);
            }
        });
        EasyExcel.read(fileName, UserInfoVo.class, listener).sheet().doRead();
        // 获取数据数量
        log.info("数据总量{}", list.size());
    }

    /**
     * 读取Excel每一行的数据
     */
    @Test
    void easyExcelRead02() {
        String fileName = "data\\user.xlsx";
        ArrayList<UserInfoVo> list = new ArrayList<>();
        // 设置listener
        AnalysisEventListener<UserInfoVo> listener = new AnalysisEventListener<UserInfoVo>() {
            @Override
            public void invoke(UserInfoVo data, AnalysisContext context) {
                list.add(data);
                log.info("读取的一条信息：{}", data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                log.info("{}条数据，解析完成", list.size());
            }
        };
        EasyExcel.read(fileName, UserInfoVo.class, listener).sheet().doRead();
    }

    /**
     * 分块读取Excel的数据
     */
    @Test
    void easyExcelRead03() {
        String fileName = "data\\user.xlsx";
        // 设置listener
        ReadListener<UserInfoVo> listener = new ReadListener<UserInfoVo>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;
            /**
             * 临时存储
             */
            private List<UserInfoVo> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(UserInfoVo data, AnalysisContext context) {
                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                log.info("存储数据库成功！");
            }

            /**
             * 业务处理
             */
            private void saveData() {
                //Integer count = mapper.insertBatch(cachedDataList);
                log.info("{}条数据，开始存储数据库！", cachedDataList.size());
            }
        };
        EasyExcel.read(fileName, UserInfoVo.class, listener).sheet().doRead();
    }

}
