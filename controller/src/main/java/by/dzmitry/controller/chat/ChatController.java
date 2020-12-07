package by.dzmitry.controller.chat;

import by.dzmitry.service.chat.ChatService;
import by.dzmitry.service.dto.MessageDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chats")
@Api(tags = "Chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    @ApiOperation("Used to get messages by advert and profile.")
    public ResponseEntity<List<MessageDto>> getMessages(@RequestParam Integer advertId, @RequestParam Integer senderProfileId) {
        return ResponseEntity.ok(chatService.getMessages(advertId, senderProfileId));
    }
}
