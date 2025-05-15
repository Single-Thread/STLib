package org.singlethread.plugins.api.format

object TextFormatter {


    enum class Type {
        /**
         * 이름 전체가 모두 잘못된 문자
         */
        ALL,

        /**
         * 이름에 잘못된 문자를 포함
         */
        WRONG,

        /**
         * 이름의 길이가 10자 초과
         */
        LONG,

        /**
         * 이미 존재하는 이름
         */
        EXIST,

        /**
         * 문제 없음
         */
        PASS
    }

}