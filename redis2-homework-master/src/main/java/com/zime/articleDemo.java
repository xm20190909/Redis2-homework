package com.zime;

import redis.clients.jedis.Jedis;

import java.util.*;

public class ArticleDemo {
    public static void main(String[] args) {
        Jedis jedis = null;
        try {
            jedis = new Jedis("127.0.0.1", 6379);

            Article article=new Article();
            article.setTitle("hi");
            article.setAuthor("xxx");
            article.setLink("helloWorld");
            article.setTime("1");

//            saveArticle(article,jedis);
//            Votes("Article:1",3,jedis);
            System.out.println(selArticleByTimeOrVotes(1,"time",jedis));

        } catch (Exception e) {
//            Logger.Error(e.getMessage(),e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**添加文章.**/
    public static void saveArticle(Article article,Jedis jedis){
        Map<String,String> articleProperties = new HashMap<String, String>();
        articleProperties.put("title",article.getTitle());
        articleProperties.put("link",article.getLink());
        articleProperties.put("author",article.getAuthor());
        articleProperties.put("time",article.getTime());
        articleProperties.put("votes","1");

        long postId = jedis.incr("postID");

        jedis.hmset("Article:" + postId,articleProperties);

        jedis.sadd("Article:" + postId + ":Set","Article:" + postId);/**保存点赞用户.**/
        jedis.zadd("article:votes",1,"Article:" + postId);/**默认1赞.**/
        jedis.zadd("article:time",System.currentTimeMillis(),"Article:" + postId);/**获取当前毫秒数,作为分数.**/
    }
    /**根据文章发布时间或投票多少来分页显示文章,用str控制是按time还是votes来排序.**/
    public static List<Map<String, String>> selArticleByTimeOrVotes(int page,String str,Jedis jedis){
        Set set= jedis.zrevrange("article:" + str,(page-1)*5,page*5-1);

        List<Map<String, String>> maps = new ArrayList<Map<String,String>>();

        for (Object obj : set) {
            maps.add(jedis.hgetAll(String.valueOf(obj)));
        }

        return maps;
    }
    /**用户点赞文章(一篇仅一次).**/
    public static void Votes(String userID,int postID,Jedis jedis){
        if (jedis.sismember("Article:"+postID+":Set",userID)){
            System.out.println("已点过赞");
        }else {
            jedis.sadd("Article:"+postID+":Set",userID);
            jedis.zincrby("article:votes",1,"Article:" + postID);
            /**更新点赞数.**/
            UpArticle(postID,String.valueOf(jedis.scard("Article:"+postID+":Set")),jedis);
            System.out.println("点赞成功");
        }
    }
    /**修改点赞数.**/
    public static void UpArticle(int postID,String val,Jedis jedis){
        Map<String, String> properties = jedis.hgetAll("Article:" + postID);
        properties.put("votes",val);
        jedis.hmset("Article:" + postID,properties);
    }
}
