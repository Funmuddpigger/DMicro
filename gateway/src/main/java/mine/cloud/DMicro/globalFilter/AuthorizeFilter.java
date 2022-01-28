package mine.cloud.DMicro.globalFilter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(-1)  //注解实现优先级
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    /**
     * 处理当前请求,有必要的话通过 GatewayFilterChain 将请求交给下一个过滤器处理
     * @param exchange  请求上下文,里面可以获取Request,Response
     * @param chain 过滤器链,用来把请求委托给下一个过滤器
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> params = request.getQueryParams();
        // 2. 获取参数中的authorization参数
        String auth = params.getFirst("获取参数中的authorization参数");
        // 3. 判断
        if ("admin".equals(auth)){
            //4.是,放行
            return chain.filter(exchange);
        }
        //5. 否,设置状态码,拦截
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    //实现Ordered接口,实现优先级
    @Override
    public int getOrder() {
        return -1;
    }
}
