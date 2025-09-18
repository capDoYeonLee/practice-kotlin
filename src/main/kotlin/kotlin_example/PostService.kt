package kotlin_example

import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun findAll(): List<Post> = postRepository.findAll()

    fun findById(id: Long): Post =
        postRepository.findById(id).orElseThrow { IllegalArgumentException("Post not found") }

    fun save(post: Post): Post = postRepository.save(post)

    fun deleteById(id: Long) = postRepository.deleteById(id)
}
