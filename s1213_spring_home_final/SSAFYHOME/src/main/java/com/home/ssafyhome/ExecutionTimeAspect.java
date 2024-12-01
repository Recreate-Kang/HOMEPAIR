package com.home.ssafyhome;


//@org.aspectj.lang.annotation.Aspect
//@org.springframework.stereotype.Component
public class ExecutionTimeAspect {

    //@org.aspectj.lang.annotation.Around("execution(* com.*.*.*.controller..*(..))") // 컨트롤러 패키지 경로 지정
    public Object logExecutionTime(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록
        try {
            return joinPoint.proceed(); // 실제 메서드 실행
        } finally {
            long endTime = System.currentTimeMillis(); // 종료 시간 기록
            String methodName = joinPoint.getSignature().toShortString();
            System.out.println(methodName + " executed in " + (endTime - startTime) + " ms");
        }
    }
}
