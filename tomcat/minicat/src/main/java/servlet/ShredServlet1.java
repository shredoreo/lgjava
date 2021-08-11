package servlet;

import com.shred.minicat.server.HttpProtocolUtil;
import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;
import com.shred.minicat.server.servlet.HttpServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ShredServlet1 extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>shred servlet11 of app2 get</h1>";
        try {
            response.outPut(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length) +content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>shred servlet post</h1>";
        try {
            response.outPut(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length) +content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
