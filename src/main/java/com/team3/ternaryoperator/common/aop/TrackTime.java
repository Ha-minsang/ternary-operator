package com.team3.ternaryoperator.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 메소드에 적용
@Retention(RetentionPolicy.RUNTIME) // 수명 정하기
public @interface TrackTime {

    String type(); // 어노테이션 타입 입력받으려면 필요

}
