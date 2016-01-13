package com.zhengshang.meeting.service;

import android.content.Context;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.UserDao;
import com.zhengshang.meeting.dao.entity.User;
import com.zhengshang.meeting.remote.UserRO;
import com.zhengshang.meeting.remote.dto.UserDto;
import com.zhengshang.meeting.ui.vo.UserVO;

import org.json.JSONException;

/**
 * 用户 service
 * Created by sun on 2016/1/11.
 */
public class UserService extends BaseService {

    private UserRO userRO;
    private UserDao userDao;

    public UserService(Context context) {
        super(context);
        userRO = new UserRO(context);
        userDao = new UserDao(context);
    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @throws JSONException
     */
    public void login(String userName, String password) throws JSONException {
        UserDto userDto = userRO.login(userName, password);
        if (userDto != null) {
            User user = new User();
            user.setUserId(userDto.getUserId());
            user.setUserName(userDto.getUserName());
            user.setNickName(userDto.getNickName());
            user.setEmail(userDto.getEmail());
            user.setRegisterTime(userDto.getRegisterTime());
            user.setLastLoginTime(userDto.getLastLoginTime());
            userDao.saveUser(user);
            configDao.saveUserId(user.getUserId());
        }
    }

    /**
     * 检测用户登录状态
     *
     * @return true 登录状态正常  false 未登录或登录状态失效
     */
    public boolean checkLoginState() {
        String userId = configDao.getUserId();
        if (Utils.isEmpty(userId)) {
            return false;
        }
        User user = userDao.getUserById(userId);
        if (user == null) {
            return false;
        }
        if (user.getLastLoginTime() == 0) {
            return false;
        }
        if (System.currentTimeMillis() - user.getLastLoginTime() > BonConstants.TIME_TO_SAVE_LOGIN_STATE) {
            return false;
        }
        return true;
    }

    public void logout() {
        // 清除用户id
        configDao.saveUserId(null);
        // TODO 根据以后具体需求 确定是否要清除用户信息

    }

    /**
     * 获取登录用户信息
     * @return
     */
    public UserVO getLoginUserInfo() {
        String userId = configDao.getUserId();
        User user = userDao.getUserById(userId);
        if (user == null) {
            return null;
        } else {
            UserVO vo = new UserVO();
            vo.setUserId(userId);
            vo.setUserName(user.getUserName());
            vo.setNickName(user.getNickName());
            vo.setEmail(user.getEmail());
            return vo;
        }
    }

    /**
     * 获取登录用户 id
     * @return
     */
    public String getLoginUserId(){
        return configDao.getUserId();
    }
}
