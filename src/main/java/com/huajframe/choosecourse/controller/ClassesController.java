package com.huajframe.choosecourse.controller;

import com.huajframe.choosecourse.enums.RespBeanEnum;
import com.huajframe.choosecourse.service.IClassesService;
import com.huajframe.choosecourse.util.UUIDUtil;
import com.huajframe.choosecourse.vo.ClassesVo;
import com.huajframe.choosecourse.vo.RespBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
@Controller
@RequestMapping("/classes")
public class ClassesController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IClassesService classesService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private RedisScript<Long> redisScript;

    /**
     * 跳转选修课列表页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model) throws InterruptedException {
        String html = (String) redisTemplate.opsForValue().get("classesList");
        //从redis中获取到如果不为空，直接返回页面
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        // redis获取锁
        String uuid = UUIDUtil.uuid();
        String lockKey = "classesListLock";
        Boolean lockResult = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, 2, TimeUnit.SECONDS);
        if(Boolean.TRUE.equals(lockResult)){
            //获取到锁的线程去查数据库
            model.addAttribute("classesList", classesService.findClassesVo());
            //如果为空，渲染后存入Redis并返回
            WebContext context = new WebContext(request, response,
                    request.getServletContext(), request.getLocale(),
                    model.asMap());
            //渲染
            html = thymeleafViewResolver.getTemplateEngine().process("classesList",
                    context);
            // 存入redis
            redisTemplate.opsForValue().set("classesList", html, 30, TimeUnit.SECONDS);
            //删除锁，使用lua脚本删除
            redisTemplate.execute(redisScript, Collections.singletonList(lockKey), uuid);
            return html;
        }else{
            //没获取到的休息一会，在递归访问
            Thread.sleep(100);
            return toList(request, response, model);
        }
    }

    /**
     * 返回选修课程详细信息
     *
     * @param classesId 课程信息id
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/detail/{classesId}")
    public RespBean toDetail(@PathVariable Long classesId) throws InterruptedException {
        BoundValueOperations<String, Object> valueOps = redisTemplate.boundValueOps("classesDetail:" + classesId);
        ClassesVo classesVo = (ClassesVo) valueOps.get();
        if(classesVo != null){
            //redis中有直接返回
            if(classesVo.getId() != -1){
                return RespBean.success(classesVo);
            }else{
                //解决缓存穿透
                return RespBean.error(RespBeanEnum.CHOOSE_CLOSE);
            }
        }
        // redis中没有
        // redis获取锁
        String uuid = UUIDUtil.uuid();
        String lockKey = "classesDetailLock";
        Boolean lockResult = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, 2, TimeUnit.SECONDS);
        if(Boolean.TRUE.equals(lockResult)){
            //获取锁成功的线程去数据库中查
            classesVo = classesService.findClassesVoByClassesId(classesId);
            if(classesVo != null){
                //查找到则成功
                valueOps.set(classesVo, 30, TimeUnit.SECONDS);
                redisTemplate.execute(redisScript, Collections.singletonList(lockKey), uuid);
                return RespBean.success(classesVo);
            }else{
                valueOps.set(new ClassesVo(-1L), 20, TimeUnit.SECONDS);
                return RespBean.error(RespBeanEnum.CHOOSE_CLOSE);
            }
        }else{
            //没获取到锁，休息一下
            Thread.sleep(20);
            return toDetail(classesId);
        }
    }

}
