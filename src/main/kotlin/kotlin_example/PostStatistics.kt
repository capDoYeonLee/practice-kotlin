package kotlin_example

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * 통계 정보를 저장하는 엔티티
 * Count 결과를 INSERT하여 저장할 수 있습니다.
 */
@Entity
@Table(name = "post_statistics")
data class PostStatistics(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val authorName: String,
    val postCount: Long,
    val calculatedAt: LocalDateTime = LocalDateTime.now(),
    val statisticsType: String = "AUTHOR_POST_COUNT"
)
