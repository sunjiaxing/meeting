package com.zhengshang.meeting.service;

import android.content.Context;

import com.zhengshang.meeting.common.Utils;
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
     * @param catId  栏目id
     * @return
     * @throws JSONException
     */
    public List<CommentVO> getCommentList(String newsId, String catId) throws JSONException {
        List<CommentDto> dtos = commentRO.getCommentList(newsId, catId);
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

}
