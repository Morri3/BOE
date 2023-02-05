package com.team.boeboard.form.announce;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetAnnounceDto implements Serializable {
    private String content;
    private String text_color;
    private int text_size;
    private String background_color;
    private String start_time;
    private String finish_time;
}
