package kotlin_example

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional

interface PostStatisticsRepository : JpaRepository<PostStatistics, Long> {
    
    // ========== ❌ 잘못된 사용 예제 - 예외 발생 시나리오 ==========
    
    /**
     * ❌ 예외 1: INSERT 쿼리에 Sort 파라미터 사용
     * Spring Data JPA가 예외를 던집니다.
     */
    @Modifying
    @Transactional
    @Query(
        value = """
            INSERT INTO post_statistics (author_name, post_count, calculated_at, statistics_type)
            SELECT u.name, COUNT(p.id), NOW(), 'SORT_TEST'
            FROM users u
            LEFT JOIN posts p ON p.author_id = u.id
            GROUP BY u.id, u.name
        """,
        nativeQuery = true
    )
    fun insertWithSort(sort: Sort): Int
    
    /**
     * ❌ 예외 2: INSERT 쿼리에 Pageable 파라미터 사용
     */
    @Modifying
    @Transactional
    @Query(
        value = """
            INSERT INTO post_statistics (author_name, post_count, calculated_at, statistics_type)
            VALUES ('test', 10, NOW(), 'PAGE_TEST')
        """,
        nativeQuery = true
    )
    fun insertWithPageable(pageable: Pageable): Int
    
    /**
     * ❌ 예외 3: UPDATE 쿼리에 Sort 사용
     */
    @Modifying
    @Transactional
    @Query(
        value = "UPDATE post_statistics SET statistics_type = 'UPDATED'",
        nativeQuery = true
    )
    fun updateWithSort(sort: Sort): Int
    
    /**
     * ❌ 예외 4: DELETE 쿼리에 Pageable 사용
     */
    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM post_statistics WHERE statistics_type = :type",
        nativeQuery = true
    )
    fun deleteWithPageable(type: String, pageable: Pageable): Int
}
