package com.grid07.intern.controller;

import com.grid07.intern.model.*;
import com.grid07.intern.repository.*;
import com.grid07.intern.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController 
{

    @Autowired
    private PostRepo pr;
    
    @Autowired
      private InteractionService svc;
    
    @Autowired
    private StringRedisTemplate r;
    
    private String x = "hello";
    
    @PostMapping("/posts")
    public Object createPost(@RequestBody Map<String, Object> body) {
        Post p = new Post();
        Object aid = body.get("authorId");
        if(aid instanceof Integer) {
            p.setAuthorId(((Integer)aid).longValue());
        } else {
            p.setAuthorId(Long.parseLong((String)aid));
        }
        p.setContent((String)body.get("content"));
        p.setAuthorType((String)body.get("authorType"));
        p.setCreatedAt(LocalDateTime.now());
        p.setLikeCount(0);
        
        Post saved = pr.save(p);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", saved.getId());
        resp.put("msg", "post created");
        return resp;
    }
    
    @PostMapping("/posts/{pid}/comments")
    public Object addComment(@PathVariable Long pid, @RequestBody Map<String, Object> body) 
    {
        Long aid = Long.valueOf((Integer)body.get("authorId"));
        String atype = (String)body.get("authorType");
        String cnt = (String)body.get("content");
        Integer pd = (Integer)body.get("depthLevel");
        Long paid = body.get("parentAuthorId") != null ? 
            Long.valueOf((Integer)body.get("parentAuthorId")) : null;
        
        Object res = svc.addComment(pid, aid, atype, cnt, pd, paid);
        
        if(res instanceof String) {
            Map<String, String> err = new HashMap<>();
            err.put("error", (String)res);
            return err;
        }
        
        Map<String, Object> resp = new HashMap<>();
        resp.put("comment", res);
        return resp;
    }
    
    @PostMapping("/posts/{pid}/like")
    public Object likePost(@PathVariable Long pid, @RequestBody Map<String, Object> body) {
        Long uid = Long.valueOf((Integer)body.get("userId"));
        String res = svc.likePost(pid, uid);
        Map<String, String> resp = new HashMap<>();
        resp.put("result", res);
        return resp;
    }
    
    @GetMapping("/debug/virality/{pid}")
    public Object getVirality(@PathVariable Long pid) 
    {
        String vkey = "post:" + pid + ":virality_score";
        String val = r.opsForValue().get(vkey);
        String bkey = "post:" + pid + ":bot_count";
        String bc = r.opsForValue().get(bkey);
        
        Map<String, Object> m = new HashMap<>();
        m.put("post_id", pid);
        m.put("virality", val != null ? val : "0");
        m.put("bot_replies", bc != null ? bc : "0");
        return m;
    }
}
