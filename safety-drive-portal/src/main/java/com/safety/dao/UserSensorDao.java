package com.safety.dao;

import com.safety.po.UserSensorPo;
import com.safety.po.UserSensorPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserSensorDao {
    long countByExample(UserSensorPoExample example);

    int deleteByExample(UserSensorPoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserSensorPo record);

    int insertSelective(UserSensorPo record);

    List<UserSensorPo> selectByExample(UserSensorPoExample example);

    UserSensorPo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserSensorPo record, @Param("example") UserSensorPoExample example);

    int updateByExample(@Param("record") UserSensorPo record, @Param("example") UserSensorPoExample example);

    int updateByPrimaryKeySelective(UserSensorPo record);

    int updateByPrimaryKey(UserSensorPo record);
}