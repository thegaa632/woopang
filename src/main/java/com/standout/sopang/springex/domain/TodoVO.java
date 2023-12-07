package com.standout.sopang.springex.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoVO {

    private Long tno;

    private String title;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dueDate;

    private String writer;

    private boolean finished;
}
