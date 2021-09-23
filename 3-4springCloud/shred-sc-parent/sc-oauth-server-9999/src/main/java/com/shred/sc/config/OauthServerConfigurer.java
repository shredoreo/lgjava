package com.shred.sc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * 当前配置类需要继承特定的父类 AuthorizationServerConfigurerAdapter
 */
@Configuration
@EnableAuthorizationServer//开启认证服务器功能
public class OauthServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private  AuthenticationManager authenticationManager;

    /**
     * 认证服务最终是以api接口方式对外提供服务。校验合法性、生成令牌、校验令牌
     * 以api接口方式对外，涉及api的访问权限
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        // 相当于打开endpoints 访问接口的开关，这样的话后期我们能够访问 该接口
        security
                // 允许客户端表单认证
                .allowFormAuthenticationForClients()
                // 开启端口/oauth/token_key的访问权限(允许)
                .tokenKeyAccess("permitAll()")
                // 开启端口/oauth/check_token的访问权限(允许)
                .checkTokenAccess("permitAll()");
    }

    /**
     * 客户端详情 配置
     * client_id、secret
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()// 客户端信息存储在什么地方，可以在内存中，可以在数据库里
                //// 添加一个client配
                .withClient("client_shred")
                // 指定客户端的密码/安全码
                .secret("asdf")
                // 指定客户端 所能访问资源id清单，此处的资源id是需要在具体的资源服务器上也配置一样
                .resourceIds("autodeliver")
                // 认证类型/令牌颁发模式，可以配置多个在这里，但是不一定 都用，具体使用哪种方式颁发token，需要客户端调用的时候传递参数指定
                .authorizedGrantTypes("password", "refresh_token")
                // 客户端的权限范围，此处配置为all全部即可
                .scopes("all");
// 从内存中加载客户端详情改为从数据库中加载客户端详情
        clients.withClientDetails(createJdbcClientDetailsService());
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcClientDetailsService createJdbcClientDetailsService(){
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        return jdbcClientDetailsService;
    }


    /**
     * 和 token 管理相关
     * 存储token
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints.tokenStore(tokenStore()) //指定token存储方法
                .tokenServices(authorizationServerTokenServices()) //token服务的描述
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
        ;
    }

    /**
     * token 服务对象，描述token的信息
     * @return
     */
    private AuthorizationServerTokenServices authorizationServerTokenServices() {
        //使用默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true);//是否开启刷新
        defaultTokenServices.setTokenStore(tokenStore());

        //针对 jwt 令牌的添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());

        // 令牌的有效时间、一般为2h
        defaultTokenServices.setAccessTokenValiditySeconds(20);
        //刷新令牌的有效时间
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);// 3天
        return defaultTokenServices;
    }

    /**
     * 创建令牌存储对象
     * @return
     */
    private TokenStore tokenStore() {
//        return new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    private String sign_key = "shred123";

    @Autowired
    private ShredAccessTokenConverter shredAccessTokenConverter;

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
        //设置自定义的转换器
        converter.setAccessTokenConverter(shredAccessTokenConverter);

        return converter;
    }


}
