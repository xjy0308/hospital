package com.aaa.mapper;

import com.aaa.entity.PaibanTimePeriod;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface PaibanTimePeriodMapper {

    List<PaibanTimePeriod> findAllPaibanTimePeriod();

    List<PaibanTimePeriod> getTimePeriodByDoctorId(Integer doctorId);
}
