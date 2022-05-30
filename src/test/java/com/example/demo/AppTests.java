package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTests {

    @LocalServerPort
    private int port;
//
//    private URL base;
//
//    @Autowired
//    private TestRestTemplate template;
//
//    @Before
//    public void setUp() throws Exception {
//        // TODO 因为我们修改了 content-path 所以请求后面要带上
//        this.base = new URL("http://localhost:" + port + "/demo/hello");
//        System.out.println(base.toString());
//    }
//
//    @Test
//    public void demo1() throws Exception {
//        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
//        assertEquals(response.getBody(), "开始了");
//    }

    @Test
    void contextLoads() {
        System.out.println(port);
    }

}
