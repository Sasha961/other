package searchengine.dto.search;

import lombok.Getter;

@Getter
public class LemmaError implements SearchRepository{

    final boolean result = false;

    final String error = "Ни одно слово, не встречается на страницах";
}
