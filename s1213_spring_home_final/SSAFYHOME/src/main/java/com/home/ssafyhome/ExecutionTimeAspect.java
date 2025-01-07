package com.home.ssafyhome;


public class ExecutionTimeAspect {

    public Object logExecutionTime(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록 한번더 두번더 세번더 도커도
        try {
            return joinPoint.proceed(); // 실제 메서드 실행
        } finally {
            long endTime = System.currentTimeMillis(); // 종료 시간 기록
            String methodName = joinPoint.getSignature().toShortString();
            System.out.println(methodName + " executed in " + (endTime - startTime) + " ms");
        }
    }
}
