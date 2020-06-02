package com.example.demo.controller;

import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUser;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.provider.GithubProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    //为了避免当在线上环境时，当修改一些值时，不需要到代码中去修改，将这些变量封装到application.propereties中
    @Value("${github.client.id}")
    //将配置文件中key为github.client.id的值读出来，然后赋给 clientId
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserService userService;

    //接收登陆点击后生成的'http://localhost:8080/callback'回调地址
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        //接收回调地址携带的code、state信息，用一个AccessTokenDTO对象进行赋值封装（令牌）
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //用一个GithubProvider对象，通过获取进行封装后的AccessTokenDTO对象之后，调用其getAccessToken()方法，生成一个accessToken
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //再用GithubProvider对象，调用其getUser()方法，参数是上面的accessToken，返回user对象，[如果user不为空，就代表登陆成功了]
        GithubUser githubUser = githubProvider.getUser(accessToken);
        /*修复登陆问题？？修复了什么登陆问题，这里地方是要做什么事情撒？？？*/
        if (githubUser != null && githubUser.getId() != null) {
            //IDEA自动补全返回类型的快捷键ctrl+Alt+V
            User user = new User();
            //user.setToken(UUID.randomUUID().toString());,按commend+shift+N，可以抽取出一个变量出来
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //登陆成功，如果通过验证，这一步就相当于写入session
            userService.createOrUpdate(user);
            //这里原来是手动写入cookie，我们现在要自动写入cookie
            /*request.getSession().setAttribute("user", githubUser);
            return "redirect:/";*/
            //这里按commend+P，便可以在方法中提示参数信息
            //登陆成功以后，如果通过验证，把token写到 cookie里面
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            // 登录失败，重新登录
            return "redirect:/";
        }
    }

    /**
     * 退出第三方登录
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        //退出登录时，将session和cookie里面的用户信息删除
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
