package me.leafs.timechanger.handler.config;

import lombok.Data;
import me.leafs.timechanger.command.TimeMode;

@Data
public class TimeConfiguration {
    private TimeMode mode = TimeMode.STATIC;
    private long worldTime = 0L;
    private float speedMultiplier = 1F;
}
