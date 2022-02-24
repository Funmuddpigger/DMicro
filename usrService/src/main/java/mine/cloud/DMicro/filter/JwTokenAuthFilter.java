package mine.cloud.DMicro.filter;

import io.jsonwebtoken.Claims;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.impl.LoginUserDetailsImpl;
import mine.cloud.DMicro.utils.JwtUtil;
import mine.cloud.DMicro.utils.StringHelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

//JWT自定义解析过滤器---public class JwTokenAuthFilter implements Filter 在不同版本servlet可能会导致循环调用
@Component
public class JwTokenAuthFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //get token
        String token = httpServletRequest.getHeader("token");
        if(!StringHelperUtils.isNotEmpty(token)){
            //无token,放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //resolve token
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String usrId = claims.getSubject();
            //get redis
            String redisKey = "loginUser:"+usrId;
            User userDetails = (User) redisTemplate.opsForValue().get(redisKey);
            //set spring security context
            if(Objects.isNull(userDetails)){
                throw new RuntimeException("用户未登录/凭证已过期");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            //token exist exit
            filterChain.doFilter(httpServletRequest,httpServletResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
