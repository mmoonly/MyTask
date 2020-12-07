package by.dzmitry.controller.comment;

import by.dzmitry.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@Api(tags = "Comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @ApiOperation("Used to add comments.")
    public void addComment(@RequestParam String value, @RequestParam Integer profileId,
                           @RequestParam Integer advertId) {
        commentService.addComment(value, profileId, advertId);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Used to delete comments.")
    public void deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Used to modify comments.")
    public void modifyComment(@PathVariable Integer id, @RequestParam String value) {
        commentService.modifyComment(id, value);
    }
}
