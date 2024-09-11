package local.kongyu.mybatisFlexPostGis.controller;

import local.kongyu.mybatisFlexPostGis.entity.MyLocationEntity;
import local.kongyu.mybatisFlexPostGis.mapper.MyLocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-11 21:53
 */
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyLocationController {
    private final MyLocationMapper myLocationMapper;

    @GetMapping("/list")
    public String list() {
        List<MyLocationEntity> list = myLocationMapper.selectAll();
        return list.toString();
    }
}
