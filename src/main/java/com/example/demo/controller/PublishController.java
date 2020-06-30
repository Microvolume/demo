package com.example.demo.controller;
import com.example.demo.cache.TagCache;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import com.example.demo.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

/**
 * 返回空白的发布页面，供用户提交新问题
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    /* 编辑功能，似曾相识，不过是地址中带了个id；edit()方法：点击编辑的时候，通过请求地址中的id，去数据库中的question表中去取，获取到当前的question，然后展示出来，
    再点击发布按钮的时候，如果有ID就做更新操作，没有ID就做插入操作 */
    public String edit(@PathVariable(name = "id") Long id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        // 点击编辑时，额外需要注意需要把question的id也传到publish页面 ，这样是为了当表单再次提交的时候，将id这个属性传递到Post提交的Controller中
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    /*当点击“发布”按钮的时候，首先是接收前端参数，然后是信息的校验，再然后是从拦截器中获取user信息（user如果为空的话，就回显到页面“用户未登陆的信息”）
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model) {
        //将接收的参数回显到前端，这个地方是咋回事？
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());

        // 信息的校验
        if (StringUtils.isBlank(title)) {           //StringUtils这个工具类是什么意思？
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }

        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }

        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        //从拦截器类中获取user
        User user = (User) request.getSession().getAttribute("user");
        //如果要是有异常的话，就跳转回去（博客发布问题页）
        if (user == null) {
            model.addAttribute("error", "用户未登陆");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        //这个地方为什么不是用user的AccountID
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        //无异常的话，就重定向回首页
        return "redirect:/";
    }
}