package com.shred.sc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@EnableWebSecurity // 开启web访问安全，针对于下面定义的资源访问策略
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    private String sign_key = "shred123";

    /**
     * 用于定义资源服务器 向 远程认证服务器发起请求，进行token校验
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
       /* //设置资源id
        resources.resourceId("autodeliver");

        // 定义token服务对象
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        // 校验端点
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9999/oauth/check_token");
        // 携带客户端id 、安全码
        remoteTokenServices.setClientId("client_shred");
        remoteTokenServices.setClientSecret("asdf");

        //设置服务对象，服务描述对象
        resources.tokenServices(remoteTokenServices);
*/
        resources
                .resourceId("autodeliver")
                .tokenStore(tokenStore())
                // 无状态
                .stateless(true);
    }

    /**
     * 场景：
     * 一个服务中可能有很多资源，
     * 某些api，需要认证才能访问；
     * 某些则直接开放；
     * 在当前方法进行配置。
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement()
                // session的创建策略：根据需要创建
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                //   /autodeliver/** 和/demo/**都需要认证
                .antMatchers("/autodeliver/**").authenticated()
                .antMatchers("/demo/**").authenticated()
                // 其他请求不认证
                .anyRequest().permitAll()
        ;
    }


    /**
     * 创建令牌存储对象
     * @return
     */
    private TokenStore tokenStore() {
//        return new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 返回令牌转换器
     * @return
     */
    private JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 签名密钥
        converter.setSigningKey(sign_key);

        //验证时使用的密钥，和签名密钥保持一致
        converter.setVerifier(new MacSigner(sign_key));

        return converter;
    }



}
