package kotlin_example

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {
    
    // ========== COUNT QUERY 예제 ==========
    
    // 1. 기본 count - 전체 개수
    // JpaRepository에서 기본 제공: count()
    
    // 2. 특정 조건으로 count - Method Naming으로 자동 생성
    fun countByAuthor(author: User): Long
    
    // 3. title에 특정 키워드가 포함된 게시글 개수
    fun countByTitleContaining(keyword: String): Long
    
    // 4. 여러 조건을 조합한 count
    fun countByAuthorAndTitleContaining(author: User, keyword: String): Long
    
    // 5. @Query를 사용한 커스텀 count 쿼리
    @Query("SELECT COUNT(p) FROM Post p WHERE p.author.name = :authorName")
    fun countByAuthorName(authorName: String): Long

    @Query("INSERT INTO COUNT(p) FROM Post p WHERE p.author.name = :authorName")
    fun countByAuthorNameInsert(authorName: String): Long
    
    // 6. 특정 날짜 이후의 게시글 개수
    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :date")
    fun countPostsAfterDate(date: java.time.LocalDateTime): Long


    
    // 7. 복잡한 조건의 count (JPQL)
    @Query("""
        SELECT COUNT(p) FROM Post p 
        WHERE LENGTH(p.content) > :minLength 
        AND p.author.name LIKE %:authorNamePattern%
    """)
    fun countByContentLengthAndAuthorNamePattern(
        minLength: Int, 
        authorNamePattern: String
    ): Long
    
    
    // ========== SORT QUERY 예제 ==========
    
    // 8. Sort를 파라미터로 받는 메서드 - 동적 정렬
    fun findByAuthor(author: User, sort: Sort): List<Post>
    
    // 9. title에 키워드가 포함된 게시글을 Sort로 정렬
    fun findByTitleContaining(keyword: String, sort: Sort): List<Post>
    
    // 10. Method Naming에 정렬 조건 명시 - createdAt 내림차순
    fun findByAuthorOrderByCreatedAtDesc(author: User): List<Post>
    
    // 11. 여러 정렬 조건 명시 - title 오름차순, createdAt 내림차순
    fun findByAuthorOrderByTitleAscCreatedAtDesc(author: User): List<Post>
    
    // 12. @Query와 Sort 함께 사용
    @Query("SELECT p FROM Post p WHERE p.author.name = :authorName")
    fun findByAuthorNameWithSort(authorName: String, sort: Sort): List<Post>
    
    // 13. Pageable을 사용한 정렬 및 페이징 (Sort 포함)
    fun findByTitleContaining(keyword: String, pageable: Pageable): Page<Post>
    
    // 14. 전체 조회 with Sort (JpaRepository 기본 제공)
    // findAll(sort: Sort): List<Post>
    
    // 15. @Query에서 명시적으로 ORDER BY 사용
    @Query("SELECT p FROM Post p WHERE LENGTH(p.title) > :minLength ORDER BY p.createdAt DESC")
    fun findLongTitlePostsOrderedByDate(minLength: Int): List<Post>
    
    
    // ========== COUNT + SORT 조합 예제 ==========
    
    // 16. Page 객체를 사용하면 count와 정렬된 데이터를 함께 얻을 수 있음
    fun findByAuthor(author: User, pageable: Pageable): Page<Post>
    // Page는 getTotalElements()로 count, getContent()로 정렬된 데이터 제공
    
    // 17. 커스텀 쿼리 + Pageable
    @Query("""
        SELECT p FROM Post p 
        WHERE p.author.name LIKE %:pattern% 
        AND LENGTH(p.content) > :minLength
    """)
    fun searchPosts(pattern: String, minLength: Int, pageable: Pageable): Page<Post>
}
