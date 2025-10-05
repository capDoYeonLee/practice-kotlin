package kotlin_example

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * INSERT/UPDATE/DELETE 쿼리에 Sort/Pageable을 잘못 사용했을 때
 * 발생하는 예외를 학습하기 위한 서비스
 */
@Service
class ExceptionTestService(
    private val postStatisticsRepository: PostStatisticsRepository
) {
    
    /**
     * 테스트 1: INSERT + Sort
     * 
     * 예상 결과: InvalidDataAccessApiUsageException 발생
     * Spring Data JPA는 INSERT 쿼리에 Sort를 적용할 수 없다고 판단하고 예외를 던집니다.
     */
    @Transactional
    fun testInsertWithSort() {
        println("\n=== 테스트 1: INSERT + Sort ===")
        try {
            val sort = Sort.by(Sort.Direction.DESC, "authorName")
            val result = postStatisticsRepository.insertWithSort(sort)
            println("✅ 성공 (예상치 못한 결과): $result rows inserted")
        } catch (e: Exception) {
            println("❌ 예외 발생 (예상된 결과):")
            println("   타입: ${e.javaClass.simpleName}")
            println("   메시지: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * 테스트 2: INSERT + Pageable
     * 
     * 예상 결과: InvalidDataAccessApiUsageException 발생
     * INSERT 쿼리는 페이징할 수 없습니다.
     */
    @Transactional
    fun testInsertWithPageable() {
        println("\n=== 테스트 2: INSERT + Pageable ===")
        try {
            val pageable = PageRequest.of(0, 10)
            val result = postStatisticsRepository.insertWithPageable(pageable)
            println("✅ 성공 (예상치 못한 결과): $result rows inserted")
        } catch (e: Exception) {
            println("❌ 예외 발생 (예상된 결과):")
            println("   타입: ${e.javaClass.simpleName}")
            println("   메시지: ${e.message}")
        }
    }
    
    /**
     * 테스트 3: UPDATE + Sort
     * 
     * UPDATE 쿼리에 Sort를 사용하려고 시도
     */
    @Transactional
    fun testUpdateWithSort() {
        println("\n=== 테스트 3: UPDATE + Sort ===")
        try {
            val sort = Sort.by(Sort.Direction.ASC, "postCount")
            val result = postStatisticsRepository.updateWithSort(sort)
            println("✅ 성공 (예상치 못한 결과): $result rows updated")
        } catch (e: Exception) {
            println("❌ 예외 발생 (예상된 결과):")
            println("   타입: ${e.javaClass.simpleName}")
            println("   메시지: ${e.message}")
        }
    }
    
    /**
     * 테스트 4: DELETE + Pageable
     * 
     * DELETE 쿼리에 Pageable을 사용하려고 시도
     */
    @Transactional
    fun testDeleteWithPageable() {
        println("\n=== 테스트 4: DELETE + Pageable ===")
        try {
            val pageable = PageRequest.of(0, 5, Sort.by("authorName"))
            val result = postStatisticsRepository.deleteWithPageable("TEST", pageable)
            println("✅ 성공 (예상치 못한 결과): $result rows deleted")
        } catch (e: Exception) {
            println("❌ 예외 발생 (예상된 결과):")
            println("   타입: ${e.javaClass.simpleName}")
            println("   메시지: ${e.message}")
        }
    }
    
    /**
     * 모든 테스트 실행
     */
    fun runAllExceptionTests() {
        println("\n" + "=".repeat(60))
        println("Spring Data JPA 예외 학습 테스트 시작")
        println("INSERT/UPDATE/DELETE에 Sort/Pageable 사용 시 예외 발생")
        println("=".repeat(60))
        
        testInsertWithSort()
        testInsertWithPageable()
        testUpdateWithSort()
        testDeleteWithPageable()
        
        println("\n" + "=".repeat(60))
        println("테스트 완료")
        println("=".repeat(60))
    }
    
    /**
     * 핵심 개념 설명
     */
    fun printConcepts() {
        println("""
            
            ========================================
            핵심 개념: Spring Data JPA의 제약사항
            ========================================
            
            1. Sort는 SELECT 쿼리에만 사용 가능
               - INSERT, UPDATE, DELETE는 결과를 반환하지 않으므로 정렬 불가
               - Spring Data JPA가 자동으로 예외를 발생시킴
            
            2. Pageable은 SELECT 쿼리에만 사용 가능
               - 페이징은 조회 결과를 나누는 것
               - INSERT/UPDATE/DELETE에는 의미가 없음
            
            3. @Modifying은 필수
               - INSERT, UPDATE, DELETE는 반드시 @Modifying 필요
               - 없으면 Spring이 예외 발생
            
            4. countQuery는 SELECT에만 의미 있음
               - Page<T>를 반환할 때 totalElements 계산용
               - DML 쿼리에는 사용 불가
            
            5. 올바른 사용법
               - SELECT: Sort, Pageable, countQuery 모두 사용 가능
               - INSERT/UPDATE/DELETE: @Modifying만 사용, Int 반환
            
            ========================================
            
        """.trimIndent())
    }
}
