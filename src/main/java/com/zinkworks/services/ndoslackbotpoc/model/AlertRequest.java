package com.zinkworks.services.ndoslackbotpoc.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
public class AlertRequest extends SlackNotificationRequest {
    @NotNull private Level level;

    @NotNull @NotBlank private String title;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
