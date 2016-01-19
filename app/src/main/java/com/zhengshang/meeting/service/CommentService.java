package com.zhengshang.meeting.service;

import android.content.Context;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.CommentRO;
import com.zhengshang.meeting.remote.dto.CommentDto;
import com.zhengshang.meeting.remote.dto.ReplyDto;
import com.zhengshang.meeting.ui.vo.CommentVO;
import com.zhengshang.meeting.ui.vo.ReplyVO;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 评论相关 服务
 * Created by sun on 2016/1/8.
 */
public class CommentService extends BaseService {

    private CommentRO commentRO;

    public CommentService(Context context) {
        super(context);
        commentRO = new CommentRO(context);
    }

    /**
     * 获取评论列表
     *
     * @param newsId 新闻id
     * @return
     * @throws JSONException
     */
    public List<CommentVO> getCommentList(String newsId) throws JSONException {
        List<CommentDto> dtos = commentRO.getCommentList(newsId);
        List<CommentVO> showData = null;
        if (!Utils.isEmpty(dtos)) {
            showData = new ArrayList<>();
            // 转换 vo
            CommentVO vo;
            ReplyVO replyVO;
            for (CommentDto dto : dtos) {
                vo = new CommentVO();
                vo.setId(dto.getId());
                vo.setUserId(dto.getUserId());
                vo.setUserName(dto.getUserName());
                vo.setUserAvatar(Utils.getUserAvatar(dto.getUserId(), BonConstants.UserAvatarType.TYPE_45X45));
                vo.setContent(dto.getContent());
                vo.setCreateTime(Utils.formateCommentTime(dto.getCreateTime()));
                if (!Utils.isEmpty(dto.getReplies())) {
                    // 转换回复
                    for (ReplyDto replyDto : dto.getReplies()) {
                        replyVO = new ReplyVO();
                        replyVO.setId(replyDto.getId());
                        replyVO.setUserId(replyDto.getUserId());
                        replyVO.setUserName(replyDto.getUserName());
                        replyVO.setContent(replyDto.getContent());
                        replyVO.setCreateTime(Utils.formateCommentTime(replyDto.getCreateTime()));
                        replyVO.setReplyToUserId(replyDto.getReplyToUserId());
                        replyVO.setReplyToUserName(replyDto.getReplyToUserName());
                        vo.getReplies().add(replyVO);
                    }
                }
                showData.add(vo);
            }
        }
        return showData;
    }

    /**
     * 发表评论
     *
     * @param newsId  新闻id
     * @param content 评论内容
     * @throws JSONException
     */
    public void sendComment(String newsId, String content) throws JSONException {
        // 数据校验
        if (Utils.isEmpty(newsId)) {
            throw new AppException("param --> newsId can not be null");
        }
        if (Utils.isEmpty(content)) {
            throw new AppException("param --> content can not be null");
        }
        // TODO 剔除 评论内容中的特殊字符（如 delete drop update insert 等）

        // 获取登录用户id
        String userId = configDao.getUserId();
        if (Utils.isEmpty(userId)) {
            throw new AppException("user not login");
        }
        commentRO.addComment(newsId, userId, content);
    }

    /**
     * 发表回复
     *
     * @param newsId   新闻id
     * @param parentId 评论id
     * @param content  回复内容
     * @throws JSONException
     */
    public void sendReply(String newsId, int parentId, String content) throws JSONException {
        // 数据校验
        if (Utils.isEmpty(newsId)) {
            throw new AppException("param --> newsId can not be null ");
        }
        if (Utils.isEmpty(content)) {
            throw new AppException("param --> content can not be null ");
        }
        if (parentId == 0) {
            throw new AppException("param --> parentId can not be 0 ");
        }
        // TODO 剔除 评论内容中的特殊字符（如 delete drop update insert 等）

        // 获取登录用户id
        String userId = configDao.getUserId();
        if (Utils.isEmpty(userId)) {
            throw new AppException("user not login");
        }
        commentRO.addReply(newsId, userId, parentId, content);
    }
}
