package ru.otus.dto;

import lombok.Builder;
import lombok.Data;
import ru.otus.player.Say;

@Data
@Builder
public class Message {
    private Say say;
}
