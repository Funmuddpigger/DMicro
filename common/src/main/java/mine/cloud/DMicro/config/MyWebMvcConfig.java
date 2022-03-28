package mine.cloud.DMicro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    private String filePath = "F:/images/" ;
//    private static final String filePath = "/data/images/" ;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 配置资源映射
         * 意思是：如果访问的资源路径是以“/images/”开头的，
         * 就给我映射到本机的“E:/images/”这个文件夹内，去找你要的资源
         * 注意：F:/images/ 后面的 “/”一定要带上
         */
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+filePath);
    }
}
