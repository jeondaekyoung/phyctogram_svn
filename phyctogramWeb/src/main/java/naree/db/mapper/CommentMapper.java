package naree.db.mapper;

import java.util.List;

import naree.db.domain.Comment;

public interface CommentMapper {

	List<Comment> selectCommentByCommntySeq(int commnty_seq);

	int insertComment(Comment comment);

	int deleteCommentByMemberSeq(int member_seq);

	int deleteCommentByCommntySeq(int commnty_seq);

}
