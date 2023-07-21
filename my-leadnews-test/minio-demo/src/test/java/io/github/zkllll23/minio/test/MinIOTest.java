package io.github.zkllll23.minio.test;

import io.github.zkllll23.file.service.FileStorageService;
import io.github.zkllll23.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testUpdateHtmlFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\list.html");
            String filePath = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
            System.out.println(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把 list.html 文件上传到minio中, 用于在浏览器中访问
     *
     * @param args
     */
    /*

    public static void main(String[] args) {
        FileInputStream fileInputStream;

        try {
            // 创建 minio 客户端, 获取链接信息
            MinioClient minioClient = MinioClient.builder().credentials("minio", "minio123").endpoint("http://192.168.200.130:9000").build();

            // 上传文件
            fileInputStream = new FileInputStream("D:\\list.html");
            PutObjectArgs putObjectArgs = new PutObjectArgs().builder()
                    .object("list.html")    // 文件名称
                    .contentType("text/html")   // 文件类型
                    .bucket("leadnews") // 桶名称
                    .stream(fileInputStream, fileInputStream.available(), -1)    // 文件流
                    .build();
            minioClient.putObject(putObjectArgs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

     */
}
