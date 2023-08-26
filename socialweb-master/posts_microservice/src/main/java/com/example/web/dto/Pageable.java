package com.example.web.dto;

import lombok.Data;

@Data
public class Pageable {

    int page = 0;

    int size = 1;

    PageableSort sort;
}
