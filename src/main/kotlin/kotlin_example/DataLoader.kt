package kotlin_example

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class DataLoader(
    private val postRepository: PostRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (postRepository.count() == 0L) {
            val posts = mutableListOf<Post>()
            for (i in 1..1_000_000) {
                posts.add(
                    Post(
                        title = "샘플 제목 $i",
                        content = "이것은 샘플 게시글 내용 $i 입니다.",
                        author = "작성자${i % 1000}" // 작성자 1000명 반복
                    )
                )
                // 메모리 절약을 위해 일정 단위마다 DB에 저장
                if (i % 10_000 == 0) {
                    postRepository.saveAll(posts)
                    posts.clear()
                    println("$i 개 데이터 저장 완료")
                }
            }
            // 남은 데이터 저장
            if (posts.isNotEmpty()) {
                postRepository.saveAll(posts)
            }
        }
    }
}
