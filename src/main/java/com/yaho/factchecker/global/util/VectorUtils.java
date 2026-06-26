package com.yaho.factchecker.global.util;

import com.pgvector.PGvector;

public final class VectorUtils {

    private VectorUtils() {}

    /**
     * float[] 임베딩을 pgvector 리터럴 문자열로 변환.
     * 예: [0.1, 0.2, 0.3] → "[0.1,0.2,0.3]"
     * 네이티브 쿼리에서 ::vector 로 캐스팅하여 사용.
     */
    public static String toVectorLiteral(float[] embedding) {
        return new PGvector(embedding).toString();
    }

    public static String toVectorLiteral(PGvector vector) {
        return vector.toString();
    }
}
