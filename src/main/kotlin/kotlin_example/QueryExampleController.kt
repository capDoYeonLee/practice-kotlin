package kotlin_example

import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/query-examples")
class QueryExampleController(
    private val queryExampleService: QueryExampleService,
    private val userRepository: UserRepository
) {
    
    // ========== COUNT QUERY 테스트 엔드포인트 ==========
    
    @GetMapping("/count/posts/total")
    fun getTotalPostCount(): ResponseEntity<Map<String, Any>> {
        val count = queryExampleService.getTotalPostCount()
        return ResponseEntity.ok(mapOf(
            "description" to "전체 게시글 수",
            "count" to count
        ))
    }



    @PostMapping("test/query")
    fun insertCountQueryTest() {

    }
    
    @GetMapping("/count/posts/by-user/{userId}")
    fun getPostCountByUser(@PathVariable userId: Long): ResponseEntity<Map<String, Any>> {
        val user = userRepository.findById(userId).orElseThrow()
        val count = queryExampleService.getPostCountByUser(user)
        return ResponseEntity.ok(mapOf(
            "description" to "사용자 ${user.name}의 게시글 수",
            "userId" to userId,
            "userName" to user.name,
            "count" to count
        ))
    }
    
    @GetMapping("/count/posts/by-keyword")
    fun getPostCountByKeyword(@RequestParam keyword: String): ResponseEntity<Map<String, Any>> {
        val count = queryExampleService.getPostCountByTitleKeyword(keyword)
        return ResponseEntity.ok(mapOf(
            "description" to "제목에 '$keyword'가 포함된 게시글 수",
            "keyword" to keyword,
            "count" to count
        ))
    }
    
    @GetMapping("/count/posts/after-date")
    fun getPostCountAfterDate(@RequestParam date: String): ResponseEntity<Map<String, Any>> {
        val dateTime = LocalDateTime.parse(date)
        val count = queryExampleService.getPostCountAfterDate(dateTime)
        return ResponseEntity.ok(mapOf(
            "description" to "$date 이후의 게시글 수",
            "date" to date,
            "count" to count
        ))
    }
    
    @GetMapping("/count/users/total")
    fun getTotalUserCount(): ResponseEntity<Map<String, Any>> {
        val count = queryExampleService.getTotalUserCount()
        return ResponseEntity.ok(mapOf(
            "description" to "전체 사용자 수",
            "count" to count
        ))
    }
    
    @GetMapping("/count/users/with-min-posts")
    fun getUserCountWithMinPosts(@RequestParam minPosts: Int): ResponseEntity<Map<String, Any>> {
        val count = queryExampleService.getUserCountWithMinPosts(minPosts)
        return ResponseEntity.ok(mapOf(
            "description" to "게시글 ${minPosts}개 이상 작성한 사용자 수",
            "minPosts" to minPosts,
            "count" to count
        ))
    }
    
    
    // ========== SORT QUERY 테스트 엔드포인트 ==========
    
    @GetMapping("/sort/posts/by-title")
    fun getAllPostsSortedByTitle(): ResponseEntity<Map<String, Any>> {
        val posts = queryExampleService.getAllPostsSortedByTitle()
        return ResponseEntity.ok(mapOf(
            "description" to "모든 게시글 - 제목 오름차순 정렬",
            "sortBy" to "title ASC",
            "count" to posts.size,
            "posts" to posts.map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "author" to it.author.name,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    @GetMapping("/sort/posts/by-date-desc")
    fun getAllPostsSortedByDate(): ResponseEntity<Map<String, Any>> {
        val posts = queryExampleService.getAllPostsSortedByCreatedAtDesc()
        return ResponseEntity.ok(mapOf(
            "description" to "모든 게시글 - 작성일 내림차순 정렬",
            "sortBy" to "createdAt DESC",
            "count" to posts.size,
            "posts" to posts.map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "author" to it.author.name,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    @GetMapping("/sort/posts/multiple")
    fun getAllPostsWithMultipleSort(): ResponseEntity<Map<String, Any>> {
        val posts = queryExampleService.getAllPostsWithMultipleSort()
        return ResponseEntity.ok(mapOf(
            "description" to "모든 게시글 - 제목 오름차순, 작성일 내림차순",
            "sortBy" to "title ASC, createdAt DESC",
            "count" to posts.size,
            "posts" to posts.map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    @GetMapping("/sort/posts/by-user/{userId}")
    fun getUserPostsSorted(@PathVariable userId: Long): ResponseEntity<Map<String, Any>> {
        val user = userRepository.findById(userId).orElseThrow()
        val posts = queryExampleService.getUserPostsSortedByDate(user)
        return ResponseEntity.ok(mapOf(
            "description" to "사용자 ${user.name}의 게시글 - 작성일 내림차순",
            "userId" to userId,
            "userName" to user.name,
            "count" to posts.size,
            "posts" to posts.map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    @GetMapping("/sort/posts/search")
    fun searchPostsWithSort(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: String
    ): ResponseEntity<Map<String, Any>> {
        val sortDirection = if (direction.equals("DESC", ignoreCase = true)) 
            Sort.Direction.DESC else Sort.Direction.ASC
        
        val posts = queryExampleService.searchPostsByTitleWithSort(keyword, sortBy, sortDirection)
        
        return ResponseEntity.ok(mapOf(
            "description" to "제목에 '$keyword' 포함 - $sortBy $direction 정렬",
            "keyword" to keyword,
            "sortBy" to sortBy,
            "direction" to direction,
            "count" to posts.size,
            "posts" to posts.map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    @GetMapping("/sort/users/by-name")
    fun getAllUsersSortedByName(): ResponseEntity<Map<String, Any>> {
        val users = queryExampleService.getAllUsersSortedByName()
        return ResponseEntity.ok(mapOf(
            "description" to "모든 사용자 - 이름 오름차순",
            "count" to users.size,
            "users" to users.map { mapOf(
                "id" to it.id,
                "name" to it.name,
                "postCount" to it.posts.size
            )}
        ))
    }

    @GetMapping("/pagination/posts/by-user/{userId}")
    fun getUserPostsWithPagination(
        @PathVariable userId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): ResponseEntity<Map<String, Any>> {
        val user = userRepository.findById(userId).orElseThrow()
        val result = queryExampleService.getUserPostsWithPagination(user, page, size)
        
        return ResponseEntity.ok(mapOf(
            "description" to "사용자 ${user.name}의 게시글 페이지네이션",
            "userId" to userId,
            "userName" to user.name,
            "pagination" to mapOf(
                "totalElements" to result.totalElements,
                "totalPages" to result.totalPages,
                "currentPage" to result.number,
                "pageSize" to result.size,
                "sort" to "createdAt DESC, title ASC"
            ),
            "posts" to result.content.map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    
    // ========== 동적 정렬 테스트 엔드포인트 ==========
    
    @GetMapping("/sort/posts/dynamic")
    fun getPostsWithDynamicSort(
        @RequestParam sortFields: String,  // 예: "title,createdAt"
        @RequestParam directions: String    // 예: "ASC,DESC"
    ): ResponseEntity<Map<String, Any>> {
        val fieldList = sortFields.split(",")
        val directionList = directions.split(",").map { 
            if (it.equals("DESC", ignoreCase = true)) 
                Sort.Direction.DESC 
            else 
                Sort.Direction.ASC 
        }
        
        val posts = queryExampleService.getPostsWithDynamicSort(fieldList, directionList)
        
        return ResponseEntity.ok(mapOf(
            "description" to "동적 정렬 예제",
            "sortFields" to fieldList,
            "directions" to directionList.map { it.name },
            "count" to posts.size,
            "posts" to posts.take(10).map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    @GetMapping("/sort/posts/string-sort")
    fun getPostsWithStringSort(
        @RequestParam sortString: String  // 예: "title,asc;createdAt,desc"
    ): ResponseEntity<Map<String, Any>> {
        val posts = queryExampleService.getPostsWithStringSort(sortString)
        
        return ResponseEntity.ok(mapOf(
            "description" to "문자열 기반 동적 정렬",
            "sortString" to sortString,
            "count" to posts.size,
            "posts" to posts.take(10).map { mapOf(
                "id" to it.id,
                "title" to it.title,
                "createdAt" to it.createdAt
            )}
        ))
    }
    
    
    // ========== 도움말 엔드포인트 ==========
    
    @GetMapping("/help")
    fun getHelp(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(mapOf(
            "title" to "Spring Data JPA Count & Sort Query 학습 API",
            "description" to "Count Query와 Sort Query를 학습하기 위한 예제 엔드포인트입니다.",
            "endpoints" to mapOf(
                "count" to listOf(
                    "GET /api/query-examples/count/posts/total - 전체 게시글 수",
                    "GET /api/query-examples/count/posts/by-user/{userId} - 특정 사용자의 게시글 수",
                    "GET /api/query-examples/count/posts/by-keyword?keyword=검색어 - 키워드로 검색한 게시글 수",
                    "GET /api/query-examples/count/posts/after-date?date=2024-01-01T00:00:00 - 특정 날짜 이후 게시글 수",
                    "GET /api/query-examples/count/users/total - 전체 사용자 수",
                    "GET /api/query-examples/count/users/with-min-posts?minPosts=5 - 최소 게시글 수를 가진 사용자 수"
                ),
                "sort" to listOf(
                    "GET /api/query-examples/sort/posts/by-title - 제목 오름차순 정렬",
                    "GET /api/query-examples/sort/posts/by-date-desc - 작성일 내림차순 정렬",
                    "GET /api/query-examples/sort/posts/multiple - 다중 정렬 (제목 ASC, 작성일 DESC)",
                    "GET /api/query-examples/sort/posts/by-user/{userId} - 사용자별 게시글 정렬",
                    "GET /api/query-examples/sort/posts/search?keyword=검색어&sortBy=title&direction=ASC - 동적 정렬",
                    "GET /api/query-examples/sort/users/by-name - 사용자 이름 정렬"
                ),
                "pagination" to listOf(
                    "GET /api/query-examples/pagination/posts?page=0&size=10&keyword=검색어 - 게시글 페이지네이션",
                    "GET /api/query-examples/pagination/posts/by-user/{userId}?page=0&size=5 - 사용자별 페이지네이션",
                    "GET /api/query-examples/pagination/users?pattern=이름패턴&page=0&size=10 - 사용자 페이지네이션"
                ),
                "dynamic" to listOf(
                    "GET /api/query-examples/sort/posts/dynamic?sortFields=title,createdAt&directions=ASC,DESC - 동적 다중 정렬",
                    "GET /api/query-examples/sort/posts/string-sort?sortString=title,asc;createdAt,desc - 문자열 기반 정렬"
                )
            ),
            "tips" to listOf(
                "Page 객체는 totalElements로 count 정보를 제공합니다",
                "Sort.by()를 사용하여 동적으로 정렬 조건을 만들 수 있습니다",
                "Pageable은 Sort를 포함하므로 페이징과 정렬을 함께 처리할 수 있습니다",
                "Method Naming으로 간단한 쿼리는 자동 생성됩니다",
                "@Query 어노테이션으로 복잡한 쿼리를 작성할 수 있습니다"
            )
        ))
    }
}
