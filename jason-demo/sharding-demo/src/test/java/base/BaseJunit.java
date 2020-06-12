package base;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


/**
 * 基础测试类，junit测试类都需要继承此类
 * <p>
 * {#@Transactional}打开的话测试之后数据可自动回滚
 * <p>
 * {#@FixMethodOrder(MethodSorters.NAME_ASCENDING)}可以让测试方法顺序执行
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@WebAppConfiguration
@Transactional
public class BaseJunit {

    @Autowired
    WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void init() {
        System.out.println("开始测试-----------------");

    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

}
