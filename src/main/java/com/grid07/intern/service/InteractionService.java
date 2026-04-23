package com.grid07.intern.service;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import com.grid07.intern.model.*;
import com.grid07.intern.repository.*;

@Service
public class InteractionService
{

    @Autowired
    private StringRedisTemplate r;

    @Autowired
    private PostRepo pr;

    @Autowired
    private CommentRepo cr;

    @Autowired
    private BotRepo br;



    private DefaultRedisScript<Long> botLimitScript;

    @PostConstruct
    public void init() {
        botLimitScript = new DefaultRedisScript<>();
        botLimitScript.setScriptText(
                "local c = redis.call('INCR', KEYS[1]) " +
                        "if c > 100 then " +
                        "  redis.call('DECR', KEYS[1]) " +
                        "  return 0 " +
                        "end " +
                        "return 1"
        );
        botLimitScript.setResultType(Long.class);
    }

    @Transactional
    public String likePost(Long pid, Long uid)
    {
        Post p = pr.findById(pid).orElse(null);
        if(p == null) return "no post";

        String vkey = "post:" + pid + ":virality_score";
        r.opsForValue().increment(vkey, 20.0);

        p.setLikeCount(p.getLikeCount() + 1);
        pr.save(p);

        return "ok";
    }

    @Transactional
    public Object addComment(Long pid, Long aid, String atype, String cnt, Integer pdepth, Long parentAid)
    {
        Post p = pr.findById(pid).orElse(null);
        if(p == null) return "post missing";

        int depth = (pdepth == null) ? 0 : pdepth + 1;

        if(depth > 20) {
            return "depth too high";
        }

        if("bot".equals(atype))
        {
            String bkey = "post:" + pid + ":bot_count";

            // using lua script for atomic check
            List<String> keys = Arrays.asList(bkey);
            Long result = r.execute(botLimitScript, keys);

            if(result == 0) {
                return "max 100 bot replies";
            }

            if(parentAid != null) {
                String ckey = "cooldown:bot_" + aid + ":human_" + parentAid;
                if(Boolean.TRUE.equals(r.hasKey(ckey))) {
                    // need to decrement manually if cooldown fails
                    r.opsForValue().decrement(bkey);
                    return "cooldown 10min";
                }
                r.opsForValue().set(ckey, "1", 10, TimeUnit.MINUTES);
            }

            String vkey = "post:" + pid + ":virality_score";
            r.opsForValue().increment(vkey, 1.0);

            Long ownerId = p.getAuthorId();
            String nkey = "notif_cooldown:user:" + ownerId;
            String lock = r.opsForValue().get(nkey);

            Bot b = br.findById(aid).orElse(null);
            String bname = (b != null) ? b.getName() : "unknown";
            String msg = "Bot " + bname + " replied to your post";

            if(lock == null) {
                System.out.println("Push Notification Sent to User: " + msg);
                r.opsForValue().set(nkey, "1", 15, TimeUnit.MINUTES);
            } else {
                String pendkey = "user:" + ownerId + ":pending_notifs";
                r.opsForList().rightPush(pendkey, msg);
            }
        }
        else
        {
            String vkey = "post:" + pid + ":virality_score";
            r.opsForValue().increment(vkey, 50.0);
        }

        Comment c = new Comment();
        c.setPostId(pid);
        c.setAuthorId(aid);
        c.setContent(cnt);
        c.setDepthLevel(depth);
        c.setAuthorType(atype);
        c.setCreatedAt(LocalDateTime.now());

        Comment saved = cr.save(c);
        return saved;
    }

    @org.springframework.scheduling.annotation.Scheduled(cron = "${notif.sweep.cron}")
    public void sweepNotifs()
    {
        System.out.println("\n--- sweep run ---");

        String pattern = "user:*:pending_notifs";
        Set<String> keys = r.keys(pattern);

        if(keys == null || keys.isEmpty()) {
            System.out.println("nothing pending");
            return;
        }

        for(String k : keys) {
            String[] parts = k.split(":");


            List<String> msgs = r.opsForList().range(k, 0, -1);
            if(msgs == null || msgs.isEmpty()) continue;

            Set<String> bots = new HashSet<>();
            for(String m : msgs) {
                String[] tmpArr = m.split(" ");
                if(tmpArr.length > 1) bots.add(tmpArr[1]);
            }

            String summary;
            if(bots.size() == 1) {
                summary = "Summarized Push Notification: " + bots.iterator().next() + " interacted with your posts";
            } else {
                int ot = msgs.size() - 1;
                summary = "Summarized Push Notification: " + bots.iterator().next() + " and " + ot + " others interacted with your posts";
            }

            System.out.println(summary);
            r.delete(k);
        }

        System.out.println("--- done ---\n");
    }
}