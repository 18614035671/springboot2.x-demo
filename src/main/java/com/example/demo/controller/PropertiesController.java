package com.example.demo.controller;

import com.example.demo.common.RequestClient;
import com.example.demo.domain.User;
import com.example.demo.properties.MyProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpRequest;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Map;

@Api(tags = "用户接口")//描述UserController的信息
@RequestMapping("/properties")
@RestController
public class PropertiesController {

    private static final Logger log = LoggerFactory.getLogger(PropertiesController.class);

    private final MyProperties myProperties;

    @Autowired
    public PropertiesController(MyProperties myProperties) {
        this.myProperties = myProperties;
    }
    // 注入接口实例
    @Autowired
    private RequestClient requestClient;


    @GetMapping("/1")
    public MyProperties myProperties1() {
        log.info("=================================================================================================");
        log.info(myProperties.toString());
        log.info("=================================================================================================");
        return myProperties;
    }

    @GetMapping("/testApi")
    public void testApi() {
        // 调用接口
        Map result = requestClient.getLocation("121.475078", "31.223577");
        System.out.println(result);
    }

    @GetMapping("/getIdCard")
    public Map getIdCard() {
        Map result = requestClient.getIdCard("c367210907f3036418ef4b5fad46b2b4", "D:\\zheng.jpg", progress -> {
            System.out.println("progress: " + Math.round(progress.getRate() * 100) + "%");  // 已上传百分比
            if (progress.isDone()) {   // 是否上传完成
                System.out.println("--------   Upload Completed!   --------");
            }
        });
        System.out.println(result);
        return result;
    }

    @ApiOperation(value = "查询用户",notes = "根据id查询用户")
    @ApiImplicitParam(paramType = "path",name="id",value = "用户id",required = true)
    @GetMapping("/user/query/{id}")
    public String getUserById(@PathVariable Integer id) {
        return "/user/"+id;
    }

    @ApiResponses({
            @ApiResponse(code=200,message="删除成功"),
            @ApiResponse(code=500,message="删除失败")})
    @ApiOperation(value = "删除用户",notes = "根据id删除用户")
    @DeleteMapping("/user/delete/{id}")
    public Integer deleteUserById(@PathVariable Integer id) {
        return id;
    }

    @ApiOperation(value = "添加用户",notes = "添加一个用户，传入用户名和性别")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="username",value = "用户名",required = true,defaultValue = "张三"),
            @ApiImplicitParam(paramType = "query",name="sex",value = "性别",required = true,defaultValue = "女")
    })
    @PostMapping("/user")
    public String addUser(@RequestParam String username, @RequestParam String sex){
        return username+","+sex;
    }

    @ApiOperation(value="修改用户",notes = "根据传入的用户信息修改用户")
    @PutMapping("/user")
    public String updateUser(@RequestBody User user){
        return user.toString();
    }

    @GetMapping("/ignore")
    @ApiIgnore
    public void ignoreMethod(){}

    @GetMapping("/getExcel")
    public void getExcel(HttpServletResponse response){
        try {
            ClassPathResource resource = new ClassPathResource("bankNameTemplate.xlsx");    // static/pattern下的 test.txt文件
            InputStream in = resource.getInputStream();  //获取文件输入流
            //转换为文件流
            BufferedInputStream fs = new BufferedInputStream(in);
            //指定默认下载名称
            String fileName = "XXX.xlsx";
            //配置response的头
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //循环从文件中读出数据后写出，完成下载
            byte[] b = new byte[1024];
            int len;
            while ((len = fs.read(b)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            fs.close();
        } catch (Exception e) {
            log.error("下载Excel模板异常", e);
        }
    }
}