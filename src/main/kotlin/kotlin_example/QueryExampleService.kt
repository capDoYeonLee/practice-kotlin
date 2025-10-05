package kotlin_example

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class QueryExampleService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    
    // ========== COUNT QUERY 사용 예제 ==========
    
    /**
     * 1. 전체 게시글 수 조회
     */
    fun getTotalPostCount(): Long {
        return postRepository.count()
    }


    
    /**
     * 2. 특정 사용자가 작성한 게시글 수
     */
    fun getPostCountByUser(user: User): Long {
        return postRepository.countByAuthor(user)
    }
    
    /**
     * 3. 제목에 특정 키워드가 포함된 게시글 수
     */
    fun getPostCountByTitleKeyword(keyword: String): Long {
        return postRepository.countByTitleContaining(keyword)
    }
    
    /**
     * 4. 특정 사용자의 게시글 중 제목에 키워드가 포함된 게시글 수
     */
    fun getPostCountByUserAndKeyword(user: User, keyword: String): Long {
        return postRepository.countByAuthorAndTitleContaining(user, keyword)
    }
    
    /**
     * 5. 사용자 이름으로 게시글 수 조회
     */
    fun getPostCountByAuthorName(authorName: String): Long {
        return postRepository.countByAuthorName(authorName)
    }
    
    /**
     * 6. 특정 날짜 이후의 게시글 수
     */
    fun getPostCountAfterDate(date: LocalDateTime): Long {
        return postRepository.countPostsAfterDate(date)
    }
    
    /**
     * 7. 복잡한 조건: 내용 길이와 작성자 이름 패턴
     */
    fun getPostCountByComplexCondition(minLength: Int, authorPattern: String): Long {
        return postRepository.countByContentLengthAndAuthorNamePattern(minLength, authorPattern)
    }
    
    /**
     * 8. 전체 사용자 수
     */
    fun getTotalUserCount(): Long {
        return userRepository.count()
    }
    
    /**
     * 9. 특정 이름을 가진 사용자 수
     */
    fun getUserCountByName(name: String): Long {
        return userRepository.countByName(name)
    }
    
    /**
     * 10. 최소 게시글 수를 가진 사용자 수
     */
    fun getUserCountWithMinPosts(minPosts: Int): Long {
        return userRepository.countUsersWithMinimumPosts(minPosts)
    }
    
    
    // ========== SORT QUERY 사용 예제 ==========
    
    /**
     * 11. 모든 게시글을 제목 오름차순으로 조회
     */
    fun getAllPostsSortedByTitle(): List<Post> {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
    }
    
    /**
     * 12. 모든 게시글을 작성일 내림차순으로 조회
     */
    fun getAllPostsSortedByCreatedAtDesc(): List<Post> {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
    }
    
    /**
     * 13. 여러 조건으로 정렬 (제목 오름차순, 작성일 내림차순)
     */
    fun getAllPostsWithMultipleSort(): List<Post> {
        val sort = Sort.by(
            Sort.Order.asc("title"),
            Sort.Order.desc("createdAt")
        )
        return postRepository.findAll(sort)
    }
    
    /**
     * 14. 특정 사용자의 게시글을 작성일 내림차순으로 조회
     */
    fun getUserPostsSortedByDate(user: User): List<Post> {
        return postRepository.findByAuthorOrderByCreatedAtDesc(user)
    }
    
    /**
     * 15. 특정 사용자의 게시글을 여러 조건으로 정렬
     */
    fun getUserPostsWithMultipleSort(user: User): List<Post> {
        return postRepository.findByAuthorOrderByTitleAscCreatedAtDesc(user)
    }
    
    /**
     * 16. 제목 키워드 검색 + 동적 정렬
     */
    fun searchPostsByTitleWithSort(keyword: String, sortBy: String, direction: Sort.Direction): List<Post> {
        val sort = Sort.by(direction, sortBy)
        return postRepository.findByTitleContaining(keyword, sort)
    }
    
    /**
     * 17. 사용자 이름으로 게시글 검색 + 정렬
     */
    fun getPostsByAuthorNameWithSort(authorName: String, sortField: String): List<Post> {
        val sort = Sort.by(Sort.Direction.DESC, sortField)
        return postRepository.findByAuthorNameWithSort(authorName, sort)
    }
    
    /**
     * 18. 모든 사용자를 이름 오름차순으로 조회
     */
    fun getAllUsersSortedByName(): List<User> {
        return userRepository.findAllByOrderByNameAsc()
    }
    
    /**
     * 19. 이름 패턴으로 사용자 검색 + 동적 정렬
     */
    fun searchUsersByNameWithSort(pattern: String, sortField: String): List<User> {
        val sort = Sort.by(Sort.Direction.ASC, sortField)
        return userRepository.findByNameContaining(pattern, sort)
    }
    
    
    // ========== PAGINATION (COUNT + SORT 조합) 예제 ==========
    
    /**
     * 20. 페이지네이션 기본 예제 - 첫 페이지, 10개, 작성일 내림차순
     */
    fun getPostsFirstPage(): Page<Post> {
        val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        return postRepository.findAll(pageable)
    }
    
    /**
     * 21. 제목 키워드로 검색 + 페이지네이션
     */
    fun searchPostsWithPagination(keyword: String, page: Int, size: Int): Page<Post> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return postRepository.findByTitleContaining(keyword, pageable)
    }
    
    /**
     * 22. 특정 사용자의 게시글 페이지네이션
     */
    fun getUserPostsWithPagination(user: User, page: Int, size: Int): Page<Post> {
        val pageable = PageRequest.of(
            page, 
            size, 
            Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.asc("title")
            )
        )
        return postRepository.findByAuthor(user, pageable)
    }
    
    /**
     * 23. 복잡한 조건 검색 + 페이지네이션
     */
    fun searchPostsComplexWithPagination(
        pattern: String, 
        minLength: Int, 
        page: Int, 
        size: Int
    ): Page<Post> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        return postRepository.searchPosts(pattern, minLength, pageable)
    }
    
    /**
     * 24. 사용자 검색 + 페이지네이션
     */
    fun searchUsersWithPagination(pattern: String, page: Int, size: Int): Page<User> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"))
        return userRepository.findByNameContaining(pattern, pageable)
    }
    
    /**
     * 25. 활동적인 사용자 검색 (최소 게시글 수) + 페이지네이션
     */
    fun getProductiveUsersWithPagination(minPosts: Int, page: Int, size: Int): Page<User> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name"))
        return userRepository.findProductiveUsers(minPosts, pageable)
    }
    
    
    // ========== 페이지네이션 결과 분석 예제 ==========
    
    /**
     * 26. Page 객체 상세 정보 출력 예제
     */
    fun analyzePageResult(keyword: String) {
        val page = searchPostsWithPagination(keyword, 0, 5)
        
        println("=== Page 분석 결과 ===")
        println("총 요소 수 (count): ${page.totalElements}")  // count() 역할
        println("총 페이지 수: ${page.totalPages}")
        println("현재 페이지: ${page.number}")
        println("페이지 크기: ${page.size}")
        println("현재 페이지의 요소 수: ${page.numberOfElements}")
        println("첫 페이지 여부: ${page.isFirst}")
        println("마지막 페이지 여부: ${page.isLast}")
        println("정렬 정보: ${page.sort}")
        
        println("\n=== 실제 데이터 ===")
        page.content.forEach { post ->
            println("${post.id}: ${post.title} (${post.createdAt})")
        }
    }
    
    
    // ========== 동적 정렬 조건 생성 예제 ==========
    
    /**
     * 27. 클라이언트 요청에 따른 동적 정렬 생성
     */
    fun getPostsWithDynamicSort(
        sortFields: List<String>,
        directions: List<Sort.Direction>
    ): List<Post> {
        require(sortFields.size == directions.size) { "정렬 필드와 방향의 개수가 일치해야 합니다" }
        
        val orders = sortFields.zip(directions).map { (field, direction) ->
            Sort.Order(direction, field)
        }
        
        val sort = Sort.by(orders)
        return postRepository.findAll(sort)
    }
    
    /**
     * 28. 문자열 기반 동적 정렬 생성 (예: "title,asc;createdAt,desc")
     */
    fun getPostsWithStringSort(sortString: String): List<Post> {
        val orders = sortString.split(";").map { orderStr ->
            val (field, direction) = orderStr.split(",")
            Sort.Order(
                if (direction.equals("desc", ignoreCase = true)) 
                    Sort.Direction.DESC 
                else 
                    Sort.Direction.ASC,
                field
            )
        }
        
        return postRepository.findAll(Sort.by(orders))
    }
}
