package searchengine.dto.Responce;

import lombok.Data;
import org.springframework.http.HttpStatus;
import searchengine.service.SearchServiceImpl;

@Data
public class Response {

    SearchServiceImpl searchService;
    HttpStatus httpStatus;
}
