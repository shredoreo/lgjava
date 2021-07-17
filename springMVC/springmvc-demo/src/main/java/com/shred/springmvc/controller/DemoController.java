package com.shred.springmvc.controller;

import com.shred.springmvc.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/demo")
public class DemoController {

    // 在具体的Controller中声明只对当前Controller有效
    // SpringMVC异常处理机制
    /*@ExceptionHandler(ArithmeticException.class)
    public void handleException(ArithmeticException exception, HttpServletResponse response){
        try {
            response.getWriter().write(exception.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    /**
     * 转发：url不变，参数不丢失，一个请求
     * 重定向：url会变，参数会丢失，需要重新携带参数，两个请求
     * @param name
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("/handleRedirect")
    public String handleRedirect(String name, RedirectAttributes
            redirectAttributes) {
//return "redirect:handle01?name=" + name; // 拼接参数安全性、参数⻓度都有局限
// addFlashAttribute方法设置了一个flash类型属性，该属性会被暂存到session中，在 跳转到⻚面之后该属性销毁
        redirectAttributes.addFlashAttribute("name", name);
        return "redirect:handle01";
    }

    @RequestMapping("/handle01")
    public ModelAndView handle01(@ModelAttribute("name") String name,@RequestParam) {
        System.out.println(name);
        int i = 1 / 0;
        Date date = new Date();
        ModelAndView modelAndView = new ModelAndView();
        // 想请求与中添加属性 request.setAttribute(
        modelAndView.addObject("datae", date);
        // 视图信息(封装跳转的页面信息
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @RequestMapping("/handle02")
    public String handle02(ModelMap model) {
        Date date = new Date();
        // 想请求与中添加属性 request.setAttribute(
        model.addAttribute("date", date);
        System.out.println(model.getClass());

        return "success";
    }

    @RequestMapping("/handle03")
    public String handle03(Model model) {
        Date date = new Date();
        // 想请求与中添加属性 request.setAttribute(
        model.addAttribute("date", date);
        System.out.println(model.getClass());
        return "success";
    }

    @RequestMapping("/handle04")
    public String handle04(Map<String, Object> model) {
        Date date = new Date();
        // 想请求与中添加属性 request.setAttribute(
        model.put("date", date);
        System.out.println(model.getClass());

        return "success";
    }

    @GetMapping("/handle/{id}")
    public ModelAndView handleGet(@PathVariable("id") Integer id) {
        Date date = new Date();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("date", date);
        return modelAndView;
    }

    @PostMapping("/handle")
    public ModelAndView handlePost(String username) {
        Date date = new Date();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("date", date);
        return modelAndView;
    }


    @PutMapping("/handle/{id}/{name}")
    public ModelAndView handlePut(@PathVariable("id") Integer id,
                                  @PathVariable("name") String username
    ) {
        Date date = new Date();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("date", date);
        return modelAndView;
    }

    @DeleteMapping("/handle/{id}")
    public ModelAndView handleDelete(@PathVariable("id") Integer id) {
        Date date = new Date();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("date", date);
        return modelAndView;
    }

    @PostMapping("/handle07")
    //添加ResponseBody 后，不走视图解析器，而是response直接输出数据
    @ResponseBody
    public User handle07(@RequestBody User user) {
        System.out.println(user);
        user.setName("ssssssssss111111");
        return user;
    }

    @PostMapping("/upload")
    public ModelAndView upload(MultipartFile uploadFile, HttpSession session) throws IOException {
        //处理上传文件
        // 重命名 123.png，获取后缀
        String originalFilename = uploadFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newName = UUID.randomUUID().toString() + "." + ext;

        // 存储，要存储到指定到文件夹，/uploads， 考虑文件过多情况，按日期生成文件夹
        String realPath = session.getServletContext().getRealPath("/uploads");
        String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //文件夹
        File folder = new File(realPath + "/" + datePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        /*将文件转移到指定目录*/
        uploadFile.transferTo(new File(folder, newName));

        Date date = new Date();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("date", date);
        modelAndView.setViewName("success");
        return modelAndView;
    }

}
