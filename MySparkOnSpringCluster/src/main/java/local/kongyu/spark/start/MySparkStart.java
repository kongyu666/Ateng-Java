package local.kongyu.spark.start;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 运行Spark任务
 *
 * @author 孔余
 * @email 2385569970@qq.com
 * @date 2024-07-20 17:55:35
 */
@Component
public class MySparkStart implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 获取需要运行的任务名称
        List<String> classArgs = args.getOptionValues("class");
        List<String> methodArgs = args.getOptionValues("method");
        if (ObjectUtils.isEmpty(classArgs) || ObjectUtils.isEmpty(methodArgs)) {
            System.out.println("请提供参数：--class xxx --method xxx");
            return;
        }
        String className = classArgs.get(0);
        String methodName = methodArgs.get(0);
        System.out.println("运行" + className + "的" + methodName + "方法");
        // 使用反射机制调用指定的类和方法
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getMethod(methodName);
        method.invoke(instance);
    }
}
