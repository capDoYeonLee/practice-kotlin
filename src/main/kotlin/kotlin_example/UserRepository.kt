package kotlin_example

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    
    // ========== COUNT QUERY 예제 ==========
    
    // 1. 이름으로 검색한 사용자 수
    fun countByName(name: String): Long
    
    // 2. 이름에 특정 패턴이 포함된 사용자 수
    fun countByNameContaining(pattern: String): Long
    
    // 3. @Query를 사용한 커스텀 count - 게시글이 N개 이상인 사용자 수
    @Query("SELECT COUNT(u) FROM User u WHERE SIZE(u.posts) >= :minPosts")
    fun countUsersWithMinimumPosts(minPosts: Int): Long
    
    // 4. 네이티브 쿼리로 count (nativeQuery = true)
    @Query(
        value = "SELECT COUNT(*) FROM users WHERE LENGTH(name) > :minLength",
        nativeQuery = true
    )
    fun countByNameLengthNative(minLength: Int): Long
    
    
    // ========== SORT QUERY 예제 ==========
    
    // 5. 이름으로 검색 + Sort
    fun findByNameContaining(pattern: String, sort: Sort): List<User>
    
    // 6. Method Naming으로 정렬 조건 명시
    fun findAllByOrderByNameAsc(): List<User>
    fun findAllByOrderByNameDesc(): List<User>
    
    // 7. @Query + Sort 조합
    @Query("SELECT u FROM User u WHERE SIZE(u.posts) > :minPosts")
    fun findActiveUsers(minPosts: Int, sort: Sort): List<User>
    
    // 8. Pageable 사용 (Sort + Pagination 포함)
    fun findByNameContaining(pattern: String, pageable: Pageable): Page<User>
    
    
    // ========== 복합 예제 ==========
    
    // 9. JOIN을 포함한 쿼리 + Sort
    @Query("""
        SELECT DISTINCT u FROM User u 
        LEFT JOIN FETCH u.posts p 
        WHERE u.name LIKE %:pattern%
    """)
    fun findUsersWithPostsByName(pattern: String, sort: Sort): List<User>
    
    // 10. Count와 함께 사용할 Page 쿼리
    @Query("SELECT u FROM User u WHERE SIZE(u.posts) >= :minPosts")
    fun findProductiveUsers(minPosts: Int, pageable: Pageable): Page<User>
}
