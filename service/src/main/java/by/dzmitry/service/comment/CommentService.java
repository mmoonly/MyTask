package by.dzmitry.service.comment;

public interface CommentService {

    void addComment(String value, Integer profileId, Integer advertId);

    void modifyComment(Integer commentId, String newValue);

    void deleteComment(Integer commentId);

}
