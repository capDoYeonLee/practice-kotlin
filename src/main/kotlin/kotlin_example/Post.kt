package kotlin_example

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
data class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,   // val: 불변. 기본값 0을 줘야 JPA 프록시가 문제 없이 동작
    var title: String,
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    var author: User,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
