package by.dzmitry.service.util;

import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.comment.Comment;
import by.dzmitry.model.message.Message;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.CommentDto;
import by.dzmitry.service.dto.MessageDto;
import by.dzmitry.service.dto.ProfileDto;

public class ModelDtoMapper {

    public static AdvertDto convertToAdvertDto(Advert advert) {
        AdvertDto advertDto = new AdvertDto();
        advertDto.setId(advert.getId());
        advertDto.setHeading(advert.getHeading());
        advertDto.setPublicationDate(advert.getPublicationDate());
        advertDto.setPrice(advert.getPrice());
        advertDto.setActive(advert.getActive());
        advertDto.setPaid(advert.getPaid());
        advertDto.setEndPaidDate(advert.getEndPaidDate());
        advertDto.setDescription(advert.getDescription());
        return advertDto;
    }

    public static CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setValue(comment.getValue());
        commentDto.setName(comment.getProfile().getName() + " " + comment.getProfile().getSurname());
        commentDto.setPublicationDate(comment.getPublicationDate());
        return commentDto;
    }

    public static ProfileDto convertToProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setName(profile.getName());
        profileDto.setSurname(profile.getSurname());
        profileDto.setRating(profile.getRating());
        return profileDto;
    }

    public static MessageDto convertToMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setValue(message.getValue());
        messageDto.setSendDate(message.getSendDate());
        messageDto.setName(message.getProfile().getName() + " " + message.getProfile().getSurname());
        return messageDto;
    }
}
