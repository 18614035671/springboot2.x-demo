package com.example.demo.common.util;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

public class GenerateDoc {
    /**
     * 生成文件的结果路径
     */
    public static final String RESULT_PATH = "D:\\";
    /**
     * 要生成的表名称
     */
    public static final String TABLE_NAMES_TXT = "tableNames.txt";

    public static void main(String[] args) {
        //generateDoc(EngineFileType.WORD);
        generateDoc(EngineFileType.MD);
        //generateDoc(EngineFileType.HTML);
    }

    private static void generateDoc(EngineFileType type) {
        // 1.获取数据源
        DataSource dataSource = getDataSource();
        // 2.获取数据库文档生成配置（文件路径、文件类型）
        EngineConfig engineConfig = getEngineConfig(type, RESULT_PATH);
        // 3.获取数据库表的处理配置，可忽略
        ProcessConfig processConfig = getProcessConfig();
        // 4.Screw 完整配置
        Configuration config = getScrewConfig(dataSource, engineConfig, processConfig);
        // 5.执行生成数据库文档
        new DocumentationExecute(config).execute();
    }

    /**
     * 获取数据库源
     */
    private static DataSource getDataSource() {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://*.*.*.*:*/ams?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
        hikariConfig.setUsername("");
        hikariConfig.setPassword("");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(2);
        return new HikariDataSource(hikariConfig);
    }


    /**
     * 获取文件生成配置
     *
     * @param type 生成文件类型
     * @param path 生成文件路径
     * @return EngineConfig
     */
    private static EngineConfig getEngineConfig(EngineFileType type, String path) {

        return EngineConfig.builder()
                //生成文件路径
                .fileOutputDir(path)
                //打开目录
                .openOutputDir(true)
                //文件类型
                .fileType(type)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                //自定义文件名称
                .fileName("数据库结构文档").build();
    }

    /**
     * 获取数据库表的处理配置，可忽略，可以配置表前缀，尾缀
     */
    private static ProcessConfig getProcessConfig() {
//        String filePath = GenerateDoc.class.getClassLoader().getResource(TABLE_NAMES_TXT).getFile();
//        List<String> tableList = FileUtil.readUtf8Lines(filePath);
        return ProcessConfig.builder()
                // 指定只生成的表集合
//                .designatedTableName(tableList)
                //忽略的表集合
                .ignoreTableName(new ArrayList<>())
                .build();
    }

    private static Configuration getScrewConfig(DataSource dataSource, EngineConfig engineConfig, ProcessConfig processConfig) {
        return Configuration.builder()
                //版本
                .version("1.0.0")
                //描述
                .description("数据库设计文档生成")
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig).build();
    }
}


