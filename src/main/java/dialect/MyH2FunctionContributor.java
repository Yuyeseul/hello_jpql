package dialect;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.BasicType;
import org.hibernate.type.spi.TypeConfiguration;

public class MyH2FunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions fc) {
        TypeConfiguration typeConfig = fc.getTypeConfiguration();
        SqmFunctionRegistry registry = fc.getFunctionRegistry();

        // ✅ 문자열 반환 타입을 직접 조회
        BasicType<?> stringType = typeConfig.getBasicTypeRegistry().getRegisteredType("string");

        // ✅ 사용자 정의 함수 등록
        registry.registerPattern(
                "group_concat",
                "group_concat(?1)",
                stringType
        );
    }
}