package com.example.demo.controller.feignClient;


import com.example.demo.config.FeignConfig;
import com.example.demo.dto.comment.Comment;
import com.example.demo.dto.post.Post;
import com.example.demo.dto.statistic.PostStatisticRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ControllerFromPost", url = "http://localhost:8080", configuration = FeignConfig.class)
public interface ControllerFromPosts {

    @GetMapping(value = "/api/v1/post/statistic/post", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> postStatistic(PostStatisticRequestDto postStatisticRequest);

    //Получение статистики лайков
    @GetMapping(value = "/api/v1/post/statistic/like", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> likeStatistic(PostStatisticRequestDto postStatisticRequest);

    //Получение статистики комментариев
    @GetMapping(value = "/api/v1/post/statistic/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> commentStatistic(PostStatisticRequestDto postStatisticRequest);

    @GetMapping(value = "/api/v1/post", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<Void> postsGet(PostSearchDto postSearch, Pageable pageable);

    // блокировка/разблокировка поста
    @PutMapping(value = "/api/v1/is-post")
    public ResponseEntity<Void> blockPost(@RequestParam(value = "id") Long id);

    //Поиск постов
    @GetMapping(value = "/api/v1/post/search-posts/{searchWord}")
    List<Post> searchPosts(@PathVariable("searchWord") String searchWord);

    //всего постов
    @GetMapping(value = "/api/v1/post/count")
    long allCountPosts();

    // блокировка/разблокировка комментариев
    @PutMapping(value = "/api/v1/is-comment")
    ResponseEntity<Void> blockComment(@RequestParam(value = "id") Long id);

    //Поиск комментариев
    @GetMapping(value = "/api/v1/post/search-comment/{searchWord}")
    List<Comment> searchComment(@PathVariable("searchWord") String searchWord);

    //всего комментариев
    @GetMapping(value = "/api/v1/comment/count")
    long allCountComment();
}


