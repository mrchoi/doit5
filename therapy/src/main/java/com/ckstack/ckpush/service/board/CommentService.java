package com.ckstack.ckpush.service.board;

import com.ckstack.ckpush.domain.board.DocumentCommentEntity;
import org.apache.ibatis.annotations.Param;

import javax.print.Doc;
import java.util.List;
import java.util.Map;

/**
 * Created by kodaji on 2016. 1. 31..
 */
public interface CommentService {
    /**
     * 댓글을 생성 한다.
     *
     * @param documentCommentEntity 추가할 댓글 정보
     */
    void addDocumentComment(DocumentCommentEntity documentCommentEntity);

    /**
     * 댓글의 카운트를 구한다.
     *
     * @param appSrl 댓글이 포함된 앱 시리얼 넘버
     * @param boardSrl 댓글이 포함된 게시판 시리얼 넘버
     * @param categorySrl 댓글이 포함된 카테고리 시리얼 넘버
     * @param documentSrl 댓글이 포함된 게시판 시리얼 넘버
     * @return 조건에 맞는 댓글 카운트
     */
    long countDocumentComment(int appSrl, long boardSrl, long categorySrl, long documentSrl);


    /**
     * 댓글의 목록을 가져온다.
     *
     * @param appSrl 댓글이 포함된 앱 시리얼 넘버
     * @param boardSrl 댓글이 포함된 게시판 시리얼 넘버
     * @param categorySrl 댓글이 포함된 카테고리 시리얼 넘버
     * @param documentSrl 댓글이 포함된 게시판 시리얼 넘버
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *              - column : ordering 할 칼럼명
     *              - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 링크 게시물 리스트
     */
    List<DocumentCommentEntity> getDocumentComment(int appSrl, long boardSrl, long categorySrl, long documentSrl,
                                                   Map<String, String> sort, int offset, int limit);

    /**
     * 댓글을 삭제 한다.
     *
     * @param commentSrl 삭제할 게시물의 시리얼 넘버
     * @param commentSrls 삭제할 게시물의 시리얼 넘버 리스트
     */
    void deleteComments(long commentSrl, List<Long> commentSrls);

}
