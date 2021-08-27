package com.lagou.rpc.provider;

import com.lagou.rpc.provider.register.RegisterServer;
import com.lagou.rpc.provider.server.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SpringBootApplication
public class ServerBootstrapApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ServerBootstrapApplication.class, args);
    }

    @Autowired
    RpcServer rpcServer;

    @Autowired
    RegisterServer registerServer;

    /**
     * 可用来启动其他业务
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        SimpleCommandLinePropertySource cmd = new SimpleCommandLinePropertySource(args);
        String ip = "127.0.0.1";
        String port = cmd.getProperty("port");
        if (port == null){
            throw new RuntimeException("未传入端口");
        }

        System.out.println("cmd arg: ip= "+ip);
        new Thread(() ->
                rpcServer.startServer(ip, Integer.parseInt(port)))
                .start();

       /* new Thread(() ->
                rpcServer.startServer("127.0.0.1", 8898))
                .start();
        new Thread(() ->
                rpcServer.startServer("127.0.0.1", 8899))
                .start();

        //服务注册
        new Thread(registerServer).start();*/
    }
}
