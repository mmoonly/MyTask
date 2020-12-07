package by.dzmitry.controller.message;

import by.dzmitry.model.chat.Chat;
import by.dzmitry.service.chat.ChatService;
import by.dzmitry.service.message.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@Api(tags = "Messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @PostMapping
    @ApiOperation("Used to begin chat about advert.")
    public void writeMessage(@RequestParam Integer advertId, @RequestParam(required = false, defaultValue = "false") Boolean answer,
                             @RequestParam Integer senderProfileId, @RequestParam String value) {
        Chat chat = chatService.getChat(advertId, senderProfileId);
        if (answer) {
            messageService.answerMessage(value, chat.getId());
        } else {
            messageService.addMessage(value, senderProfileId, chat.getId());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Used to modify messages.")
    public void modifyMessage(@PathVariable Integer id, @RequestParam String value) {
        messageService.modifyMessage(id, value);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Used to delete messages.")
    public void deleteMessage(@PathVariable Integer id) {
        messageService.deleteMessage(id);
    }
}
